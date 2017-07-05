package com.expoagro.expoagrobrasil.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.expoagro.expoagrobrasil.R;
import com.expoagro.expoagrobrasil.util.GoogleSignIn;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class AnunciosActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private Button btn_sair;
    private Button btn_alterar;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncios);

        btn_sair = (Button) findViewById(R.id.btn_sair);
        btn_alterar = (Button) findViewById(R.id.alterar);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(AnunciosActivity.this)
                .enableAutoManage(AnunciosActivity.this /* FragmentActivity */, AnunciosActivity.this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        btn_sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignIn.signOut(AnunciosActivity.this, mGoogleApiClient);
            }
        });

        btn_alterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(AnunciosActivity.this, AlterarUsuarioActivity.class);
                startActivity(it);
                finish();
            }
        });
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        System.out.println("onConnectionFailed:" + connectionResult);
    }


}
