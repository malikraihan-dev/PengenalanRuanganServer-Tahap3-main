package com.askrida.web.service.model;

import java.sql.Timestamp;
import java.math.BigDecimal;

public class ExpenseReport {
    private int idReport;
    private String judulReport;
    private String deskripsi;
    private String periode;
    private String statusReport; // Draft, Submitted, Approved, Rejected
    private BigDecimal totalAmount;
    private int totalItems;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public ExpenseReport() {}

    public int getIdReport() { return idReport; }
    public void setIdReport(int idReport) { this.idReport = idReport; }

    public String getJudulReport() { return judulReport; }
    public void setJudulReport(String judulReport) { this.judulReport = judulReport; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    public String getPeriode() { return periode; }
    public void setPeriode(String periode) { this.periode = periode; }

    public String getStatusReport() { return statusReport; }
    public void setStatusReport(String statusReport) { this.statusReport = statusReport; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public int getTotalItems() { return totalItems; }
    public void setTotalItems(int totalItems) { this.totalItems = totalItems; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}
