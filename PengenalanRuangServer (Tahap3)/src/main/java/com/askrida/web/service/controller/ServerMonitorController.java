package com.askrida.web.service.controller;

import com.askrida.web.service.model.AccessLog;
import com.askrida.web.service.model.ServerUser;
import com.askrida.web.service.repository.AccessLogRepository;
import com.askrida.web.service.repository.ServerUserRepository;
import com.askrida.web.service.repository.FaceRepository;
import com.askrida.web.service.repository.AttendanceRepository;
import com.askrida.web.service.util.JwtUtil;
import com.askrida.web.service.mqtt.MqttPublisherService;
import com.askrida.web.service.realtime.MonitorRealtimePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.*;

/**
 * REST Controller untuk Server Room Monitoring System.
 * 
 * Endpoints:
 * ---- AUTH ----
 * POST   /api/monitor/auth/login        -> Login via NIM + face verification
 * POST   /api/monitor/auth/register     -> Register user baru (nim, nama, role, faceDescriptor)
 * 
 * ---- USER MANAGEMENT ----
 * GET    /api/monitor/users              -> List semua user
 * GET    /api/monitor/users/{nim}        -> Detail satu user
 * PUT    /api/monitor/users/{nim}/role   -> Update role user (ADMIN only)
 * PUT    /api/monitor/users/{nim}/status -> Activate/deactivate user
 * DELETE /api/monitor/users/{nim}        -> Hapus user (ADMIN only)
 * 
 * ---- FACE VERIFICATION ----
 * POST   /api/monitor/verify             -> Verifikasi wajah (kirim descriptor, return user data)
 * POST   /api/monitor/face/register      -> Tambah face data ke user yang sudah ada
 * 
 * ---- ACCESS LOGS ----
 * GET    /api/monitor/logs               -> Semua log akses
 * GET    /api/monitor/logs/today         -> Log akses hari ini
 * GET    /api/monitor/logs/{nim}         -> Log akses per NIM
 * 
 * ---- DASHBOARD STATS ----
 * GET    /api/monitor/stats              -> Dashboard statistics
 * GET    /api/monitor/stats/hourly       -> Statistik akses per jam (chart)
 * GET    /api/monitor/stats/daily        -> Statistik akses 7 hari terakhir (chart)
 */
@RestController
@RequestMapping("/api/monitor")
@CrossOrigin(origins = "*")
public class ServerMonitorController {

    @Autowired
    private ServerUserRepository userRepository;

    @Autowired
    private AccessLogRepository accessLogRepository;

    @Autowired
    private FaceRepository faceRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired(required = false)
    private MqttPublisherService mqttPublisher;

    @Autowired(required = false)
    private MonitorRealtimePublisher realtimePublisher;

    // ======================== AUTH ========================

