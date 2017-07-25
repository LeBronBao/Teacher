package com.example.mrwen.bean;

/**
 * Created by LeBron on 2017/7/20.
 */

public class Knowledge {
    private int id;
    private String label;
    private String description;
    private ExerciseSubject subject;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ExerciseSubject getSubject() {
        return subject;
    }

    public void setSubject(ExerciseSubject subject) {
        this.subject = subject;
    }
}
