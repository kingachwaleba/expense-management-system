package com.example.mobile.service.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.R;
import com.example.mobile.model.Message;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private final List<Message> mMessage;
    private final LayoutInflater mInflater;
    private final int OTHER_USER_MESSAGE = 0;
    private final int CURRENT_USER_MESSAGE = 1;

    public ChatAdapter(Context context, List<Message> messages) {
        mMessage = messages;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public @NotNull ChatAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View messageView;
        if (viewType == OTHER_USER_MESSAGE) {
            messageView = mInflater.inflate(R.layout.other_user_message, parent, false);
            return new ChatAdapter.ViewHolder(messageView);
        } else if (viewType == CURRENT_USER_MESSAGE) {
            messageView = mInflater.inflate(R.layout.current_user_message, parent, false);
            return new ChatAdapter.ViewHolder(messageView);
        } else return null;
    }

    @Override
    public void onBindViewHolder(ChatAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mMessage.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView dateTv, loginTv, contentTv;

        public ViewHolder(View itemView) {
            super(itemView);
            dateTv = itemView.findViewById(R.id.txtDate);
            loginTv = itemView.findViewById(R.id.txtLogin);
            contentTv = itemView.findViewById(R.id.txtMessage);
        }
    }

}
