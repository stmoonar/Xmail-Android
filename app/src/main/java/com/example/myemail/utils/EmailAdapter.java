package com.example.myemail.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myemail.R;
import com.example.myemail.pojo.Mail;

import java.util.List;

public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.ViewHolder> {

    private List<Mail> mailList;
    private OnItemClickListener onItemClickListener;

    public EmailAdapter(List<Mail> mailList, OnItemClickListener onItemClickListener) {
        this.mailList = mailList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_email, parent, false);
        return new ViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mail mail = mailList.get(position);
        holder.tvSubject.setText(mail.getSubject());
        holder.tvDate.setText(mail.getSendTime().toString());
        holder.tvFrom.setText(mail.getFromEmail());
    }

    @Override
    public int getItemCount() {
        return mailList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvSubject;
        TextView tvDate;
        TextView tvFrom;
        OnItemClickListener onItemClickListener;

        public ViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            tvSubject = itemView.findViewById(R.id.tv_subject);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvFrom = itemView.findViewById(R.id.tv_from);
            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}