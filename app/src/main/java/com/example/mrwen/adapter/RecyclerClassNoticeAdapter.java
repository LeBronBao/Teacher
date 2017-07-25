package com.example.mrwen.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mrwen.Utils.TimeUtils;
import com.example.mrwen.Utils.UiUtils;
import com.example.mrwen.activity.R;
import com.example.mrwen.bean.Notice;
import com.example.mrwen.view.OnDeleteClickListener;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.ButterKnife;

/**
 * Created by LeBron on 2017/4/21.
 */

public class RecyclerClassNoticeAdapter extends BaseRecyclerViewAdapter<Notice,RecyclerClassNoticeAdapter.MyNoticesViewHolder> {
    public RecyclerClassNoticeAdapter(ArrayList<Notice> data) {
        super(data);
    }
    private OnDeleteClickListener mOnDeleteClickListener;

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        mOnDeleteClickListener = onDeleteClickListener;
    }

    @Override
    public RecyclerClassNoticeAdapter.MyNoticesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_recycler_class_notice, parent, false);

        return new RecyclerClassNoticeAdapter.MyNoticesViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerClassNoticeAdapter.MyNoticesViewHolder holder, int position) {
        final Notice notice = data.get(position);

        holder.tv_class_notice_name.setText( notice.getName());
        holder.tv_class_notice_content.setText(notice.getContent() );
        holder.tv_class_notice_number.setText("浏览"+notice.getNumber()+"次");
        holder.tv_class_notice_date.setText(TimeUtils.ExactFormat(notice.getDate()));
        holder.layout_class_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener!=null)
                    mListener.onItemClick(v, notice);
            }
        });
        holder.layout_delete_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnDeleteClickListener!=null)
                    mOnDeleteClickListener.onDeleteClick(notice.getId());
            }
        });


    }

    public void remove(int id){
        for (Iterator it = data.iterator(); it.hasNext();) {
            Notice notice=(Notice) it.next();
            if (notice.getId()==id)
                it.remove();
        }
        UiUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    class MyNoticesViewHolder extends RecyclerView.ViewHolder{
        TextView tv_class_notice_name;
        TextView tv_class_notice_content;
        TextView tv_class_notice_number;
        TextView tv_class_notice_date;
        LinearLayout layout_class_notice;
        LinearLayout layout_delete_notice;
        public MyNoticesViewHolder(View itemView) {
            super(itemView);
            tv_class_notice_name = ButterKnife.findById(itemView, R.id.tv_class_notice_name);
            tv_class_notice_content = ButterKnife.findById(itemView, R.id.tv_class_notice_content);
            tv_class_notice_number = ButterKnife.findById(itemView, R.id.tv_class_notice_number);
            tv_class_notice_date = ButterKnife.findById(itemView, R.id.tv_class_notice_date);
            layout_class_notice = ButterKnife.findById(itemView, R.id.layout_class_notice);
            layout_delete_notice = ButterKnife.findById(itemView, R.id.layout_delete_notice);
        }
    }
}
