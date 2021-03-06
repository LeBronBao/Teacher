package com.example.mrwen.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mrwen.Utils.UiUtils;
import com.example.mrwen.activity.R;
import com.example.mrwen.bean.Exercise;
import com.example.mrwen.view.OnDeleteClickListener;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.ButterKnife;

/**
 * Created by mrwen on 2017/3/23.
 */

public class RecyclerTestAdapter extends BaseRecyclerViewAdapter<Exercise,RecyclerTestAdapter.MyTestsViewHolder> {
    public RecyclerTestAdapter(ArrayList<Exercise> data) {
        super(data);
    }
    private OnDeleteClickListener mOnDeleteClickListener;

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        mOnDeleteClickListener = onDeleteClickListener;
    }

    @Override
    public MyTestsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_recycler_test, parent, false);
        return new MyTestsViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MyTestsViewHolder holder, int position) {
        final Exercise cq = data.get(position);

        holder.tv_test_title.setText(cq.getTitle());
        holder.tv_test_optionA.setText(cq.getOptionA());
        holder.tv_test_optionB.setText(cq.getOptionB());
        holder.tv_test_optionC.setText(cq.getOptionC());
        holder.tv_test_optionD.setText(cq.getOptionD());
        holder.tv_test_number.setText("作答"+cq.getNumber()+"次");

        holder.layout_test_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener!=null)
                    mListener.onItemClick(v, cq);
            }
        });
        holder.iv_test_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnDeleteClickListener!=null)
                    mOnDeleteClickListener.onDeleteClick(cq.getId());
            }
        });


    }
    public void remove(int id){
        for (Iterator iter = data.iterator(); iter.hasNext();) {
            Exercise cq=(Exercise) iter.next();
            if (cq.getId()==id)
                iter.remove();
        }
        UiUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    class MyTestsViewHolder extends RecyclerView.ViewHolder{
        TextView tv_test_title;
        TextView tv_test_optionA;
        TextView tv_test_optionB;
        TextView tv_test_optionC;
        TextView tv_test_optionD;
        TextView tv_test_number;
        ImageView iv_test_delete;
        LinearLayout layout_test_check;
        public MyTestsViewHolder(View itemView) {
            super(itemView);
            tv_test_title = ButterKnife.findById(itemView, R.id.tv_test_title);
            tv_test_optionA = ButterKnife.findById(itemView, R.id.tv_test_optionA);
            tv_test_optionB = ButterKnife.findById(itemView, R.id.tv_test_optionB);
            tv_test_optionC = ButterKnife.findById(itemView, R.id.tv_test_optionC);
            tv_test_optionD = ButterKnife.findById(itemView, R.id.tv_test_optionD);
            tv_test_number = ButterKnife.findById(itemView, R.id.tv_test_number);
            iv_test_delete = ButterKnife.findById(itemView, R.id.iv_test_delete);
            layout_test_check = ButterKnife.findById(itemView, R.id.layout_test_check);
        }
    }
}