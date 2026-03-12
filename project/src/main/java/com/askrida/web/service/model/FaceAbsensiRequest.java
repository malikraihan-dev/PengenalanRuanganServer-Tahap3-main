package com.askrida.web.service.model;

/**
 * Model untuk request absensi via face recognition.
 */
public class FaceAbsensiRequest {
    private String nim;
    private double confidence;  // tingkat kepercayaan pengenalan (0.0 - 1.0)

    public FaceAbsensiRequest() {}

    public String getNim() { return nim; }
    public void setNim(String nim) { this.nim = nim; }

    public double getConfidence() { return confidence; }
    public void setConfidence(double confidence) { this.confidence = confidence; }
}
