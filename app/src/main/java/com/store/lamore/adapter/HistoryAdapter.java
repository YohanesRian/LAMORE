package com.store.lamore.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.store.lamore.R;
import com.store.lamore.data.History;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.Holder>{

    private Context ctx;
    private ArrayList<History> alh;

    public HistoryAdapter(Context ctx, ArrayList<History> alh){
        this.ctx = ctx;
        this.alh = alh;
    }

    @NonNull
    @Override
    public HistoryAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.activity_history_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.Holder holder, int position) {
        holder.product_img.setImageDrawable(ctx.getResources().getDrawable(alh.get(position).getImg_ref()));
        holder.product_size.setText(alh.get(position).getSize());
        holder.product_name.setText(alh.get(position).getProductName());
        holder.product_price.setText(alh.get(position).getPrice());
        holder.outlet_name.setText(alh.get(position).getOutletName());
    }

    @Override
    public int getItemCount() {
        return alh.size();
    }

    public static class Holder extends RecyclerView.ViewHolder{

        ImageView product_img;
        TextView product_name, product_price, outlet_name, product_size;

        public Holder(@NonNull View itemView) {
            super(itemView);
            product_img = itemView.findViewById(R.id.product_image);
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            product_size = itemView.findViewById(R.id.size);
            outlet_name = itemView.findViewById(R.id.outlet_name);

        }
    }
}
