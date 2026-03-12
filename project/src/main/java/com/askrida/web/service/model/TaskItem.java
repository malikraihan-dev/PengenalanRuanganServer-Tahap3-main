package com.askrida.web.service.model;

import java.sql.Timestamp;

public class TaskItem {
    private int idTask;
    private int idProject;
    private String judulTask;
    private String deskripsi;
    private String statusTask;
    private String prioritas;
    private String assignee;
    private String tanggalDeadline;
    private Timestamp createdAt;
    private String namaProject;

    public TaskItem() {}

    public int getIdTask() { return idTask; }
    public void setIdTask(int idTask) { this.idTask = idTask; }

    public int getIdProject() { return idProject; }
    public void setIdProject(int idProject) { this.idProject = idProject; }

    public String getJudulTask() { return judulTask; }
    public void setJudulTask(String judulTask) { this.judulTask = judulTask; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    public String getStatusTask() { return statusTask; }
    public void setStatusTask(String statusTask) { this.statusTask = statusTask; }

    public String getPrioritas() { return prioritas; }
    public void setPrioritas(String prioritas) { this.prioritas = prioritas; }

    public String getAssignee() { return assignee; }
    public void setAssignee(String assignee) { this.assignee = assignee; }

    public String getTanggalDeadline() { return tanggalDeadline; }
    public void setTanggalDeadline(String tanggalDeadline) { this.tanggalDeadline = tanggalDeadline; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getNamaProject() { return namaProject; }
    public void setNamaProject(String namaProject) { this.namaProject = namaProject; }
}
