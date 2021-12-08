package com.example.mobile.service.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.R;
import com.example.mobile.activity.ProfileActivity;
import com.example.mobile.activity.WalletActivity;
import com.example.mobile.model.Message;
import com.example.mobile.service.AccountService;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MessageNotificationAdapter extends RecyclerView.Adapter<MessageNotificationAdapter.ViewHolder> {
    private final List<Message> mMessages;
    private final LayoutInflater mInflater;
    private final String mAccessToken;
    private final AccountService accountService;

    public MessageNotificationAdapter(Context context, List<Message> messageItems, String accessToken) {
        mMessages = messageItems;
        mInflater = LayoutInflater.from(context);
        mAccessToken = accessToken;
        accountService = new AccountService(context);
    }

    @Override
    public @NotNull MessageNotificationAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View warningView = mInflater.inflate(R.layout.item_notification_message, parent, false);
        return new MessageNotificationAdapter.ViewHolder(warningView);
    }

    @Override
    public void onBindViewHolder(MessageNotificationAdapter.@NotNull ViewHolder holder, int position) {
        Message messageItem = mMessages.get(position);
        String walletNameS = "Portfel: " + messageItem.getWallet().getName();
        String numberOfMessageS = "Liczba nowych wiadomości: " + messageItem.getContent();

        holder.walletNameTv.setText(walletNameS);
        holder.numberOfMessageTv.setText(numberOfMessageS);

        holder.goToWalletBtn.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), WalletActivity.class);
            intent.putExtra("id", String.valueOf(messageItem.getWallet().getId()));
            holder.itemView.getContext().startActivity(intent);
            ((ProfileActivity) (holder.itemView.getContext())).finish();
        });

        holder.denyBtn.setOnClickListener(v -> {
            accountService.deleteMessageNotification(mAccessToken, messageItem.getId());
            mMessages.remove(position);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public void clear() {
        mMessages.clear();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView walletNameTv, numberOfMessageTv;
        public Button goToWalletBtn, denyBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            walletNameTv = itemView.findViewById(R.id.wallet_name_tv);
            numberOfMessageTv = itemView.findViewById(R.id.number_of_message_tv);
            goToWalletBtn = itemView.findViewById(R.id.go_to_wallet_btn);
            denyBtn = itemView.findViewById(R.id.delete_notification_btn);
        }
    }
}
