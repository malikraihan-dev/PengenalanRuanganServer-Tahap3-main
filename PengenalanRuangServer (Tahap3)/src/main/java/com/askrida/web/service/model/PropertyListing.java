package com.askrida.web.service.model;

import java.sql.Timestamp;

public class PropertyListing {
    private int idProperty;
    private String judul;
    private String deskripsi;
    private String tipeProperti;
    private long harga;
    private int luasTanah;
    private int luasBangunan;
    private int jumlahKamar;
    private int jumlahKamarMandi;
    private String alamat;
    private String kota;
    private String provinsi;
    private String statusListing;
    private String namaPemilik;
    private String teleponPemilik;
    private String gambarUrl;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public PropertyListing() {}

    public int getIdProperty() { return idProperty; }
    public void setIdProperty(int idProperty) { this.idProperty = idProperty; }

    public String getJudul() { return judul; }
    public void setJudul(String judul) { this.judul = judul; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    public String getTipeProperti() { return tipeProperti; }
    public void setTipeProperti(String tipeProperti) { this.tipeProperti = tipeProperti; }

    public long getHarga() { return harga; }
    public void setHarga(long harga) { this.harga = harga; }

    public int getLuasTanah() { return luasTanah; }
    public void setLuasTanah(int luasTanah) { this.luasTanah = luasTanah; }

    public int getLuasBangunan() { return luasBangunan; }
    public void setLuasBangunan(int luasBangunan) { this.luasBangunan = luasBangunan; }

    public int getJumlahKamar() { return jumlahKamar; }
    public void setJumlahKamar(int jumlahKamar) { this.jumlahKamar = jumlahKamar; }

    public int getJumlahKamarMandi() { return jumlahKamarMandi; }
    public void setJumlahKamarMandi(int jumlahKamarMandi) { this.jumlahKamarMandi = jumlahKamarMandi; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    public String getKota() { return kota; }
    public void setKota(String kota) { this.kota = kota; }

    public String getProvinsi() { return provinsi; }
    public void setProvinsi(String provinsi) { this.provinsi = provinsi; }

    public String getStatusListing() { return statusListing; }
    public void setStatusListing(String statusListing) { this.statusListing = statusListing; }

    public String getNamaPemilik() { return namaPemilik; }
    public void setNamaPemilik(String namaPemilik) { this.namaPemilik = namaPemilik; }

    public String getTeleponPemilik() { return teleponPemilik; }
    public void setTeleponPemilik(String teleponPemilik) { this.teleponPemilik = teleponPemilik; }

    public String getGambarUrl() { return gambarUrl; }
    public void setGambarUrl(String gambarUrl) { this.gambarUrl = gambarUrl; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}
