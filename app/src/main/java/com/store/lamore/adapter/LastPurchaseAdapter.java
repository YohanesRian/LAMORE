package com.store.lamore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.store.lamore.R;
import com.store.lamore.data.Product;

import java.util.ArrayList;

public class LastPurchaseAdapter extends RecyclerView.Adapter<LastPurchaseAdapter.Holder>{

    private Context ctx;
    private ArrayList<Product> last_purchase;

    public LastPurchaseAdapter(Context ctx, ArrayList<Product> last_purchase){
        this.ctx = ctx;
        this.last_purchase = last_purchase;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.activity_last_purchase_items, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LastPurchaseAdapter.Holder holder, int position) {
        holder.product_img.setImageDrawable(ctx.getResources().getDrawable(last_purchase.get(position).getImg_ref()));
        holder.product_size.setText(last_purchase.get(position).getSize());
        holder.product_name.setText(last_purchase.get(position).getProductName());
        holder.product_price.setText(last_purchase.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return last_purchase.size();
    }

    public static class Holder extends RecyclerView.ViewHolder{

        ImageView product_img;
        TextView product_name, product_price, product_size;

        public Holder(@NonNull View itemView) {
            super(itemView);

            product_img = itemView.findViewById(R.id.product_image);
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            product_size = itemView.findViewById(R.id.size);
        }
    }
}
