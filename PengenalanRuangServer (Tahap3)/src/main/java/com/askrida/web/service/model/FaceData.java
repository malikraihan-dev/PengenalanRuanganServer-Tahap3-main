package com.askrida.web.service.model;

import java.sql.Timestamp;

/**
 * Model untuk data wajah anggota.
 * Menyimpan face descriptor (128 float array) sebagai JSON string.
 */
public class FaceData {
    private int idFace;
    private String nim;
    private String label;           // nama anggota
    private String faceDescriptor;  // JSON array of 128 floats
    private String imageData;       // base64 encoded image (opsional)
    private Timestamp createdAt;

    public FaceData() {}

    public FaceData(String nim, String label, String faceDescriptor) {
        this.nim = nim;
        this.label = label;
        this.faceDescriptor = faceDescriptor;
    }

    public int getIdFace() { return idFace; }
    public void setIdFace(int idFace) { this.idFace = idFace; }

    public String getNim() { return nim; }
    public void setNim(String nim) { this.nim = nim; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getFaceDescriptor() { return faceDescriptor; }
    public void setFaceDescriptor(String faceDescriptor) { this.faceDescriptor = faceDescriptor; }

    public String getImageData() { return imageData; }
    public void setImageData(String imageData) { this.imageData = imageData; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
