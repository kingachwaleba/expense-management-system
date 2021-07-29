package com.example.mobile.service;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.R;
import com.example.mobile.model.Wallet;

import java.util.List;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.ViewHolder> {

    private List<Wallet> mWallet;
    private  LayoutInflater mInflater;
    public WalletAdapter(Activity context, List<Wallet> wallets){
        mWallet = wallets;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View walletView = mInflater.inflate(R.layout.item_wallet, parent, false);
        ViewHolder viewHolder = new ViewHolder(walletView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WalletAdapter.ViewHolder holder, int position) {
        Wallet wallet = mWallet.get(position);
        holder.walletName.setText(wallet.getName());
        holder.ownerName.setText(mInflater.getContext().getResources().getString(R.string.owner) + " " + wallet.getOwner());
        holder.numberOfMembers.setText(mInflater.getContext().getResources().getString(R.string.number_of_members) + " " + String.valueOf(wallet.getNumeberOfMembers()));
     //   holder.balance.setText(wallet.getBalance().toString());

    }

    @Override
    public int getItemCount() {
        return mWallet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
         public TextView walletName, ownerName, numberOfMembers, balance;

        public ViewHolder(View itemView) {
            super(itemView);
            walletName = (TextView) itemView.findViewById(R.id.walletName_tv);
            ownerName = (TextView) itemView.findViewById(R.id.owner_tv);
            numberOfMembers = (TextView) itemView.findViewById(R.id.number_of_members_tv);
            balance = (TextView) itemView.findViewById(R.id.balance_tv);
        }
    }
}
