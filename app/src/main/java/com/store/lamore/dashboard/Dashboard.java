package com.store.lamore.dashboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.store.lamore.R;
import com.store.lamore.dashboard.history.History;
import com.store.lamore.dashboard.home.Home;
import com.store.lamore.dashboard.location.Location;
import com.store.lamore.loginregister.GetStarted;


import nl.joery.animatedbottombar.AnimatedBottomBar;

public class Dashboard extends AppCompatActivity {

    private AlertDialog alertDialog;
    private AnimatedBottomBar bb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        bb = findViewById(R.id.Bottom_Navbar);
        bb.selectTabAt(0, true);
        Home h = new Home();
        getSupportFragmentManager().beginTransaction().replace(R.id.dashboard_container, h).commit();

        bb.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int i, @Nullable AnimatedBottomBar.Tab tab, int i1, @NonNull AnimatedBottomBar.Tab tab1) {
                switch(i1){
                    case 0:
                        Home home = new Home();
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right, R.anim.enter_right_to_left, R.anim.exit_right_to_left).replace(R.id.dashboard_container, home).commit();
                        getSupportFragmentManager().popBackStack();
                        break;
                    case 1:
                        History history = new History();
                        if(i > i1){
                            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right, R.anim.enter_right_to_left, R.anim.exit_right_to_left).replace(R.id.dashboard_container, history).commit();
                        }
                        else{
                            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right).replace(R.id.dashboard_container, history).commit();
                        }
                        getSupportFragmentManager().popBackStack();
                        break;
                    case 2:
                        Location location = new Location();
                        if(i > i1){
                            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right, R.anim.enter_right_to_left, R.anim.exit_right_to_left).replace(R.id.dashboard_container, location).commit();
                        }
                        else{
                            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right).replace(R.id.dashboard_container, location).commit();
                        }
                        getSupportFragmentManager().popBackStack();
                        break;
                    case 3:
                        logout(i);
                        break;
                }
            }

            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {

            }
        });
    }
    private void logout(int i){
        AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);

        View view = getLayoutInflater().inflate(R.layout.logout_dialog, null);
        Button yes = view.findViewById(R.id.yes_btn);
        Button no = view.findViewById(R.id.no_btn);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Dashboard.this, GetStarted.class);
                startActivity(intent);
                finish();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                bb.selectTabAt(i, true);
            }
        });


        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
}