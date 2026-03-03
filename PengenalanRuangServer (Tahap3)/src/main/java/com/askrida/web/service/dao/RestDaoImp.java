package com.askrida.web.service.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.askrida.web.service.conf.JdbcTemplate;
import com.askrida.web.service.model.Rest;
import com.askrida.web.service.model.RestMapper;
import com.askrida.web.service.model.RestResult;
import com.askrida.web.service.model.AbsensiCheckResult;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.time.LocalDateTime;
import java.sql.Timestamp;

@Repository
public class RestDaoImp  {

    @Autowired
    @Qualifier("jdbcTemplate1")
    private JdbcTemplate jdbcTemplate1;

    public List<RestResult> getAll() {
        String sql = "SELECT a.id_absensi AS id, ang.nama_lengkap AS nama, ang.nim AS key, r.nama_ruangan AS value, a.id_ruangan AS rand, a.jam_masuk AS waktu_input FROM absensi_divisi a JOIN anggota ang ON a.nim_anggota = ang.nim JOIN ruangan r ON a.id_ruangan = r.id_ruangan ORDER BY a.jam_masuk DESC";
        return jdbcTemplate1.query(sql, new RestMapper());
    }

    public RestResult getRestById(int id) {
        String sql2 = "SELECT a.id_absensi AS id, ang.nama_lengkap AS nama, ang.nim AS key, r.nama_ruangan AS value, a.id_ruangan AS rand, a.jam_masuk AS waktu_input FROM absensi_divisi a JOIN anggota ang ON a.nim_anggota = ang.nim JOIN ruangan r ON a.id_ruangan = r.id_ruangan WHERE a.id_absensi = ?";
        return jdbcTemplate1.queryForObject(sql2, new Object[]{id}, new RestMapper());
    }

    public void addRest(Rest restexamplecrud) throws SQLException {
        String sql = "INSERT INTO absensi_divisi (nim_anggota, id_ruangan, jam_masuk) VALUES (?, ?, ?)";
        Timestamp waktuSekarang = Timestamp.valueOf(LocalDateTime.now());
        Object[] param = new Object[] { restexamplecrud.getKey(), getRandomNumber(), waktuSekarang };
        jdbcTemplate1.update(sql, param);
    }

    public void updateRest(Rest restexamplecrud, int id) throws SQLException {
        int a = getRandomNumber();
        String sql = "UPDATE absensi_divisi SET nim_anggota=?, id_ruangan=?, jam_keluar=? WHERE id_absensi=?";
        Timestamp waktuKeluar = Timestamp.valueOf(LocalDateTime.now());
        Object[] param = new Object[] { restexamplecrud.getKey(), a, waktuKeluar, id };
        jdbcTemplate1.update(sql, param);
        jdbcTemplate1.commit();
    }

    public void deleteRestById(int id) throws SQLException {
        String sql = "DELETE FROM absensi_divisi WHERE id_absensi=?";
        Object[] param = new Object[] { id };
        jdbcTemplate1.update(sql, param);
        jdbcTemplate1.commit();
    }

    private int getRandomNumber() {
        Random rand = new Random();
        return rand.nextInt(4) + 1;
    }

    public void simpanDataOtomatis() {
        String sql = "INSERT INTO absensi_divisi (nim_anggota, id_ruangan, jam_masuk) VALUES (?, NOW())";
        jdbcTemplate1.update(sql);
    }

    public int lastestInput() {
        String sql2 = "SELECT MAX(id_absensi) FROM absensi_divisi";
        return jdbcTemplate1.queryForObject(sql2, Integer.class);
    }

    public void addAnggota(String nim, String nama_lengkap, String kelas, int idDivisi) throws SQLException {
        String sql = "INSERT INTO anggota (nim, nama_lengkap, kelas, id_divisi) VALUES (?, ?, ?, ?)";
        Object[] param = new Object[] { nim, nama_lengkap, kelas, idDivisi };
        jdbcTemplate1.update(sql, param);
        jdbcTemplate1.commit();
    }

    public void addAnggotaBatch(List<Rest> anggotaList) throws SQLException {
        String sql = "INSERT INTO anggota (nim, nama_lengkap, kelas, id_divisi) VALUES (?, ?, ?, ?)";
        for (Rest anggota : anggotaList) {
            Object[] param = new Object[] { anggota.getKey(), anggota.getNama(), anggota.getKelas(), anggota.getIdDivisi() };
            jdbcTemplate1.update(sql, param);
        }
        jdbcTemplate1.commit();
    }

    public void updateAnggota(String nim, String nama_lengkap, String kelas, int idDivisi) throws SQLException {
        String sql = "UPDATE anggota SET nama_lengkap=?, kelas=?, id_divisi=? WHERE nim=?";
        Object[] param = new Object[] { nama_lengkap, kelas, idDivisi, nim };
        jdbcTemplate1.update(sql, param);
        jdbcTemplate1.commit();
    }

    public List<String> checkAbsensiKeys(List<String> keys) {
        String inSql = String.join(",", java.util.Collections.nCopies(keys.size(), "?"));
        String sql = "SELECT DISTINCT nim_anggota FROM absensi_divisi WHERE nim_anggota IN (" + inSql + ")";
        return jdbcTemplate1.query(sql, keys.toArray(), (rs, rowNum) -> rs.getString("nim_anggota"));
    }

    public List<AbsensiCheckResult> checkAbsensiDetails(List<String> keys) {
        String inSql = String.join(",", java.util.Collections.nCopies(keys.size(), "?"));
        String sql = "SELECT a.nim_anggota, r.nama_ruangan, a.jam_masuk, a.jam_keluar FROM absensi_divisi a JOIN ruangan r ON a.id_ruangan = r.id_ruangan WHERE a.nim_anggota IN (" + inSql + ")";
        return jdbcTemplate1.query(sql, keys.toArray(), (rs, rowNum) -> {
            AbsensiCheckResult result = new AbsensiCheckResult();
            result.setNimAnggota(rs.getString("nim_anggota"));
            result.setNamaRuangan(rs.getString("nama_ruangan"));
            result.setJamMasuk(rs.getTimestamp("jam_masuk"));
            result.setJamKeluar(rs.getTimestamp("jam_keluar"));
            return result;
        });
    }
}
