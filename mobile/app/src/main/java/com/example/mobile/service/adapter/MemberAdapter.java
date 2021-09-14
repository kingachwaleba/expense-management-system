package com.example.mobile.service.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.R;
import com.example.mobile.model.Member;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {

    private final List<Member> mMember;
    private final LayoutInflater mInflater;
    private String mLogin;

    public MemberAdapter(Context context, List<Member> members, String login){
        mMember= members;
        mInflater = LayoutInflater.from(context);
        mLogin = login;
    }

    @Override
    public @NotNull ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View memberView = mInflater.inflate(R.layout.item_member, parent, false);
        return new ViewHolder(memberView);
    }

    @Override
    public void onBindViewHolder(MemberAdapter.ViewHolder holder, int position) {
        Member member = mMember.get(position);
        holder.memberNameTv.setText(member.getLogin());

        holder.memberBalanceTv.setText(String.valueOf(member.getBalance()));
        if(member.getBalance() >= 0)
            holder.memberBalanceTv.setTextColor(Color.GREEN);
        else holder.memberBalanceTv.setTextColor(Color.RED);

        if(member.getDebt()!=null && !member.getLogin().equals(mLogin)){
            holder.showDebthBtn.setVisibility(View.VISIBLE);
            if(member.getDebt().getCreditor().getLogin().equals(mLogin)){
                holder.showDebthBtn.setBackgroundResource(R.drawable.btn_debtor);
            } else {
                holder.showDebthBtn.setBackgroundResource(R.drawable.btn_creditor);
            }
        }

        holder.showDebthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(member.getDebt().getCreditor().getLogin().equals(mLogin)){
                    holder.memberBalanceToYouTv.setText(String.valueOf(member.getDebt().getHowMuch()));
                    holder.memberBalanceToYouTv.setTextColor(Color.GREEN);
                    holder.memberBalanceToYouTv.setVisibility(View.VISIBLE);
                    holder.reminderBtn.setVisibility(View.VISIBLE);
                    holder.handshakeBtn.setVisibility(View.VISIBLE);
                } else {
                    holder.memberBalanceToYouTv.setText(String.valueOf(member.getDebt().getHowMuch()));
                    holder.memberBalanceToYouTv.setTextColor(Color.RED);
                    holder.memberBalanceToYouTv.setVisibility(View.VISIBLE);
                    holder.reminderTv.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMember.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView memberNameTv, memberBalanceTv, reminderTv, memberBalanceToYouTv;
        public Button reminderBtn, handshakeBtn, showDebthBtn;
        public int id;

        public ViewHolder(View itemView) {
            super(itemView);
            memberNameTv = itemView.findViewById(R.id.member_name_tv);
            memberBalanceTv = itemView.findViewById(R.id.member_balance_tv);
            reminderTv = itemView.findViewById(R.id.reminder_tv);
            memberBalanceToYouTv = itemView.findViewById(R.id.member_balance_to_you_tv);
            reminderBtn = itemView.findViewById(R.id.reminder_btn);
            handshakeBtn = itemView.findViewById(R.id.handshake_btn);
            showDebthBtn = itemView.findViewById(R.id.show_debth_btn);

            memberBalanceToYouTv.setVisibility(View.GONE);
            reminderTv.setVisibility(View.GONE);
            reminderBtn.setVisibility(View.GONE);
            handshakeBtn.setVisibility(View.GONE);
            showDebthBtn.setVisibility(View.GONE);
        }
    }
}
