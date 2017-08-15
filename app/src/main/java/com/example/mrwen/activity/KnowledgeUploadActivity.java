package com.example.mrwen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrwen.bean.Knowledge;
import com.example.mrwen.bean.UniversalResult;
import com.example.mrwen.interfaces.InterfaceTeacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.vov.vitamio.utils.Log;
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

    //关联知识的控件
    @Bind(R.id.sp_related_knowledge)
    Spinner sp_related_knowledge;
    @Bind(R.id.sp_knowledge_relation)
    Spinner sp_knowledge_relation;
    @Bind(R.id.knowledge_relation_example_layout)
    LinearLayout layout_knowledge_relation_eg;
    @Bind(R.id.tv_knowledge_to_be_uploaded)
    TextView tv_knowledge_to_be_uploaded;
    @Bind(R.id.tv_knowledge_relation)
    TextView tv_knowledge_relation;
    @Bind(R.id.tv_knowledge)
    TextView tv_knowledge;

    //上传按钮
    @Bind(R.id.bt_upload_knowledge)
    Button bt_upload_knowledge;

    private ArrayList<Knowledge> knowledgeList;
    private ArrayAdapter<String> knowledgeAdapter;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_knowledge);
        ButterKnife.bind(this);

        //未对科目和年级进行选择，则使用初始的科目和年级加载知识点
        int grade = getIntGrade(sp_grade.getSelectedItem().toString());
        String subject = sp_subject.getSelectedItem().toString();
        loadKnowledgeByInfo(grade, subject);

        //初始化几个Spinner需要用到的数据和适配器
        final String[] grades = getResources().getStringArray(R.array.grade);
        final String[] subjects = getResources().getStringArray(R.array.subject);
        knowledgeList = new ArrayList<>();
        knowledgeAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, new ArrayList<String>());
        knowledgeAdapter.setDropDownViewResource(R.layout.dropdown_style);

        sp_grade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int grade = getIntGrade(grades[position]);
                String subject = sp_subject.getSelectedItem().toString();
                loadKnowledgeByInfo(grade, subject);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String subject = subjects[position];
                int grade = getIntGrade(sp_grade.getSelectedItem().toString());
                loadKnowledgeByInfo(grade, subject);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //向用户展示其所编辑的关联关系
        et_knowledge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_knowledge_to_be_uploaded.setText(s.toString());
            }
        });

        sp_related_knowledge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tv_knowledge.setText(sp_related_knowledge.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_knowledge_relation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(sp_knowledge_relation.getSelectedItem().toString().equals("无")){
                    layout_knowledge_relation_eg.setVisibility(View.GONE);
                }else{
                    layout_knowledge_relation_eg.setVisibility(View.VISIBLE);
                    tv_knowledge_relation.setText(sp_knowledge_relation.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

    //上传知识
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
                        id = response.body().getId();
                        uploadKnowledgeRelationship();
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

    //上传知识关联关系
    private void uploadKnowledgeRelationship(){
        Map<String, String> map = new HashMap<>();
        String relatedKnowledge = sp_related_knowledge.getSelectedItem().toString();
        String knowledgeRelation = sp_knowledge_relation.getSelectedItem().toString();
        map.put("kid1", String.valueOf(id));
        //若未选择关联知识点则不创建关联关系
        if(relatedKnowledge.equals("无"))
            return;
        map.put("kid2", String.valueOf(getKnowledgeId(relatedKnowledge)));
        map.put("relation", knowledgeRelation);

        Retrofit retrofitCreateClass = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceTeacher uploadKnowledgeRelation = retrofitCreateClass.create(InterfaceTeacher.class);
        final Call<UniversalResult> call = uploadKnowledgeRelation.uploadKnowledgeRelation(map);
        call.enqueue(new Callback<UniversalResult>() {
            @Override
            public void onResponse(Call<UniversalResult> call, Response<UniversalResult> response) {
                if(response.body()!=null){
                    if(response.body().getResultCode()==1){
                        Toast.makeText(getApplicationContext(), "关联建立成功", Toast.LENGTH_SHORT).show();
                    }else if(response.body().getResultCode()==0){
                        Toast.makeText(getApplicationContext(), "关联建立失败，请稍后再试", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "关联建立失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UniversalResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //根据年级和科目加载知识标签
    private void loadKnowledgeByInfo(int grade, String subject){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceTeacher getKnowledgeByInfo = retrofit.create(InterfaceTeacher.class);
        final Call<ArrayList<Knowledge>> call = getKnowledgeByInfo.getKnowledgeByInfo(grade, subject);
        call.enqueue(new Callback<ArrayList<Knowledge>>() {
            @Override
            public void onResponse(Call<ArrayList<Knowledge>> call, Response<ArrayList<Knowledge>> response) {
                if(response.body()!=null){
                    //每次更改科目和年级先将原来的Spinner清空
                    if(!knowledgeAdapter.isEmpty()){
                        knowledgeAdapter.clear();
                        knowledgeAdapter.add("无");
                        sp_related_knowledge.setAdapter(knowledgeAdapter);
                    }
                    if(knowledgeList!=null)
                        knowledgeList.clear();
                    knowledgeList = response.body();
                    if(knowledgeList.size()!=0){
                        knowledgeAdapter.addAll(getKnowledgeLabel());
                        sp_related_knowledge.setAdapter(knowledgeAdapter);
                        Toast.makeText(getApplicationContext(), "获取相应知识点成功", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "该科目该年级下没有知识标签，请重新选择", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "获取知识失败，请稍后重试", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Knowledge>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //获取所有Knowledge对象的标签
    private ArrayList<String> getKnowledgeLabel(){
        ArrayList<String> list = new ArrayList<>();
        for(Knowledge knowledge:knowledgeList){
            list.add(knowledge.getLabel());
        }
        return list;
    }

    //将“年级”的数据类型转化为int
    private int getIntGrade(String grade){
        int intGrade = 0;
        switch (grade){
            case "一年级":
                intGrade = 1;
                break;
            case "二年级":
                intGrade = 2;
                break;
            case "三年级":
                intGrade = 3;
                break;
            case "四年级":
                intGrade = 4;
                break;
            case "五年级":
                intGrade = 5;
                break;
            case "六年级":
                intGrade = 6;
                break;
            case "七年级":
                intGrade = 7;
                break;
            case "八年级":
                intGrade = 8;
                break;
            case "九年级":
                intGrade = 9;
                break;
        }
        return intGrade;
    }

    //获取关联知识的id
    private int getKnowledgeId(String knowledge){
        for(Knowledge k:knowledgeList){
            if(k.getLabel().equals(knowledge))
                return k.getId();
        }
        return 0;
    }

}
