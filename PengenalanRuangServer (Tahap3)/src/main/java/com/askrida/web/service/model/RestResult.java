package com.askrida.web.service.model;

import java.util.Date;

public class RestResult {

	private int id;
	private int rand;
	private String key;
	private String value;
	private String nama;
	private Date waktu_input;
	private String keterangan;

	public RestResult() {
	}

	public RestResult(int id, int rand, String key, String value, Date waktu_input, String nama) {
		this.id = id;
		this.rand = rand;
		this.key = key;
		this.value = value;
		this.waktu_input = waktu_input;
		this.nama = nama;
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

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
}