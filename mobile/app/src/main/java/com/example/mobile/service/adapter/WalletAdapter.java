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
import com.example.mobile.model.WalletItem;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.ViewHolder> {

    private final List<WalletItem> mWalletItem;
    private final LayoutInflater mInflater;

    public WalletAdapter(Context context, List<WalletItem> walletItems){
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
        WalletItem walletItem = mWalletItem.get(position);
        holder.walletName.setText(walletItem.getName());
        holder.ownerName.setText(mInflater.getContext().getResources().getString(R.string.owner) + " " + walletItem.getOwner());
        holder.userListCounter.setText(mInflater.getContext().getResources().getString(R.string.number_of_members) + " " + walletItem.getUserListCounter());
      //holder.balance.setText(mInflater.getContext().getResources().getString(R.string.balance) + " " + Double.toString(walletItem.getBalance()));
        holder.id = walletItem.getWalletId();

        holder.button.setOnClickListener(v -> {
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

         public TextView walletName, ownerName, userListCounter, balance;
         public int id;
         public Button button;

        public ViewHolder(View itemView) {
            super(itemView);
            walletName = itemView.findViewById(R.id.walletName_tv);
            ownerName = itemView.findViewById(R.id.owner_tv);
            userListCounter = itemView.findViewById(R.id.number_of_members_tv);
            balance = itemView.findViewById(R.id.balance_tv);
            button = itemView.findViewById(R.id.goToWallet_btn);
        }
    }
}
