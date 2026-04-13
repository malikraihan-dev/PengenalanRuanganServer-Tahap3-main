package com.askrida.web.service.model;

import java.sql.Timestamp;

/**
 * Entity untuk Server Room User dengan Role-Based Access Control
 */
public class ServerUser {
    private int id;
    private String nim;
    private String nama;
    private String role; // USER or ADMIN
    private String passwordHash;
    private String faceEmbedding;
    private String photoUrl;
    private boolean isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Fingerprint fields
    private Integer fingerprintId;      // ID fingerprint di sensor Arduino
    private String fingerprintData;     // Template fingerprint (binary/hex encoded)
    private boolean fingerprintEnabled; // Status fingerprint
    private Timestamp fingerprintEnrolledAt;

    // Transient fields
    private int faceCount;
    private int fingerprintCount;

    public ServerUser() {}

    public ServerUser(String nim, String nama, String role) {
        this.nim = nim;
        this.nama = nama;
        this.role = role;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNim() { return nim; }
    public void setNim(String nim) { this.nim = nim; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getFaceEmbedding() { return faceEmbedding; }
    public void setFaceEmbedding(String faceEmbedding) { this.faceEmbedding = faceEmbedding; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public int getFaceCount() { return faceCount; }
    public void setFaceCount(int faceCount) { this.faceCount = faceCount; }

    public Integer getFingerprintId() { return fingerprintId; }
    public void setFingerprintId(Integer fingerprintId) { this.fingerprintId = fingerprintId; }

    public String getFingerprintData() { return fingerprintData; }
    public void setFingerprintData(String fingerprintData) { this.fingerprintData = fingerprintData; }

    public boolean isFingerprintEnabled() { return fingerprintEnabled; }
    public void setFingerprintEnabled(boolean fingerprintEnabled) { this.fingerprintEnabled = fingerprintEnabled; }

    public Timestamp getFingerprintEnrolledAt() { return fingerprintEnrolledAt; }
    public void setFingerprintEnrolledAt(Timestamp fingerprintEnrolledAt) { this.fingerprintEnrolledAt = fingerprintEnrolledAt; }

    public int getFingerprintCount() { return fingerprintCount; }
    public void setFingerprintCount(int fingerprintCount) { this.fingerprintCount = fingerprintCount; }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(this.role);
    }
}
