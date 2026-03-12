package com.askrida.web.service.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class ExpenseItem {
    private int idItem;
    private int idReport;
    private String namaItem;
    private String kategori; // Transport, Makan, Akomodasi, Supplies, Entertainment, Lainnya
    private BigDecimal jumlah;
    private String tanggal;
    private String keterangan;
    private String buktiUrl;
    private Timestamp createdAt;

    public ExpenseItem() {}

    public int getIdItem() { return idItem; }
    public void setIdItem(int idItem) { this.idItem = idItem; }

    public int getIdReport() { return idReport; }
    public void setIdReport(int idReport) { this.idReport = idReport; }

    public String getNamaItem() { return namaItem; }
    public void setNamaItem(String namaItem) { this.namaItem = namaItem; }

    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }

    public BigDecimal getJumlah() { return jumlah; }
    public void setJumlah(BigDecimal jumlah) { this.jumlah = jumlah; }

    public String getTanggal() { return tanggal; }
    public void setTanggal(String tanggal) { this.tanggal = tanggal; }

    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }

    public String getBuktiUrl() { return buktiUrl; }
    public void setBuktiUrl(String buktiUrl) { this.buktiUrl = buktiUrl; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
