package com.askrida.web.service.model;

import java.sql.Timestamp;

public class ProjectModel {
    private int idProject;
    private String namaProject;
    private String deskripsi;
    private String statusProject;
    private String prioritas;
    private String tanggalMulai;
    private String tanggalSelesai;
    private Timestamp createdAt;
    private int totalTasks;
    private int completedTasks;

    public ProjectModel() {}

    public int getIdProject() { return idProject; }
    public void setIdProject(int idProject) { this.idProject = idProject; }

    public String getNamaProject() { return namaProject; }
    public void setNamaProject(String namaProject) { this.namaProject = namaProject; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    public String getStatusProject() { return statusProject; }
    public void setStatusProject(String statusProject) { this.statusProject = statusProject; }

    public String getPrioritas() { return prioritas; }
    public void setPrioritas(String prioritas) { this.prioritas = prioritas; }

    public String getTanggalMulai() { return tanggalMulai; }
    public void setTanggalMulai(String tanggalMulai) { this.tanggalMulai = tanggalMulai; }

    public String getTanggalSelesai() { return tanggalSelesai; }
    public void setTanggalSelesai(String tanggalSelesai) { this.tanggalSelesai = tanggalSelesai; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public int getTotalTasks() { return totalTasks; }
    public void setTotalTasks(int totalTasks) { this.totalTasks = totalTasks; }

    public int getCompletedTasks() { return completedTasks; }
    public void setCompletedTasks(int completedTasks) { this.completedTasks = completedTasks; }
}
