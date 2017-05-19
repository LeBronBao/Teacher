package com.example.mrwen.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrwen.adapter.RecyclerStudentAdapter;
import com.example.mrwen.bean.AddClassRequest;
import com.example.mrwen.bean.Student;
import com.example.mrwen.bean.StudentAndStudy;
import com.example.mrwen.bean.UniversalResult;
import com.example.mrwen.interfaces.InterfaceClass;
import com.example.mrwen.interfaces.InterfaceTeacher;
import com.example.mrwen.staticClass.StaticInfo;
import com.example.mrwen.view.OnGetStudentInfoListener;
import com.example.mrwen.view.OnStudyInfoClickListener;
import com.example.mrwen.view.OnUserInfoClickListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StudentActivity extends AppCompatActivity {

    private static final int NO_STUDENT = 0;
    private static final int STUDENT_SUCCESS = 1;
    @Bind(R.id.ll_no_student)
    LinearLayout mLlNoStudent;
    @Bind(R.id.content_student)
    NestedScrollView mContentStudent;
    @Bind(R.id.tv_class_name)
    TextView className;
    @Bind(R.id.recycler_class_students)
    RecyclerView studentRecycler;
    private RecyclerStudentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        ButterKnife.bind(this);
        className.setText(StaticInfo.grade+StaticInfo.classNumber+"班");
        Intent intent = getIntent();
        int flag = intent.getIntExtra("flag", 0);
        show(flag);
        loadStudents();
    }

    private void show(int flag){
        mLlNoStudent.setVisibility(flag==NO_STUDENT? View.VISIBLE:View.GONE);
        mContentStudent.setVisibility(flag==STUDENT_SUCCESS?View.VISIBLE:View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_check_student_request, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.check_student_request:
                Intent checkRequestIntent = new Intent(StudentActivity.this,StudentRequestActivity.class);
                if(StaticInfo.studentRequestNumber==0)
                    checkRequestIntent.putExtra("flag", 0);
                else
                    checkRequestIntent.putExtra("flag", 1);
                startActivity(checkRequestIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //加载学生信息
    private void loadStudents(){
        adapter = new RecyclerStudentAdapter(new ArrayList<StudentAndStudy>());
        studentRecycler.setLayoutManager(new LinearLayoutManager(this));
        studentRecycler.setAdapter(adapter);

        adapter.setOnStudyInfoClickListener(new OnStudyInfoClickListener() {
            @Override
            public void onStudyInfoClickListener(int id, String name, ArrayList<Integer> idArray, ArrayList<String> nameArray) {
                Intent intent=new Intent(StudentActivity.this,StudyTimeLineActivity.class);
                intent.putExtra("studentId",id);
                intent.putExtra("studentName",name);
                intent.putExtra("idArray",idArray);
                intent.putExtra("nameArray",nameArray);
                startActivity(intent);
            }
        });

        adapter.setOnUserInfoClickListener(new OnUserInfoClickListener() {
            @Override
            public void onUserInfoClickListener(String uid) {
                Intent intent=new Intent(StudentActivity.this,PersonalInfoActivity.class);
                intent.putExtra("uid",uid);
                startActivity(intent);
            }
        });

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceClass getStudentList = retrofit.create(InterfaceClass.class);
        final Call<ArrayList<StudentAndStudy>> call = getStudentList.getStudentList(Integer.valueOf(StaticInfo.classId));
        call.enqueue(new Callback<ArrayList<StudentAndStudy>>() {
            @Override
            public void onResponse(Call<ArrayList<StudentAndStudy>> call, Response<ArrayList<StudentAndStudy>> response) {
                if(response.body()!=null){
                    Toast.makeText(StudentActivity.this, "获取学生列表成功", Toast.LENGTH_SHORT).show();
                    adapter.setData(response.body());
                }
            }
            @Override
            public void onFailure(Call<ArrayList<StudentAndStudy>> call, Throwable t) {
                Toast.makeText(StudentActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //查看单个学生信息
    private void showStudentInfo(int id){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceClass getStudent = retrofit.create(InterfaceClass.class);
        final Call<Student> call = getStudent.getStudent(id);
        call.enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                if (response.body()!=null) {
                    Student student = response.body();
                    new android.support.v7.app.AlertDialog.Builder(StudentActivity.this).setTitle("学生信息")
                            .setMessage("姓名："+student.getRealname()+"\n"+
                                        "性别："+student.getGender()+"\n"+
                                        "邮箱："+student.getEmail()+"\n"+
                                        "电话："+student.getPhone()+"\n"+
                                        "昵称："+student.getNickname()+"\n"+
                                        "地区："+student.getRegion())
                            .setPositiveButton("完成浏览", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setNegativeButton("取消",null)
                            .show();
                } else {
                    Toast.makeText(StudentActivity.this, "获取学生信息失败", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                Toast.makeText(StudentActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
