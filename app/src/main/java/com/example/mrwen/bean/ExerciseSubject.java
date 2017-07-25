package com.example.mrwen.bean;

import java.util.Set;

/**
 * Created by LeBron on 2017/7/20.
 */

public class ExerciseSubject {
    private int id;
    private String subject;
    private int grade;
    private Set<Knowledge> knowledges;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public Set<Knowledge> getKnowledges() {
        return knowledges;
    }

    public void setKnowledges(Set<Knowledge> knowledges) {
        this.knowledges = knowledges;
    }
}
