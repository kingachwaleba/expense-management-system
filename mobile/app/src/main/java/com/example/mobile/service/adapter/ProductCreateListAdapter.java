package com.example.mobile.service.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.R;
import com.example.mobile.SetActivityField;
import com.example.mobile.model.Product;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ProductCreateListAdapter extends RecyclerView.Adapter<ProductCreateListAdapter.ViewHolder> {

    private final List<Product> mProducts;
    private final LayoutInflater mInflater;
    private final SetActivityField editInterface;

    public ProductCreateListAdapter(Context context, List<Product> productsItems, SetActivityField setActivityField) {
        mProducts = productsItems;
        mInflater = LayoutInflater.from(context);
        editInterface = setActivityField;
    }

    @Override
    public @NotNull ProductCreateListAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View invitationView = mInflater.inflate(R.layout.item_list_create, parent, false);
        return new ProductCreateListAdapter.ViewHolder(invitationView);
    }

    @Override
    public void onBindViewHolder(ProductCreateListAdapter.ViewHolder holder, int position) {
        Product productItem = mProducts.get(position);

        holder.productNameTv.setText(productItem.getName());
        holder.quantityTv.setText(String.valueOf(productItem.getQuantity()));
        holder.unitTv.setText(productItem.getUnit().getName());

        holder.editProductBtn.setOnClickListener(v -> {
            mProducts.remove(position);
            editInterface.editProduct(productItem.getName(), String.valueOf(productItem.getQuantity()), productItem.getUnit(), productItem.getId());
            notifyDataSetChanged();
        });

        holder.deleteProductBtn.setOnClickListener(v -> {
            mProducts.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public void clear() {
        mProducts.clear();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView productNameTv, quantityTv, unitTv;
        public Button deleteProductBtn, editProductBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            productNameTv = itemView.findViewById(R.id.name_list_item_tv);
            quantityTv = itemView.findViewById(R.id.quantity_list_item_tv);
            unitTv = itemView.findViewById(R.id.unit_list_item_tv);
            deleteProductBtn = itemView.findViewById(R.id.delete_list_item_btn);
            editProductBtn = itemView.findViewById(R.id.edit_list_item_btn);
        }
    }
}
