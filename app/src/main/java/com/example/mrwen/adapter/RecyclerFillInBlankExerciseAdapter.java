package com.example.mrwen.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mrwen.Utils.UiUtils;
import com.example.mrwen.activity.R;
import com.example.mrwen.bean.FillInBlankExercise;
import com.example.mrwen.bean.Knowledge;
import com.example.mrwen.view.OnCheckExerciseInfoClickListener;
import com.example.mrwen.view.OnDeleteClickListener;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.ButterKnife;

/**
 * Created by LeBron on 2017/7/24.
 */

public class RecyclerFillInBlankExerciseAdapter extends BaseRecyclerViewAdapter<FillInBlankExercise, RecyclerFillInBlankExerciseAdapter.MyFillInBlankExerciseViewHolder> {
    public RecyclerFillInBlankExerciseAdapter(ArrayList<FillInBlankExercise> data){
        super(data);
    }
    private OnDeleteClickListener onDeleteClickListener;
    private OnCheckExerciseInfoClickListener onCheckExerciseInfoClickListener;

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener){
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public void setOnClickListener(OnCheckExerciseInfoClickListener onCheckExerciseInfoClickListener){
        this.onCheckExerciseInfoClickListener = onCheckExerciseInfoClickListener;
    }

    @Override
    public RecyclerFillInBlankExerciseAdapter.MyFillInBlankExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_recycler_fill_in_blank_exercise, parent, false);
        return new RecyclerFillInBlankExerciseAdapter.MyFillInBlankExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerFillInBlankExerciseAdapter.MyFillInBlankExerciseViewHolder holder, int position){
        final FillInBlankExercise exercise = data.get(position);
        String[] array = exercise.getQuestion().split(",");
        String question = array[0];
        String grade = getGrade(Integer.valueOf(array[1]));
        String subject = array[2];
        String knowledge = array[3];
        String answer = exercise.getAnswer();
        holder.tv_exercise_grade_and_subject.setText(grade+"    "+subject);
        holder.tv_exercise_question.setText(question);
        holder.tv_exercise_answer.setText(answer);
        holder.tv_exercise_knowledge.setText(knowledge);
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
            FillInBlankExercise exercise = (FillInBlankExercise) it.next();
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

    class MyFillInBlankExerciseViewHolder extends RecyclerView.ViewHolder{
        TextView tv_exercise_grade_and_subject;
        TextView tv_exercise_question;
        TextView tv_exercise_answer;
        TextView tv_exercise_knowledge;
        Button bt_check_exercise_info;
        LinearLayout layout_delete_exercise;
        MyFillInBlankExerciseViewHolder(View itemView){
            super(itemView);
            tv_exercise_grade_and_subject = ButterKnife.findById(itemView, R.id.tv_exercise_grade_and_subject);
            tv_exercise_question = ButterKnife.findById(itemView, R.id.tv_exercise_question);
            tv_exercise_answer = ButterKnife.findById(itemView, R.id.tv_exercise_answer);
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
