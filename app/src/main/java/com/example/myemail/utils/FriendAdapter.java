package com.example.myemail.utils;

import android.annotation.SuppressLint;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myemail.R;

import java.util.ArrayList;
import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    private List<String> friends;
    private SparseBooleanArray selectedItems;

    public FriendAdapter(List<String> friends) {
        this.friends = friends;
        this.selectedItems = new SparseBooleanArray();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String friend = friends.get(position);
        holder.friendName.setText(friend);
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(selectedItems.get(position, false));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedItems.put(position, true);
                } else {
                    selectedItems.delete(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public List<String> getSelectedFriends() {
        List<String> selectedFriends = new ArrayList<>();
        for (int i = 0; i < selectedItems.size(); i++) {
            selectedFriends.add(friends.get(selectedItems.keyAt(i)));
        }
        return selectedFriends;
    }

    public void clearSelectedFriends() {
        selectedItems.clear();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;
        TextView friendName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.cb_friend);
            friendName = itemView.findViewById(R.id.friendname);
        }
    }
}