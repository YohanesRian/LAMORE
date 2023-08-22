package com.store.lamore.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.store.lamore.R;
import com.store.lamore.data.History;
import com.store.lamore.handler.dateHandler;

import java.util.ArrayList;

public class HistoryPerMonthAdapter extends RecyclerView.Adapter<HistoryPerMonthAdapter.Holder>{
    private Context ctx;
    private ArrayList<ArrayList<History>> alalh;
    private Activity activity;

    public HistoryPerMonthAdapter(Activity activity, Context ctx, ArrayList<ArrayList<History>> alalh){
        this.ctx = ctx;
        this.alalh = alalh;
        this.activity = activity;
    }
    @NonNull
    @Override
    public HistoryPerMonthAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.activity_history_item_per_month, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryPerMonthAdapter.Holder holder, int position) {
        dateHandler dh = new dateHandler();
        dh.setDate(alalh.get(position).get(0).getDate());
        holder.month_year.setText(dh.getM_Y());

        HistoryAdapter ha = new HistoryAdapter(ctx, alalh.get(position));
        LinearLayoutManager llm = new LinearLayoutManager(activity);
        holder.rv.setAdapter(ha);
        holder.rv.setLayoutManager(llm);

        Context ctx = holder.rv.getContext();
        LayoutAnimationController lac = AnimationUtils.loadLayoutAnimation(ctx, R.anim.layout_item_vertical_animation);
        holder.rv.setLayoutAnimation(lac);
        holder.rv.scheduleLayoutAnimation();
    }

    @Override
    public int getItemCount() {
        return alalh.size();
    }

    public static class Holder extends RecyclerView.ViewHolder{
        TextView month_year;
        RecyclerView rv;
        public Holder(@NonNull View itemView) {
            super(itemView);
            month_year = itemView.findViewById(R.id.purchase_month);
            rv = itemView.findViewById(R.id.recycler_history);
        }
    }
}
