package com.example.mobile.service.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.ImageHelper;
import com.example.mobile.R;
import com.example.mobile.model.Message;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<Message> mMessage;
    private final LayoutInflater mInflater;
    private String mUserLogin, mAccessToken;

    public ChatAdapter(Context context, List<Message> messages, String userLogin, String accessToken) {
        mMessage = messages;
        mInflater = LayoutInflater.from(context);
        mUserLogin = userLogin;
        mAccessToken = accessToken;
    }

    @Override
    public @NotNull RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View messageView;
        if (viewType == 0) {
            messageView = mInflater.inflate(R.layout.other_user_message, parent, false);
            return new ChatAdapter.ViewHolder0(messageView);
        } else if (viewType == 1) {
            messageView = mInflater.inflate(R.layout.current_user_message, parent, false);
            return new ChatAdapter.ViewHolder1(messageView);
        } else return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
       Message message = mMessage.get(position);
        switch (holder.getItemViewType()){
            case 0:
                ViewHolder0 viewHolder0 = (ViewHolder0) holder;
                viewHolder0.contentTv.setText(message.getContent());
                viewHolder0.loginTv.setText(message.getSender().getLogin());
                viewHolder0.dateTv.setText(message.getDate().replace("T", " "));
               /* if (message.getSender().getImage() != null) {
                    ImageHelper.downloadImage((picasso, urlBuilder) -> picasso.load(String.valueOf(urlBuilder)).into((viewHolder0).profilePhoto), holder.itemView.getContext(), mAccessToken, message.getSender().getImage());
                }*/
                break;
            case 1:
                ViewHolder1 viewHolder1 = (ViewHolder1) holder;
                viewHolder1.contentTv.setText(message.getContent());
                viewHolder1.dateTv.setText(message.getDate().replace("T", " "));
                /* if (message.getSender().getImage() != null) {
                    ImageHelper.downloadImage((picasso, urlBuilder) -> picasso.load(String.valueOf(urlBuilder)).into((viewHolder1).profilePhoto), holder.itemView.getContext(), mAccessToken, message.getSender().getImage());
                }*/
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mMessage.get(position).getSender().getLogin().equals(mUserLogin))
            return 1;
        else return 0;
    }

    @Override
    public int getItemCount() {
        return mMessage.size();
    }

    public class ViewHolder0 extends RecyclerView.ViewHolder {
        TextView dateTv, loginTv, contentTv;
        ImageView profilePhoto;
        public ViewHolder0(View itemView) {
            super(itemView);
            profilePhoto = itemView.findViewById(R.id.profile_user_image);
            dateTv = itemView.findViewById(R.id.txtDate);
            loginTv = itemView.findViewById(R.id.txtLogin);
            contentTv = itemView.findViewById(R.id.txtMessage);
        }
    }

    public class ViewHolder1 extends RecyclerView.ViewHolder {
        TextView dateTv, loginTv, contentTv;
        ImageView profilePhoto;
        public ViewHolder1(View itemView) {
            super(itemView);
            profilePhoto = itemView.findViewById(R.id.current_profile_user_image);
            dateTv = itemView.findViewById(R.id.current_txtDate);
            contentTv = itemView.findViewById(R.id.current_txtMessage);
        }
    }

}
