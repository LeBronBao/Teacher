package com.example.mrwen.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.adapter.RecyclerKnowledgeAdapter;
import com.example.mrwen.bean.Knowledge;
import com.example.mrwen.bean.Notice;
import com.example.mrwen.bean.UniversalResult;
import com.example.mrwen.interfaces.InterfaceClass;
import com.example.mrwen.interfaces.InterfaceCourse;
import com.example.mrwen.interfaces.InterfaceTeacher;
import com.example.mrwen.staticClass.StaticInfo;
import com.example.mrwen.view.OnDeleteClickListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.vov.vitamio.utils.Log;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class KnowledgeManageActivity extends Activity {
    @Bind(R.id.bt_back)
    Button bt_back;
    @Bind(R.id.sp_grade)
    Spinner sp_grade;
    @Bind(R.id.sp_subject)
    Spinner sp_subject;
    @Bind(R.id.bt_search_knowledge)
    Button bt_search_knowledge;
    @Bind(R.id.bt_show_all_knowledge)
    Button bt_show_all_knowledge;
    @Bind(R.id.layout_no_knowledge)
    LinearLayout layout_no_knowledge;
    @Bind(R.id.recycler_knowledge)
    RecyclerView recyclerView;

    private RecyclerKnowledgeAdapter adapter;
    private static ArrayList<Knowledge> knowledgeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_manage);
        ButterKnife.bind(this);

        loadKnowledge();

        bt_search_knowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String subject = sp_subject.getSelectedItem().toString();
                int grade = getIntGrade(sp_grade.getSelectedItem().toString());
                searchKnowledge(grade, subject);
            }
        });

        bt_show_all_knowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(knowledgeList!=null)
                    layout_no_knowledge.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter.setData(knowledgeList);
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadKnowledge(){
        adapter = new RecyclerKnowledgeAdapter(new ArrayList<Knowledge>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnDeleteClickListener(new OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int id) {
                final int thisId = id;
                new android.support.v7.app.AlertDialog.Builder(KnowledgeManageActivity.this).setTitle("提示")
                        .setMessage("您确认要删除该知识标签吗？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteKnowledge(thisId);
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
        final InterfaceTeacher getKnowledge = retrofit.create(InterfaceTeacher.class);
        final Call<ArrayList<Knowledge>> call = getKnowledge.getKnowledge();
        call.enqueue(new Callback<ArrayList<Knowledge>>() {
            @Override
            public void onResponse(Call<ArrayList<Knowledge>> call, Response<ArrayList<Knowledge>> response) {
                if(response.body()!=null){
                    Toast.makeText(KnowledgeManageActivity.this, "获取知识图谱成功", Toast.LENGTH_SHORT).show();
                    //将知识点存放于此用于后面做搜索
                    knowledgeList = response.body();
                    if(knowledgeList.size()==0){
                        recyclerView.setVisibility(View.GONE);
                        layout_no_knowledge.setVisibility(View.VISIBLE);
                    }else{
                        layout_no_knowledge.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        adapter.setData(response.body());
                    }
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Knowledge>> call, Throwable t) {
                Toast.makeText(KnowledgeManageActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void deleteKnowledge(final int id){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceTeacher deleteKnowledge = retrofit.create(InterfaceTeacher.class);
        final Call<UniversalResult> call = deleteKnowledge.deleteKnowledge(id);
        call.enqueue(new Callback<UniversalResult>() {
            @Override
            public void onResponse(Call<UniversalResult> call, Response<UniversalResult> response) {
                if(response.body().getResultCode()==1) {
                    adapter.remove(id);
                    Toast.makeText(KnowledgeManageActivity.this, "知识标签删除成功", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(KnowledgeManageActivity.this, "知识标签删除失败", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<UniversalResult> call, Throwable t) {
                Toast.makeText(KnowledgeManageActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchKnowledge(int grade, String subject){
        ArrayList<Knowledge> tempList = new ArrayList<>();
        for(Knowledge knowledge:knowledgeList){
            String[] array = knowledge.getLabel().split(",");
            int kGrade = Integer.valueOf(array[1]);
            String kSubject = array[2];
            if(kGrade==grade&&kSubject.equals(subject))
                tempList.add(knowledge);
        }
        for(Knowledge knowledge:tempList)
            Log.e("knowledge:", knowledge.getLabel());
        if(tempList.size()==0){
            recyclerView.setVisibility(View.GONE);
            layout_no_knowledge.setVisibility(View.VISIBLE);
        }else{
            layout_no_knowledge.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.setData(tempList);
        }
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