    /**
     * Login via face recognition.
     * Client mengirim NIM + confidence dari face matching.
     * Server memvalidasi dan mengembalikan JWT token.
     */
    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, Object> request, HttpServletRequest httpReq) {
        Map<String, Object> response = new HashMap<>();
        try {
            String nim = (String) request.get("nim");
            Double confidence = request.get("confidence") != null ? 
                Double.parseDouble(request.get("confidence").toString()) : 0.0;

            if (nim == null || nim.isEmpty()) {
                response.put("success", false);
                response.put("message", "NIM tidak boleh kosong");
                return ResponseEntity.badRequest().body(response);
            }

            ServerUser user = userRepository.findByNim(nim);
            if (user == null) {
                // Log akses ditolak
                accessLogRepository.logDenied(httpReq.getRemoteAddr(), "NIM tidak ditemukan: " + nim);
                response.put("success", false);
                response.put("message", "User tidak ditemukan. Hubungi administrator.");
                response.put("redirect", "/");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            if (!user.isActive()) {
                accessLogRepository.logDenied(httpReq.getRemoteAddr(), "User nonaktif: " + nim);
                response.put("success", false);
                response.put("message", "Akun Anda telah dinonaktifkan. Hubungi admin.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

            // Threshold confidence
            if (confidence < 0.6) {
                accessLogRepository.logDenied(httpReq.getRemoteAddr(), 
                    "Confidence terlalu rendah: " + String.format("%.2f", confidence) + " untuk NIM " + nim);
                response.put("success", false);
                response.put("message", "Verifikasi wajah gagal. Confidence terlalu rendah.");
                response.put("confidence", confidence);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            // Generate JWT Token
            String token = JwtUtil.generateToken(user.getNim(), user.getNama(), user.getRole());

            // Log akses berhasil
            accessLogRepository.logGranted(user.getNim(), user.getNama(), user.getRole(), confidence, httpReq.getRemoteAddr());

            response.put("success", true);
            response.put("message", "Akses diberikan");
            response.put("token", token);
            response.put("user", buildUserMap(user));
            response.put("confidence", confidence);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Register user baru + face data.
     */
    @PostMapping("/auth/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String nim = (String) request.get("nim");
            String nama = (String) request.get("nama");
            String role = request.get("role") != null ? (String) request.get("role") : "USER";
            String faceDescriptor = (String) request.get("faceDescriptor");
            String imageData = (String) request.get("imageData");

            // Validasi
            if (nim == null || nim.isEmpty() || nama == null || nama.isEmpty()) {
                response.put("success", false);
                response.put("message", "NIM dan Nama wajib diisi");
                return ResponseEntity.badRequest().body(response);
            }

            // Cek duplikasi
            if (userRepository.existsByNim(nim)) {
                response.put("success", false);
                response.put("message", "NIM sudah terdaftar. Gunakan halaman login.");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }

            // Register user
            ServerUser user = userRepository.registerUser(nim, nama, role);

            // Simpan face data jika ada
            if (faceDescriptor != null && !faceDescriptor.isEmpty()) {
                com.askrida.web.service.model.FaceData faceData = new com.askrida.web.service.model.FaceData(nim, nama, faceDescriptor);
                faceData.setImageData(imageData);
                faceRepository.saveFaceData(faceData);

                // Update photo URL di user
                if (imageData != null && !imageData.isEmpty()) {
                    userRepository.updatePhotoUrl(nim, imageData);
                }
            }

            // Generate token
            String token = JwtUtil.generateToken(user.getNim(), user.getNama(), user.getRole());

            response.put("success", true);
            response.put("message", "Registrasi berhasil! Selamat datang, " + nama);
            response.put("token", token);
            response.put("user", buildUserMap(user));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Gagal registrasi: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ======================== USER MANAGEMENT ========================

    /**
     * Get semua user terdaftar
     */
    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getAllUsers(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<ServerUser> users = userRepository.getAllUsers();
            List<Map<String, Object>> userList = new ArrayList<>();
            for (ServerUser u : users) {
                userList.add(buildUserMap(u));
            }
            response.put("success", true);
            response.put("users", userList);
            response.put("total", users.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get detail user berdasarkan NIM
     */
    @GetMapping("/users/{nim}")
    public ResponseEntity<Map<String, Object>> getUserByNim(@PathVariable String nim) {
        Map<String, Object> response = new HashMap<>();
        ServerUser user = userRepository.findByNim(nim);
        if (user == null) {
            response.put("success", false);
            response.put("message", "User tidak ditemukan");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        response.put("success", true);
        response.put("user", buildUserMap(user));
        return ResponseEntity.ok(response);
    }

    /**
     * Update role user (hanya ADMIN)
     */
    @PutMapping("/users/{nim}/role")
    public ResponseEntity<Map<String, Object>> updateRole(
            @PathVariable String nim,
            @RequestBody Map<String, String> request,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Validate admin access
            JwtUtil.JwtClaims claims = JwtUtil.extractFromHeader(authHeader);
            if (claims == null || !claims.isAdmin()) {
                response.put("success", false);
                response.put("message", "Akses ditolak. Hanya ADMIN yang bisa mengubah role.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

            String newRole = request.get("role");
            if (newRole == null || (!newRole.equalsIgnoreCase("USER") && !newRole.equalsIgnoreCase("ADMIN"))) {
                response.put("success", false);
                response.put("message", "Role harus USER atau ADMIN");
                return ResponseEntity.badRequest().body(response);
            }

            ServerUser user = userRepository.findByNim(nim);
            if (user == null) {
                response.put("success", false);
                response.put("message", "User tidak ditemukan");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            userRepository.updateRole(nim, newRole);
            response.put("success", true);
            response.put("message", "Role " + nim + " berhasil diubah menjadi " + newRole.toUpperCase());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Activate/Deactivate user
     */
    @PutMapping("/users/{nim}/status")
    public ResponseEntity<Map<String, Object>> updateUserStatus(
            @PathVariable String nim,
            @RequestBody Map<String, Object> request,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Map<String, Object> response = new HashMap<>();
        try {
            JwtUtil.JwtClaims claims = JwtUtil.extractFromHeader(authHeader);
            if (claims == null || !claims.isAdmin()) {
                response.put("success", false);
                response.put("message", "Akses ditolak");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

            Boolean active = (Boolean) request.get("active");
            if (active == null) {
                response.put("success", false);
                response.put("message", "Parameter 'active' diperlukan");
                return ResponseEntity.badRequest().body(response);
            }

            userRepository.setActive(nim, active);
            response.put("success", true);
            response.put("message", nim + (active ? " diaktifkan" : " dinonaktifkan"));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Delete user (ADMIN only)
     */
    @DeleteMapping("/users/{nim}")
    public ResponseEntity<Map<String, Object>> deleteUser(
            @PathVariable String nim,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Map<String, Object> response = new HashMap<>();
        try {
            JwtUtil.JwtClaims claims = JwtUtil.extractFromHeader(authHeader);
            if (claims == null || !claims.isAdmin()) {
                response.put("success", false);
                response.put("message", "Akses ditolak. Hanya ADMIN yang bisa menghapus user.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

            // Hapus face data dulu
            faceRepository.deleteFaceDataByNim(nim);
            // Hapus user
            userRepository.deleteUser(nim);

            response.put("success", true);
            response.put("message", "User " + nim + " berhasil dihapus");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ======================== FACE VERIFICATION ========================

    /**
     * Verifikasi wajah.
     * Client mengirim face descriptor, server mencari match.
     * Response: user data + JWT token jika match.
     */
    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyFace(
            @RequestBody Map<String, Object> request,
            HttpServletRequest httpReq) {
        Map<String, Object> response = new HashMap<>();
        try {
            String nim = (String) request.get("nim");
            Double confidence = request.get("confidence") != null ? 
                Double.parseDouble(request.get("confidence").toString()) : 0.0;
            String faceDescriptor = (String) request.get("faceDescriptor");

            if (nim == null || nim.isEmpty()) {
                accessLogRepository.logDenied(httpReq.getRemoteAddr(), "Wajah tidak dikenali");
                response.put("success", false);
                response.put("recognized", false);
                response.put("message", "Wajah tidak dikenali. Hubungi administrator.");
                response.put("redirect", "/");
                return ResponseEntity.ok(response);
            }

            ServerUser user = userRepository.findByNim(nim);
            if (user == null) {
                accessLogRepository.logDenied(httpReq.getRemoteAddr(), "NIM tidak ditemukan: " + nim);
                response.put("success", false);
                response.put("recognized", false);
                response.put("message", "User tidak ditemukan. Hubungi administrator.");
                response.put("redirect", "/");
                return ResponseEntity.ok(response);
            }

            if (!user.isActive()) {
                accessLogRepository.logDenied(httpReq.getRemoteAddr(), "User nonaktif: " + nim);
                response.put("success", false);
                response.put("recognized", true);
                response.put("message", "Akun Anda telah dinonaktifkan.");
                return ResponseEntity.ok(response);
            }

            // Log akses
            if (confidence >= 0.6) {
                accessLogRepository.logGranted(user.getNim(), user.getNama(), user.getRole(), confidence, httpReq.getRemoteAddr());
                String token = JwtUtil.generateToken(user.getNim(), user.getNama(), user.getRole());

                response.put("success", true);
                response.put("recognized", true);
                response.put("status", "GRANTED");
                response.put("message", "Selamat datang, " + user.getNama() + "!");
                response.put("token", token);
                response.put("user", buildUserMap(user));
                response.put("confidence", confidence);
            } else {
                accessLogRepository.logDenied(httpReq.getRemoteAddr(), 
                    "Confidence rendah: " + String.format("%.2f", confidence) + " untuk " + nim);
                response.put("success", false);
                response.put("recognized", true);
                response.put("status", "DENIED");
                response.put("message", "Verifikasi gagal. Confidence terlalu rendah.");
                response.put("confidence", confidence);
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error verifikasi: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Tambah face data ke user existing
     */
    @PostMapping("/face/register")
    public ResponseEntity<Map<String, Object>> addFaceData(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String nim = (String) request.get("nim");
            String faceDescriptor = (String) request.get("faceDescriptor");
            String imageData = (String) request.get("imageData");

            if (nim == null || faceDescriptor == null) {
                response.put("success", false);
                response.put("message", "NIM dan faceDescriptor diperlukan");
                return ResponseEntity.badRequest().body(response);
            }

            ServerUser user = userRepository.findByNim(nim);
            if (user == null) {
                response.put("success", false);
                response.put("message", "User belum terdaftar. Register terlebih dahulu.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            com.askrida.web.service.model.FaceData faceData = new com.askrida.web.service.model.FaceData(nim, user.getNama(), faceDescriptor);
            faceData.setImageData(imageData);
            faceRepository.saveFaceData(faceData);

            int totalFoto = faceRepository.countFaceByNim(nim);
            response.put("success", true);
            response.put("message", "Data wajah tambahan berhasil disimpan");
            response.put("totalFoto", totalFoto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ======================== ACCESS LOGS ========================

    /**
     * Get semua log akses
     */
    @GetMapping("/logs")
    public ResponseEntity<Map<String, Object>> getLogs(
            @RequestParam(defaultValue = "50") int limit) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("logs", accessLogRepository.getRecentLogs(limit));
        return ResponseEntity.ok(response);
    }

    /**
     * Get log akses hari ini
     */
    @GetMapping("/logs/today")
    public ResponseEntity<Map<String, Object>> getTodayLogs() {
        Map<String, Object> response = new HashMap<>();
        List<AccessLog> logs = accessLogRepository.getTodayLogs();
        response.put("success", true);
        response.put("logs", logs);
        response.put("total", logs.size());
        return ResponseEntity.ok(response);
    }

    /**
     * Get log akses per NIM
     */
    @GetMapping("/logs/{nim}")
    public ResponseEntity<Map<String, Object>> getLogsByNim(@PathVariable String nim) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("logs", accessLogRepository.getLogsByNim(nim));
        return ResponseEntity.ok(response);
    }

    // ======================== DASHBOARD STATS ========================

    /**
     * Get semua dashboard statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> response = new HashMap<>();
        try {
            Map<String, Object> userStats = userRepository.getDashboardStats();
            Map<String, Object> logStats = accessLogRepository.getStats();

            response.put("success", true);
            response.putAll(userStats);
            response.putAll(logStats);
            response.put("cameraStatus", "active");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get hourly statistics for chart
     */
    @GetMapping("/stats/hourly")
    public ResponseEntity<Map<String, Object>> getHourlyStats() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", accessLogRepository.getHourlyStats());
        return ResponseEntity.ok(response);
    }

    /**
     * Get daily statistics for chart (7 days)
     */
    @GetMapping("/stats/daily")
    public ResponseEntity<Map<String, Object>> getDailyStats() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", accessLogRepository.getDailyStats());
        return ResponseEntity.ok(response);
    }

    // ======================== ATTENDANCE (Clock In / Clock Out) ========================

    /**
     * Clock-in via face recognition
     */
    @PostMapping("/attendance/clock-in")
    public ResponseEntity<Map<String, Object>> clockIn(
            @RequestBody Map<String, Object> request,
            HttpServletRequest httpReq) {
        Map<String, Object> response = new HashMap<>();
        try {
            String nim = (String) request.get("nim");
            String nama = (String) request.get("nama");
            Double confidence = request.get("confidence") != null ?
                Double.parseDouble(request.get("confidence").toString()) : 0.0;
            String notes = (String) request.get("notes");

            if (nim == null || nim.isEmpty()) {
                response.put("success", false);
                response.put("message", "NIM diperlukan");
                return ResponseEntity.badRequest().body(response);
            }

            ServerUser user = userRepository.findByNim(nim);
            if (user == null) {
                response.put("success", false);
                response.put("message", "User tidak ditemukan");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            if (nama == null) nama = user.getNama();

            Map<String, Object> result = attendanceRepository.clockIn(
                nim, nama, confidence, "face_recognition", httpReq.getRemoteAddr(), notes);

            if ((Boolean) result.get("alreadyClockedIn")) {
                response.put("success", false);
                response.put("message", "Anda sudah clock-in hari ini dan belum clock-out.");
                response.put("attendanceId", result.get("attendanceId"));
                return ResponseEntity.ok(response);
            }

            // Log akses juga
            accessLogRepository.logGranted(nim, nama, user.getRole(), confidence, httpReq.getRemoteAddr());

            response.put("success", true);
            response.put("message", "Clock-in berhasil! Selamat bekerja, " + nama);
            response.put("attendanceId", result.get("attendanceId"));
            response.put("clockIn", result.get("clockIn"));
            response.put("user", buildUserMap(user));
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Clock-out via face recognition
     */
    @PostMapping("/attendance/clock-out")
    public ResponseEntity<Map<String, Object>> clockOut(
            @RequestBody Map<String, Object> request,
            HttpServletRequest httpReq) {
        Map<String, Object> response = new HashMap<>();
        try {
            String nim = (String) request.get("nim");
            Double confidence = request.get("confidence") != null ?
                Double.parseDouble(request.get("confidence").toString()) : 0.0;

            if (nim == null || nim.isEmpty()) {
                response.put("success", false);
                response.put("message", "NIM diperlukan");
                return ResponseEntity.badRequest().body(response);
            }

            ServerUser user = userRepository.findByNim(nim);
            if (user == null) {
                response.put("success", false);
                response.put("message", "User tidak ditemukan");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Map<String, Object> result = attendanceRepository.clockOut(nim, confidence, httpReq.getRemoteAddr());

            if ((Boolean) result.get("notClockedIn")) {
                response.put("success", false);
                response.put("message", "Anda belum clock-in hari ini.");
                return ResponseEntity.ok(response);
            }

            response.put("success", true);
            response.put("message", "Clock-out berhasil! Durasi kerja: " + result.get("duration"));
            response.put("clockIn", result.get("clockIn"));
            response.put("clockOut", result.get("clockOut"));
            response.put("duration", result.get("duration"));
            response.put("user", buildUserMap(user));
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get attendance hari ini
     */
    @GetMapping("/attendance/today")
    public ResponseEntity<Map<String, Object>> getTodayAttendance() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("attendance", attendanceRepository.getTodayAttendance());
        response.put("stats", attendanceRepository.getAttendanceStats());
        return ResponseEntity.ok(response);
    }

    /**
     * Get attendance history by NIM
     */
    @GetMapping("/attendance/{nim}")
    public ResponseEntity<Map<String, Object>> getAttendanceByNim(
            @PathVariable String nim,
            @RequestParam(defaultValue = "30") int limit) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("attendance", attendanceRepository.getAttendanceByNim(nim, limit));
        response.put("activeClockIn", attendanceRepository.getActiveClockIn(nim));
        return ResponseEntity.ok(response);
    }

    /**
     * Cek status clock-in user
     */
    @GetMapping("/attendance/status/{nim}")
    public ResponseEntity<Map<String, Object>> getClockInStatus(@PathVariable String nim) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> active = attendanceRepository.getActiveClockIn(nim);
        response.put("success", true);
        response.put("isClockedIn", active != null);
        response.put("activeClockIn", active);
        return ResponseEntity.ok(response);
    }

    // ======================== USER PROFILE ========================

    /**
     * Get user profile lengkap (profil + riwayat absensi + statistik)
     */
    @GetMapping("/profile/{nim}")
    public ResponseEntity<Map<String, Object>> getUserProfile(@PathVariable String nim) {
        Map<String, Object> response = new HashMap<>();
        try {
            ServerUser user = userRepository.findByNim(nim);
            if (user == null) {
                response.put("success", false);
                response.put("message", "User tidak ditemukan");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // User info
            Map<String, Object> userMap = buildUserMap(user);
            int faceCount = faceRepository.countFaceByNim(nim);
            userMap.put("faceCount", faceCount);

            // Riwayat attendance
            List<Map<String, Object>> attendance = attendanceRepository.getAttendanceByNim(nim, 30);
            Map<String, Object> activeClockIn = attendanceRepository.getActiveClockIn(nim);

            // Access logs
            List<AccessLog> accessLogs = accessLogRepository.getLogsByNim(nim);

            response.put("success", true);
            response.put("user", userMap);
            response.put("attendance", attendance);
            response.put("activeClockIn", activeClockIn);
            response.put("accessLogs", accessLogs);
            response.put("totalAttendance", attendance.size());
            response.put("totalAccessLogs", accessLogs.size());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Update profil user (nama) — user bisa self-update
     */
    @PutMapping("/profile/{nim}")
    public ResponseEntity<Map<String, Object>> updateProfile(
            @PathVariable String nim,
            @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            ServerUser user = userRepository.findByNim(nim);
            if (user == null) {
                response.put("success", false);
                response.put("message", "User tidak ditemukan");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            String nama = request.get("nama");
            if (nama != null && !nama.isEmpty()) {
                userRepository.updateNama(nim, nama);
            }

            response.put("success", true);
            response.put("message", "Profil berhasil diperbarui");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ======================== HELPERS ========================

    private Map<String, Object> buildUserMap(ServerUser user) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", user.getId());
        map.put("nim", user.getNim());
        map.put("nama", user.getNama());
        map.put("role", user.getRole());
        map.put("isActive", user.isActive());
        map.put("isAdmin", user.isAdmin());
        map.put("photoUrl", user.getPhotoUrl());
        map.put("faceCount", user.getFaceCount());
        map.put("createdAt", user.getCreatedAt() != null ? user.getCreatedAt().toString() : null);
        return map;
    }

    // ======================== ARDUINO INTEGRATION ========================

    /**
     * Menerima data sensor dari Arduino
     * Format JSON: {"ruangan": "Ruang Utama", "nilaiSensor": 45.2, "timestamp": "2026-04-10T10:30:00"}
     */
    @PostMapping("/arduino/sensor-data")
    public ResponseEntity<?> receiveSensorData(@RequestBody Map<String, Object> sensorData) {
        Map<String, Object> response = new HashMap<>();
        try {
            String ruangan = (String) sensorData.get("ruangan");
            Double nilaiSensor = sensorData.get("nilaiSensor") != null ? 
                Double.parseDouble(sensorData.get("nilaiSensor").toString()) : 0.0;
            String timestamp = (String) sensorData.get("timestamp");
            
            System.out.println("[ARDUINO] Data diterima - Ruangan: " + ruangan + 
                             ", Nilai: " + nilaiSensor + ", Waktu: " + timestamp);
            
            response.put("status", "success");
            response.put("message", "Data sensor berhasil diterima");
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Gagal menerima data: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Mengirim status Arduino dan data terbaru ke frontend
     */
    @GetMapping("/arduino/status")
    public ResponseEntity<?> getArduinoStatus() {
        Map<String, Object> status = new HashMap<>();
        try {
            status.put("connected", true);
            status.put("lastUpdate", System.currentTimeMillis());
            status.put("ruangan", "Ruang Utama");
            status.put("nilaiSensor", 45.2);
            status.put("status", "aktif");
            status.put("message", "Arduino terhubung dengan baik");
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            status.put("connected", false);
            status.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(status);
        }
    }

    /**
     * Mengirim perintah ke Arduino
     * Format JSON: {"command": "nyalakan", "ruangan": "Ruang Utama", "parameter": "optional_value"}
     */
    @PostMapping("/arduino/command")
    public ResponseEntity<?> sendCommand(@RequestBody Map<String, Object> command) {
        Map<String, Object> response = new HashMap<>();
        try {
            String cmd = (String) command.get("command");
            String ruangan = (String) command.get("ruangan");
            String parameter = (String) command.get("parameter");
            
            System.out.println("[ARDUINO] Perintah dikirim - Cmd: " + cmd + 
                             ", Ruangan: " + ruangan + 
                             (parameter != null ? ", Parameter: " + parameter : ""));
            
            response.put("status", "success");
            response.put("command", cmd);
            response.put("ruangan", ruangan);
            response.put("message", "Perintah berhasil dikirim ke Arduino");
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Gagal mengirim perintah: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Mendapatkan riwayat data Arduino
     */
    @GetMapping("/arduino/history")
    public ResponseEntity<?> getArduinoHistory(@RequestParam(defaultValue = "10") int limit) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Map<String, Object>> history = new ArrayList<>();
            response.put("status", "success");
            response.put("data", history);
            response.put("count", history.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // FINGERPRINT INTEGRATION
    @PostMapping("/fingerprint/enroll")
    public ResponseEntity<?> enrollFingerprint(@RequestBody Map<String, Object> request, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            Object role = session != null ? session.getAttribute("role") : null;
            if (role == null || !"ADMIN".equalsIgnoreCase(role.toString())) {
                response.put("status", "forbidden");
                response.put("message", "Hanya admin yang boleh mendaftarkan fingerprint");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
            String nim = (String) request.get("nim");
            Integer fpId = Integer.parseInt(request.get("fingerprintId").toString());
            String fpData = request.get("fingerprintData") != null ? request.get("fingerprintData").toString() : null;
            ServerUser user = userRepository.findByNim(nim);
            if (user == null) {
                response.put("status", "error");
                response.put("message", "User tidak ditemukan");
                return ResponseEntity.badRequest().body(response);
            }
            ServerUser existingFp = userRepository.findByFingerprintId(fpId);
            if (existingFp != null && !existingFp.getNim().equals(nim)) {
                response.put("status", "error");
                response.put("message", "Fingerprint ID sudah digunakan oleh user lain");
                return ResponseEntity.badRequest().body(response);
            }
            userRepository.updateFingerprint(nim, fpId, fpData, true);
            System.out.println("[FINGERPRINT] Enroll OK - NIM: " + nim);
            response.put("status", "success");
            response.put("message", "Fingerprint berhasil didaftarkan");
            response.put("fingerprintId", fpId);
            response.put("userName", user.getNama());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/fingerprint/verify")
    public ResponseEntity<?> verifyFingerprint(@RequestBody Map<String, Object> request, HttpServletRequest httpReq) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (!userRepository.isFingerprintFeatureAvailable()) {
                response.put("status", "db-not-ready");
                response.put("accessGranted", false);
                response.put("message", "Fitur fingerprint belum aktif di database. Jalankan script server_monitoring_tables.sql untuk menambahkan kolom fingerprint_*");
                if (realtimePublisher != null) {
                    realtimePublisher.publish("fingerprint-verify", new HashMap<>(response));
                }
                return ResponseEntity.ok(response);
            }
            Integer fpId = Integer.parseInt(request.get("fingerprintId").toString());
            Double confidence = request.get("confidence") != null ?
                Double.parseDouble(request.get("confidence").toString()) : 0.0;
            ServerUser user = userRepository.findByFingerprintId(fpId);
            if (user == null) {
                accessLogRepository.logDenied(httpReq.getRemoteAddr(), "Fingerprint tidak ditemukan");
                // Publish ke MQTT
                if (mqttPublisher != null) {
                    mqttPublisher.publishFingerprintResult(fpId, false, "Unknown");
                    mqttPublisher.publishAccessLog("UNKNOWN", "FINGERPRINT", false);
                }
                response.put("status", "not-found");
                response.put("message", "Fingerprint tidak dikenali");
                response.put("accessGranted", false);
                response.put("fingerprintId", fpId);
                response.put("confidence", confidence);
                if (realtimePublisher != null) {
                    realtimePublisher.publish("fingerprint-verify", new HashMap<>(response));
                }
                return ResponseEntity.ok(response);
            }
            if (!user.isActive() || !user.isFingerprintEnabled()) {
                response.put("status", "forbidden");
                response.put("message", "Akses ditolak");
                response.put("accessGranted", false);
                response.put("fingerprintId", fpId);
                response.put("confidence", confidence);
                response.put("user", buildUserMap(user));
                // Publish ke MQTT
                if (mqttPublisher != null) {
                    mqttPublisher.publishFingerprintResult(fpId, false, user.getNama());
                    mqttPublisher.publishAccessLog(user.getNim(), "FINGERPRINT", false);
                }
                if (realtimePublisher != null) {
                    realtimePublisher.publish("fingerprint-verify", new HashMap<>(response));
                }
                return ResponseEntity.ok(response);
            }
            accessLogRepository.logGranted(user.getNim(), user.getNama(), user.getRole(), confidence, httpReq.getRemoteAddr());
            String token = JwtUtil.generateToken(user.getNim(), user.getNama(), user.getRole());
            System.out.println("[FINGERPRINT] Verify OK - NIM: " + user.getNim());

            Map<String, Object> attendancePayload = new HashMap<>();
            String attendanceAction = "none";
            String attendanceMessage = "";
            Map<String, Object> activeClockIn = attendanceRepository.getActiveClockIn(user.getNim());
            if (activeClockIn != null) {
                Map<String, Object> result = attendanceRepository.clockOut(user.getNim(), confidence, httpReq.getRemoteAddr());
                attendanceAction = "clock-out";
                attendanceMessage = "Clock-out berhasil";
                attendancePayload.putAll(result);
            } else {
                Map<String, Object> result = attendanceRepository.clockIn(
                    user.getNim(),
                    user.getNama(),
                    confidence,
                    "fingerprint",
                    httpReq.getRemoteAddr(),
                    "fingerprint"
                );
                if ((Boolean) result.get("alreadyClockedIn")) {
                    attendanceAction = "already-clocked-in";
                    attendanceMessage = "Sudah clock-in dan belum clock-out";
                } else {
                    attendanceAction = "clock-in";
                    attendanceMessage = "Clock-in berhasil";
                }
                attendancePayload.putAll(result);
            }
            
            // Publish ke MQTT
            if (mqttPublisher != null) {
                mqttPublisher.publishFingerprintResult(fpId, true, user.getNama());
                mqttPublisher.publishAccessLog(user.getNim(), "FINGERPRINT", true);
            }
            
            response.put("status", "success");
            response.put("accessGranted", true);
            response.put("message", "Akses diizinkan");
            response.put("token", token);
            response.put("user", buildUserMap(user));
            response.put("attendanceAction", attendanceAction);
            response.put("attendanceMessage", attendanceMessage);
            response.put("attendance", attendancePayload);

            if (realtimePublisher != null) {
                Map<String, Object> event = new HashMap<>();
                event.put("status", "success");
                event.put("accessGranted", true);
                event.put("fingerprintId", fpId);
                event.put("confidence", confidence);
                event.put("user", buildUserMap(user));
                event.put("attendanceAction", attendanceAction);
                realtimePublisher.publish("fingerprint-verify", event);
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error: " + e.getMessage());
            response.put("accessGranted", false);
            if (realtimePublisher != null) {
                realtimePublisher.publish("fingerprint-verify", new HashMap<>(response));
            }
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/fingerprint/status/{nim}")
    public ResponseEntity<?> getFingerprintStatus(@PathVariable String nim) {
        Map<String, Object> response = new HashMap<>();
        try {
            ServerUser user = userRepository.findByNim(nim);
            if (user == null) {
                response.put("status", "error");
                response.put("message", "User tidak ditemukan");
                return ResponseEntity.badRequest().body(response);
            }
            response.put("status", "success");
            response.put("fingerprintEnabled", user.isFingerprintEnabled());
            response.put("fingerprintId", user.getFingerprintId());
            response.put("enrolledAt", user.getFingerprintEnrolledAt());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
