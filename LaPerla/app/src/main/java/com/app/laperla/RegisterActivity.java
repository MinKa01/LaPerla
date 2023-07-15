package com.app.laperla;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class RegisterActivity extends AppCompatActivity {

    RegisterFirstFragment firstFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstFragment = new RegisterFirstFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, firstFragment).commit();

    }
}