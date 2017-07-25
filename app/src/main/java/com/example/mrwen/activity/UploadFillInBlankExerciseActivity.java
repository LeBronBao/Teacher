package com.example.mrwen.activity;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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


public class UploadFillInBlankExerciseActivity extends Activity {
    @Bind(R.id.bt_back)
    Button bt_back;
    @Bind(R.id.sp_subject)
    Spinner sp_subject;
    @Bind(R.id.sp_grade)
    Spinner sp_grade;
    @Bind(R.id.sp_knowledge)
    Spinner sp_knowledge;
    @Bind(R.id.et_question)
    EditText et_question;
    @Bind(R.id.et_answer)
    EditText et_answer;
    @Bind(R.id.et_analysis)
    EditText et_analysis;
    @Bind(R.id.iv_question_image)
    ImageView iv_question_image;
    @Bind(R.id.bt_upload_fill_in_blank_exercise)
    Button bt_upload_fill_in_blank_exercise;

    private ArrayList<Knowledge> knowledgeList;
    private ArrayAdapter<String> adapter;
    private int grade;
    private String subject;
    private String knowledge;
    private String question;
    private String answer;
    private String analysis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_fill_in_blank_exercise);
        ButterKnife.bind(this);

        //未对科目和年级进行选择，则使用初始的科目和年级加载知识点
        grade = getIntGrade(sp_grade.getSelectedItem().toString());
        subject = sp_subject.getSelectedItem().toString();
        loadKnowledgeByInfo(grade, subject);

        final String[] grades = getResources().getStringArray(R.array.grade);
        final String[] subjects = getResources().getStringArray(R.array.subject);
        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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

        bt_upload_fill_in_blank_exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                question = et_question.getText().toString();
                answer = et_answer.getText().toString();
                knowledge = sp_knowledge.getSelectedItem().toString();
                analysis = et_analysis.getText().toString();
                if(question.equals("")||answer.equals("")||knowledge.equals("")){
                    Toast.makeText(UploadFillInBlankExerciseActivity.this, "知识点、题目或答案还未填写", Toast.LENGTH_SHORT).show();
                }else{
                    uploadFillInBlankExercise();
                }
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
                    if(!adapter.isEmpty()){
                        adapter.clear();
                        sp_knowledge.setAdapter(adapter);
                    }
                    if(knowledgeList!=null)
                        knowledgeList.clear();
                    knowledgeList = response.body();
                    if(knowledgeList.size()!=0){
                        adapter.addAll(getKnowledgeLabel());
                        sp_knowledge.setAdapter(adapter);
                        Toast.makeText(UploadFillInBlankExerciseActivity.this, "获取相应知识点成功", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(UploadFillInBlankExerciseActivity.this, "该科目该年级下没有知识标签，请重新选择", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(UploadFillInBlankExerciseActivity.this, "获取知识失败，请稍后重试", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Knowledge>> call, Throwable t) {
                Toast.makeText(UploadFillInBlankExerciseActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadFillInBlankExercise(){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Map<String, String> map = formMap();
        final InterfaceTeacher uploadFillInBlankExercise = retrofit.create(InterfaceTeacher.class);
        final Call<UniversalResult> call = uploadFillInBlankExercise.uploadFillInBlankExercise(map);
        call.enqueue(new Callback<UniversalResult>() {
            @Override
            public void onResponse(Call<UniversalResult> call, Response<UniversalResult> response) {
                if(response.body()!=null){
                    UniversalResult universalResult = response.body();
                    if(universalResult.getResultCode()==1)
                        Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(UploadFillInBlankExerciseActivity.this, "上传题目失败，请稍后重试", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<UniversalResult> call, Throwable t) {
                Toast.makeText(UploadFillInBlankExerciseActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

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

    private ArrayList<String> getKnowledgeLabel(){
        ArrayList<String> list = new ArrayList<>();
        for(Knowledge knowledge:knowledgeList){
            list.add(knowledge.getLabel());
        }
        return list;
    }

    //将需要上传的数据存入map
    private Map<String, String> formMap(){
        Map<String, String> map = new HashMap<>();
        map.put("grade", String.valueOf(grade));
        map.put("subject", subject);
        map.put("knowledge", knowledge);
        map.put("question", question);
        map.put("answer", answer);
        map.put("analysis", analysis);
        return map;
    }

}
