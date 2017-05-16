package com.example.mrwen.bean;

import com.example.mrwen.bean.Teacher;

/**
 * Created by mrwen on 2017/2/9.
 */

public class LoginInResult {
    private int resultCode;
    private Teacher teacher;

    public int getResultCode() {
        return resultCode;
    }
    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
    public Teacher getTeacher() {
        return teacher;
    }
    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
    public int getClassNum(){   return teacher.getClasses().size(); }

    @Override
    public String toString() {
        return teacher.getId()+"dwadawdawdawdawdawd";
    }
}
