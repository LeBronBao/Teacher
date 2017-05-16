package com.example.mrwen.activity;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.adapter.RecyclerTestAdapter;
import com.example.mrwen.bean.Exercise;
import com.example.mrwen.interfaces.InterfaceTeacher;
import com.example.mrwen.staticClass.StaticInfo;
import com.example.mrwen.view.OnDeleteClickListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TestActivity extends AppCompatActivity {


    @Bind(R.id.recycler_test)
    RecyclerView recycler_test;
    private RecyclerTestAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    //actionBar显示创建单元的menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_new_test, menu);
        return true;
    }

    //创建单元menu设置按钮响应
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.create_new_test_item) {

        }
        return super.onOptionsItemSelected(item);
    }

    public void initView(){
        mAdapter = new RecyclerTestAdapter(new ArrayList<Exercise>());
        recycler_test.setLayoutManager(new LinearLayoutManager(this));
        recycler_test.setAdapter(mAdapter);
//        mAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<Notice>() {
//            @Override
//            public void onItemClick(View v, Notice data) {
//
//            }
//        });
        mAdapter.setOnDeleteClickListener(new OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int id) {
                final int thisId=id;
                new android.support.v7.app.AlertDialog.Builder(TestActivity.this).setTitle("提示")
                        .setMessage("您确认要删除该题目么？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //deleteNotice(thisId);
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
            }
        });
    }

    private void initData() {
        final MyDialog alertDialog=new MyDialog();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceTeacher getTest=retrofit.create(InterfaceTeacher.class);
        final Call<ArrayList<Exercise>> call=getTest.getTest("l"+StaticInfo.currentLessonId);
        call.enqueue(new Callback<ArrayList<Exercise>>() {
            @Override
            public void onResponse(Call<ArrayList<Exercise>> call, Response<ArrayList<Exercise>> response) {
                if(response.body()!=null) {
                    mAdapter.setData(response.body());
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Exercise>> call, Throwable t) {
                alertDialog.showAlertDialog(TestActivity.this,t.toString());
            }
        });
    }
}
