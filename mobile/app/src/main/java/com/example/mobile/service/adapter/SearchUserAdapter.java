package com.example.mobile.service.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.R;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.ViewHolder> {

    private final List<String> mMember;
    private final LayoutInflater mInflater;
    public static final List<String> selectedUser = new ArrayList<>();

    public SearchUserAdapter(Context context, List<String> members){
        mMember= members;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public @NotNull ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View memberView = mInflater.inflate(R.layout.item_user_search, parent, false);
        return new ViewHolder(memberView);
    }

    @Override
    public void onBindViewHolder(SearchUserAdapter.ViewHolder holder, int position) {
        String member = mMember.get(position);
        holder.memberName.setText(member);

        if(selectedUser.contains(member)){
            holder.isChecked = true;
            holder.button.setBackgroundResource(R.drawable.btn_added_person);
        } else {
            holder.isChecked = false;
            holder.button.setBackgroundResource(R.drawable.btn_add_person);
        }

        holder.button.setOnClickListener(v -> {
            if(holder.isChecked){
                v.setBackgroundResource(R.drawable.btn_add_person);
                selectedUser.remove(member);
            } else {
                v.setBackgroundResource(R.drawable.btn_added_person);
                selectedUser.add(member);
            }
            holder.isChecked = !holder.isChecked;
        });
    }

    @Override
    public int getItemCount() {
        return mMember.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView memberName;
        public Button button;
        public boolean isChecked;

        public ViewHolder(View itemView) {
            super(itemView);
            isChecked = false;
            memberName = itemView.findViewById(R.id.member_name);
            button = itemView.findViewById(R.id.add_user_btn);
        }
    }

    public List<String> getSelectedUser(){
        return selectedUser;
    }
}
