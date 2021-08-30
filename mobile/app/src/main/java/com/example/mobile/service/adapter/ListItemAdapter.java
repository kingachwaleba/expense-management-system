package com.example.mobile.service.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.R;
import com.example.mobile.SetActivityField;
import com.example.mobile.model.Product;
import com.example.mobile.model.Status;
import com.example.mobile.model.User;
import com.example.mobile.service.ListService;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ViewHolder> {
    private final List<Product> mProduct;
    private final LayoutInflater mInflater;
    private final String mAccessToken;
    private final String mLogin;
    private final ListService listService;
    private final SetActivityField editInterface;

    public ListItemAdapter(Context context, List<Product> product, String accessToken, String login, SetActivityField setActivityField){
        mProduct = product;
        mInflater = LayoutInflater.from(context);
        mAccessToken = accessToken;
        mLogin = login;
        listService = new ListService(context);
        editInterface = setActivityField;
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
            holder.personCb.setChecked(true);
            holder.personCb.setEnabled(product.getUser().getLogin().equals(mLogin));
            holder.whoTakeItem.setVisibility(View.VISIBLE);
        } else {
            holder.personCb.setEnabled(true);
            holder.personCb.setChecked(false);
            holder.whoTakeItem.setVisibility(View.INVISIBLE);
        }

        if(product.getStatus().getId()==1){
            holder.itemCb.setChecked(true);
            holder.personCb.setEnabled(false);
            holder.deleteItem.setEnabled(false);
            holder.deleteItem.setBackgroundResource(R.drawable.btn_delete_not_active);
            holder.editItem.setEnabled(false);
            //zmiana background dla editItem
        } else {
            holder.itemCb.setChecked(false);
            holder.deleteItem.setEnabled(true);
            holder.deleteItem.setBackgroundResource(R.drawable.btn_delete_active);
            holder.editItem.setEnabled(true);
        }

        holder.itemNameTv.setText(product.getName());
        holder.itemQuantityTv.setText(String.valueOf(product.getQuantity()));
        holder.itemUnitTv.setText(product.getUnit().getName());

        holder.whoTakeItem.setOnClickListener(v -> Toast.makeText(holder.itemView.getContext(), "To kupi " + product.getUser().getLogin(), Toast.LENGTH_SHORT).show());


        holder.personCb.setOnClickListener(v -> {
            if(product.getUser()==null){
                product.setUser(new User(mLogin));
                listService.changeListElementStatus(mAccessToken, holder.itemId, 2);
            } else if(product.getUser().getLogin().equals(mLogin)){
                product.setUser(null);
                listService.changeListElementStatus(mAccessToken, holder.itemId, 3);
            }
            notifyDataSetChanged();
        });

        holder.itemCb.setOnClickListener(v -> {
            if(product.getStatus().getId() == 3) {
                product.setUser(new User(mLogin));
                product.setStatus(new Status(1, "zrealizowany"));
                listService.changeListElementStatus(mAccessToken, holder.itemId, 1);
            }
            else if(product.getStatus().getId() == 2){
                product.setUser(new User(mLogin));
                product.setStatus(new Status(1, "zrealizowany"));
                listService.changeListElementStatus(mAccessToken, holder.itemId, 1);

            } else {
                product.setUser(null);
                product.setStatus(new Status(3, "oczekujący"));
                listService.changeListElementStatus(mAccessToken, holder.itemId, 3);
            }
            notifyDataSetChanged();
        });

        holder.deleteItem.setOnClickListener(v -> {
            mProduct.remove(product);
            listService.deleteListElement(mAccessToken, holder.itemId);
            notifyDataSetChanged();
        });

        holder.editItem.setOnClickListener(v -> editInterface.editProduct(product.getName(), String.valueOf(product.getQuantity()), product.getUnit(), holder.itemId));

    }

    @Override
    public int getItemCount() {
        return mProduct.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView itemNameTv, itemQuantityTv, itemUnitTv;
        public int itemId;
        public CheckBox personCb, itemCb;
        public Button deleteItem, editItem, whoTakeItem;

        public ViewHolder(View itemView) {
            super(itemView);
            itemNameTv = itemView.findViewById(R.id.name_item_tv);
            itemQuantityTv = itemView.findViewById(R.id.quantity_item_tv);
            itemUnitTv = itemView.findViewById(R.id.unit_item_tv);
            personCb = itemView.findViewById(R.id.take_element_cb);
            itemCb = itemView.findViewById(R.id.taken_element_cb);
            deleteItem = itemView.findViewById(R.id.delete_item_btn);
            editItem = itemView.findViewById(R.id.edit_item_btn);
            whoTakeItem = itemView.findViewById(R.id.who_take_item_btn);
        }
    }
}
