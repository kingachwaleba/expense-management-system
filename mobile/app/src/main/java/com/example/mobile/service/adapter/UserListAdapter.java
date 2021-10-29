package com.example.mobile.service.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.ImageHelper;
import com.example.mobile.R;
import com.example.mobile.model.Member;
import com.example.mobile.service.WalletService;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    public static List<String> selectedUser = new ArrayList<>();
    public static String mTAG;
    private final List<Member> mMembers;
    private final LayoutInflater mInflater;
    private final String mAccessToken;
    private final String mLogin;
    private final int mWalletId;
    private final Boolean mOwner;

    public UserListAdapter(Context context, List<Member> members, String TAG) {
        mMembers = members;
        mInflater = LayoutInflater.from(context);
        mTAG = TAG;
        mAccessToken = "";
        mWalletId = 0;
        mLogin = "";
        mOwner = false;
    }

    public UserListAdapter(Context context, List<Member> members, String accessToken, int walletId, Boolean owner, String login, String TAG) {
        mMembers = members;
        mInflater = LayoutInflater.from(context);
        mAccessToken = accessToken;
        mWalletId = walletId;
        mOwner = owner;
        mLogin = login;
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

        if (member.getImage() != null) {
            ImageHelper.downloadImage((picasso, urlBuilder) -> picasso.load(String.valueOf(urlBuilder)).into(holder.profileImageIv), holder.itemView.getContext(), mAccessToken, member.getImage());
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
                holder.userBtn.setOnClickListener(v -> new AlertDialog.Builder(holder.itemView.getContext(), android.R.style.Theme_Material_Dialog_Alert)
                        .setTitle("Edytowanie członków portfela")
                        .setMessage("Czy użytkownik " + member.getLogin() + " ma opuścić portfel?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                            WalletService walletService = new WalletService(holder.itemView.getContext());
                            if (mLogin.equals(member.getLogin()))
                                walletService.deleteCurrentMember(mAccessToken, mWalletId, 1);
                            else
                                walletService.deleteMember(mAccessToken, mWalletId, member.getLogin());
                            notifyDataSetChanged();
                        }).setNegativeButton(android.R.string.no, null).show());
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

    public void clear() {
        mMembers.clear();
    }

    public void clearSelected() {
        selectedUser.clear();
    }

    public List<String> getSelectedUser() {
        return selectedUser;
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
}
