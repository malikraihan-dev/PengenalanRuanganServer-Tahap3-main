    package com.askrida.web.service.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.askrida.web.service.conf.JdbcTemplate;
import com.askrida.web.service.model.Rest;
import com.askrida.web.service.model.RestResult;
import com.askrida.web.service.model.AbsensiCheckResult;
import com.askrida.web.service.model.AnggotaResult;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Repository
public class RepositoryTes  {

    @Autowired
    @Qualifier("jdbcTemplate1")
    public JdbcTemplate jdbcTemplate1;

    public List<RestResult> getAll() {
        String sql = "SELECT a.id_absensi AS id, ang.nama_lengkap AS nama, ang.nim AS key, r.nama_ruangan AS value, a.id_ruangan AS rand, a.jam_masuk AS waktu_input FROM absensi_divisi a JOIN anggota ang ON a.nim_anggota = ang.nim JOIN ruangan r ON a.id_ruangan = r.id_ruangan ORDER BY a.jam_masuk DESC";
        return jdbcTemplate1.query(sql, new com.askrida.web.service.model.RestMapper());
    }

    public RestResult getRestById(int id) {
        String sql2 = "SELECT a.id_absensi AS id, ang.nama_lengkap AS nama, ang.nim AS key, r.nama_ruangan AS value, a.id_ruangan AS rand, a.jam_masuk AS waktu_input FROM absensi_divisi a JOIN anggota ang ON a.nim_anggota = ang.nim JOIN ruangan r ON a.id_ruangan = r.id_ruangan WHERE a.id_absensi = ?";
        return jdbcTemplate1.queryForObject(sql2, new Object[]{id}, new com.askrida.web.service.model.RestMapper());
    }

    public void addRest(Rest restexamplecrud) throws SQLException {
        String sql = "INSERT INTO absensi_divisi (nim_anggota, id_ruangan, jam_masuk) VALUES (?, ?, ?)";
        Timestamp waktuSekarang = Timestamp.valueOf(LocalDateTime.now());
        Object[] param = new Object[] { restexamplecrud.getKey(), getRandomNumber(), waktuSekarang };
        jdbcTemplate1.update(sql, param);
        jdbcTemplate1.commit();
    }

    public void updateRest(Rest restexamplecrud, int id) throws SQLException {
        int a = getRandomNumber();
        String sql = "UPDATE absensi_divisi SET nim_anggota=?, id_ruangan=?, jam_keluar=? WHERE id_absensi=?";
        Timestamp waktuKeluar = Timestamp.valueOf(LocalDateTime.now());
        Object[] param = new Object[] { restexamplecrud.getKey(), a, waktuKeluar, id };
        jdbcTemplate1.update(sql, param);

        String sqlNama = "UPDATE anggota SET nama_lengkap=? WHERE nim=?";
        Object[] paramNama = new Object[] { restexamplecrud.getNama(), restexamplecrud.getKey() };
        jdbcTemplate1.update(sqlNama, paramNama);

        jdbcTemplate1.commit();
    }

    public void deleteRestById(int id) throws SQLException {
        String sql = "delete from absensi_divisi where id_absensi=?";
        Object[] param = new Object[] { id };
        jdbcTemplate1.update(sql, param);
        jdbcTemplate1.commit();
    }

    public int getRandomNumber() {
        Random rand = new Random();
        return rand.nextInt(4) + 1;
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

    public List<AnggotaResult> getAllAnggota() {
        String sql = "SELECT a.nim, a.nama_lengkap, a.kelas, a.id_divisi, COALESCE(d.nama_divisi, '-') AS nama_divisi FROM anggota a LEFT JOIN divisi d ON a.id_divisi = d.id_divisi ORDER BY a.nama_lengkap ASC";
        return jdbcTemplate1.query(sql, (rs, rowNum) -> {
            AnggotaResult result = new AnggotaResult();
            result.setNim(rs.getString("nim"));
            result.setNamaLengkap(rs.getString("nama_lengkap"));
            result.setKelas(rs.getString("kelas"));
            result.setIdDivisi(rs.getInt("id_divisi"));
            result.setNamaDivisi(rs.getString("nama_divisi"));
            return result;
        });
    }

    public void deleteAnggota(String nim) throws SQLException {
        String sqlAbsensi = "DELETE FROM absensi_divisi WHERE nim_anggota=?";
        jdbcTemplate1.update(sqlAbsensi, new Object[] { nim });
        String sql = "DELETE FROM anggota WHERE nim=?";
        jdbcTemplate1.update(sql, new Object[] { nim });
        jdbcTemplate1.commit();
    }

    public RestResult handleAbsensi(Rest rest) throws SQLException {
        String sqlCheck = "SELECT id_absensi FROM absensi_divisi WHERE nim_anggota=? AND jam_keluar IS NULL ORDER BY jam_masuk DESC LIMIT 1";
        List<Integer> ids = jdbcTemplate1.query(sqlCheck, new Object[]{rest.getKey()}, (rs, rowNum) -> rs.getInt("id_absensi"));

        if (ids.isEmpty()) {
            String sqlInsert = "INSERT INTO absensi_divisi (nim_anggota, id_ruangan, jam_masuk) VALUES (?, ?, ?)";
            Timestamp now = Timestamp.valueOf(LocalDateTime.now());
            jdbcTemplate1.update(sqlInsert, new Object[]{rest.getKey(), getRandomNumber(), now});
            jdbcTemplate1.commit();
            int newId = lastestInput();
            RestResult result = getRestById(newId);
            result.setKeterangan("Absen masuk berhasil");
            return result;
        } else {
            int idAbsensi = ids.get(0);
            String sqlUpdate = "UPDATE absensi_divisi SET jam_keluar=? WHERE id_absensi=?";
            Timestamp now = Timestamp.valueOf(LocalDateTime.now());
            jdbcTemplate1.update(sqlUpdate, new Object[]{now, idAbsensi});
            jdbcTemplate1.commit();
            RestResult result = getRestById(idAbsensi);
            result.setKeterangan("Absen keluar berhasil");
            return result;
        }
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

    /**
     * Cari anggota berdasarkan NIM
     */
    public AnggotaResult findAnggotaByNim(String nim) {
        String sql = "SELECT a.nim, a.nama_lengkap, a.kelas, a.id_divisi, COALESCE(d.nama_divisi, '-') AS nama_divisi FROM anggota a LEFT JOIN divisi d ON a.id_divisi = d.id_divisi WHERE a.nim = ?";
        List<AnggotaResult> results = jdbcTemplate1.query(sql, new Object[]{nim}, (rs, rowNum) -> {
            AnggotaResult result = new AnggotaResult();
            result.setNim(rs.getString("nim"));
            result.setNamaLengkap(rs.getString("nama_lengkap"));
            result.setKelas(rs.getString("kelas"));
            result.setIdDivisi(rs.getInt("id_divisi"));
            result.setNamaDivisi(rs.getString("nama_divisi"));
            return result;
        });
        return results.isEmpty() ? null : results.get(0);
    }
}
