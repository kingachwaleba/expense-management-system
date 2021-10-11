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

public class WarningAdapter extends RecyclerView.Adapter<WarningAdapter.ViewHolder> {
    private final List<Message> mWarnings;
    private final LayoutInflater mInflater;
    private final String mAccessToken;
    private final AccountService accountService;

    public WarningAdapter(Context context, List<Message> warningItems, String accessToken){
        mWarnings = warningItems;
        mInflater = LayoutInflater.from(context);
        mAccessToken = accessToken;
        accountService = new AccountService(context);
    }

    @Override
    public @NotNull WarningAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View warningView = mInflater.inflate(R.layout.item_notification_warning, parent, false);
        return new WarningAdapter.ViewHolder(warningView);
    }

    @Override
    public void onBindViewHolder(WarningAdapter.ViewHolder holder, int position) {
        Message warningItem = mWarnings.get(position);
        String debtS = mInflater.getContext().getResources().getString(R.string.debt) + " " + warningItem.getContent();

       /* if(warningItem.getType().equals("S")){
            holder.ownerNameTv.setVisibility(View.INVISIBLE);
            holder.contentTv.setText(mInflater.getContext().getResources().getString(R.string.new_debt_notification));
        } else {
            String ownerS = mInflater.getContext().getResources().getString(R.string.owner_label) + " " + warningItem.getSender().getLogin();
            holder.ownerNameTv.setText(ownerS);
        }*/

        if(warningItem.getSender()==null){
            holder.ownerNameTv.setVisibility(View.INVISIBLE);
            holder.contentTv.setText(mInflater.getContext().getResources().getString(R.string.new_debt_notification));
        } else {
            String ownerS = mInflater.getContext().getResources().getString(R.string.owner_label) + " " + warningItem.getSender().getLogin();
            holder.ownerNameTv.setText(ownerS);
        }
        holder.debtTv.setText(debtS);

        holder.acceptBtn.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), WalletActivity.class);
            intent.putExtra("id", String.valueOf(warningItem.getWallet().getId()));
            holder.itemView.getContext().startActivity(intent);
            ((ProfileActivity)(holder.itemView.getContext())).finish();
        });

        holder.denyBtn.setOnClickListener(v -> {
            accountService.deleteNotification(mAccessToken, warningItem.getId());
            mWarnings.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return mWarnings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView ownerNameTv, debtTv, contentTv;
        public Button acceptBtn, denyBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            ownerNameTv = itemView.findViewById(R.id.owner_tv);
            debtTv = itemView.findViewById(R.id.debt_tv);
            acceptBtn = itemView.findViewById(R.id.accept_btn);
            denyBtn = itemView.findViewById(R.id.delete_notification_btn);
            contentTv = itemView.findViewById(R.id.warning_info_tv);
        }
    }

    public void clear(){
        mWarnings.clear();
    }
}
