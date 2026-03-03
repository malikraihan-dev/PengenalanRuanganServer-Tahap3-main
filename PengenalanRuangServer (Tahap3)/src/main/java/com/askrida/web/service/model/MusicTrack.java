package com.askrida.web.service.model;

import java.sql.Timestamp;

public class MusicTrack {
    private int idTrack;
    private String judul;
    private String artist;
    private String album;
    private String genre;
    private int durasi; // in seconds
    private String fileUrl;
    private String coverUrl;
    private int playCount;
    private Timestamp createdAt;

    public MusicTrack() {}

    public int getIdTrack() { return idTrack; }
    public void setIdTrack(int idTrack) { this.idTrack = idTrack; }

    public String getJudul() { return judul; }
    public void setJudul(String judul) { this.judul = judul; }

    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }

    public String getAlbum() { return album; }
    public void setAlbum(String album) { this.album = album; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public int getDurasi() { return durasi; }
    public void setDurasi(int durasi) { this.durasi = durasi; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public String getCoverUrl() { return coverUrl; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }

    public int getPlayCount() { return playCount; }
    public void setPlayCount(int playCount) { this.playCount = playCount; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getDurasiFormatted() {
        int min = durasi / 60;
        int sec = durasi % 60;
        return String.format("%d:%02d", min, sec);
    }
}
