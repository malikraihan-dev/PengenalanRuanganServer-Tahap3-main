package com.askrida.web.service.repository;

import com.askrida.web.service.conf.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Repository untuk tabel user_attendance.
 * Mengelola clock-in / clock-out absensi wajah user.
 */
@Repository
public class AttendanceRepository {

    @Autowired
    @Qualifier("jdbcTemplate1")
    public JdbcTemplate jdbcTemplate1;

    private static final SimpleDateFormat DATE_FMT = new SimpleDateFormat("dd-MM-yyyy");
    private static final SimpleDateFormat TIME_FMT = new SimpleDateFormat("HH:mm:ss");
    private static final SimpleDateFormat DATETIME_FMT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    /**
     * Clock-in: catat waktu masuk user
     */
    public Map<String, Object> clockIn(String nim, String nama, double confidence, String method, String ipAddress, String notes) throws SQLException {
        // Cek apakah sudah clock-in hari ini tapi belum clock-out
        String checkSql = "SELECT id FROM user_attendance WHERE nim = ? AND DATE(clock_in) = CURRENT_DATE AND clock_out IS NULL LIMIT 1";
        List<Map<String, Object>> existing = jdbcTemplate1.queryForList(checkSql, new Object[]{nim});
        if (!existing.isEmpty()) {
            Map<String, Object> result = new HashMap<>();
            result.put("alreadyClockedIn", true);
            result.put("attendanceId", existing.get(0).get("id"));
            return result;
        }

        String sql = "INSERT INTO user_attendance (nim, nama, method, confidence, clock_in, ip_address, notes) " +
                     "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, ?, ?) RETURNING id, clock_in";
        Map<String, Object> row = jdbcTemplate1.queryForMap(sql, new Object[]{nim, nama, method, confidence, ipAddress, notes});

        Map<String, Object> result = new HashMap<>();
        result.put("alreadyClockedIn", false);
        result.put("attendanceId", row.get("id"));
        result.put("clockIn", row.get("clock_in").toString());
        return result;
    }

    /**
     * Clock-out: catat waktu keluar user
     */
    public Map<String, Object> clockOut(String nim, double confidence, String ipAddress) throws SQLException {
        // Cari record clock-in hari ini yang belum clock-out
        String findSql = "SELECT id, clock_in FROM user_attendance WHERE nim = ? AND DATE(clock_in) = CURRENT_DATE AND clock_out IS NULL ORDER BY clock_in DESC LIMIT 1";
        List<Map<String, Object>> rows = jdbcTemplate1.queryForList(findSql, new Object[]{nim});

        if (rows.isEmpty()) {
            Map<String, Object> result = new HashMap<>();
            result.put("notClockedIn", true);
            return result;
        }

        int attendanceId = (int) rows.get(0).get("id");
        String updateSql = "UPDATE user_attendance SET clock_out = CURRENT_TIMESTAMP WHERE id = ? RETURNING clock_in, clock_out";
        Map<String, Object> updated = jdbcTemplate1.queryForMap(updateSql, new Object[]{attendanceId});

        Map<String, Object> result = new HashMap<>();
        result.put("notClockedIn", false);
        result.put("attendanceId", attendanceId);
        result.put("clockIn", updated.get("clock_in").toString());
        result.put("clockOut", updated.get("clock_out").toString());

        // Hitung durasi
        java.sql.Timestamp clockIn = (java.sql.Timestamp) updated.get("clock_in");
        java.sql.Timestamp clockOut = (java.sql.Timestamp) updated.get("clock_out");
        long durationMinutes = (clockOut.getTime() - clockIn.getTime()) / (1000 * 60);
        long hours = durationMinutes / 60;
        long minutes = durationMinutes % 60;
        result.put("duration", hours + " jam " + minutes + " menit");
        result.put("durationMinutes", durationMinutes);

        return result;
    }

    /**
     * Get attendance hari ini
     */
    public List<Map<String, Object>> getTodayAttendance() {
        String sql = "SELECT a.id, a.nim, a.nama, a.method, a.confidence, a.clock_in, a.clock_out, a.notes, a.ip_address, " +
                     "u.role, u.photo_url " +
                     "FROM user_attendance a " +
                     "LEFT JOIN server_users u ON a.nim = u.nim " +
                     "WHERE DATE(a.clock_in) = CURRENT_DATE " +
                     "ORDER BY a.clock_in DESC";
        return jdbcTemplate1.query(sql, (rs, rowNum) -> {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("id", rs.getInt("id"));
            row.put("nim", rs.getString("nim"));
            row.put("nama", rs.getString("nama"));
            row.put("role", rs.getString("role"));
            row.put("photoUrl", rs.getString("photo_url"));
            row.put("method", rs.getString("method"));
            row.put("confidence", rs.getDouble("confidence"));
            row.put("notes", rs.getString("notes"));

            java.sql.Timestamp clockIn = rs.getTimestamp("clock_in");
            java.sql.Timestamp clockOut = rs.getTimestamp("clock_out");
            row.put("clockIn", clockIn != null ? DATETIME_FMT.format(clockIn) : null);
            row.put("clockOut", clockOut != null ? DATETIME_FMT.format(clockOut) : null);
            row.put("clockInTime", clockIn != null ? TIME_FMT.format(clockIn) : null);
            row.put("clockOutTime", clockOut != null ? TIME_FMT.format(clockOut) : null);

            // Status
            if (clockOut != null) {
                long dur = (clockOut.getTime() - clockIn.getTime()) / (1000 * 60);
                row.put("status", "Selesai");
                row.put("duration", (dur / 60) + "j " + (dur % 60) + "m");
            } else {
                row.put("status", "Aktif");
                row.put("duration", "-");
            }

            return row;
        });
    }

