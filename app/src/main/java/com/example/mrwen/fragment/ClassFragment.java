package com.example.mrwen.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mrwen.Utils.GlideCircleTransform;
import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.Utils.UiUtils;
import com.example.mrwen.activity.ClassCreateActivity;
import com.example.mrwen.activity.CollegeStudentActivity;
import com.example.mrwen.activity.MainActivity;
import com.example.mrwen.activity.MyClassActivity;
import com.example.mrwen.activity.R;
import com.example.mrwen.activity.StudentActivity;
import com.example.mrwen.activity.StudentRequestActivity;
import com.example.mrwen.activity.WriteNoticeActivity;
import com.example.mrwen.adapter.RecyclerClassNoticeAdapter;
import com.example.mrwen.bean.Notice;
import com.example.mrwen.bean.Student;
import com.example.mrwen.bean.UniversalResult;
import com.example.mrwen.interfaces.InterfaceClass;
import com.example.mrwen.interfaces.InterfaceCourse;
import com.example.mrwen.staticClass.StaticInfo;
import com.example.mrwen.view.OnDeleteClickListener;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.SinaRefreshView;

import java.util.ArrayList;
import java.util.zip.Inflater;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassFragment extends BaseFragment {
    private RecyclerView noticeRecyclerView;
    private RecyclerView courseRecyclerView;
    private RecyclerClassNoticeAdapter adapter;
    private Button createClassButton;
    private TextView teacherName;
    private TextView classStuNum;
    private TextView clgStuNum;
    private TextView stuRequestNum;
    private ImageView teacherImage;
    private Button createClassNotice;
    private Button deleteClassButton;
    private Button classStudentButton;
    private Button collegeStudentButton;
    private Button studentRequestButton;
    TwinklingRefreshLayout refreshLayout;

    public ClassFragment() {
        // Required empty public constructor
    }


    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;

        if(StaticInfo.classSum==0){
            view = inflater.inflate(R.layout.warning_no_adminclass, null);
            createClassButton = (Button)view.findViewById(R.id.create_new_class);
            createClassButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent createClassIntent = new Intent(getActivity(), ClassCreateActivity.class);
                    startActivity(createClassIntent);
                }
            });
        }else{
            view = inflater.inflate(R.layout.content_my_class, null);
            teacherName = (TextView)view.findViewById(R.id.tv_name);
            classStuNum = (TextView)view.findViewById(R.id.tv_students);
            clgStuNum = (TextView)view.findViewById(R.id.tv_college_students);
            stuRequestNum = (TextView)view.findViewById(R.id.tv_student_requests);
            teacherImage = (ImageView)view.findViewById(R.id.iv_teacher_image);
            createClassNotice = (Button)view.findViewById(R.id.bt_class_notice);
            deleteClassButton = (Button)view.findViewById(R.id.bt_delete_class);
            deleteClassButton.setVisibility(View.GONE);
            classStudentButton = (Button)view.findViewById(R.id.bt_class_student);
            collegeStudentButton = (Button)view.findViewById(R.id.bt_college_student);
            studentRequestButton = (Button)view.findViewById(R.id.bt_student_requests);
            noticeRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_class_notices);
            teacherName.setText(StaticInfo.realname+"(班级Id："+StaticInfo.classId+")");
            classStuNum.setText(StaticInfo.classStudentNumber+"位学生");
            clgStuNum.setText(StaticInfo.collegeStudentNumber+"位志愿者");
            stuRequestNum.setText(StaticInfo.studentRequestNumber+"个加班申请");

            Glide.with(UiUtils.getContext()).load(UiUtils.getContext().getResources().getString(R.string.baseURL) + StaticInfo.imageURL)
                    .placeholder(R.drawable.ic_account_circle_blue_600_24dp)
                    .transform(new GlideCircleTransform(UiUtils.getContext()))
                    .into(teacherImage);
            createClassNotice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent createNoticeIntent = new Intent(getActivity(), WriteNoticeActivity.class);
                    startActivity(createNoticeIntent);
                }
            });
            deleteClassButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(getActivity()).setTitle("系统提示")
                            .setMessage("您确认要删除该班级吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    deleteClass(Integer.valueOf(StaticInfo.classId));

                                }
                            }).setNegativeButton("返回", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                    }).show();
                }
            });
            classStudentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent studentIntent = new Intent(getActivity(), StudentActivity.class);
                    if(StaticInfo.classStudentNumber==0)
                        studentIntent.putExtra("flag", 0);
                    else
                        studentIntent.putExtra("flag", 1);
                    startActivity(studentIntent);
                }
            });
            collegeStudentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent clgStuIntent = new Intent(getActivity(), CollegeStudentActivity.class);
                    startActivity(clgStuIntent);
                }
            });
            studentRequestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent studentRequestIntent = new Intent(getActivity(), StudentRequestActivity.class);
                    if(StaticInfo.studentRequestNumber==0)
                        studentRequestIntent.putExtra("flag", 0);
                    else
                        studentRequestIntent.putExtra("flag", 1);
                    startActivity(studentRequestIntent);
                }
            });
        }

        return view;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if(StaticInfo.classSum!=0)
            loadClassNotices();
    }

    //删除班级
    private void deleteClass(int id){
        final MyDialog alertDialog=new MyDialog();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceClass deleteClass = retrofit.create(InterfaceClass.class);
        final Call<UniversalResult> call = deleteClass.deleteClass(id);
        call.enqueue(new Callback<UniversalResult>() {
            @Override
            public void onResponse(Call<UniversalResult> call, Response<UniversalResult> response) {
                if(response.body()!=null){
                    if(response.body().getResultCode()==1) {
                        alertDialog.showAlertDialog(getActivity(), "删除班级成功");
                        StaticInfo.classSum = 0;
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }else{
                        alertDialog.showAlertDialog(getActivity(), "删除班级失败");
                    }
                }else{
                    alertDialog.showAlertDialog(getActivity(), "后台返回数据出错");
                }
            }
            @Override
            public void onFailure(Call<UniversalResult> call, Throwable t) {
                alertDialog.showAlertDialog(getActivity(), t.toString());
            }
        });
    }

    //加载班级公告
    private void loadClassNotices(){
        adapter = new RecyclerClassNoticeAdapter(new ArrayList<Notice>());
        noticeRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        noticeRecyclerView.setAdapter(adapter);

        adapter.setOnDeleteClickListener(new OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int id) {
                final int thisId=id;
                new android.support.v7.app.AlertDialog.Builder(getActivity()).setTitle("提示")
                        .setMessage("您确认要删除该公告么？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteNotice(thisId);
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
        final InterfaceClass getNotices = retrofit.create(InterfaceClass.class);
        final Call<ArrayList<Notice>> call = getNotices.getClassNotice(StaticInfo.classId);
        call.enqueue(new Callback<ArrayList<Notice>>() {
            @Override
            public void onResponse(Call<ArrayList<Notice>> call, Response<ArrayList<Notice>> response) {
                if(response.body()!=null){
                    Toast.makeText(getActivity(),"获取班级公告成功，共"+response.body().size()+"条班级公告", Toast.LENGTH_SHORT).show();
                    adapter.setData(response.body());
                }else{
                    Toast.makeText(getActivity(), "获取班级公告失败，请检查网络", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Notice>> call, Throwable t) {
                Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //删除一条班级公告
    private void deleteNotice(final int id) {
        final MyDialog alertDialog=new MyDialog();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceCourse deleteNotice=retrofit.create(InterfaceCourse.class);
        final Call<UniversalResult> call=deleteNotice.deleteNotice(id);
        call.enqueue(new Callback<UniversalResult>() {
            @Override
            public void onResponse(Call<UniversalResult> call, Response<UniversalResult> response) {
                if(response.body().getResultCode()==1) {
                    adapter.remove(id);
                    alertDialog.showAlertDialog(getActivity(),"删除公告成功");
                }else{
                    alertDialog.showAlertDialog(getActivity(),"删除公告失败");
                }
            }
            @Override
            public void onFailure(Call<UniversalResult> call, Throwable t) {
                alertDialog.showAlertDialog(getActivity(),t.toString());
            }
        });
    }

}
