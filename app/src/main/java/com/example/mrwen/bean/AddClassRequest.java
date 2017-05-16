package com.example.mrwen.bean;

/**
 * Created by LeBron on 2017/5/6.
 */

public class AddClassRequest {
    private int id;
    private Student student;
    private AdminClass adminClass;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public AdminClass getAdminClass() {
        return adminClass;
    }

    public void setAdminClass(AdminClass adminClass) {
        this.adminClass = adminClass;
    }
}
