package com.example.mobile.service.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.R;
import com.example.mobile.activity.OneListActivity;
import com.example.mobile.model.ListShop;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class ListsAdapter extends RecyclerView.Adapter<ListsAdapter.ViewHolder> {
    private final List<ListShop> mListShops;
    private final LayoutInflater mInflater;
    private final String mAccessToken;

    public ListsAdapter(Context context, List<ListShop> listShops, String accessToken){
        mListShops = listShops;
        mInflater = LayoutInflater.from(context);
        mAccessToken = accessToken;
    }

    @Override
    public @NotNull ListsAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View invitationView = mInflater.inflate(R.layout.item_one_list, parent, false);
        return new ListsAdapter.ViewHolder(invitationView);
    }

    @Override
    public void onBindViewHolder(ListsAdapter.ViewHolder holder, int position) {
        ListShop listShop = mListShops.get(position);
        holder.listNameTv.setText(listShop.getName());
        holder.walletId = listShop.getWalletCreate().getId();
        holder.listId = listShop.getId();
        holder.numberOfElementsTv.setText(mInflater.getContext().getResources().getString(R.string.number_of_elements_label) + " " + listShop.getListDetailSet().size());

        holder.goToListBtn.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), OneListActivity.class);
            intent.putExtra("listId", holder.listId);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mListShops.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView listNameTv, numberOfElementsTv, goToListBtn;
        public int walletId, listId, userId;

        public ViewHolder(View itemView) {
            super(itemView);
            listNameTv = itemView.findViewById(R.id.name_list_tv);
            goToListBtn = itemView.findViewById(R.id.go_to_list_tv);
            numberOfElementsTv = itemView.findViewById(R.id.number_of_elements_tv);
        }
    }

    public void clear(){
        mListShops.clear();
    }
}
