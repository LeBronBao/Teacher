package com.example.mrwen.adapter;

import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrwen.Utils.UiUtils;
import com.example.mrwen.activity.R;
import com.example.mrwen.bean.Knowledge;
import com.example.mrwen.bean.Notice;
import com.example.mrwen.view.OnDeleteClickListener;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.ButterKnife;

/**
 * Created by LeBron on 2017/7/20.
 */

public class RecyclerKnowledgeAdapter extends BaseRecyclerViewAdapter<Knowledge, RecyclerKnowledgeAdapter.MyKnowledgeViewHolder>{
    public RecyclerKnowledgeAdapter(ArrayList<Knowledge> data){super(data);}
    private OnDeleteClickListener onDeleteClickListener;

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener){
        this.onDeleteClickListener = onDeleteClickListener;
    }

    //通过适配器返回一个ViewHolder
    @Override
    public RecyclerKnowledgeAdapter.MyKnowledgeViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_recycler_knowledge, parent, false);
        return new RecyclerKnowledgeAdapter.MyKnowledgeViewHolder(view);
    }

    //将数据与ViewHolder绑定起来
    @Override
    public void onBindViewHolder(RecyclerKnowledgeAdapter.MyKnowledgeViewHolder holder, int position){
        final Knowledge knowledge = data.get(position);
        String[] array = knowledge.getLabel().split(",");
        String label = array[0];
        String grade = getGrade(Integer.valueOf(array[1]));
        String subject = array[2];
        String description = knowledge.getDescription();
        if(description.equals(""))
            description = "（该知识未有更多描述）";
        holder.tv_knowledge_grade_and_subject.setText(grade+"    "+subject);
        holder.tv_knowledge_label.setText(label);
        holder.tv_knowledge_description.setText(description);
        holder.layout_delete_knowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onDeleteClickListener!=null)
                    onDeleteClickListener.onDeleteClick(knowledge.getId());
            }
        });
    }

    public void remove(int id){
        for (Iterator it = data.iterator(); it.hasNext();) {
            Knowledge knowledge = (Knowledge) it.next();
            if (knowledge.getId()==id)
                it.remove();
        }
        UiUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
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

    class MyKnowledgeViewHolder extends RecyclerView.ViewHolder{
        TextView tv_knowledge_grade_and_subject;
        TextView tv_knowledge_label;
        TextView tv_knowledge_description;
        LinearLayout layout_delete_knowledge;
        MyKnowledgeViewHolder(View itemView){
            super(itemView);
            tv_knowledge_grade_and_subject = ButterKnife.findById(itemView, R.id.tv_knowledge_grade_and_subject);
            tv_knowledge_label = ButterKnife.findById(itemView, R.id.tv_knowledge_label);
            tv_knowledge_description = ButterKnife.findById(itemView, R.id.tv_knowledge_description);
            layout_delete_knowledge = ButterKnife.findById(itemView, R.id.layout_delete_knowledge);
        }
    }
}
