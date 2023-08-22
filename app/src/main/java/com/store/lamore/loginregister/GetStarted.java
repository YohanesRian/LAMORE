package com.store.lamore.loginregister;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.store.lamore.R;

public class GetStarted extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        Login login = new Login();
        getSupportFragmentManager().beginTransaction().replace(R.id.container_form, login).commit();
    }
}