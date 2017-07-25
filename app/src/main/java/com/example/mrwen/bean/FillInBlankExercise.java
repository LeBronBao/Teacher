package com.example.mrwen.bean;

import java.util.Set;

/**
 * Created by LeBron on 2017/7/24.
 */

public class FillInBlankExercise {
    private int id;
    private String question;
    private String answer;
    private String analysis;
    private String imageURL;
    private int finishedTimes;
    private int correctTimes;
    private Set<Knowledge> knowledges;
    private ExerciseSubject subject;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getFinishedTimes() {
        return finishedTimes;
    }

    public void setFinishedTimes(int finishedTimes) {
        this.finishedTimes = finishedTimes;
    }

    public int getCorrectTimes() {
        return correctTimes;
    }

    public void setCorrectTimes(int correctTimes) {
        this.correctTimes = correctTimes;
    }

    public Set<Knowledge> getKnowledges() {
        return knowledges;
    }

    public void setKnowledges(Set<Knowledge> knowledges) {
        this.knowledges = knowledges;
    }

    public ExerciseSubject getSubject() {
        return subject;
    }

    public void setSubject(ExerciseSubject subject) {
        this.subject = subject;
    }
}
