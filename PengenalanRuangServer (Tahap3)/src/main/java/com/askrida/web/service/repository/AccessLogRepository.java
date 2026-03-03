package com.askrida.web.service.repository;

import com.askrida.web.service.conf.JdbcTemplate;
import com.askrida.web.service.model.AccessLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Repository untuk operasi CRUD tabel access_log.
 * Mencatat semua aktivitas akses ke ruang server.
 */
@Repository
public class AccessLogRepository {

    @Autowired
    @Qualifier("jdbcTemplate1")
    public JdbcTemplate jdbcTemplate1;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

    /**
     * Catat log akses baru
     */
    public void logAccess(AccessLog log) throws SQLException {
        String sql = "INSERT INTO access_log (user_id, nim, nama, role, status, confidence, method, ip_address, notes) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] params = new Object[]{
            log.getUserId(),
            log.getNim(),
            log.getNama(),
            log.getRole(),
            log.getStatus(),
            log.getConfidence(),
            log.getMethod() != null ? log.getMethod() : "face_recognition",
            log.getIpAddress(),
            log.getNotes()
        };
        jdbcTemplate1.update(sql, params);
    }

    /**
     * Catat akses GRANTED
     */
    public void logGranted(String nim, String nama, String role, double confidence, String ipAddress) throws SQLException {
        AccessLog log = new AccessLog(nim, nama, role, "GRANTED", confidence);
        log.setIpAddress(ipAddress);
        log.setMethod("face_recognition");
        log.setNotes("Akses diberikan - Face verified");
        logAccess(log);
    }

    /**
     * Catat akses DENIED
     */
    public void logDenied(String ipAddress, String notes) throws SQLException {
        AccessLog log = new AccessLog(null, "Unknown", null, "DENIED", 0.0);
        log.setIpAddress(ipAddress);
        log.setMethod("face_recognition");
        log.setNotes(notes != null ? notes : "Wajah tidak dikenali");
        logAccess(log);
    }

    /**
     * Get semua logs (terbaru dulu), dengan limit
     */
    public List<AccessLog> getRecentLogs(int limit) {
        String sql = "SELECT id, user_id, nim, nama, role, status, confidence, method, ip_address, notes, created_at " +
                     "FROM access_log ORDER BY created_at DESC LIMIT ?";
        return jdbcTemplate1.query(sql, new Object[]{limit}, (rs, rowNum) -> {
            AccessLog al = new AccessLog();
            al.setId(rs.getInt("id"));
            al.setUserId(rs.getObject("user_id") != null ? rs.getInt("user_id") : null);
            al.setNim(rs.getString("nim"));
            al.setNama(rs.getString("nama"));
            al.setRole(rs.getString("role"));
            al.setStatus(rs.getString("status"));
            al.setConfidence(rs.getDouble("confidence"));
            al.setMethod(rs.getString("method"));
            al.setIpAddress(rs.getString("ip_address"));
            al.setNotes(rs.getString("notes"));
            al.setCreatedAt(rs.getTimestamp("created_at"));
            // Format tanggal dan jam
            if (al.getCreatedAt() != null) {
                al.setTanggal(DATE_FORMAT.format(al.getCreatedAt()));
                al.setJam(TIME_FORMAT.format(al.getCreatedAt()));
            }
            return al;
        });
    }

    /**
     * Get semua logs
     */
    public List<AccessLog> getAllLogs() {
        return getRecentLogs(1000);
    }

    /**
     * Get logs hari ini
     */
    public List<AccessLog> getTodayLogs() {
        String sql = "SELECT id, user_id, nim, nama, role, status, confidence, method, ip_address, notes, created_at " +
                     "FROM access_log WHERE DATE(created_at) = CURRENT_DATE ORDER BY created_at DESC";
        return jdbcTemplate1.query(sql, (rs, rowNum) -> {
            AccessLog al = new AccessLog();
            al.setId(rs.getInt("id"));
            al.setUserId(rs.getObject("user_id") != null ? rs.getInt("user_id") : null);
            al.setNim(rs.getString("nim"));
            al.setNama(rs.getString("nama"));
            al.setRole(rs.getString("role"));
            al.setStatus(rs.getString("status"));
            al.setConfidence(rs.getDouble("confidence"));
            al.setMethod(rs.getString("method"));
            al.setIpAddress(rs.getString("ip_address"));
            al.setNotes(rs.getString("notes"));
            al.setCreatedAt(rs.getTimestamp("created_at"));
            if (al.getCreatedAt() != null) {
                al.setTanggal(DATE_FORMAT.format(al.getCreatedAt()));
                al.setJam(TIME_FORMAT.format(al.getCreatedAt()));
            }
            return al;
        });
    }

    /**
     * Get logs berdasarkan NIM
     */
    public List<AccessLog> getLogsByNim(String nim) {
        String sql = "SELECT id, user_id, nim, nama, role, status, confidence, method, ip_address, notes, created_at " +
                     "FROM access_log WHERE nim = ? ORDER BY created_at DESC";
        return jdbcTemplate1.query(sql, new Object[]{nim}, (rs, rowNum) -> {
            AccessLog al = new AccessLog();
            al.setId(rs.getInt("id"));
            al.setUserId(rs.getObject("user_id") != null ? rs.getInt("user_id") : null);
            al.setNim(rs.getString("nim"));
            al.setNama(rs.getString("nama"));
            al.setRole(rs.getString("role"));
            al.setStatus(rs.getString("status"));
            al.setConfidence(rs.getDouble("confidence"));
            al.setMethod(rs.getString("method"));
            al.setIpAddress(rs.getString("ip_address"));
            al.setNotes(rs.getString("notes"));
            al.setCreatedAt(rs.getTimestamp("created_at"));
            if (al.getCreatedAt() != null) {
                al.setTanggal(DATE_FORMAT.format(al.getCreatedAt()));
                al.setJam(TIME_FORMAT.format(al.getCreatedAt()));
            }
            return al;
        });
    }

    /**
     * Get statistics
     */
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        try {
            Integer totalToday = jdbcTemplate1.queryForObject(
                "SELECT COUNT(*) FROM access_log WHERE DATE(created_at) = CURRENT_DATE", Integer.class);
            Integer grantedToday = jdbcTemplate1.queryForObject(
                "SELECT COUNT(*) FROM access_log WHERE DATE(created_at) = CURRENT_DATE AND status = 'GRANTED'", Integer.class);
            Integer deniedToday = jdbcTemplate1.queryForObject(
                "SELECT COUNT(*) FROM access_log WHERE DATE(created_at) = CURRENT_DATE AND status = 'DENIED'", Integer.class);
            Integer totalAll = jdbcTemplate1.queryForObject(
                "SELECT COUNT(*) FROM access_log", Integer.class);

            stats.put("totalToday", totalToday != null ? totalToday : 0);
            stats.put("grantedToday", grantedToday != null ? grantedToday : 0);
            stats.put("deniedToday", deniedToday != null ? deniedToday : 0);
            stats.put("totalAll", totalAll != null ? totalAll : 0);
        } catch (Exception e) {
            stats.put("totalToday", 0);
            stats.put("grantedToday", 0);
            stats.put("deniedToday", 0);
            stats.put("totalAll", 0);
        }
        return stats;
    }

    /**
     * Get hourly access count for chart (last 24 hours)
     */
    public List<Map<String, Object>> getHourlyStats() {
        String sql = "SELECT EXTRACT(HOUR FROM created_at) AS hour, COUNT(*) AS count, " +
                     "SUM(CASE WHEN status = 'GRANTED' THEN 1 ELSE 0 END) AS granted, " +
                     "SUM(CASE WHEN status = 'DENIED' THEN 1 ELSE 0 END) AS denied " +
                     "FROM access_log WHERE DATE(created_at) = CURRENT_DATE " +
                     "GROUP BY EXTRACT(HOUR FROM created_at) ORDER BY hour";
        return jdbcTemplate1.query(sql, (rs, rowNum) -> {
            Map<String, Object> row = new HashMap<>();
            row.put("hour", rs.getInt("hour"));
            row.put("count", rs.getInt("count"));
            row.put("granted", rs.getInt("granted"));
            row.put("denied", rs.getInt("denied"));
            return row;
        });
    }

    /**
     * Get daily access count for chart (last 7 days)
     */
    public List<Map<String, Object>> getDailyStats() {
        String sql = "SELECT DATE(created_at) AS date, COUNT(*) AS count, " +
                     "SUM(CASE WHEN status = 'GRANTED' THEN 1 ELSE 0 END) AS granted, " +
                     "SUM(CASE WHEN status = 'DENIED' THEN 1 ELSE 0 END) AS denied " +
                     "FROM access_log WHERE created_at >= CURRENT_DATE - INTERVAL '7 days' " +
                     "GROUP BY DATE(created_at) ORDER BY date";
        return jdbcTemplate1.query(sql, (rs, rowNum) -> {
            Map<String, Object> row = new HashMap<>();
            row.put("date", rs.getString("date"));
            row.put("count", rs.getInt("count"));
            row.put("granted", rs.getInt("granted"));
            row.put("denied", rs.getInt("denied"));
            return row;
        });
    }

    /**
     * Delete logs older than X days
     */
    public int cleanupOldLogs(int daysToKeep) throws SQLException {
        String sql = "DELETE FROM access_log WHERE created_at < CURRENT_DATE - INTERVAL '" + daysToKeep + " days'";
        return jdbcTemplate1.update(sql);
    }
}
