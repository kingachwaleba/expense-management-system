package com.example.mobile.service.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.R;
import com.example.mobile.model.Member;
import com.example.mobile.service.WalletService;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class EditMemberAdapter extends RecyclerView.Adapter<EditMemberAdapter.ViewHolder> {
    private final List<Member> mMembers;
    private final LayoutInflater mInflater;
    private final String mAccessToken;
    private final int mWalletId;
    private final Boolean mOwner;

    public EditMemberAdapter(Context context, List<Member> members, String accessToken, int walletId, Boolean owner) {
        mMembers = members;
        mInflater = LayoutInflater.from(context);
        mAccessToken = accessToken;
        mWalletId = walletId;
        mOwner = owner;
    }

    @Override
    public @NotNull EditMemberAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_edit_user_wallet, parent, false);
        return new EditMemberAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EditMemberAdapter.ViewHolder holder, int position) {
        Member member = mMembers.get(position);

        if(!mOwner) holder.deleteMemberBtn.setVisibility(View.GONE);

        holder.memberNameTv.setText(member.getLogin());
        holder.deleteMemberBtn.setOnClickListener(v -> {
            WalletService walletService = new WalletService(holder.itemView.getContext());
            walletService.deleteMember(mAccessToken, mWalletId,member.getLogin());
           // mMembers.remove(member);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return mMembers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView memberNameTv;
        Button deleteMemberBtn;
        public ViewHolder(View itemView) {
            super(itemView);
            memberNameTv = itemView.findViewById(R.id.member_name_tv);
            deleteMemberBtn = itemView.findViewById(R.id.delete_user_btn);
        }
    }
}