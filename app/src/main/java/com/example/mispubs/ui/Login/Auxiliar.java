package com.example.mispubs.ui.Login;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

public class Auxiliar extends FragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, new FragmentRegistro()).commit();}
    }
}