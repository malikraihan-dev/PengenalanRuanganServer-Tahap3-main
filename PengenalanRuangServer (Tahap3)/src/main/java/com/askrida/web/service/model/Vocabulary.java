package com.askrida.web.service.model;

import java.sql.Timestamp;

public class Vocabulary {
    private int idVocab;
    private int idCourse;
    private String kata;
    private String terjemahan;
    private String pelafalan;
    private String contohKalimat;
    private String kategoriKata; // Noun, Verb, Adjective, Adverb, Phrase, Greeting
    private boolean learned;
    private Timestamp createdAt;

    // extra field for display
    private String bahasaCourse;

    public Vocabulary() {}

    public int getIdVocab() { return idVocab; }
    public void setIdVocab(int idVocab) { this.idVocab = idVocab; }

    public int getIdCourse() { return idCourse; }
    public void setIdCourse(int idCourse) { this.idCourse = idCourse; }

    public String getKata() { return kata; }
    public void setKata(String kata) { this.kata = kata; }

    public String getTerjemahan() { return terjemahan; }
    public void setTerjemahan(String terjemahan) { this.terjemahan = terjemahan; }

    public String getPelafalan() { return pelafalan; }
    public void setPelafalan(String pelafalan) { this.pelafalan = pelafalan; }

    public String getContohKalimat() { return contohKalimat; }
    public void setContohKalimat(String contohKalimat) { this.contohKalimat = contohKalimat; }

    public String getKategoriKata() { return kategoriKata; }
    public void setKategoriKata(String kategoriKata) { this.kategoriKata = kategoriKata; }

    public boolean isLearned() { return learned; }
    public void setLearned(boolean learned) { this.learned = learned; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getBahasaCourse() { return bahasaCourse; }
    public void setBahasaCourse(String bahasaCourse) { this.bahasaCourse = bahasaCourse; }
}