    /**
     * Get attendance berdasarkan NIM (riwayat)
     */
    public List<Map<String, Object>> getAttendanceByNim(String nim, int limit) {
        String sql = "SELECT id, nim, nama, method, confidence, clock_in, clock_out, notes " +
                     "FROM user_attendance WHERE nim = ? ORDER BY clock_in DESC LIMIT ?";
        return jdbcTemplate1.query(sql, new Object[]{nim, limit}, (rs, rowNum) -> {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("id", rs.getInt("id"));
            row.put("nim", rs.getString("nim"));
            row.put("nama", rs.getString("nama"));
            row.put("method", rs.getString("method"));
            row.put("confidence", rs.getDouble("confidence"));
            row.put("notes", rs.getString("notes"));

            java.sql.Timestamp clockIn = rs.getTimestamp("clock_in");
            java.sql.Timestamp clockOut = rs.getTimestamp("clock_out");
            row.put("clockIn", clockIn != null ? DATETIME_FMT.format(clockIn) : null);
            row.put("clockOut", clockOut != null ? DATETIME_FMT.format(clockOut) : null);
            row.put("tanggal", clockIn != null ? DATE_FMT.format(clockIn) : null);
            row.put("clockInTime", clockIn != null ? TIME_FMT.format(clockIn) : null);
            row.put("clockOutTime", clockOut != null ? TIME_FMT.format(clockOut) : null);

            if (clockOut != null) {
                long dur = (clockOut.getTime() - clockIn.getTime()) / (1000 * 60);
                row.put("status", "Selesai");
                row.put("duration", (dur / 60) + "j " + (dur % 60) + "m");
            } else {
                row.put("status", "Aktif");
                row.put("duration", "-");
            }

            return row;
        });
    }

    /**
     * Cek apakah user sudah clock-in hari ini
     */
    public Map<String, Object> getActiveClockIn(String nim) {
        String sql = "SELECT id, clock_in FROM user_attendance WHERE nim = ? AND DATE(clock_in) = CURRENT_DATE AND clock_out IS NULL ORDER BY clock_in DESC LIMIT 1";
        List<Map<String, Object>> rows = jdbcTemplate1.queryForList(sql, new Object[]{nim});
        if (rows.isEmpty()) return null;

        Map<String, Object> result = new HashMap<>();
        result.put("attendanceId", rows.get(0).get("id"));
        java.sql.Timestamp clockIn = (java.sql.Timestamp) rows.get(0).get("clock_in");
        result.put("clockIn", clockIn != null ? DATETIME_FMT.format(clockIn) : null);
        result.put("clockInTime", clockIn != null ? TIME_FMT.format(clockIn) : null);
        return result;
    }

    /**
     * Get ALL attendance records (semua data, tidak hanya hari ini)
     */
    public List<Map<String, Object>> getAllAttendance() {
        String sql = "SELECT a.id, a.nim, a.nama, a.method, a.confidence, a.clock_in, a.clock_out, a.notes, a.ip_address " +
                     "FROM user_attendance a " +
                     "ORDER BY a.clock_in DESC";
        return jdbcTemplate1.query(sql, (rs, rowNum) -> {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("id", rs.getInt("id"));
            row.put("nim", rs.getString("nim"));
            row.put("nama", rs.getString("nama"));
            row.put("method", rs.getString("method"));
            row.put("confidence", rs.getDouble("confidence"));
            row.put("notes", rs.getString("notes"));

            java.sql.Timestamp clockIn = rs.getTimestamp("clock_in");
            java.sql.Timestamp clockOut = rs.getTimestamp("clock_out");
            row.put("clockIn", clockIn != null ? DATETIME_FMT.format(clockIn) : null);
            row.put("clockOut", clockOut != null ? DATETIME_FMT.format(clockOut) : null);
            row.put("clockInTime", clockIn != null ? TIME_FMT.format(clockIn) : null);
            row.put("clockOutTime", clockOut != null ? TIME_FMT.format(clockOut) : null);
            row.put("tanggal", clockIn != null ? DATE_FMT.format(clockIn) : null);

            if (clockOut != null) {
                long dur = (clockOut.getTime() - clockIn.getTime()) / (1000 * 60);
                row.put("status", "Selesai");
                row.put("duration", (dur / 60) + "j " + (dur % 60) + "m");
            } else {
                row.put("status", "Aktif");
                row.put("duration", "-");
            }

            return row;
        });
    }

    /**
     * Get attendance statistics
     */
    public Map<String, Object> getAttendanceStats() {
        Map<String, Object> stats = new HashMap<>();
        try {
            Integer todayTotal = jdbcTemplate1.queryForObject(
                "SELECT COUNT(*) FROM user_attendance WHERE DATE(clock_in) = CURRENT_DATE", Integer.class);
            Integer activeNow = jdbcTemplate1.queryForObject(
                "SELECT COUNT(*) FROM user_attendance WHERE DATE(clock_in) = CURRENT_DATE AND clock_out IS NULL", Integer.class);
            Integer completedToday = jdbcTemplate1.queryForObject(
                "SELECT COUNT(*) FROM user_attendance WHERE DATE(clock_in) = CURRENT_DATE AND clock_out IS NOT NULL", Integer.class);

            stats.put("todayTotal", todayTotal != null ? todayTotal : 0);
            stats.put("activeNow", activeNow != null ? activeNow : 0);
            stats.put("completedToday", completedToday != null ? completedToday : 0);
        } catch (Exception e) {
            stats.put("todayTotal", 0);
            stats.put("activeNow", 0);
            stats.put("completedToday", 0);
        }
        return stats;
    }
}
