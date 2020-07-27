package com.jiangkang.storage.greendao;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiangkang.storage.R;

import java.util.List;

/**
 * Created by jiangkang on 2017/11/13.
 * description：Note
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder>{

    private Context mContext;

    private List<Note> mData;


    public NoteAdapter(Context context, List<Note> data) {
        this.mContext = context;
        this.mData = data;
    }

    public interface OnItemClickListener{
        void onItemClick();
    }


    private OnItemClickListener listener;


    public void setOnItemClickListener(@NonNull OnItemClickListener listener){
        this.listener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_note,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mData != null && !mData.isEmpty()){
            holder.tvContent.setText(mData.get(position).getContent());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        listener.onItemClick();
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvContent;

        TextView tvTimeAdded;

        public ViewHolder(View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tv_item_content);
            tvTimeAdded = itemView.findViewById(R.id.tv_item_add_time);
        }
    }
}
