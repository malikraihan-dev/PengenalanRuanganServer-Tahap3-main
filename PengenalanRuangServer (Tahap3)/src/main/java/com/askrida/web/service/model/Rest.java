package com.askrida.web.service.model;

import java.util.Date;

public class Rest {
    
	private int id;
    private int rand;
    private String key;
    private String value;
    private String nama;
    private Date waktu_input;
    private String kelas;   // add this
    private String divisi;  // add this
    public String getKelas() {
		return kelas;
	}

	public void setKelas(String kelas) {
		this.kelas = kelas;
	}

	public String getDivisi() {
		return divisi;
	}

	public void setDivisi(String divisi) {
		this.divisi = divisi;
	}

	private int idDivisi;

    public int getIdDivisi() {
        return idDivisi;
    }

    public void setIdDivisi(int idDivisi) {
        this.idDivisi = idDivisi;
    }

    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRand() {
        return rand;
    }

    public void setRand(int rand) {
        this.rand = rand;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Date getWaktu_input() {
        return waktu_input;
    }

    public void setWaktu_input(Date waktu_input) {
        this.waktu_input = waktu_input;
    }

	public char[] getTambah() {
		// TODO Auto-generated method stub
		return null;
	}
}