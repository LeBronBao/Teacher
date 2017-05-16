package com.example.mrwen.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.ButterKnife;
import com.example.mrwen.bean.Course;
import com.example.mrwen.Utils.*;
import com.example.mrwen.activity.R;

/**
 * Created by fate on 2016/11/13.
 */

public class RecyclerSubjectAdapter extends BaseRecyclerViewAdapter<Course,RecyclerSubjectAdapter.ViewHolder> {

    private static String SERVER_URL = "http://60.205.190.45:8080/education/";

    public RecyclerSubjectAdapter(ArrayList<Course> data) {
        super(data);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_recycler_subject,parent,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(v, (Course) v.getTag());
            }
        });
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Course course = data.get(position);
        Glide.with(UiUtils.getContext())
                .load(SERVER_URL+ course.getCoverURL())
                .placeholder(R.drawable.bg)
                .dontTransform()
                .into(holder.ivLesson);
        holder.tvName.setText(course.getName());
        holder.tvDuration.setText(course.getChapterNumber()+"个章节");
        holder.tvNumber.setText(course.getFocusNumber()+"人关注");
        holder.itemView.setTag(data.get(position));
    }

     static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivLesson;
        TextView tvName;
        TextView tvDuration;
        TextView tvNumber;
         View itemView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ivLesson = ButterKnife.findById(itemView, R.id.iv_item_lesson_seletion);
            tvName = ButterKnife.findById(itemView, R.id.tv_item_selection_lesson_name);
            tvDuration = ButterKnife.findById(itemView, R.id.tv_item_selection_lesson_duration);
            tvNumber = ButterKnife.findById(itemView, R.id.tv_item_selection_lesson_numbers);

        }
    }
}
