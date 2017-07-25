package com.example.mrwen.activity;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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


public class ExerciseManageActivity extends AppCompatActivity {
    @Bind(R.id.bt_back)
    Button bt_back;
    @Bind(R.id.sp_grade)
    Spinner sp_grade;
    @Bind(R.id.sp_subject)
    Spinner sp_subject;
    @Bind(R.id.sp_exercise_type)
    Spinner sp_exercise_type;
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

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadFillInBlankExercises(){
        fillInBlankExerciseAdapter = new RecyclerFillInBlankExerciseAdapter(new ArrayList<FillInBlankExercise>());
        rv_fill_in_blank_exercise.setLayoutManager(new LinearLayoutManager(this));
        rv_fill_in_blank_exercise.setAdapter(fillInBlankExerciseAdapter);

        fillInBlankExerciseAdapter.setOnDeleteClickListener(new OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int id) {
                final int thisId = id;
                new android.support.v7.app.AlertDialog.Builder(getApplicationContext()).setTitle("提示")
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

    private void loadMultipleChoicesExercises(){
        multipleChoicesExerciseAdapter = new RecyclerMultipleChoicesExerciseAdapter(new ArrayList<MultipleChoicesExercise>());
        rv_multiple_choices_exercise.setLayoutManager(new LinearLayoutManager(this));
        rv_multiple_choices_exercise.setAdapter(multipleChoicesExerciseAdapter);

        multipleChoicesExerciseAdapter.setOnDeleteClickListener(new OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int id) {
                final int thisId = id;
                new android.support.v7.app.AlertDialog.Builder(getApplicationContext()).setTitle("提示")
                        .setMessage("您确认要删除该题目吗？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteExercise(thisId, 1);
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
            }
        });

        multipleChoicesExerciseAdapter.setOnCheckExerciseInfoClickListener(new OnCheckExerciseInfoClickListener() {
            @Override
            public void onCheckExerciseInfoClickListener(int id) {

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
}
