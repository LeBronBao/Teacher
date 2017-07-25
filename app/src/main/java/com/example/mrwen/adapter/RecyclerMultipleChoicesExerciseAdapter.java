package com.example.mrwen.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mrwen.Utils.UiUtils;
import com.example.mrwen.activity.R;
import com.example.mrwen.bean.MultipleChoicesExercise;
import com.example.mrwen.view.OnCheckExerciseInfoClickListener;
import com.example.mrwen.view.OnDeleteClickListener;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.ButterKnife;

/**
 * Created by LeBron on 2017/7/24.
 */

public class RecyclerMultipleChoicesExerciseAdapter extends BaseRecyclerViewAdapter<MultipleChoicesExercise, RecyclerMultipleChoicesExerciseAdapter.MyMultipleChoicesExerciseViewHolder>{
    public RecyclerMultipleChoicesExerciseAdapter(ArrayList<MultipleChoicesExercise> data){
        super(data);
    }
    private OnDeleteClickListener onDeleteClickListener;
    private OnCheckExerciseInfoClickListener onCheckExerciseInfoClickListener;

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener){
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public void setOnCheckExerciseInfoClickListener(OnCheckExerciseInfoClickListener onCheckExerciseInfoClickListener){
        this.onCheckExerciseInfoClickListener = onCheckExerciseInfoClickListener;
    }

    @Override
    public RecyclerMultipleChoicesExerciseAdapter.MyMultipleChoicesExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_recycler_multiple_choices_exercise, parent, false);
        return new RecyclerMultipleChoicesExerciseAdapter.MyMultipleChoicesExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerMultipleChoicesExerciseAdapter.MyMultipleChoicesExerciseViewHolder holder, int position){
        final MultipleChoicesExercise exercise = data.get(position);
        String[] array = exercise.getQuestion().split(",");
        String question = array[0];
        String grade = getGrade(Integer.valueOf(array[1]));
        String subject = array[2];
        String knowledge = array[3];
        String option1 = exercise.getOption1();
        String option2 = exercise.getOption2();
        String option3 = exercise.getOption3();
        String option4 = exercise.getOption4();
        int answer = exercise.getAnswer();
        holder.tv_exercise_grade_and_subject.setText(grade+"   "+subject);
        holder.tv_exercise_question.setText(question);
        holder.tv_exercise_option1.setText(option1);
        holder.tv_exercise_option2.setText(option2);
        holder.tv_exercise_option3.setText(option3);
        holder.tv_exercise_option4.setText(option4);
        holder.tv_exercise_knowledge.setText(knowledge);
        //将正确选项标为红色
        switch (answer){
            case 1:
                holder.tv_exercise_option1.setTextColor(Color.rgb(255, 0, 0));
                break;
            case 2:
                holder.tv_exercise_option2.setTextColor(Color.rgb(255, 0, 0));
                break;
            case 3:
                holder.tv_exercise_option3.setTextColor(Color.rgb(255, 0, 0));
                break;
            case 4:
                holder.tv_exercise_option4.setTextColor(Color.rgb(255, 0, 0));
                break;
        }
        holder.layout_delete_exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onDeleteClickListener!=null)
                    onDeleteClickListener.onDeleteClick(exercise.getId());
            }
        });
        holder.bt_check_exercise_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onCheckExerciseInfoClickListener!=null)
                    onCheckExerciseInfoClickListener.onCheckExerciseInfoClickListener(exercise.getId());
            }
        });
    }

    public void remove(int id){
        for (Iterator it = data.iterator(); it.hasNext();) {
            MultipleChoicesExercise exercise = (MultipleChoicesExercise) it.next();
            if (exercise.getId()==id)
                it.remove();
        }
        UiUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    class MyMultipleChoicesExerciseViewHolder extends RecyclerView.ViewHolder{
        TextView tv_exercise_grade_and_subject;
        TextView tv_exercise_question;
        TextView tv_exercise_option1;
        TextView tv_exercise_option2;
        TextView tv_exercise_option3;
        TextView tv_exercise_option4;
        TextView tv_exercise_knowledge;
        LinearLayout layout_delete_exercise;
        Button bt_check_exercise_info;
        MyMultipleChoicesExerciseViewHolder(View itemView){
            super(itemView);
            tv_exercise_grade_and_subject = ButterKnife.findById(itemView, R.id.tv_exercise_grade_and_subject);
            tv_exercise_question = ButterKnife.findById(itemView, R.id.tv_exercise_question);
            tv_exercise_option1 = ButterKnife.findById(itemView, R.id.tv_exercise_option1);
            tv_exercise_option2 = ButterKnife.findById(itemView, R.id.tv_exercise_option2);
            tv_exercise_option3 = ButterKnife.findById(itemView, R.id.tv_exercise_option3);
            tv_exercise_option4 = ButterKnife.findById(itemView, R.id.tv_exercise_option4);
            tv_exercise_knowledge = ButterKnife.findById(itemView, R.id.tv_exercise_knowledge);
            bt_check_exercise_info = ButterKnife.findById(itemView, R.id.bt_check_exercise_info);
            layout_delete_exercise = ButterKnife.findById(itemView, R.id.layout_delete_exercise);
        }
    }

    public String getGrade(int grade){
        String gradeString = "未知年级";
        switch (grade){
            case 1:
                gradeString = "一年级";
                break;
            case 2:
                gradeString = "二年级";
                break;
            case 3:
                gradeString = "三年级";
                break;
            case 4:
                gradeString = "四年级";
                break;
            case 5:
                gradeString = "五年级";
                break;
            case 6:
                gradeString = "六年级";
                break;
            case 7:
                gradeString = "七年级";
                break;
            case 8:
                gradeString = "八年级";
                break;
            case 9:
                gradeString = "九年级";
                break;
        }
        return gradeString;
    }
}
