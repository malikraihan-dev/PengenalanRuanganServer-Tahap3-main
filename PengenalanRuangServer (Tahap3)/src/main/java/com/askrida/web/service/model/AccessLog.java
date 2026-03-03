package com.askrida.web.service.model;

import java.sql.Timestamp;

/**
 * Entity untuk Access Log - Mencatat aktivitas akses ruang server
 */
public class AccessLog {
    private int id;
    private Integer userId;
    private String nim;
    private String nama;
    private String role;
    private String status; // GRANTED or DENIED
    private double confidence;
    private String method;
    private String ipAddress;
    private String notes;
    private Timestamp createdAt;

    // Formatted fields for display
    private String tanggal;
    private String jam;

    public AccessLog() {}

    public AccessLog(String nim, String nama, String role, String status, double confidence) {
        this.nim = nim;
        this.nama = nama;
        this.role = role;
        this.status = status;
        this.confidence = confidence;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getNim() { return nim; }
    public void setNim(String nim) { this.nim = nim; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getConfidence() { return confidence; }
    public void setConfidence(double confidence) { this.confidence = confidence; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getTanggal() { return tanggal; }
    public void setTanggal(String tanggal) { this.tanggal = tanggal; }

    public String getJam() { return jam; }
    public void setJam(String jam) { this.jam = jam; }

    public boolean isGranted() {
        return "GRANTED".equalsIgnoreCase(this.status);
    }

    public String getConfidencePercent() {
        return String.format("%.1f%%", this.confidence * 100);
    }
}
