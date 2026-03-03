package com.askrida.web.service.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.askrida.web.service.conf.JdbcTemplate;
import com.askrida.web.service.model.PropertyListing;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PropertyRepository {

    @Autowired
    @Qualifier("jdbcTemplate1")
    public JdbcTemplate jdbcTemplate1;

    public List<PropertyListing> getAll() {
        String sql = "SELECT * FROM property_listing ORDER BY created_at DESC";
        return jdbcTemplate1.query(sql, (rs, rowNum) -> {
            PropertyListing p = new PropertyListing();
            p.setIdProperty(rs.getInt("id_property"));
            p.setJudul(rs.getString("judul"));
            p.setDeskripsi(rs.getString("deskripsi"));
            p.setTipeProperti(rs.getString("tipe_properti"));
            p.setHarga(rs.getLong("harga"));
            p.setLuasTanah(rs.getInt("luas_tanah"));
            p.setLuasBangunan(rs.getInt("luas_bangunan"));
            p.setJumlahKamar(rs.getInt("jumlah_kamar"));
            p.setJumlahKamarMandi(rs.getInt("jumlah_kamar_mandi"));
            p.setAlamat(rs.getString("alamat"));
            p.setKota(rs.getString("kota"));
            p.setProvinsi(rs.getString("provinsi"));
            p.setStatusListing(rs.getString("status_listing"));
            p.setNamaPemilik(rs.getString("nama_pemilik"));
            p.setTeleponPemilik(rs.getString("telepon_pemilik"));
            p.setGambarUrl(rs.getString("gambar_url"));
            p.setCreatedAt(rs.getTimestamp("created_at"));
            p.setUpdatedAt(rs.getTimestamp("updated_at"));
            return p;
        });
    }

    public PropertyListing getById(int id) {
        String sql = "SELECT * FROM property_listing WHERE id_property = ?";
        return jdbcTemplate1.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            PropertyListing p = new PropertyListing();
            p.setIdProperty(rs.getInt("id_property"));
            p.setJudul(rs.getString("judul"));
            p.setDeskripsi(rs.getString("deskripsi"));
            p.setTipeProperti(rs.getString("tipe_properti"));
            p.setHarga(rs.getLong("harga"));
            p.setLuasTanah(rs.getInt("luas_tanah"));
            p.setLuasBangunan(rs.getInt("luas_bangunan"));
            p.setJumlahKamar(rs.getInt("jumlah_kamar"));
            p.setJumlahKamarMandi(rs.getInt("jumlah_kamar_mandi"));
            p.setAlamat(rs.getString("alamat"));
            p.setKota(rs.getString("kota"));
            p.setProvinsi(rs.getString("provinsi"));
            p.setStatusListing(rs.getString("status_listing"));
            p.setNamaPemilik(rs.getString("nama_pemilik"));
            p.setTeleponPemilik(rs.getString("telepon_pemilik"));
            p.setGambarUrl(rs.getString("gambar_url"));
            p.setCreatedAt(rs.getTimestamp("created_at"));
            p.setUpdatedAt(rs.getTimestamp("updated_at"));
            return p;
        });
    }

    public List<PropertyListing> searchByKota(String kota) {
        String sql = "SELECT * FROM property_listing WHERE LOWER(kota) LIKE LOWER(?) ORDER BY created_at DESC";
        return jdbcTemplate1.query(sql, new Object[]{"%" + kota + "%"}, (rs, rowNum) -> {
            PropertyListing p = new PropertyListing();
            p.setIdProperty(rs.getInt("id_property"));
            p.setJudul(rs.getString("judul"));
            p.setDeskripsi(rs.getString("deskripsi"));
            p.setTipeProperti(rs.getString("tipe_properti"));
            p.setHarga(rs.getLong("harga"));
            p.setLuasTanah(rs.getInt("luas_tanah"));
            p.setLuasBangunan(rs.getInt("luas_bangunan"));
            p.setJumlahKamar(rs.getInt("jumlah_kamar"));
            p.setJumlahKamarMandi(rs.getInt("jumlah_kamar_mandi"));
            p.setAlamat(rs.getString("alamat"));
            p.setKota(rs.getString("kota"));
            p.setProvinsi(rs.getString("provinsi"));
            p.setStatusListing(rs.getString("status_listing"));
            p.setNamaPemilik(rs.getString("nama_pemilik"));
            p.setTeleponPemilik(rs.getString("telepon_pemilik"));
            p.setGambarUrl(rs.getString("gambar_url"));
            p.setCreatedAt(rs.getTimestamp("created_at"));
            p.setUpdatedAt(rs.getTimestamp("updated_at"));
            return p;
        });
    }

    public void addProperty(PropertyListing p) throws SQLException {
        String sql = "INSERT INTO property_listing (judul, deskripsi, tipe_properti, harga, luas_tanah, luas_bangunan, jumlah_kamar, jumlah_kamar_mandi, alamat, kota, provinsi, status_listing, nama_pemilik, telepon_pemilik, gambar_url) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] params = new Object[]{
            p.getJudul(), p.getDeskripsi(), p.getTipeProperti(), p.getHarga(),
            p.getLuasTanah(), p.getLuasBangunan(), p.getJumlahKamar(), p.getJumlahKamarMandi(),
            p.getAlamat(), p.getKota(), p.getProvinsi(),
            p.getStatusListing() != null ? p.getStatusListing() : "Tersedia",
            p.getNamaPemilik(), p.getTeleponPemilik(), p.getGambarUrl()
        };
        jdbcTemplate1.update(sql, params);
        jdbcTemplate1.commit();
    }

    public void updateProperty(PropertyListing p) throws SQLException {
        String sql = "UPDATE property_listing SET judul=?, deskripsi=?, tipe_properti=?, harga=?, luas_tanah=?, luas_bangunan=?, jumlah_kamar=?, jumlah_kamar_mandi=?, alamat=?, kota=?, provinsi=?, status_listing=?, nama_pemilik=?, telepon_pemilik=?, gambar_url=?, updated_at=? WHERE id_property=?";
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        Object[] params = new Object[]{
            p.getJudul(), p.getDeskripsi(), p.getTipeProperti(), p.getHarga(),
            p.getLuasTanah(), p.getLuasBangunan(), p.getJumlahKamar(), p.getJumlahKamarMandi(),
            p.getAlamat(), p.getKota(), p.getProvinsi(), p.getStatusListing(),
            p.getNamaPemilik(), p.getTeleponPemilik(), p.getGambarUrl(), now, p.getIdProperty()
        };
        jdbcTemplate1.update(sql, params);
        jdbcTemplate1.commit();
    }

    public void deleteProperty(int id) throws SQLException {
        String sql = "DELETE FROM property_listing WHERE id_property=?";
        jdbcTemplate1.update(sql, new Object[]{id});
        jdbcTemplate1.commit();
    }

    public int countAll() {
        String sql = "SELECT COUNT(*) FROM property_listing";
        return jdbcTemplate1.queryForObject(sql, Integer.class);
    }

    public int countByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM property_listing WHERE status_listing=?";
        return jdbcTemplate1.queryForObject(sql, new Object[]{status}, Integer.class);
    }
}
