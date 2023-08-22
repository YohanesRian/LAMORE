package com.store.lamore.dashboard.history;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.store.lamore.R;
import com.store.lamore.adapter.HistoryPerMonthAdapter;
import com.store.lamore.handler.databaseHandler;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link History#newInstance} factory method to
 * create an instance of this fragment.
 */
public class History extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View v;
    private RecyclerView rv;
    private HistoryPerMonthAdapter historyPerMonthAdapter;

    public History() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment History.
     */
    // TODO: Rename and change types and number of parameters
    public static History newInstance(String param1, String param2) {
        History fragment = new History();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_history, container, false);
        rv = v.findViewById(R.id.recycler_history_permonth);
        getHistory();
        return v;
    }

    private void getHistory(){
        databaseHandler dh = new databaseHandler();
        ArrayList<ArrayList<com.store.lamore.data.History>> alalh = dh.getHistory();
        historyPerMonthAdapter = new HistoryPerMonthAdapter(getActivity(), getContext(), alalh);
        rv.setAdapter(historyPerMonthAdapter);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        runRecyclerAnimation();
    }

    private void runRecyclerAnimation(){
        Context ctx = rv.getContext();
        LayoutAnimationController lac = AnimationUtils.loadLayoutAnimation(ctx, R.anim.layout_item_vertical_animation);
        rv.setLayoutAnimation(lac);
        rv.scheduleLayoutAnimation();
    }
}