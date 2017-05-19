package com.example.mrwen.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mrwen.Utils.GlideCircleTransform;
import com.example.mrwen.Utils.UiUtils;
import com.example.mrwen.activity.R;
import com.example.mrwen.bean.AddClassRequest;
import com.example.mrwen.bean.AdminClass;
import com.example.mrwen.bean.Student;
import com.example.mrwen.bean.StudentAndStudy;
import com.example.mrwen.view.OnAddStudentClickListener;
import com.example.mrwen.view.OnGetStudentInfoListener;
import com.example.mrwen.view.OnStudyInfoClickListener;
import com.example.mrwen.view.OnUserInfoClickListener;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by LeBron on 2017/5/8.
 */

public class RecyclerStudentAdapter extends BaseRecyclerViewAdapter<StudentAndStudy,RecyclerStudentAdapter.MyStudentViewHolder> {
    public RecyclerStudentAdapter(ArrayList<StudentAndStudy> data) {
        super(data);
    }
    private OnStudyInfoClickListener onStudyInfoClickListener;
    private OnUserInfoClickListener  onUserInfoClickListener;

    //在Activity中调用，设置监听响应事件
    public void setOnStudyInfoClickListener(OnStudyInfoClickListener onStudyInfoClickListener){
        this.onStudyInfoClickListener = onStudyInfoClickListener;
    }

    public void setOnUserInfoClickListener(OnUserInfoClickListener onUserInfoClickListener){
        this.onUserInfoClickListener = onUserInfoClickListener;
    }

    @Override
    public RecyclerStudentAdapter.MyStudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_recycler_student, parent, false);
        return new RecyclerStudentAdapter.MyStudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerStudentAdapter.MyStudentViewHolder holder, int position) {
        final ArrayList<Integer> idArray=new ArrayList<>();
        final ArrayList<String> nameArray=new ArrayList<>();
        for(StudentAndStudy s:data){
            idArray.add(s.getId());
            nameArray.add(s.getName());
        }
        final StudentAndStudy studentAndStudy = data.get(position);

        Glide.with(UiUtils.getContext())
                .load(UiUtils.getContext().getResources().getString(R.string.baseURL) + studentAndStudy.getImageURL())
                .placeholder(R.drawable.ic_account_circle_blue_600_24dp)
                .transform(new GlideCircleTransform(UiUtils.getContext()))
                .into(holder.iv_student_image);
        holder.tv_student_name.setText(studentAndStudy.getName());
        holder.tv_student_gender.setText(studentAndStudy.getGender());
        holder.tv_student_signature.setText(studentAndStudy.getSignature());
        holder.ll_student_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener!=null)
                    mListener.onItemClick(v, studentAndStudy);
            }
        });
        holder.bt_more_student_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onStudyInfoClickListener!=null) {
                    onStudyInfoClickListener.onStudyInfoClickListener(studentAndStudy.getId(),studentAndStudy.getName(),idArray,nameArray);
                }
            }
        });
        holder.iv_student_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onUserInfoClickListener!=null)
                    onUserInfoClickListener.onUserInfoClickListener("s"+studentAndStudy.getId());
            }
        });
    }


    class MyStudentViewHolder extends RecyclerView.ViewHolder{
        TextView tv_student_name;
        TextView tv_student_gender;
        TextView tv_student_signature;
        ImageView iv_student_image;
        LinearLayout ll_student_info;
        Button bt_more_student_info;
        public MyStudentViewHolder(View itemView) {
            super(itemView);
            tv_student_name = ButterKnife.findById(itemView, R.id.tv_student_name);
            tv_student_gender = ButterKnife.findById(itemView, R.id.tv_student_gender);
            tv_student_signature = ButterKnife.findById(itemView, R.id.tv_student_signature);
            iv_student_image = ButterKnife.findById(itemView, R.id.iv_student_image);
            ll_student_info = ButterKnife.findById(itemView, R.id.ll_student_info);
            bt_more_student_info = ButterKnife.findById(itemView, R.id.bt_more_student_info);
        }
    }
}
