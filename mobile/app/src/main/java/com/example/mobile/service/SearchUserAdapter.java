package com.example.mobile.service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.R;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.ViewHolder> {

    private final ArrayList<String> mMember;
    private final LayoutInflater mInflater;
    public SearchUserAdapter(Context context, ArrayList<String> members){
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
    }

    @Override
    public int getItemCount() {
        return mMember.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView memberName;
        public int id;

        public ViewHolder(View itemView) {
            super(itemView);
            memberName = itemView.findViewById(R.id.member_name);
        }
    }
}
