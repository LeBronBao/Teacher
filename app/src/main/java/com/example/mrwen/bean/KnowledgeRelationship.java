package com.example.mrwen.bean;

/**
 * Created by LeBron on 2017/7/30.
 */

public class KnowledgeRelationship {
    private int id;
    private Knowledge knowledge1;
    private Knowledge knowledge2;
    private String relationship;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Knowledge getKnowledge1() {
        return knowledge1;
    }

    public void setKnowledge1(Knowledge knowledge1) {
        this.knowledge1 = knowledge1;
    }

    public Knowledge getKnowledge2() {
        return knowledge2;
    }

    public void setKnowledge2(Knowledge knowledge2) {
        this.knowledge2 = knowledge2;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }
}
