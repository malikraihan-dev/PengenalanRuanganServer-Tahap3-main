package com.askrida.web.service.repository;

import com.askrida.web.service.conf.JdbcTemplate;
import com.askrida.web.service.model.FaceData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository untuk operasi CRUD data wajah (face descriptor).
 * Face descriptor adalah array 128 float yang dihasilkan oleh face-api.js
 * dan disimpan sebagai JSON string di database.
 */
@Repository
public class FaceRepository {

    @Autowired
    @Qualifier("jdbcTemplate1")
    public JdbcTemplate jdbcTemplate1;

    /**
     * Simpan data wajah baru ke database.
     * Setiap anggota bisa punya beberapa foto wajah untuk meningkatkan akurasi.
     */
    public void saveFaceData(FaceData faceData) throws SQLException {
        String sql = "INSERT INTO face_data (nim, label, face_descriptor, image_data) VALUES (?, ?, ?, ?)";
        Object[] params = new Object[]{
            faceData.getNim(),
            faceData.getLabel(),
            faceData.getFaceDescriptor(),
            faceData.getImageData()
        };
        jdbcTemplate1.update(sql, params);
        jdbcTemplate1.commit();
    }

    /**
     * Ambil semua data wajah dari database.
     * Digunakan untuk membangun "label descriptor" di sisi client untuk face matching.
     */
    public List<FaceData> getAllFaceData() {
        String sql = "SELECT id_face, nim, label, face_descriptor, image_data, created_at FROM face_data ORDER BY nim";
        return jdbcTemplate1.query(sql, (rs, rowNum) -> {
            FaceData fd = new FaceData();
            fd.setIdFace(rs.getInt("id_face"));
            fd.setNim(rs.getString("nim"));
            fd.setLabel(rs.getString("label"));
            fd.setFaceDescriptor(rs.getString("face_descriptor"));
            fd.setImageData(rs.getString("image_data"));
            fd.setCreatedAt(rs.getTimestamp("created_at"));
            return fd;
        });
    }

    /**
     * Ambil face data berdasarkan NIM.
     */
    public List<FaceData> getFaceDataByNim(String nim) {
        String sql = "SELECT id_face, nim, label, face_descriptor, image_data, created_at FROM face_data WHERE nim = ?";
        return jdbcTemplate1.query(sql, new Object[]{nim}, (rs, rowNum) -> {
            FaceData fd = new FaceData();
            fd.setIdFace(rs.getInt("id_face"));
            fd.setNim(rs.getString("nim"));
            fd.setLabel(rs.getString("label"));
            fd.setFaceDescriptor(rs.getString("face_descriptor"));
            fd.setImageData(rs.getString("image_data"));
            fd.setCreatedAt(rs.getTimestamp("created_at"));
            return fd;
        });
    }

    /**
     * Hapus satu data wajah berdasarkan ID.
     */
    public void deleteFaceData(int idFace) throws SQLException {
        String sql = "DELETE FROM face_data WHERE id_face = ?";
        jdbcTemplate1.update(sql, new Object[]{idFace});
        jdbcTemplate1.commit();
    }

    /**
     * Hapus semua data wajah berdasarkan NIM.
     */
    public void deleteFaceDataByNim(String nim) throws SQLException {
        String sql = "DELETE FROM face_data WHERE nim = ?";
        jdbcTemplate1.update(sql, new Object[]{nim});
        jdbcTemplate1.commit();
    }

    /**
     * Hitung jumlah foto wajah per anggota.
     */
    public int countFaceByNim(String nim) {
        String sql = "SELECT COUNT(*) FROM face_data WHERE nim = ?";
        return jdbcTemplate1.queryForObject(sql, new Object[]{nim}, Integer.class);
    }

    /**
     * Catat log absensi wajah.
     */
    public void logFaceAbsensi(String nim, double confidence) throws SQLException {
        String sql = "INSERT INTO face_absensi_log (nim, confidence) VALUES (?, ?)";
        jdbcTemplate1.update(sql, new Object[]{nim, confidence});
        jdbcTemplate1.commit();
    }
}
