package com.store.lamore.dashboard.home;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.store.lamore.R;
import com.store.lamore.adapter.LastPurchaseAdapter;
import com.store.lamore.data.Product;
import com.store.lamore.data.UserData;
import com.store.lamore.handler.databaseHandler;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View v;

    private UserData ud = new UserData();
    private Bitmap bitmap;
    private AlertDialog alertDialog;
    private TextView name, point_togo, point;
    private ProgressBar pb;
    private ArrayList<String> imageList;
    private ImageSlider imageSlider;
    private LastPurchaseAdapter lpa;
    private RecyclerView rv;
    private ArrayList<SlideModel> slideModels = new ArrayList<>();

    public Home() {
        getUserData();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
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
        v = inflater.inflate(R.layout.fragment_home, container, false);
        imageSlider = v.findViewById(R.id.ad_slider);
        name = v.findViewById(R.id.name);
        point_togo = v.findViewById(R.id.tv_point_togo);
        point = v.findViewById(R.id.tv_point);
        pb = v.findViewById(R.id.vip_bar);
        rv = v.findViewById(R.id.recycler_last_purchase);

        getAds();
        getLastPurchase();

        Button barcode = v.findViewById(R.id.btn_qr);
        barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barcode.setEnabled(false);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                View view = getLayoutInflater().inflate(R.layout.barcode_dialog, null);
                Button done = view.findViewById(R.id.done_btn);
                TextView membercode = view.findViewById(R.id.member_code);
                ImageView iv_qr = (ImageView) view.findViewById(R.id.iv_qr);

                membercode.setText(ud.getMemberID());
                if(bitmap == null){
                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                    try{
                        BitMatrix bitMatrix = multiFormatWriter.encode(ud.getMemberID(), BarcodeFormat.QR_CODE, iv_qr.getMaxWidth(), iv_qr.getMaxHeight());
                        bitmap = Bitmap.createBitmap(iv_qr.getMaxWidth(), iv_qr.getMaxHeight(), Bitmap.Config.RGB_565);
                        for(int i = 0; i < iv_qr.getMaxWidth(); i++){
                            for (int j = 0; j < iv_qr.getMaxHeight(); j++){
                                bitmap.setPixel(i, j, (bitMatrix.get(i, j) ? getResources().getColor(R.color.text_d_2) : getResources().getColor(R.color.background)));
                            }
                        }
                        iv_qr.setImageBitmap(bitmap);
                    }
                    catch (Exception e){
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    iv_qr.setImageBitmap(bitmap);
                }

                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        barcode.setEnabled(true);
                        alertDialog.dismiss();
                    }
                });


                builder.setView(view);
                alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
            }
        });

        return v;
    }

    private void getUserData(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(auth.getCurrentUser().getUid().toString()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ud.setName(documentSnapshot.getString("Name"));
                ud.setMemberID(documentSnapshot.getString("MemberID"));
                ud.setPoint(documentSnapshot.getLong("Point"));

                name.setText(ud.getName());
                point.setText(ud.getPoint()+"");
                long togo = ((2000 - ud.getPoint()) > 0 ? (2000 - ud.getPoint()) : 0);
                point_togo.setText(togo+"");
                int x = Math.round(ud.getPoint() * 100 / 2000);
                pb.setProgress(x);
            }
        });
    }

    private void getAds(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("ads").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot qds: queryDocumentSnapshots){
                    String link = qds.getString("Link");
                    slideModels.add(new SlideModel(link, ScaleTypes.CENTER_INSIDE));
                }
                imageSlider.setImageList(slideModels);
            }
        });
    }

    private void getLastPurchase(){
        databaseHandler dh = new databaseHandler();
        ArrayList<Product> p = dh.getLastPurchase();
        lpa = new LastPurchaseAdapter(getContext(), p);
        rv.setAdapter(lpa);
        rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        runRecyclerAnimation();
    }

    private void runRecyclerAnimation(){
        Context ctx = rv.getContext();
        LayoutAnimationController lac = AnimationUtils.loadLayoutAnimation(ctx, R.anim.layout_item_horizontal_animation);
        rv.setLayoutAnimation(lac);
        rv.scheduleLayoutAnimation();
    }
}