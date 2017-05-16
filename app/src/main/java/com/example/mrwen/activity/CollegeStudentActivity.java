package com.example.mrwen.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mrwen.staticClass.StaticInfo;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CollegeStudentActivity extends Activity {

    private static final int NO_STUDENT = 0;
    private static final int STUDENT_SUCCESS = 1;
    @Bind(R.id.ll_no_college_student)
    LinearLayout mLlNoCollegeStudent;
    @Bind(R.id.content_student)
    NestedScrollView mContentStudent;
    @Bind(R.id.tv_class_name)
    TextView className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college_student);
        ButterKnife.bind(this);
        className.setText(StaticInfo.grade+StaticInfo.classNumber+"班志愿者");
        Intent intent = getIntent();
        int flag = intent.getIntExtra("flag", 0);
        show(flag);
    }

    private void show(int flag){
        mLlNoCollegeStudent.setVisibility(flag==NO_STUDENT? View.VISIBLE:View.GONE);
        mContentStudent.setVisibility(flag==STUDENT_SUCCESS?View.VISIBLE:View.GONE);
    }
}
