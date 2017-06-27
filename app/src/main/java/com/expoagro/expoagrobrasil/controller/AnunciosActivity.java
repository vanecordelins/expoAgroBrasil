package com.expoagro.expoagrobrasil.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.expoagro.expoagrobrasil.R;
import com.expoagro.expoagrobrasil.util.GoogleApiClientHelper;
import com.expoagro.expoagrobrasil.util.GoogleSignIn;
import com.google.android.gms.common.api.GoogleApiClient;

public class AnunciosActivity extends AppCompatActivity {

    private Button btn_sair;
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncios);

        btn_sair = (Button) findViewById(R.id.btn_sair);
        client = GoogleApiClientHelper.getInstance().getGoogleApiClient();

        btn_sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignIn.signOut(AnunciosActivity.this, GoogleApiClientHelper.getInstance().getGoogleApiClient());
            }
        });
    }
}
