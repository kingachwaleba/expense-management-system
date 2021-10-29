package com.example.mobile.service.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.R;
import com.example.mobile.model.Invitation;
import com.example.mobile.service.AccountService;

import org.jetbrains.annotations.NotNull;
import java.util.List;

public class InvitationAdapter extends RecyclerView.Adapter<InvitationAdapter.ViewHolder> {
    private final List<Invitation> mInvitations;
    private final LayoutInflater mInflater;
    private final String mAccessToken;
    private final AccountService accountService;

    public InvitationAdapter(Context context, List<Invitation> invitationItems, String accessToken){
        mInvitations = invitationItems;
        mInflater = LayoutInflater.from(context);
        mAccessToken = accessToken;
        accountService = new AccountService(context);
    }

    @Override
    public @NotNull InvitationAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View invitationView = mInflater.inflate(R.layout.item_notification_invitation, parent, false);
        return new InvitationAdapter.ViewHolder(invitationView);
    }

    @Override
    public void onBindViewHolder(InvitationAdapter.ViewHolder holder, int position) {
        Invitation invitationItem = mInvitations.get(position);
        String ownerNameS = mInflater.getContext().getResources().getString(R.string.owner_label) + " " + invitationItem.getOwner();
        String numberOfMembersS = mInflater.getContext().getResources().getString(R.string.number_of_members_label) + " " + invitationItem.getUserListCounter();

        holder.walletNameTv.setText(invitationItem.getName());
        holder.ownerNameTv.setText(ownerNameS);
        holder.numberOfMembersTv.setText(numberOfMembersS);

        holder.acceptBtn.setOnClickListener(v -> {
            accountService.manageInvitation(mAccessToken, invitationItem.getWalletUserId(),true);
            mInvitations.remove(position);
            notifyDataSetChanged();
        });

        holder.denyBtn.setOnClickListener(v -> {
            accountService.manageInvitation(mAccessToken, invitationItem.getWalletUserId(),false);
            mInvitations.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return mInvitations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView walletNameTv, ownerNameTv, numberOfMembersTv;
        public int walletId;
        public Button acceptBtn, denyBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            walletNameTv = itemView.findViewById(R.id.name_tv);
            ownerNameTv = itemView.findViewById(R.id.owner_tv);
            numberOfMembersTv = itemView.findViewById(R.id.number_of_members_tv);
            acceptBtn = itemView.findViewById(R.id.accept_btn);
            denyBtn = itemView.findViewById(R.id.deny_btn);
        }
    }

    public void clear(){
        mInvitations.clear();
    }
}
