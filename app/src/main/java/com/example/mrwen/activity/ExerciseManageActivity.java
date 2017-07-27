package com.example.mrwen.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mrwen.adapter.RecyclerFillInBlankExerciseAdapter;
import com.example.mrwen.adapter.RecyclerMultipleChoicesExerciseAdapter;
import com.example.mrwen.bean.FillInBlankExercise;
import com.example.mrwen.bean.Knowledge;
import com.example.mrwen.bean.MultipleChoicesExercise;
import com.example.mrwen.bean.UniversalResult;
import com.example.mrwen.interfaces.InterfaceTeacher;
import com.example.mrwen.view.OnCheckExerciseInfoClickListener;
import com.example.mrwen.view.OnDeleteClickListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ExerciseManageActivity extends Activity {
    @Bind(R.id.bt_back)
    Button bt_back;
    @Bind(R.id.sp_grade)
    Spinner sp_grade;
    @Bind(R.id.sp_subject)
    Spinner sp_subject;
    @Bind(R.id.sp_exercise_type)
    Spinner sp_exercise_type;
    @Bind(R.id.bt_reload_exercises)
    Button bt_reload_exercises;
    @Bind(R.id.bt_search_exercises)
    Button bt_search_exercises;
    @Bind(R.id.bt_show_all_exercises)
    Button bt_show_all_exercises;
    @Bind(R.id.layout_no_exercise)
    LinearLayout layout_no_exercise;
    @Bind(R.id.recycler_fill_in_blank_exercise)
    RecyclerView rv_fill_in_blank_exercise;
    @Bind(R.id.recycler_multiple_choices_exercise)
    RecyclerView rv_multiple_choices_exercise;

    private RecyclerFillInBlankExerciseAdapter fillInBlankExerciseAdapter;
    private RecyclerMultipleChoicesExerciseAdapter multipleChoicesExerciseAdapter;
    private static ArrayList<FillInBlankExercise> fillInBlankExercises;
    private static ArrayList<MultipleChoicesExercise> multipleChoicesExercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_manage);
        ButterKnife.bind(this);

        loadFillInBlankExercises();
        loadMultipleChoicesExercises();
        changeView();

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bt_search_exercises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int grade = getIntGrade(sp_grade.getSelectedItem().toString());
                String subject = sp_subject.getSelectedItem().toString();
                String type = sp_exercise_type.getSelectedItem().toString();
                int intType = 0;
                switch (type){
                    case "全部类型":
                        intType = 0;
                        break;
                    case "选择题":
                        intType = 1;
                        break;
                    case "填空题":
                        intType = 2;
                        break;
                }
                searchExercises(grade, subject, intType);
            }
        });

        bt_reload_exercises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        bt_show_all_exercises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllExercises();
            }
        });
    }

    //加载所有填空题
    private void loadFillInBlankExercises(){
        fillInBlankExerciseAdapter = new RecyclerFillInBlankExerciseAdapter(new ArrayList<FillInBlankExercise>());
        rv_fill_in_blank_exercise.setLayoutManager(new LinearLayoutManager(this));
        rv_fill_in_blank_exercise.setAdapter(fillInBlankExerciseAdapter);

        fillInBlankExerciseAdapter.setOnDeleteClickListener(new OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int id) {
                final int thisId = id;
                new android.support.v7.app.AlertDialog.Builder(ExerciseManageActivity.this).setTitle("提示")
                        .setMessage("您确认要删除该题目吗？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteExercise(thisId, 0);
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();

            }
        });

        fillInBlankExerciseAdapter.setOnClickListener(new OnCheckExerciseInfoClickListener() {
            @Override
            public void onCheckExerciseInfoClickListener(int id) {
                FillInBlankExercise exercise = getFillInBlankExercise(id);
                if(exercise.getAnalysis().equals(""))
                    exercise.setAnalysis("暂无分析");
                new android.support.v7.app.AlertDialog.Builder(ExerciseManageActivity.this).setTitle("详细信息")
                        .setMessage("分析："+exercise.getAnalysis()+"\n"
                                +"被做总次数："+exercise.getFinishedTimes()+"\n"
                                +"正确次数："+exercise.getCorrectTimes())
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

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
        final InterfaceTeacher getFillInBlankExercises = retrofit.create(InterfaceTeacher.class);
        final Call<ArrayList<FillInBlankExercise>> call = getFillInBlankExercises.getFillInBlankExercises();
        call.enqueue(new Callback<ArrayList<FillInBlankExercise>>() {
            @Override
            public void onResponse(Call<ArrayList<FillInBlankExercise>> call, Response<ArrayList<FillInBlankExercise>> response) {
                if(response.body()!=null){
                    Toast.makeText(getApplicationContext(), "获取填空题成功", Toast.LENGTH_SHORT).show();
                    //将知识点存放于此用于后面做搜索
                    fillInBlankExercises = response.body();
                    if(fillInBlankExercises.size()==0){
                        rv_fill_in_blank_exercise.setVisibility(View.GONE);
                    }else{
                        rv_fill_in_blank_exercise.setVisibility(View.VISIBLE);
                        layout_no_exercise.setVisibility(View.GONE);
                        fillInBlankExerciseAdapter.setData(response.body());
                    }
                }
            }
            @Override
            public void onFailure(Call<ArrayList<FillInBlankExercise>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //加载所有选择题
    private void loadMultipleChoicesExercises(){
        multipleChoicesExerciseAdapter = new RecyclerMultipleChoicesExerciseAdapter(new ArrayList<MultipleChoicesExercise>());
        rv_multiple_choices_exercise.setLayoutManager(new LinearLayoutManager(this));
        rv_multiple_choices_exercise.setAdapter(multipleChoicesExerciseAdapter);

        multipleChoicesExerciseAdapter.setOnDeleteClickListener(new OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int id) {
                final int thisId = id;
                new AlertDialog.Builder(ExerciseManageActivity.this).setTitle("提示").setMessage("您确认要删除该题目吗？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteExercise(thisId, 1);
                            }
                        }).setNegativeButton("取消", null)
                        .show();
            }
        });

        multipleChoicesExerciseAdapter.setOnCheckExerciseInfoClickListener(new OnCheckExerciseInfoClickListener() {
            @Override
            public void onCheckExerciseInfoClickListener(int id) {
                MultipleChoicesExercise exercise = getMultipleChoicesExercise(id);
                if(exercise.getAnalysis().equals(""))
                    exercise.setAnalysis("暂无分析");
                new android.support.v7.app.AlertDialog.Builder(ExerciseManageActivity.this).setTitle("详细信息")
                        .setMessage("分析："+exercise.getAnalysis()+"\n"
                        +"被做总次数："+exercise.getFinishedTimes()+"\n"
                        +"正确次数："+exercise.getCorrectTimes())
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
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
        final InterfaceTeacher getMultipleChoicesExercises = retrofit.create(InterfaceTeacher.class);
        final Call<ArrayList<MultipleChoicesExercise>> call = getMultipleChoicesExercises.getMultipleChoicesExercises();
        call.enqueue(new Callback<ArrayList<MultipleChoicesExercise>>() {
            @Override
            public void onResponse(Call<ArrayList<MultipleChoicesExercise>> call, Response<ArrayList<MultipleChoicesExercise>> response) {
                if(response.body()!=null){
                    Toast.makeText(getApplicationContext(), "获取选择题成功", Toast.LENGTH_SHORT).show();
                    //将知识点存放于此用于后面做搜索
                    multipleChoicesExercises = response.body();
                    if(multipleChoicesExercises.size()==0){
                        rv_multiple_choices_exercise.setVisibility(View.GONE);
                    }else{
                        rv_fill_in_blank_exercise.setVisibility(View.VISIBLE);
                        layout_no_exercise.setVisibility(View.GONE);
                        multipleChoicesExerciseAdapter.setData(response.body());
                    }
                }
            }
            @Override
            public void onFailure(Call<ArrayList<MultipleChoicesExercise>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //搜索某个年级科目类型的题目，其中type为0代表全部类型题目，1代表选择题，2代填空题
    private void searchExercises(int grade, String subject, int type){
        ArrayList<FillInBlankExercise> fillInBlankExerciseList = new ArrayList<>();
        ArrayList<MultipleChoicesExercise> multipleChoicesExerciseList = new ArrayList<>();
        //获取对应年级与科目的选择题与填空题
        for(FillInBlankExercise exercise:fillInBlankExercises){
            int exerciseGrade = Integer.valueOf(exercise.getQuestion().split(",")[1]);
            String exerciseSubject = exercise.getQuestion().split(",")[2];
            if(exerciseGrade==grade&&exerciseSubject.equals(subject))
                fillInBlankExerciseList.add(exercise);
        }
        for(MultipleChoicesExercise exercise:multipleChoicesExercises){
            int exerciseGrade = Integer.valueOf(exercise.getQuestion().split(",")[1]);
            String exerciseSubject = exercise.getQuestion().split(",")[2];
            if(exerciseGrade==grade&&exerciseSubject.equals(subject))
                multipleChoicesExerciseList.add(exercise);
        }

        if(type==0){
            if(multipleChoicesExerciseList.size()==0&&fillInBlankExerciseList.size()==0){
                rv_multiple_choices_exercise.setVisibility(View.GONE);
                rv_fill_in_blank_exercise.setVisibility(View.GONE);
                layout_no_exercise.setVisibility(View.VISIBLE);
            }else{
                rv_fill_in_blank_exercise.setVisibility(View.VISIBLE);
                rv_multiple_choices_exercise.setVisibility(View.VISIBLE);
                layout_no_exercise.setVisibility(View.GONE);
                multipleChoicesExerciseAdapter.setData(multipleChoicesExerciseList);
                fillInBlankExerciseAdapter.setData(fillInBlankExerciseList);
            }
        }else if(type==1){
            rv_fill_in_blank_exercise.setVisibility(View.GONE);
            if(multipleChoicesExerciseList.size()==0){
                rv_multiple_choices_exercise.setVisibility(View.GONE);
                layout_no_exercise.setVisibility(View.VISIBLE);
            }else{
                rv_multiple_choices_exercise.setVisibility(View.VISIBLE);
                layout_no_exercise.setVisibility(View.GONE);
                multipleChoicesExerciseAdapter.setData(multipleChoicesExerciseList);
            }
        }else if(type==2){
            rv_multiple_choices_exercise.setVisibility(View.GONE);
            if(fillInBlankExerciseList.size()==0){
                rv_fill_in_blank_exercise.setVisibility(View.GONE);
                layout_no_exercise.setVisibility(View.VISIBLE);
            }else{
                rv_fill_in_blank_exercise.setVisibility(View.VISIBLE);
                layout_no_exercise.setVisibility(View.GONE);
                fillInBlankExerciseAdapter.setData(fillInBlankExerciseList);
            }
        }
    }

    //根据id删除题目，type指定所删除题目的类型，0为填空题，1为选择题
    private void deleteExercise(final int id, final int type){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceTeacher deleteExercise = retrofit.create(InterfaceTeacher.class);
        final Call<UniversalResult> call = deleteExercise.deleteExercise(id, type);
        call.enqueue(new Callback<UniversalResult>() {
            @Override
            public void onResponse(Call<UniversalResult> call, Response<UniversalResult> response) {
                if(response.body().getResultCode()==1) {
                    if(type==0){
                        fillInBlankExerciseAdapter.remove(id);
                    }else{
                        multipleChoicesExerciseAdapter.remove(id);
                    }
                    Toast.makeText(getApplicationContext(), "题目删除成功", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "题目删除失败", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<UniversalResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //根据id获取某道选择题
    private MultipleChoicesExercise getMultipleChoicesExercise(int id){
        for(MultipleChoicesExercise exercise:multipleChoicesExercises){
            if(exercise.getId()==id)
                return exercise;
        }
        return new MultipleChoicesExercise();
    }

    //根据id获取某道填空题
    private FillInBlankExercise getFillInBlankExercise(int id) {
        for (FillInBlankExercise exercise : fillInBlankExercises) {
            if(exercise.getId()==id)
                return exercise;
        }
        return new FillInBlankExercise();
    }

    //显示全部题目
    private void showAllExercises(){
        if(multipleChoicesExercises.size()!=0&&fillInBlankExercises.size()!=0){
            rv_multiple_choices_exercise.setVisibility(View.VISIBLE);
            rv_fill_in_blank_exercise.setVisibility(View.VISIBLE);
            layout_no_exercise.setVisibility(View.GONE);
            multipleChoicesExerciseAdapter.setData(multipleChoicesExercises);
            fillInBlankExerciseAdapter.setData(fillInBlankExercises);
        }else if(multipleChoicesExercises.size()==0&&fillInBlankExercises.size()!=0){
            rv_multiple_choices_exercise.setVisibility(View.GONE);
            rv_fill_in_blank_exercise.setVisibility(View.VISIBLE);
            layout_no_exercise.setVisibility(View.GONE);
            fillInBlankExerciseAdapter.setData(fillInBlankExercises);
        }else if(multipleChoicesExercises.size()!=0&&fillInBlankExercises.size()==0){
            rv_multiple_choices_exercise.setVisibility(View.VISIBLE);
            rv_fill_in_blank_exercise.setVisibility(View.GONE);
            layout_no_exercise.setVisibility(View.GONE);
            multipleChoicesExerciseAdapter.setData(multipleChoicesExercises);
        }else{
            rv_multiple_choices_exercise.setVisibility(View.GONE);
            rv_fill_in_blank_exercise.setVisibility(View.GONE);
            layout_no_exercise.setVisibility(View.VISIBLE);
        }
    }

    //当没有题目时改变布局
    private void changeView(){
        if(rv_fill_in_blank_exercise.getVisibility()==View.GONE&&rv_multiple_choices_exercise.getVisibility()==View.GONE)
            layout_no_exercise.setVisibility(View.VISIBLE);
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
}
