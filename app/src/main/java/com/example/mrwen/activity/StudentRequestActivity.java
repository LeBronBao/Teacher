package com.example.mrwen.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mrwen.adapter.RecyclerStudentRequestAdapter;
import com.example.mrwen.bean.AddClassRequest;
import com.example.mrwen.bean.Notice;
import com.example.mrwen.bean.UniversalResult;
import com.example.mrwen.interfaces.InterfaceClass;
import com.example.mrwen.interfaces.InterfaceCourse;
import com.example.mrwen.interfaces.InterfaceTeacher;
import com.example.mrwen.staticClass.StaticInfo;
import com.example.mrwen.view.OnAddStudentClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StudentRequestActivity extends Activity {
    @Bind(R.id.ll_no_student_request)
    LinearLayout noStudentRequestLayout;
    @Bind(R.id.ll_student_requests)
    LinearLayout studentRequestLayout;
    @Bind(R.id.recycler_student_requests)
    RecyclerView studentRequestRecycler;
    private RecyclerStudentRequestAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_request);
        ButterKnife.bind(this);
        loadStudentRequests();
        int flag = getIntent().getIntExtra("flag", 0);
        Toast.makeText(StudentRequestActivity.this, "studentRequestNumber:"+StaticInfo.studentRequestNumber, Toast.LENGTH_SHORT).show();
        show(flag);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_back_student_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.back_student_info:
                Intent checkRequestIntent = new Intent(StudentRequestActivity.this, StudentActivity.class);
                startActivity(checkRequestIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void show(int flag){
        noStudentRequestLayout.setVisibility(flag==0? View.VISIBLE:View.GONE);
        studentRequestLayout.setVisibility(flag==1?View.VISIBLE:View.GONE);
    }

    private void loadStudentRequests(){
        adapter = new RecyclerStudentRequestAdapter(new ArrayList<AddClassRequest>());
        studentRequestRecycler.setLayoutManager(new LinearLayoutManager(this));
        studentRequestRecycler.setAdapter(adapter);

        //同意学生加班请求
        adapter.setOnClickListener(new OnAddStudentClickListener() {
            @Override
            public void onAddStudentClick(int id) {
                addStudent(id);
            }
        });

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceClass getStuRequest = retrofit.create(InterfaceClass.class);
        final Call<ArrayList<AddClassRequest>> call = getStuRequest.getStudentRequest(StaticInfo.classId);
        call.enqueue(new Callback<ArrayList<AddClassRequest>>() {
            @Override
            public void onResponse(Call<ArrayList<AddClassRequest>> call, Response<ArrayList<AddClassRequest>> response) {
                if(response.body()!=null){
                    Toast.makeText(StudentRequestActivity.this,"获取学生加班请求成功", Toast.LENGTH_SHORT).show();
                    StaticInfo.studentRequestNumber = response.body().size();
                    adapter.setData(response.body());
                }else{
                    Toast.makeText(StudentRequestActivity.this, "获取学生加班请求失败", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<AddClassRequest>> call, Throwable t) {
                Toast.makeText(StudentRequestActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //同意请求，添加学生
    private void addStudent(final int id){
        Map<String,String> map=new HashMap<>();
        map.put("classId",StaticInfo.classId);
        map.put("studentId", String.valueOf(id));
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceTeacher addStudent = retrofit.create(InterfaceTeacher.class);
        final Call<UniversalResult> call = addStudent.addStudent(map);
        call.enqueue(new Callback<UniversalResult>() {
            @Override
            public void onResponse(Call<UniversalResult> call, Response<UniversalResult> response) {
                if (response.body().getResultCode() == 1) {
                    Toast.makeText(StudentRequestActivity.this, "已加入", Toast.LENGTH_SHORT).show();
                    deleteRequest(id);
                } else {
                    Toast.makeText(StudentRequestActivity.this, "加入班级失败", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<UniversalResult> call, Throwable t) {
                Toast.makeText(StudentRequestActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteRequest(int studentId){
        Map<String,String> map=new HashMap<>();
        map.put("classId", StaticInfo.classId);
        map.put("studentId", String.valueOf(studentId));
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceTeacher deleteStuReq = retrofit.create(InterfaceTeacher.class);
        final Call<UniversalResult> call = deleteStuReq.deleteStudentRequest(map);
        call.enqueue(new Callback<UniversalResult>() {
            @Override
            public void onResponse(Call<UniversalResult> call, Response<UniversalResult> response) {
                if (response.body().getResultCode() == 1) {
                    Toast.makeText(StudentRequestActivity.this, "已删除请求", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(StudentRequestActivity.this, "删除请求失败", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<UniversalResult> call, Throwable t) {
                Toast.makeText(StudentRequestActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
