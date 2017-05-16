package com.example.mrwen.bean;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mrwen on 2017/2/23.
 */

public class AdminClass implements Serializable {
    int id;
    private int sNumber;
    private String region;
    private String school;
    private String grade;
    private String classNumber;
    private ChatGroup chatGroup;
    private Set<Student> students;
    private Set<CollegeStudent> collegeStudents;
    private Set<AddClassRequest> addClassRequests;

    public Set<AddClassRequest> getRequests() {
        return addClassRequests;
    }

    public void setRequests(Set<AddClassRequest> addClassRequests) {
        this.addClassRequests = addClassRequests;
    }

    public void setChatGroup(ChatGroup chatGroup){
        this.chatGroup = chatGroup;
    }

    public ChatGroup getChatGroup(){
        return chatGroup;
    }

    public String getClassNumber(){ return classNumber; }

    public void setClassNumber(String classNumber){ this.classNumber = classNumber; }

    public Set<Student> getStudents(){  return students; }

    public void setStudents(Set<Student> students){
        this.students = students;
    }

    public Set<CollegeStudent> getCollegeStudents() {
        return collegeStudents;
    }

    public void setCollegeStudents(Set<CollegeStudent> collegeStudents) {
        this.collegeStudents = collegeStudents;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getsNumber() {
        return sNumber;
    }

    public void setsNumber(int sNumber) {
        this.sNumber = sNumber;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
