package com.example.mobile.service.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.R;
import com.example.mobile.model.Member;
import com.example.mobile.service.WalletService;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private final List<Member> mMembers;
    private final LayoutInflater mInflater;
    public static List<String> selectedUser = new ArrayList<>();
    public static String mTAG;
    private final String mAccessToken;
    private final int mWalletId;
    private final Boolean mOwner;

    public UserListAdapter(Context context, List<Member> members, String TAG){
        mMembers= members;
        mInflater = LayoutInflater.from(context);
        mTAG = TAG;
        mAccessToken = "";
        mWalletId = 0;
        mOwner = false;
    }

    public UserListAdapter(Context context, List<Member> members, String accessToken, int walletId, Boolean owner, String TAG) {
        mMembers = members;
        mInflater = LayoutInflater.from(context);
        mAccessToken = accessToken;
        mWalletId = walletId;
        mOwner = owner;
        mTAG = TAG;
    }

    @Override
    public @NotNull ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View memberView = mInflater.inflate(R.layout.item_user_search, parent, false);
        return new ViewHolder(memberView);
    }

    @Override
    public void onBindViewHolder(UserListAdapter.ViewHolder holder, int position) {
        Member member = mMembers.get(position);
        holder.memberNameTv.setText(member.getLogin());

        if(member.getImage()!=null){
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .authenticator((route, response) -> response.request().newBuilder()
                            .header("Authorization", "Bearer " + mAccessToken)
                            .build()).build();

            HttpUrl.Builder urlBuilder
                    = HttpUrl.parse("http://192.168.0.31:8080/files").newBuilder();
            urlBuilder.addQueryParameter("imageName", member.getImage());

            Picasso picasso = new Picasso.Builder(holder.itemView.getContext())
                    .downloader(new OkHttp3Downloader(okHttpClient))
                    .build();
            picasso.load(String.valueOf(urlBuilder)).into(holder.profileImageIv);
        }

        switch (mTAG) {
            case "USER_BROWSER":
                holder.userBtn.setBackgroundResource(R.drawable.btn_add_person);
                if (selectedUser.contains(member.getLogin())) {
                    holder.isChecked = true;
                    holder.userBtn.setBackgroundResource(R.drawable.btn_added_person);
                } else {
                    holder.isChecked = false;
                    holder.userBtn.setBackgroundResource(R.drawable.btn_add_person);
                }

                holder.userBtn.setOnClickListener(v -> {
                    if (holder.isChecked) {
                        v.setBackgroundResource(R.drawable.btn_add_person);
                        selectedUser.remove(member.getLogin());
                    } else {
                        v.setBackgroundResource(R.drawable.btn_added_person);
                        selectedUser.add(member.getLogin());
                    }
                    holder.isChecked = !holder.isChecked;
                });
                break;
            case "USER_EDIT":
                holder.userBtn.setBackgroundResource(R.drawable.btn_delete_2);
                if (!mOwner) holder.userBtn.setVisibility(View.GONE);

                holder.memberNameTv.setText(member.getLogin());
                holder.userBtn.setOnClickListener(v -> {
                    WalletService walletService = new WalletService(holder.itemView.getContext());
                    walletService.deleteMember(mAccessToken, mWalletId, member.getLogin());
                    notifyDataSetChanged();
                });
                break;
            case "USER_EXPENSE":
                holder.userBtn.setVisibility(View.GONE);
                holder.itemView.setBackgroundResource(R.drawable.bg_round_box_light);
                String text = member.getLogin() + " " + member.getBalance() + "zł";
                holder.memberNameTv.setText(text);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mMembers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView profileImageIv;
        public TextView memberNameTv;
        public Button userBtn;
        public boolean isChecked;

        public ViewHolder(View itemView) {
            super(itemView);
            isChecked = false;
            memberNameTv = itemView.findViewById(R.id.member_name_tv);
            userBtn = itemView.findViewById(R.id.user_btn);
            profileImageIv = itemView.findViewById(R.id.profile_image_iv);
        }
    }

    public void clear(){
        mMembers.clear();
    }

    public void clearSelected(){
        selectedUser.clear();
    }

    public List<String> getSelectedUser(){
        return selectedUser;
    }
}
