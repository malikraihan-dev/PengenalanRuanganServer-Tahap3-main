package com.askrida.web.service.repository;

import com.askrida.web.service.conf.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

/**
 * Repository untuk mapping fingerprint_id -> nim.
 */
@Repository
public class FingerprintMapRepository {

    @Autowired
    @Qualifier("jdbcTemplate1")
    public JdbcTemplate jdbcTemplate1;

    public void upsertMapping(String nim, int fingerprintId) throws SQLException {
        String sql = "INSERT INTO server_fingerprint_map (nim, fingerprint_id) VALUES (?, ?) " +
                     "ON CONFLICT (fingerprint_id) DO UPDATE SET nim = EXCLUDED.nim";
        jdbcTemplate1.update(sql, new Object[]{nim, fingerprintId});
    }

    public String findNimByFingerprintId(int fingerprintId) {
        String sql = "SELECT nim FROM server_fingerprint_map WHERE fingerprint_id = ?";
        try {
            return jdbcTemplate1.queryForObject(sql, new Object[]{fingerprintId}, String.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
