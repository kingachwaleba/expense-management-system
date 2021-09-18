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
import com.example.mobile.activity.WalletActivity;
import com.example.mobile.model.WalletCreate;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.ViewHolder> {

    private final List<WalletCreate> mWalletItem;
    private final LayoutInflater mInflater;

    public WalletAdapter(Context context, List<WalletCreate> walletItems){
        mWalletItem = walletItems;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public @NotNull ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View walletView = mInflater.inflate(R.layout.item_wallet, parent, false);
        return new ViewHolder(walletView);
    }

    @Override
    public void onBindViewHolder(WalletAdapter.ViewHolder holder, int position) {
        WalletCreate walletItem = mWalletItem.get(position);
        String ownerS = mInflater.getContext().getResources().getString(R.string.owner_label) + " " + walletItem.getOwner();
        String numberOfMembersS = mInflater.getContext().getResources().getString(R.string.number_of_members_label) + " " + walletItem.getUserListCounter();
        String balance = mInflater.getContext().getResources().getString(R.string.all_balance_label) + " " + walletItem.getWalletExpensesCost();
        holder.nameTv.setText(walletItem.getName());
        holder.ownerTv.setText(ownerS);
        holder.numberOfMembersTv.setText(numberOfMembersS);
        holder.balanceTv.setText(balance);
        holder.id = walletItem.getId();

        holder.goToWalletBtn.setOnClickListener(v -> {
            Intent i = new Intent(holder.itemView.getContext(), WalletActivity.class);
            i.putExtra("id",String.valueOf(holder.id));
            holder.itemView.getContext().startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return mWalletItem.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

         public TextView nameTv, ownerTv, numberOfMembersTv, balanceTv;
         public int id;
         public Button goToWalletBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.name_tv);
            ownerTv = itemView.findViewById(R.id.owner_tv);
            numberOfMembersTv = itemView.findViewById(R.id.number_of_members_tv);
            balanceTv = itemView.findViewById(R.id.balance_tv);
            goToWalletBtn = itemView.findViewById(R.id.go_to_wallet_btn);
        }
    }
}
