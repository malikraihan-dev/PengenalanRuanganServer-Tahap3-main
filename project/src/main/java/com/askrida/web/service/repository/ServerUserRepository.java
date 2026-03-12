package com.askrida.web.service.repository;

import com.askrida.web.service.conf.JdbcTemplate;
import com.askrida.web.service.model.ServerUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Repository untuk operasi CRUD tabel server_users.
 * Mengelola user yang memiliki akses ke ruang server.
 */
@Repository
public class ServerUserRepository {

    @Autowired
    @Qualifier("jdbcTemplate1")
    public JdbcTemplate jdbcTemplate1;

    /**
     * Register user baru
     */
    public ServerUser registerUser(String nim, String nama, String role) throws SQLException {
        String sql = "INSERT INTO server_users (nim, nama, role, is_active) VALUES (?, ?, ?, true) RETURNING id";
        try {
            Integer id = jdbcTemplate1.queryForObject(sql, new Object[]{nim, nama, role.toUpperCase()}, Integer.class);
            ServerUser user = new ServerUser(nim, nama, role.toUpperCase());
            user.setId(id);
            return user;
        } catch (Exception e) {
            throw new SQLException("Gagal register user: " + e.getMessage(), e);
        }
    }

    /**
     * Cari user berdasarkan NIM
     */
    public ServerUser findByNim(String nim) {
        String sql = "SELECT id, nim, nama, role, password_hash, face_embedding, photo_url, is_active, created_at, updated_at FROM server_users WHERE nim = ?";
        try {
            List<ServerUser> users = jdbcTemplate1.query(sql, new Object[]{nim}, (rs, rowNum) -> {
                ServerUser u = new ServerUser();
                u.setId(rs.getInt("id"));
                u.setNim(rs.getString("nim"));
                u.setNama(rs.getString("nama"));
                u.setRole(rs.getString("role"));
                u.setPasswordHash(rs.getString("password_hash"));
                u.setFaceEmbedding(rs.getString("face_embedding"));
                u.setPhotoUrl(rs.getString("photo_url"));
                u.setActive(rs.getBoolean("is_active"));
                u.setCreatedAt(rs.getTimestamp("created_at"));
                u.setUpdatedAt(rs.getTimestamp("updated_at"));
                return u;
            });
            return users.isEmpty() ? null : users.get(0);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Cari user berdasarkan ID
     */
    public ServerUser findById(int id) {
        String sql = "SELECT id, nim, nama, role, password_hash, face_embedding, photo_url, is_active, created_at, updated_at FROM server_users WHERE id = ?";
        try {
            List<ServerUser> users = jdbcTemplate1.query(sql, new Object[]{id}, (rs, rowNum) -> {
                ServerUser u = new ServerUser();
                u.setId(rs.getInt("id"));
                u.setNim(rs.getString("nim"));
                u.setNama(rs.getString("nama"));
                u.setRole(rs.getString("role"));
                u.setPasswordHash(rs.getString("password_hash"));
                u.setFaceEmbedding(rs.getString("face_embedding"));
                u.setPhotoUrl(rs.getString("photo_url"));
                u.setActive(rs.getBoolean("is_active"));
                u.setCreatedAt(rs.getTimestamp("created_at"));
                u.setUpdatedAt(rs.getTimestamp("updated_at"));
                return u;
            });
            return users.isEmpty() ? null : users.get(0);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Get semua user
     */
    public List<ServerUser> getAllUsers() {
        String sql = "SELECT u.id, u.nim, u.nama, u.role, u.is_active, u.created_at, u.updated_at, " +
                     "COALESCE(f.face_count, 0) AS face_count " +
                     "FROM server_users u " +
                     "LEFT JOIN (SELECT nim, COUNT(*) AS face_count FROM server_face_data GROUP BY nim) f ON u.nim = f.nim " +
                     "ORDER BY u.created_at DESC";
        return jdbcTemplate1.query(sql, (rs, rowNum) -> {
            ServerUser u = new ServerUser();
            u.setId(rs.getInt("id"));
            u.setNim(rs.getString("nim"));
            u.setNama(rs.getString("nama"));
            u.setRole(rs.getString("role"));
            u.setActive(rs.getBoolean("is_active"));
            u.setCreatedAt(rs.getTimestamp("created_at"));
            u.setUpdatedAt(rs.getTimestamp("updated_at"));
            u.setFaceCount(rs.getInt("face_count"));
            return u;
        });
    }

    /**
     * Update role user (USER / ADMIN)
     */
    public void updateRole(String nim, String newRole) throws SQLException {
        String sql = "UPDATE server_users SET role = ?, updated_at = CURRENT_TIMESTAMP WHERE nim = ?";
        jdbcTemplate1.update(sql, new Object[]{newRole.toUpperCase(), nim});
    }

    /**
     * Update face embedding
     */
    public void updateFaceEmbedding(String nim, String embedding) throws SQLException {
        String sql = "UPDATE server_users SET face_embedding = ?, updated_at = CURRENT_TIMESTAMP WHERE nim = ?";
        jdbcTemplate1.update(sql, new Object[]{embedding, nim});
    }

    /**
     * Update photo URL
     */
    public void updatePhotoUrl(String nim, String photoUrl) throws SQLException {
        String sql = "UPDATE server_users SET photo_url = ?, updated_at = CURRENT_TIMESTAMP WHERE nim = ?";
        jdbcTemplate1.update(sql, new Object[]{photoUrl, nim});
    }

    /**
     * Activate / Deactivate user
     */
    public void setActive(String nim, boolean active) throws SQLException {
        String sql = "UPDATE server_users SET is_active = ?, updated_at = CURRENT_TIMESTAMP WHERE nim = ?";
        jdbcTemplate1.update(sql, new Object[]{active, nim});
    }

    /**
     * Delete user
     */
    public void deleteUser(String nim) throws SQLException {
        String sql = "DELETE FROM server_users WHERE nim = ?";
        jdbcTemplate1.update(sql, new Object[]{nim});
    }

    /**
     * Count total users
     */
    public int countTotal() {
        try {
            return jdbcTemplate1.queryForObject("SELECT COUNT(*) FROM server_users WHERE is_active = true", Integer.class);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Count admins
     */
    public int countAdmins() {
        try {
            return jdbcTemplate1.queryForObject("SELECT COUNT(*) FROM server_users WHERE role = 'ADMIN' AND is_active = true", Integer.class);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Count users by role
     */
    public int countByRole(String role) {
        try {
            return jdbcTemplate1.queryForObject("SELECT COUNT(*) FROM server_users WHERE role = ? AND is_active = true", new Object[]{role}, Integer.class);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Get dashboard statistics
     */
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", countTotal());
        stats.put("totalAdmins", countAdmins());
        stats.put("totalRegularUsers", countByRole("USER"));
        return stats;
    }

    /**
     * Check if NIM already exists
     */
    public boolean existsByNim(String nim) {
        try {
            Integer count = jdbcTemplate1.queryForObject(
                "SELECT COUNT(*) FROM server_users WHERE nim = ?", 
                new Object[]{nim}, Integer.class);
            return count != null && count > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Update password hash
     */
    public void updatePassword(String nim, String passwordHash) throws SQLException {
        String sql = "UPDATE server_users SET password_hash = ?, updated_at = CURRENT_TIMESTAMP WHERE nim = ?";
        jdbcTemplate1.update(sql, new Object[]{passwordHash, nim});
    }

    /**
     * Update nama user
     */
    public void updateNama(String nim, String nama) throws SQLException {
        String sql = "UPDATE server_users SET nama = ?, updated_at = CURRENT_TIMESTAMP WHERE nim = ?";
        jdbcTemplate1.update(sql, new Object[]{nama, nim});
    }
}
