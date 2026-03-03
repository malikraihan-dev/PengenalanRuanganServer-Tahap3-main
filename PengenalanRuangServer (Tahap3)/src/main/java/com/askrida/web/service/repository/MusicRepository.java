package com.askrida.web.service.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.askrida.web.service.conf.JdbcTemplate;
import com.askrida.web.service.model.MusicTrack;
import com.askrida.web.service.model.Playlist;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class MusicRepository {

    @Autowired
    @Qualifier("jdbcTemplate1")
    public JdbcTemplate jdbcTemplate1;

    // ===== TRACKS =====
    public List<MusicTrack> getAllTracks() {
        String sql = "SELECT * FROM music_tracks ORDER BY created_at DESC";
        return jdbcTemplate1.query(sql, (rs, rowNum) -> {
            MusicTrack t = new MusicTrack();
            t.setIdTrack(rs.getInt("id_track"));
            t.setJudul(rs.getString("judul"));
            t.setArtist(rs.getString("artist"));
            t.setAlbum(rs.getString("album"));
            t.setGenre(rs.getString("genre"));
            t.setDurasi(rs.getInt("durasi"));
            t.setFileUrl(rs.getString("file_url"));
            t.setCoverUrl(rs.getString("cover_url"));
            t.setPlayCount(rs.getInt("play_count"));
            t.setCreatedAt(rs.getTimestamp("created_at"));
            return t;
        });
    }

    public MusicTrack getTrackById(int id) {
        String sql = "SELECT * FROM music_tracks WHERE id_track = ?";
        return jdbcTemplate1.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            MusicTrack t = new MusicTrack();
            t.setIdTrack(rs.getInt("id_track"));
            t.setJudul(rs.getString("judul"));
            t.setArtist(rs.getString("artist"));
            t.setAlbum(rs.getString("album"));
            t.setGenre(rs.getString("genre"));
            t.setDurasi(rs.getInt("durasi"));
            t.setFileUrl(rs.getString("file_url"));
            t.setCoverUrl(rs.getString("cover_url"));
            t.setPlayCount(rs.getInt("play_count"));
            t.setCreatedAt(rs.getTimestamp("created_at"));
            return t;
        });
    }

    public List<MusicTrack> searchTracks(String keyword) {
        String sql = "SELECT * FROM music_tracks WHERE LOWER(judul) LIKE LOWER(?) OR LOWER(artist) LIKE LOWER(?) OR LOWER(album) LIKE LOWER(?) ORDER BY play_count DESC";
        String param = "%" + keyword + "%";
        return jdbcTemplate1.query(sql, new Object[]{param, param, param}, (rs, rowNum) -> {
            MusicTrack t = new MusicTrack();
            t.setIdTrack(rs.getInt("id_track"));
            t.setJudul(rs.getString("judul"));
            t.setArtist(rs.getString("artist"));
            t.setAlbum(rs.getString("album"));
            t.setGenre(rs.getString("genre"));
            t.setDurasi(rs.getInt("durasi"));
            t.setFileUrl(rs.getString("file_url"));
            t.setCoverUrl(rs.getString("cover_url"));
            t.setPlayCount(rs.getInt("play_count"));
            t.setCreatedAt(rs.getTimestamp("created_at"));
            return t;
        });
    }

    public List<MusicTrack> getTracksByGenre(String genre) {
        String sql = "SELECT * FROM music_tracks WHERE genre = ? ORDER BY play_count DESC";
        return jdbcTemplate1.query(sql, new Object[]{genre}, (rs, rowNum) -> {
            MusicTrack t = new MusicTrack();
            t.setIdTrack(rs.getInt("id_track"));
            t.setJudul(rs.getString("judul"));
            t.setArtist(rs.getString("artist"));
            t.setAlbum(rs.getString("album"));
            t.setGenre(rs.getString("genre"));
            t.setDurasi(rs.getInt("durasi"));
            t.setFileUrl(rs.getString("file_url"));
            t.setCoverUrl(rs.getString("cover_url"));
            t.setPlayCount(rs.getInt("play_count"));
            t.setCreatedAt(rs.getTimestamp("created_at"));
            return t;
        });
    }

    public void addTrack(MusicTrack track) throws SQLException {
        String sql = "INSERT INTO music_tracks (judul, artist, album, genre, durasi, file_url, cover_url) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Object[] params = new Object[]{
            track.getJudul(), track.getArtist(), track.getAlbum(),
            track.getGenre(), track.getDurasi(), track.getFileUrl(), track.getCoverUrl()
        };
        jdbcTemplate1.update(sql, params);
        jdbcTemplate1.commit();
    }

    public void updateTrack(MusicTrack track) throws SQLException {
        String sql = "UPDATE music_tracks SET judul=?, artist=?, album=?, genre=?, durasi=?, file_url=?, cover_url=? WHERE id_track=?";
        Object[] params = new Object[]{
            track.getJudul(), track.getArtist(), track.getAlbum(),
            track.getGenre(), track.getDurasi(), track.getFileUrl(), track.getCoverUrl(),
            track.getIdTrack()
        };
        jdbcTemplate1.update(sql, params);
        jdbcTemplate1.commit();
    }

    public void deleteTrack(int id) throws SQLException {
        jdbcTemplate1.update("DELETE FROM music_tracks WHERE id_track=?", new Object[]{id});
        jdbcTemplate1.commit();
    }

    public void incrementPlayCount(int id) throws SQLException {
        jdbcTemplate1.update("UPDATE music_tracks SET play_count = play_count + 1 WHERE id_track=?", new Object[]{id});
        jdbcTemplate1.commit();
    }

    public int countTracks() {
        try {
            Integer count = jdbcTemplate1.queryForObject("SELECT COUNT(*) FROM music_tracks", Integer.class);
            return count != null ? count : 0;
        } catch (Exception e) { return 0; }
    }

    public List<String> getAllGenres() {
        return jdbcTemplate1.query("SELECT DISTINCT genre FROM music_tracks WHERE genre IS NOT NULL ORDER BY genre",
            (rs, rowNum) -> rs.getString("genre"));
    }

    // ===== PLAYLISTS =====
    public List<Playlist> getAllPlaylists() {
        String sql = "SELECT p.*, (SELECT COUNT(*) FROM playlist_tracks pt WHERE pt.id_playlist = p.id_playlist) AS total_tracks FROM playlists p ORDER BY p.created_at DESC";
        return jdbcTemplate1.query(sql, (rs, rowNum) -> {
            Playlist pl = new Playlist();
            pl.setIdPlaylist(rs.getInt("id_playlist"));
            pl.setNamaPlaylist(rs.getString("nama_playlist"));
            pl.setDeskripsi(rs.getString("deskripsi"));
            pl.setCoverUrl(rs.getString("cover_url"));
            pl.setTotalTracks(rs.getInt("total_tracks"));
            pl.setCreatedAt(rs.getTimestamp("created_at"));
            return pl;
        });
    }

    public void addPlaylist(Playlist pl) throws SQLException {
        String sql = "INSERT INTO playlists (nama_playlist, deskripsi, cover_url) VALUES (?, ?, ?)";
        jdbcTemplate1.update(sql, new Object[]{pl.getNamaPlaylist(), pl.getDeskripsi(), pl.getCoverUrl()});
        jdbcTemplate1.commit();
    }

    public void deletePlaylist(int id) throws SQLException {
        jdbcTemplate1.update("DELETE FROM playlists WHERE id_playlist=?", new Object[]{id});
        jdbcTemplate1.commit();
    }

    public List<MusicTrack> getTracksByPlaylist(int playlistId) {
        String sql = "SELECT mt.* FROM music_tracks mt JOIN playlist_tracks pt ON mt.id_track = pt.id_track WHERE pt.id_playlist = ? ORDER BY pt.track_order";
        return jdbcTemplate1.query(sql, new Object[]{playlistId}, (rs, rowNum) -> {
            MusicTrack t = new MusicTrack();
            t.setIdTrack(rs.getInt("id_track"));
            t.setJudul(rs.getString("judul"));
            t.setArtist(rs.getString("artist"));
            t.setAlbum(rs.getString("album"));
            t.setGenre(rs.getString("genre"));
            t.setDurasi(rs.getInt("durasi"));
            t.setFileUrl(rs.getString("file_url"));
            t.setCoverUrl(rs.getString("cover_url"));
            t.setPlayCount(rs.getInt("play_count"));
            t.setCreatedAt(rs.getTimestamp("created_at"));
            return t;
        });
    }

    public void addTrackToPlaylist(int playlistId, int trackId) throws SQLException {
        Integer maxOrder = jdbcTemplate1.queryForObject(
            "SELECT COALESCE(MAX(track_order), 0) FROM playlist_tracks WHERE id_playlist=?",
            new Object[]{playlistId}, Integer.class);
        int newOrder = (maxOrder != null ? maxOrder : 0) + 1;
        jdbcTemplate1.update("INSERT INTO playlist_tracks (id_playlist, id_track, track_order) VALUES (?, ?, ?)",
            new Object[]{playlistId, trackId, newOrder});
        jdbcTemplate1.commit();
    }

    public void removeTrackFromPlaylist(int playlistId, int trackId) throws SQLException {
        jdbcTemplate1.update("DELETE FROM playlist_tracks WHERE id_playlist=? AND id_track=?",
            new Object[]{playlistId, trackId});
        jdbcTemplate1.commit();
    }

    public int countPlaylists() {
        try {
            Integer count = jdbcTemplate1.queryForObject("SELECT COUNT(*) FROM playlists", Integer.class);
            return count != null ? count : 0;
        } catch (Exception e) { return 0; }
    }
}
