package com.example.mrwen.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mrwen.Utils.TimeUtils;
import com.example.mrwen.Utils.UiUtils;
import com.example.mrwen.activity.R;
import com.example.mrwen.bean.AddClassRequest;
import com.example.mrwen.bean.AdminClass;
import com.example.mrwen.bean.Student;
import com.example.mrwen.view.OnAddStudentClickListener;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by LeBron on 2017/5/7.
 */

public class RecyclerStudentRequestAdapter extends BaseRecyclerViewAdapter<AddClassRequest, RecyclerStudentRequestAdapter.StudentRequestViewHolder> {

    public RecyclerStudentRequestAdapter(ArrayList<AddClassRequest> data) {
        super(data);
    }
    private OnAddStudentClickListener onAddStudentClickListener;

    public void setOnClickListener(OnAddStudentClickListener onClickListener){
        this.onAddStudentClickListener = onClickListener;
    }

    @Override
    public RecyclerStudentRequestAdapter.StudentRequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_recycler_student_request, parent, false);
        return new RecyclerStudentRequestAdapter.StudentRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerStudentRequestAdapter.StudentRequestViewHolder holder, int position) {
        final AddClassRequest addClassRequest = data.get(position);
        final Student student = addClassRequest.getStudent();
        final AdminClass adminClass = addClassRequest.getAdminClass();

        holder.tv_student_name.setText(student.getRealname());
        holder.tv_student_gender.setText(student.getGender());
        holder.tv_student_info.setText(student.getSignature());
        holder.student_info_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener!=null)
                    mListener.onItemClick(v, addClassRequest);
            }
        });
        holder.bt_add_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onAddStudentClickListener!=null) {
                    onAddStudentClickListener.onAddStudentClick(student.getId());
                    holder.bt_add_student.setVisibility(View.GONE);
                    holder.tv_added.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    class StudentRequestViewHolder extends RecyclerView.ViewHolder{
        TextView tv_student_name;
        TextView tv_student_gender;
        TextView tv_student_info;
        TextView tv_added;
        LinearLayout student_info_layout;
        FrameLayout add_student_layout;
        Button bt_add_student;
        public StudentRequestViewHolder(View itemView) {
            super(itemView);
            tv_student_name = ButterKnife.findById(itemView, R.id.tv_student_name);
            tv_student_gender = ButterKnife.findById(itemView, R.id.tv_student_gender);
            tv_student_info = ButterKnife.findById(itemView, R.id.tv_student_info);
            tv_added = ButterKnife.findById(itemView, R.id.tv_added);
            student_info_layout = ButterKnife.findById(itemView, R.id.ll_student_info);
            add_student_layout = ButterKnife.findById(itemView, R.id.fl_add);
            bt_add_student = ButterKnife.findById(itemView, R.id.bt_add_student);
        }
    }
}
