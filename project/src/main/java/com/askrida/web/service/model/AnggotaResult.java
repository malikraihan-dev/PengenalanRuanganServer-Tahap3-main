package com.askrida.web.service.model;

public class AnggotaResult {
    private String nim;
    private String namaLengkap;
    private String kelas;
    private int idDivisi;
    private String namaDivisi;

    public AnggotaResult() {}

    public AnggotaResult(String nim, String namaLengkap, String kelas, int idDivisi) {
        this.nim = nim;
        this.namaLengkap = namaLengkap;
        this.kelas = kelas;
        this.idDivisi = idDivisi;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public int getIdDivisi() {
        return idDivisi;
    }

    public void setIdDivisi(int idDivisi) {
        this.idDivisi = idDivisi;
    }

    public String getNamaDivisi() {
        return namaDivisi;
    }

    public void setNamaDivisi(String namaDivisi) {
        this.namaDivisi = namaDivisi;
    }
}
