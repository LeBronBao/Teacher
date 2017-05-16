package com.example.mrwen.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.adapter.*;
import com.example.mrwen.bean.Course;
import com.example.mrwen.bean.Notice;
import com.example.mrwen.bean.Student;
import com.example.mrwen.bean.UniversalResult;
import com.example.mrwen.interfaces.InterfaceClass;
import com.example.mrwen.interfaces.InterfaceCourse;
import com.example.mrwen.staticClass.StaticInfo;
import com.example.mrwen.view.OnDeleteClickListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MyClassActivity extends AppCompatActivity {
    private static final int STATE_LOADING = 0;
    private static final int STATE_NO_CLASS = 1;
    private static final int STATE_SUCCESS = 2;

    @Bind(R.id.recycler_class_courses)
    RecyclerView recyclerClassCourses;
    @Bind(R.id.iv_teacher_image)
    ImageView mIvTeacherImage;
    @Bind(R.id.tv_name)
    TextView mTvName;
    @Bind(R.id.tv_students)
    TextView mTvStudents;
    @Bind(R.id.tv_college_students)
    TextView mTvCollegeStudents;
    @Bind(R.id.tv_student_requests)
    TextView studentRequestTv;
    @Bind(R.id.ll_loading)
    LinearLayout mLlLoading;
    @Bind(R.id.ll_no_class)
    LinearLayout mLlNoClass;
    @Bind(R.id.content_my_class)
    NestedScrollView mContentMyClass;
    @Bind(R.id.create_new_class)
    Button createClassBt;
    @Bind(R.id.bt_class_notice)
    Button createNoticeBt;
    @Bind(R.id.bt_delete_class)
    Button deleteClassBt;
    @Bind(R.id.bt_class_student)
    Button classStudent;
    @Bind(R.id.bt_college_student)
    Button collegeStudent;
    @Bind(R.id.bt_student_requests)
    Button studentRequestButton;
    @Bind(R.id.recycler_class_notices)
    RecyclerView noticeRecycler;
    private RecyclerClassNoticeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_class);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        int cid = intent.getIntExtra("cid", 1);
        loadClassNotices();
        show(cid);
        createNoticeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createNoticeIntent = new Intent(MyClassActivity.this, WriteNoticeActivity.class);
                startActivity(createNoticeIntent);
            }
        });
        deleteClassBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MyClassActivity.this).setTitle("系统提示")
                        .setMessage("您确认要删除该班级吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteClass(Integer.valueOf(StaticInfo.classId));
                            }
                        }).setNegativeButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
            }
        });
        classStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent studentIntent = new Intent(MyClassActivity.this, StudentActivity.class);
                if(StaticInfo.classStudentNumber==0)
                    studentIntent.putExtra("flag", 0);
                else
                    studentIntent.putExtra("flag", 1);
                startActivity(studentIntent);
                finish();
            }
        });
        collegeStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent clgStuIntent = new Intent(MyClassActivity.this, CollegeStudentActivity.class);
                startActivity(clgStuIntent);
                finish();
            }
        });
        studentRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent requestIntent = new Intent(MyClassActivity.this, StudentRequestActivity.class);
                startActivity(requestIntent);
                finish();
            }
        });
        mTvStudents.setText(String.format("%d位学生", StaticInfo.classStudentNumber));
        mTvCollegeStudents.setText(StaticInfo.collegeStudentNumber+"位大学生志愿者");
        studentRequestTv.setText(StaticInfo.studentRequestNumber+"个加班申请");
    }

    private void show(int state) {
        mContentMyClass.setVisibility(state==STATE_SUCCESS? View.VISIBLE:View.GONE);
        mLlLoading.setVisibility(state==STATE_LOADING?View.VISIBLE:View.GONE);
        mLlNoClass.setVisibility(state==STATE_NO_CLASS?View.VISIBLE:View.GONE);
        if(state==STATE_SUCCESS)
            mTvName.setText(StaticInfo.realname);
    }

    @OnClick({R.id.create_new_class})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.create_new_class:
                Intent createIntent = new Intent(MyClassActivity.this, ClassCreateActivity.class);
                startActivity(createIntent);
                finish();
                break;
        }
    }

    //删除班级
    private void deleteClass(int id){
        final MyDialog alertDialog=new MyDialog();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceClass deleteClass = retrofit.create(InterfaceClass.class);
        final Call<UniversalResult> call = deleteClass.deleteClass(id);
        call.enqueue(new Callback<UniversalResult>() {
            @Override
            public void onResponse(Call<UniversalResult> call, Response<UniversalResult> response) {
                if(response.body()!=null){
                    if(response.body().getResultCode()==1) {
                        alertDialog.showAlertDialog(MyClassActivity.this,"删除班级成功");
                        StaticInfo.classSum = 0;
                        Intent intent = new Intent(MyClassActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        alertDialog.showAlertDialog(MyClassActivity.this,"删除班级失败");
                    }
                }else{
                    alertDialog.showAlertDialog(MyClassActivity.this, "后台返回数据出错");
                }
            }
            @Override
            public void onFailure(Call<UniversalResult> call, Throwable t) {
                alertDialog.showAlertDialog(MyClassActivity.this,t.toString());
            }
        });
    }

    //获取班级公告
    private void loadClassNotices(){
        mAdapter = new RecyclerClassNoticeAdapter(new ArrayList<Notice>());
        noticeRecycler.setLayoutManager(new LinearLayoutManager(this));
        noticeRecycler.setAdapter(mAdapter);

        mAdapter.setOnDeleteClickListener(new OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int id) {
                final int thisId=id;
                new android.support.v7.app.AlertDialog.Builder(MyClassActivity.this).setTitle("提示")
                        .setMessage("您确认要删除该公告么？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteNotice(thisId);
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
            }
        });

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceClass getNotices = retrofit.create(InterfaceClass.class);
        final Call<ArrayList<Notice>> call = getNotices.getClassNotice(StaticInfo.classId);
        call.enqueue(new Callback<ArrayList<Notice>>() {
            @Override
            public void onResponse(Call<ArrayList<Notice>> call, Response<ArrayList<Notice>> response) {
                if(response.body()!=null){
                    Toast.makeText(MyClassActivity.this,"获取班级公告成功", Toast.LENGTH_SHORT).show();
                    mAdapter.setData(response.body());
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Notice>> call, Throwable t) {
                Toast.makeText(MyClassActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //删除公告
    private void deleteNotice(final int id) {
        final MyDialog alertDialog=new MyDialog();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceCourse deleteNotice=retrofit.create(InterfaceCourse.class);
        final Call<UniversalResult> call=deleteNotice.deleteNotice(id);
        call.enqueue(new Callback<UniversalResult>() {
            @Override
            public void onResponse(Call<UniversalResult> call, Response<UniversalResult> response) {
                if(response.body().getResultCode()==1) {
                    mAdapter.remove(id);
                    alertDialog.showAlertDialog(MyClassActivity.this,"删除公告成功");
                }else{
                    alertDialog.showAlertDialog(MyClassActivity.this,"删除公告失败");
                }
            }
            @Override
            public void onFailure(Call<UniversalResult> call, Throwable t) {
                alertDialog.showAlertDialog(MyClassActivity.this,t.toString());
            }
        });
    }

    private void loadClassCourses(int classId){

    }

}
