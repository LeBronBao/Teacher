package com.example.mrwen.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class KnowledgeRelationActivity extends Activity {
    @Bind(R.id.bt_back)
    Button bt_back;
    @Bind(R.id.sp_subject1)
    Spinner sp_subject1;
    @Bind(R.id.sp_grade1)
    Spinner sp_grade1;
    @Bind(R.id.sp_knowledge1)
    Spinner sp_knowledge1;
    @Bind(R.id.sp_knowledge_relation)
    Spinner sp_knowledge_relation;
    @Bind(R.id.sp_subject2)
    Spinner sp_subject2;
    @Bind(R.id.sp_grade2)
    Spinner sp_grade2;
    @Bind(R.id.sp_knowledge2)
    Spinner sp_knowledge2;

    //显示知识点关联的示例
    @Bind(R.id.tv_knowledge1)
    TextView tv_knowledge1;
    @Bind(R.id.tv_knowledge_relation)
    TextView tv_knowledge_relation;
    @Bind(R.id.tv_knowledge2)
    TextView tv_knowledge2;

    @Bind(R.id.bt_upload_knowledge_relation)
    Button bt_upload_knowledge_relation;

    private ArrayList<Knowledge> knowledgeList;
    private ArrayAdapter<String> knowledgeAdapter;
    private ArrayList<Knowledge> knowledgeList2;
    private ArrayAdapter<String> knowledgeAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_relation);
        ButterKnife.bind(this);

        //未对科目和年级进行选择，则使用初始的科目和年级加载知识点
        int grade1 = getIntGrade(sp_grade1.getSelectedItem().toString());
        String subject1 = sp_subject1.getSelectedItem().toString();
        loadKnowledgeByInfo1(grade1, subject1);
        int grade2 = getIntGrade(sp_grade2.getSelectedItem().toString());
        String subject2 = sp_subject2.getSelectedItem().toString();
        loadKnowledgeByInfo2(grade2, subject2);

        //初始化几个Spinner需要用到的数据和适配器
        knowledgeAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, new ArrayList<String>());
        knowledgeAdapter.setDropDownViewResource(R.layout.dropdown_style);
        knowledgeAdapter2 = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, new ArrayList<String>());
        knowledgeAdapter2.setDropDownViewResource(R.layout.dropdown_style);

        sp_grade1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reloadKnowledge(1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_subject1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reloadKnowledge(1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_grade2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reloadKnowledge(2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_subject2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reloadKnowledge(2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //向用户展示其所编辑的关联关系
        sp_knowledge1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tv_knowledge1.setText(sp_knowledge1.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_knowledge_relation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tv_knowledge_relation.setText(sp_knowledge_relation.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_knowledge2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tv_knowledge2.setText(sp_knowledge2.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bt_upload_knowledge_relation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String knowledge1 = sp_knowledge1.getSelectedItem().toString();
                String knowledge2 = sp_knowledge2.getSelectedItem().toString();
                if(knowledge1.equals(knowledge2)){
                    Toast.makeText(getApplicationContext(), "请选择不同的知识点建立关联", Toast.LENGTH_SHORT).show();
                }else{
                    uploadKnowledgeRelation();
                }
            }
        });
    }

    //根据年级和科目加载知识标签
    private void loadKnowledgeByInfo1(int grade, String subject){
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
                        sp_knowledge1.setAdapter(knowledgeAdapter);
                    }
                    if(knowledgeList!=null)
                        knowledgeList.clear();
                    knowledgeList = response.body();
                    if(knowledgeList.size()!=0){
                        knowledgeAdapter.addAll(getKnowledgeLabel(knowledgeList));
                        sp_knowledge1.setAdapter(knowledgeAdapter);
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
    private void loadKnowledgeByInfo2(int grade, String subject){
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
                    if(!knowledgeAdapter2.isEmpty()){
                        knowledgeAdapter2.clear();
                        sp_knowledge2.setAdapter(knowledgeAdapter2);
                    }
                    if(knowledgeList2!=null)
                        knowledgeList2.clear();
                    knowledgeList2 = response.body();
                    if(knowledgeList2.size()!=0){
                        knowledgeAdapter2.addAll(getKnowledgeLabel(knowledgeList2));
                        sp_knowledge2.setAdapter(knowledgeAdapter2);
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
    private ArrayList<String> getKnowledgeLabel(ArrayList<Knowledge> knowledgeList){
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

    //重新选择科目和年级时重新加载知识点, flag为1时加载知识点一，flag为2则加载知识点二
    private void reloadKnowledge(int flag){
        int grade;
        String subject;
        if(flag==1){
            grade = getIntGrade(sp_grade1.getSelectedItem().toString());
            subject = sp_subject1.getSelectedItem().toString();
            loadKnowledgeByInfo1(grade, subject);
        }else{
            grade = getIntGrade(sp_grade2.getSelectedItem().toString());
            subject = sp_subject2.getSelectedItem().toString();
            loadKnowledgeByInfo2(grade, subject);
        }
    }

    //上传知识关联关系
    private void uploadKnowledgeRelation(){
        Map<String, String> map = new HashMap<>();
        String knowledge1 = sp_knowledge1.getSelectedItem().toString();
        String knowledge2 = sp_knowledge2.getSelectedItem().toString();
        String relation = sp_knowledge_relation.getSelectedItem().toString();
        String id1 = String.valueOf(getKnowledgeId(knowledge1, 1));
        String id2 = String.valueOf(getKnowledgeId(knowledge2, 2));
        map.put("kid1", id1);
        map.put("kid2", id2);
        map.put("relation", relation);

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
                        Toast.makeText(KnowledgeRelationActivity.this, "上传关联关系成功", Toast.LENGTH_SHORT).show();
                    }else if(response.body().getResultCode()==0){
                        Toast.makeText(KnowledgeRelationActivity.this, "关联上传失败，请稍后重试", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(KnowledgeRelationActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UniversalResult> call, Throwable t) {
                Toast.makeText(KnowledgeRelationActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //通过知识标签找到其对应的id，flag为时在第一个knowledgeList中遍历，为2则在第二个knowledgeList中遍历
    private int getKnowledgeId(String knowledge, int flag){
        if(flag==1){
            for(Knowledge k:knowledgeList){
                if(k.getLabel().equals(knowledge))
                    return k.getId();
            }
        }else{
            for(Knowledge k:knowledgeList2){
                if(k.getLabel().equals(knowledge))
                    return k.getId();
            }
        }
        return 0;
    }

}
