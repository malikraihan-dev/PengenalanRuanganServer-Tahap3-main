
// src/main/java/com/askrida/web/service/model/AbsensiCheckResult.java
package com.askrida.web.service.model;

import java.util.Date;

public class AbsensiCheckResult {
    private String nimAnggota;
    private String namaRuangan;
    private Date jamMasuk;
    private Date jamKeluar;

    public String getNimAnggota() {
        return nimAnggota;
    }

    public void setNimAnggota(String nimAnggota) {
        this.nimAnggota = nimAnggota;
    }

    public String getNamaRuangan() {
        return namaRuangan;
    }

    public void setNamaRuangan(String namaRuangan) {
        this.namaRuangan = namaRuangan;
    }

    public Date getJamMasuk() {
        return jamMasuk;
    }

    public void setJamMasuk(Date jamMasuk) {
        this.jamMasuk = jamMasuk;
    }

    public Date getJamKeluar() {
        return jamKeluar;
    }

    public void setJamKeluar(Date jamKeluar) {
        this.jamKeluar = jamKeluar;
    }
}
