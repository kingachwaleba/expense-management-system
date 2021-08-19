package com.example.mobile.service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.R;
import com.example.mobile.model.Member;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {

    private final List<Member> mMember;
    private final LayoutInflater mInflater;
    public MemberAdapter(Context context, List<Member> members){
        mMember= members;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public @NotNull ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View memberView = mInflater.inflate(R.layout.item_member, parent, false);
        return new ViewHolder(memberView);
    }

    @Override
    public void onBindViewHolder(MemberAdapter.ViewHolder holder, int position) {
        Member member = mMember.get(position);
        holder.memberName.setText(member.getLogin());
        //holder.memberBalance.setText(mInflater.getContext().getResources().getString(R.string.owner) + " " + member.getBalabce());
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
           // memberBalance = itemView.findViewById(R.id.member_balance);
        }
    }
}
