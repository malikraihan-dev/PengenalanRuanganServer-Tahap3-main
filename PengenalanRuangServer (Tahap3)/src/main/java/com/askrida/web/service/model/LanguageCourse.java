package com.askrida.web.service.model;

import java.sql.Timestamp;

public class LanguageCourse {
    private int idCourse;
    private String bahasa;       // English, Japanese, Korean, Arabic, Mandarin, French, German, Spanish
    private String level;        // Beginner, Intermediate, Advanced
    private String deskripsi;
    private String iconEmoji;
    private int totalVocab;
    private Timestamp createdAt;

    public LanguageCourse() {}

    public int getIdCourse() { return idCourse; }
    public void setIdCourse(int idCourse) { this.idCourse = idCourse; }

    public String getBahasa() { return bahasa; }
    public void setBahasa(String bahasa) { this.bahasa = bahasa; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    public String getIconEmoji() { return iconEmoji; }
    public void setIconEmoji(String iconEmoji) { this.iconEmoji = iconEmoji; }

    public int getTotalVocab() { return totalVocab; }
    public void setTotalVocab(int totalVocab) { this.totalVocab = totalVocab; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
