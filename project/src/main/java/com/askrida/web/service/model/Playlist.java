package com.askrida.web.service.model;

import java.sql.Timestamp;

public class Playlist {
    private int idPlaylist;
    private String namaPlaylist;
    private String deskripsi;
    private String coverUrl;
    private int totalTracks;
    private Timestamp createdAt;

    public Playlist() {}

    public int getIdPlaylist() { return idPlaylist; }
    public void setIdPlaylist(int idPlaylist) { this.idPlaylist = idPlaylist; }

    public String getNamaPlaylist() { return namaPlaylist; }
    public void setNamaPlaylist(String namaPlaylist) { this.namaPlaylist = namaPlaylist; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    public String getCoverUrl() { return coverUrl; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }

    public int getTotalTracks() { return totalTracks; }
    public void setTotalTracks(int totalTracks) { this.totalTracks = totalTracks; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
