package com.example.mrwen.interfaces;

import com.example.mrwen.bean.AddClassRequest;
import com.example.mrwen.bean.AdminClass;
import com.example.mrwen.bean.Course;
import com.example.mrwen.bean.Notice;
import com.example.mrwen.bean.Student;
import com.example.mrwen.bean.StudentAndStudy;
import com.example.mrwen.bean.UniversalResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by mrwen on 2017/2/23.
 */

public interface InterfaceClass {

    //获得班级列表
    @GET("servlet/GetClass")
    Call<ArrayList<AdminClass>> getClass(@Query("courseId") int id);

    //获得班级信息
    @GET("servlet/GetClassInfo")
    Call<AdminClass> getClassInfo(@Query("classId") int id);

    //获得单个学生信息
    @GET("servlet/GetStudent")
    Call<Student> getStudent(@Query("studentId") int id);

    //获得学生列表
    @GET("servlet/GetStudentList")
    Call<ArrayList<StudentAndStudy>> getStudentList(@Query("classId") int id);

    //删除班级
    @GET("servlet/DeleteClass")
    Call<UniversalResult> deleteClass(@Query("classId") int id);

    //获取班级公告
    @GET("servlet/GetClassNotice")
    Call<ArrayList<Notice>> getClassNotice(@Query("classId") String id);

    //获取学生加班请求
    @GET("servlet/GetStudentRequest")
    Call<ArrayList<AddClassRequest>> getStudentRequest(@Query("classId") String id);
}
