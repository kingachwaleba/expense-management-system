package com.example.mobile.service.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.R;
import com.example.mobile.activity.OneListActivity;
import com.example.mobile.model.ListShop;
import com.example.mobile.model.Product;
import com.example.mobile.model.User;

import org.jetbrains.annotations.NotNull;
import java.util.List;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ViewHolder> {
    private final List<Product> mProduct;
    private final LayoutInflater mInflater;
    private final String mAccessToken;
    private final String mLogin;

    public ListItemAdapter(Context context, List<Product> product, String accessToken, String login){
        mProduct = product;
        mInflater = LayoutInflater.from(context);
        mAccessToken = accessToken;
        mLogin = login;
    }

    @Override
    public @NotNull ListItemAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View invitationView = mInflater.inflate(R.layout.item_list, parent, false);
        return new ListItemAdapter.ViewHolder(invitationView);
    }

    @Override
    public void onBindViewHolder(ListItemAdapter.ViewHolder holder, int position) {
        Product product = mProduct.get(position);

        holder.itemId = product.getId();
        if(product.getUser()!=null){
            holder.cbUserStatus = 2;
            holder.user = product.getUser();
        } else holder.cbUserStatus = 3;

        holder.cbItemStatus = product.getStatus().getId();

        holder.itemNameTv.setText(product.getName());
        holder.itemQuantityTv.setText(String.valueOf(product.getQuantity()));
        holder.itemUnitTv.setText(product.getUnit().getName());

        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.editItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OneListActivity.setNameQuantityProductEt(product.getName(), String.valueOf(product.getQuantity()), product.getUnit(), holder.itemId);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mProduct.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView itemNameTv, itemQuantityTv, itemUnitTv;
        public int itemId, cbUserStatus, cbItemStatus;
        public User user;
        public CheckBox personCb, itemCb;
        public Button deleteItem, editItem;

        public ViewHolder(View itemView) {
            super(itemView);
            itemNameTv = itemView.findViewById(R.id.name_item_tv);
            itemQuantityTv = itemView.findViewById(R.id.quantity_item_tv);
            itemUnitTv = itemView.findViewById(R.id.unit_item_tv);
            personCb = itemView.findViewById(R.id.take_element_cb);
            itemCb = itemView.findViewById(R.id.taken_element_cb);
            deleteItem = itemView.findViewById(R.id.delete_item_btn);
            editItem = itemView.findViewById(R.id.edit_item_btn);
        }
    }
}
