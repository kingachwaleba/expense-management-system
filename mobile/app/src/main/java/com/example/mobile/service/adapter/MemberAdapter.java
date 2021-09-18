package com.example.mobile.service.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.R;
import com.example.mobile.model.Member;
import com.example.mobile.service.ExpenseService;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {

    private final List<Member> mMember;
    private final LayoutInflater mInflater;
    private final String mLogin, mAccessToken;
    private final int mWalletId;

    public MemberAdapter(Context context, List<Member> members, String login){
        mMember= members;
        mInflater = LayoutInflater.from(context);
        mLogin = login;
        mAccessToken = "";
        mWalletId = 0;
    }

    public MemberAdapter(Context context, List<Member> members, String login, String accessToken, int walletId){
        mMember= members;
        mInflater = LayoutInflater.from(context);
        mLogin = login;
        mAccessToken = accessToken;
        mWalletId = walletId;
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
        String balance;

        if(member.getBalance() >= 0){
            balance = "+" + member.getBalance() + "zł";
            holder.memberBalanceTv.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.green));
        }
        else {
            balance = member.getBalance() + "zł";
            holder.memberBalanceTv.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.red_error));
        }
        holder.memberBalanceTv.setText(balance);

        if(member.getDebt()!=null && !member.getLogin().equals(mLogin)){
            holder.showDebthBtn.setVisibility(View.VISIBLE);
            if(member.getDebt().getCreditor().getLogin().equals(mLogin)){
                holder.showDebthBtn.setBackgroundResource(R.drawable.btn_debtor);
            } else {
                holder.showDebthBtn.setBackgroundResource(R.drawable.btn_creditor);
            }
        }

        holder.showDebthBtn.setOnClickListener(v -> {
            String label;
            if(holder.memberBalanceToYouTv.getVisibility()==View.VISIBLE){
                holder.memberBalanceToYouTv.setVisibility(View.GONE);
                holder.reminderBtn.setVisibility(View.GONE);
                holder.handshakeBtn.setVisibility(View.GONE);
            }
            else{
                if(member.getDebt().getCreditor().getLogin().equals(mLogin)){
                    label = "Należność: " + member.getDebt().getHowMuch() + "zł";
                    holder.memberBalanceToYouTv.setText(label);
                    holder.memberBalanceToYouTv.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.green));
                    holder.memberBalanceToYouTv.setVisibility(View.VISIBLE);
                    holder.reminderBtn.setVisibility(View.VISIBLE);
                    holder.handshakeBtn.setVisibility(View.VISIBLE);
                } else {
                    label = "Oddaj " + member.getDebt().getCreditor().getLogin() + " " + member.getDebt().getHowMuch() + "zł";
                    holder.memberBalanceToYouTv.setText(label);
                    holder.memberBalanceToYouTv.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.red_error));
                    holder.memberBalanceToYouTv.setVisibility(View.VISIBLE);
                }
            }
        });

        holder.handshakeBtn.setOnClickListener(v -> {
            ExpenseService expenseService = new ExpenseService(holder.itemView.getContext());
            expenseService.payDebt(mAccessToken, mWalletId, member.getDebt());
            Toast.makeText(holder.itemView.getContext(), "Dług został uregulowany", Toast.LENGTH_LONG).show();
        });

        holder.reminderBtn.setOnClickListener(v -> {
            ExpenseService expenseService = new ExpenseService(holder.itemView.getContext());
            expenseService.sendDebtNotification(mAccessToken, mWalletId, member.getDebt());
            Toast.makeText(holder.itemView.getContext(), "Przypomnienie o długu zostało wysłane", Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public int getItemCount() {
        return mMember.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView memberNameTv, memberBalanceTv, memberBalanceToYouTv;
        public Button reminderBtn, handshakeBtn, showDebthBtn;
        public int id;

        public ViewHolder(View itemView) {
            super(itemView);
            memberNameTv = itemView.findViewById(R.id.member_name_tv);
            memberBalanceTv = itemView.findViewById(R.id.member_balance_tv);
            memberBalanceToYouTv = itemView.findViewById(R.id.member_balance_to_you_tv);
            reminderBtn = itemView.findViewById(R.id.reminder_btn);
            handshakeBtn = itemView.findViewById(R.id.handshake_btn);
            showDebthBtn = itemView.findViewById(R.id.show_debth_btn);

            memberBalanceToYouTv.setVisibility(View.GONE);
            reminderBtn.setVisibility(View.GONE);
            handshakeBtn.setVisibility(View.GONE);
            showDebthBtn.setVisibility(View.GONE);
        }
    }
}
