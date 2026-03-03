package com.askrida.web.service.model;

import java.sql.Timestamp;

public class QuizResult {
    private int idQuiz;
    private int idCourse;
    private int totalQuestions;
    private int correctAnswers;
    private int score;
    private Timestamp takenAt;
    private String bahasaCourse;

    public QuizResult() {}

    public int getIdQuiz() { return idQuiz; }
    public void setIdQuiz(int idQuiz) { this.idQuiz = idQuiz; }

    public int getIdCourse() { return idCourse; }
    public void setIdCourse(int idCourse) { this.idCourse = idCourse; }

    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }

    public int getCorrectAnswers() { return correctAnswers; }
    public void setCorrectAnswers(int correctAnswers) { this.correctAnswers = correctAnswers; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public Timestamp getTakenAt() { return takenAt; }
    public void setTakenAt(Timestamp takenAt) { this.takenAt = takenAt; }

    public String getBahasaCourse() { return bahasaCourse; }
    public void setBahasaCourse(String bahasaCourse) { this.bahasaCourse = bahasaCourse; }
}
