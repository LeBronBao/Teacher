package com.example.mrwen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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


public class KnowledgeUploadActivity extends Activity {
    @Bind(R.id.bt_back)
    Button bt_back;
    @Bind(R.id.spinner_subject)
    Spinner sp_subject;
    @Bind(R.id.spinner_grade)
    Spinner sp_grade;
    @Bind(R.id.et_knowledge)
    EditText et_knowledge;
    @Bind(R.id.et_knowledge_description)
    EditText et_knowledge_description;
    @Bind(R.id.bt_upload_knowledge)
    Button bt_upload_knowledge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_knowledge);
        ButterKnife.bind(this);

        bt_upload_knowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String subject = sp_subject.getSelectedItem().toString();
                String grade = sp_grade.getSelectedItem().toString();
                String label = et_knowledge.getText().toString();
                String description = et_knowledge_description.getText().toString();
                if(!label.equals("")){
                    uploadKnowledge(subject, grade, label, description);
                }else{
                    Toast.makeText(KnowledgeUploadActivity.this, "请填写所需信息", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void uploadKnowledge(String subject, String grade, String label, String description){
        Map<String, String> map = new HashMap<>();
        map.put("subject", subject);
        map.put("grade", grade);
        map.put("label", label);
        if(!description.equals(""))
            map.put("description", description);
        else
            map.put("description", "");

        Retrofit retrofitCreateClass = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceTeacher uploadKnowledge = retrofitCreateClass.create(InterfaceTeacher.class);
        final Call<UniversalResult> call = uploadKnowledge.uploadKnowledge(map);
        call.enqueue(new Callback<UniversalResult>() {
            @Override
            public void onResponse(Call<UniversalResult> call, Response<UniversalResult> response) {
                if(response.body()!=null){
                    if(response.body().getResultCode()==1){
                        Toast.makeText(KnowledgeUploadActivity.this, "上传知识图谱成功", Toast.LENGTH_SHORT).show();
                    }else if(response.body().getResultCode()==0){
                        Toast.makeText(KnowledgeUploadActivity.this, "知识图谱已存在，请重新上传", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(KnowledgeUploadActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UniversalResult> call, Throwable t) {
                Toast.makeText(KnowledgeUploadActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
