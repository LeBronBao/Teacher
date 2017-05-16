package com.example.mrwen.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrwen.bean.AdminClass;
import com.example.mrwen.bean.LoginInResult;
import com.example.mrwen.bean.UniversalResult;
import com.example.mrwen.interfaces.InterfaceTeacher;
import com.example.mrwen.staticClass.StaticInfo;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClassCreateActivity extends AppCompatActivity {

    @Bind(R.id.grade)
    Spinner gradeSpinner;
    @Bind(R.id.class_number)
    TextView classNumber;
    @Bind(R.id.class_number_clear)
    Button clearClassNum;
    @Bind(R.id.bt_create_class)
    Button createClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class);
        ButterKnife.bind(this);

        clearClassNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classNumber.setText("");
            }
        });

        createClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String grade = gradeSpinner.getSelectedItem().toString();
                if(classNumber.getText().toString().equals("")){
                    Toast.makeText(ClassCreateActivity.this, "请输入班级号", Toast.LENGTH_SHORT).show();
                }else{
                    String classNum = classNumber.getText().toString();
                    Map<String, String> map = getClassInfo(grade, classNum);
                    Retrofit retrofitCreateClass = new Retrofit.Builder()
                            .baseUrl(getResources().getString(R.string.baseURL))
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    final InterfaceTeacher createClass = retrofitCreateClass.create(InterfaceTeacher.class);
                    final Call<UniversalResult> call = createClass.createClass(map);
                    call.enqueue(new Callback<UniversalResult>() {
                        @Override
                        public void onResponse(Call<UniversalResult> call, Response<UniversalResult> response) {
                            UniversalResult universalResult = response.body();
                            int resultCode = universalResult.getResultCode();
                            switch (resultCode){
                                case 1:
                                    Toast.makeText(ClassCreateActivity.this, "创建成功", Toast.LENGTH_SHORT).show();
                                    StaticInfo.classSum = 1;
                                    //在同一登陆周期内进行创建班级并删除，需要从此处获取班级ID
                                    StaticInfo.classId = String.valueOf(universalResult.getId());
                                    Intent intent = new Intent(ClassCreateActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    break;
                                case 0:
                                    Toast.makeText(ClassCreateActivity.this, "创建班级失败", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }

                        @Override
                        public void onFailure(Call<UniversalResult> call, Throwable t) {
                            Toast.makeText(ClassCreateActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private Map<String, String> getClassInfo(String grade, String classNum){
        Map<String, String> map = new HashMap<>();
        map.put("tid", String.valueOf(StaticInfo.id));
        map.put("region", StaticInfo.region==null?"":StaticInfo.region);
        map.put("school", StaticInfo.school==null?"":StaticInfo.school);
        map.put("grade", grade);
        map.put("sNumber", classNum);
        StaticInfo.grade = grade;
        StaticInfo.classNumber = Integer.valueOf(classNum);
        return map;
    }
}
