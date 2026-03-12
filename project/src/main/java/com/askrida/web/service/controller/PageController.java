package com.askrida.web.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.askrida.web.service.repository.RepositoryTes;
import com.askrida.web.service.repository.ServerUserRepository;
import com.askrida.web.service.repository.AttendanceRepository;
import com.askrida.web.service.model.ServerUser;
import com.askrida.web.service.model.RestResult;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PageController {

    @Autowired
    private RepositoryTes repTes;

    @Autowired
    private ServerUserRepository serverUserRepo;

    @Autowired
    private AttendanceRepository attendanceRepo;

    // ===== Helper: Check if logged in =====
    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("loggedIn") != null;
    }

    private String getRole(HttpSession session) {
        Object role = session.getAttribute("role");
        return role != null ? role.toString() : "";
    }

    private boolean isAdmin(HttpSession session) {
        return "ADMIN".equals(getRole(session));
    }

    // ===== ADMIN DASHBOARD =====
    @GetMapping("/")
    public String dashboard(Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        // If USER role, redirect to user dashboard
        if (!isAdmin(session)) {
            return "redirect:/user-dashboard";
        }

        // Load user_attendance data (semua riwayat absensi)
        List<Map<String, Object>> dataAttendance;
        try {
            dataAttendance = attendanceRepo.getAllAttendance();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("[Dashboard] Error loading attendance: " + e.getMessage());
            dataAttendance = new ArrayList<>();
        }

        // Attendance stats
        Map<String, Object> stats;
        try {
            stats = attendanceRepo.getAttendanceStats();
        } catch (Exception e) {
            stats = new HashMap<>();
            stats.put("todayTotal", 0);
            stats.put("activeNow", 0);
            stats.put("completedToday", 0);
        }

        model.addAttribute("dataAttendance", dataAttendance);
        model.addAttribute("totalAbsensi", stats.get("todayTotal"));
        model.addAttribute("totalHadir", stats.get("completedToday"));
        model.addAttribute("totalAktif", stats.get("activeNow"));
        model.addAttribute("totalRuangan", 4);
        model.addAttribute("username", session.getAttribute("username"));
        return "dashboard";
    }

    @GetMapping("/monitoring")
    public String monitoring(Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        if (!isAdmin(session)) {
            return "redirect:/user-dashboard";
        }
        List<Map<String, Object>> dataAttendance;
        try {
            dataAttendance = attendanceRepo.getAllAttendance();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("[Monitoring] Error loading attendance: " + e.getMessage());
            dataAttendance = new ArrayList<>();
        }
        model.addAttribute("dataAttendance", dataAttendance);
        model.addAttribute("username", session.getAttribute("username"));
        return "dashboard";
    }

    // ===== USER DASHBOARD =====
    @GetMapping("/user-dashboard")
    public String userDashboard(Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        // If ADMIN, redirect to admin dashboard
        if (isAdmin(session)) {
            return "redirect:/";
        }
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("userNim", session.getAttribute("userNim"));
        model.addAttribute("userRole", session.getAttribute("role"));
        return "user-dashboard";
    }

    // ===== ADMIN-ONLY PAGES =====
    @GetMapping("/face-register")
    public String faceRegister(HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        if (!isAdmin(session)) {
            return "redirect:/user-dashboard";
        }
        return "face-register";
    }

    @GetMapping("/face-absensi")
    public String faceAbsensi(HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        if (!isAdmin(session)) {
            return "redirect:/user-dashboard";
        }
        return "face-absensi";
    }

    // ===== SHARED PAGES (both roles) =====
    @GetMapping("/server-monitor")
    public String serverMonitor(HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        return "server-monitor";
    }

    @GetMapping("/server-monitor-register")
    public String serverMonitorRegister(HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        if (!isAdmin(session)) {
            return "redirect:/user-dashboard";
        }
        return "server-monitor-register";
    }

    @GetMapping("/user-management")
    public String userManagement(HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        return "user-management";
    }

    // ===== ADMIN-ONLY PAGES =====
    // NOTE: /project, /chatbot, /expense
    //       are handled by their own controllers
    //       (ProjectController, ChatController, ExpenseController)

    // ===== LOGIN & LOGOUT =====
    @GetMapping("/login")
    public String login(HttpSession session) {
        if (isLoggedIn(session)) {
            return isAdmin(session) ? "redirect:/" : "redirect:/user-dashboard";
        }
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @PostMapping("/api/login")
    @ResponseBody
    public Map<String, Object> processLogin(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam(defaultValue = "ADMIN") String role,
            HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        if (role.equalsIgnoreCase("ADMIN")) {
            // === ADMIN LOGIN ===
            // 1. Try hardcoded admin
            if ("admin".equals(username) && "admin123".equals(password)) {
                session.setAttribute("loggedIn", true);
                session.setAttribute("username", "admin");
                session.setAttribute("role", "ADMIN");
                session.setAttribute("userNim", "admin");
                response.put("success", true);
                response.put("message", "Login admin berhasil");
                response.put("role", "ADMIN");
                response.put("redirect", "/");
                return response;
            }
            // 2. Try server_users with ADMIN role
            try {
                ServerUser user = serverUserRepo.findByNim(username);
                if (user != null && "ADMIN".equalsIgnoreCase(user.getRole()) && user.isActive()) {
                    // Check password (if password_hash is set)
                    if (user.getPasswordHash() != null && !user.getPasswordHash().isEmpty()) {
                        if (!password.equals(user.getPasswordHash())) {
                            response.put("success", false);
                            response.put("message", "Password salah");
                            return response;
                        }
                    }
                    session.setAttribute("loggedIn", true);
                    session.setAttribute("username", user.getNama());
                    session.setAttribute("role", "ADMIN");
                    session.setAttribute("userNim", user.getNim());
                    response.put("success", true);
                    response.put("message", "Login admin berhasil");
                    response.put("role", "ADMIN");
                    response.put("redirect", "/");
                    return response;
                }
            } catch (Exception e) {
                System.err.println("[Login] Error checking server_users: " + e.getMessage());
            }
            response.put("success", false);
            response.put("message", "Username/password admin salah atau bukan role ADMIN");

        } else {
            // === USER LOGIN ===
            try {
                // 1. Cek NIM di tabel anggota (data dari admin)
                com.askrida.web.service.model.AnggotaResult anggota = repTes.findAnggotaByNim(username);
                if (anggota == null) {
                    response.put("success", false);
                    response.put("message", "NIM tidak ditemukan. Hubungi admin untuk mendaftarkan NIM Anda.");
                    return response;
                }

                // 2. Cek apakah sudah terdaftar di server_users
                ServerUser user = serverUserRepo.findByNim(username);

                if (user == null) {
                    // Belum terdaftar di server_users → arahkan ke registrasi password
                    response.put("success", false);
                    response.put("needsRegistration", true);
                    response.put("nim", anggota.getNim());
                    response.put("nama", anggota.getNamaLengkap());
                    response.put("message", "NIM ditemukan. Silakan buat password untuk mengakses dashboard.");
                    response.put("redirect", "/user-register?nim=" + anggota.getNim());
                    return response;
                }

                // 3. Cek apakah password sudah diset
                if (user.getPasswordHash() == null || user.getPasswordHash().isEmpty()) {
                    // Password belum diset → arahkan ke registrasi password
                    response.put("success", false);
                    response.put("needsRegistration", true);
                    response.put("nim", user.getNim());
                    response.put("nama", user.getNama());
                    response.put("message", "Akun belum memiliki password. Silakan buat password terlebih dahulu.");
                    response.put("redirect", "/user-register?nim=" + user.getNim());
                    return response;
                }

                if (!user.isActive()) {
                    response.put("success", false);
                    response.put("message", "Akun Anda tidak aktif. Hubungi admin.");
                    return response;
                }
                if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                    response.put("success", false);
                    response.put("message", "Akun ini adalah ADMIN. Silakan login melalui tab Admin.");
                    return response;
                }
                // 4. Verifikasi password
                if (!password.equals(user.getPasswordHash())) {
                    response.put("success", false);
                    response.put("message", "Password salah");
                    return response;
                }
                // 5. Login success
                session.setAttribute("loggedIn", true);
                session.setAttribute("username", user.getNama());
                session.setAttribute("role", "USER");
                session.setAttribute("userNim", user.getNim());
                response.put("success", true);
                response.put("message", "Login berhasil, selamat datang " + user.getNama());
                response.put("role", "USER");
                response.put("redirect", "/user-dashboard");
            } catch (Exception e) {
                System.err.println("[Login] Error: " + e.getMessage());
                response.put("success", false);
                response.put("message", "Terjadi kesalahan server");
            }
        }
        return response;
    }

    // ===== USER REGISTRATION (Password Only) =====
    @GetMapping("/user-register")
    public String userRegister(@RequestParam(required = false) String nim, Model model, HttpSession session) {
        if (isLoggedIn(session)) {
            return isAdmin(session) ? "redirect:/" : "redirect:/user-dashboard";
        }
        model.addAttribute("nim", nim);
        return "user-register";
    }

    @PostMapping("/api/check-nim")
    @ResponseBody
    public Map<String, Object> checkNim(@RequestParam String nim) {
        Map<String, Object> response = new HashMap<>();
        try {
            com.askrida.web.service.model.AnggotaResult anggota = repTes.findAnggotaByNim(nim);
            if (anggota == null) {
                response.put("exists", false);
                response.put("message", "NIM tidak ditemukan. Hubungi admin untuk mendaftarkan NIM Anda.");
                return response;
            }
            // Cek apakah sudah punya password
            ServerUser user = serverUserRepo.findByNim(nim);
            if (user != null && user.getPasswordHash() != null && !user.getPasswordHash().isEmpty()) {
                response.put("exists", true);
                response.put("alreadyRegistered", true);
                response.put("message", "NIM sudah memiliki password. Silakan login.");
                return response;
            }
            response.put("exists", true);
            response.put("alreadyRegistered", false);
            response.put("nama", anggota.getNamaLengkap());
            response.put("kelas", anggota.getKelas());
            response.put("message", "NIM ditemukan: " + anggota.getNamaLengkap());
        } catch (Exception e) {
            response.put("exists", false);
            response.put("message", "Error: " + e.getMessage());
        }
        return response;
    }

    @PostMapping("/api/user-register")
    @ResponseBody
    public Map<String, Object> processUserRegister(
            @RequestParam String nim,
            @RequestParam String password,
            @RequestParam String confirmPassword) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Validasi
            if (nim == null || nim.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "NIM wajib diisi");
                return response;
            }
            if (password == null || password.length() < 6) {
                response.put("success", false);
                response.put("message", "Password minimal 6 karakter");
                return response;
            }
            if (!password.equals(confirmPassword)) {
                response.put("success", false);
                response.put("message", "Password dan konfirmasi password tidak cocok");
                return response;
            }

            // Cek NIM di tabel anggota
            com.askrida.web.service.model.AnggotaResult anggota = repTes.findAnggotaByNim(nim);
            if (anggota == null) {
                response.put("success", false);
                response.put("message", "NIM tidak ditemukan di data anggota");
                return response;
            }

            // Cek apakah sudah ada di server_users
            ServerUser user = serverUserRepo.findByNim(nim);
            if (user != null) {
                if (user.getPasswordHash() != null && !user.getPasswordHash().isEmpty()) {
                    response.put("success", false);
                    response.put("message", "NIM sudah memiliki password. Silakan login.");
                    return response;
                }
                // Update password
                serverUserRepo.updatePassword(nim, password);
            } else {
                // Buat entry baru di server_users
                ServerUser newUser = serverUserRepo.registerUser(nim, anggota.getNamaLengkap(), "USER");
                serverUserRepo.updatePassword(nim, password);
            }

            response.put("success", true);
            response.put("message", "Registrasi berhasil! Silakan login dengan NIM dan password Anda.");
            response.put("redirect", "/login");
        } catch (Exception e) {
            System.err.println("[UserRegister] Error: " + e.getMessage());
            response.put("success", false);
            response.put("message", "Terjadi kesalahan: " + e.getMessage());
        }
        return response;
    }
}
