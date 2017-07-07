package com.expoagro.expoagrobrasil.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.expoagro.expoagrobrasil.R;
import com.expoagro.expoagrobrasil.util.FirebaseLogin;
import com.expoagro.expoagrobrasil.util.GoogleSignIn;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

public class AnunciosActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private Button btn_sair;
    private Button btn_alterar;
    private Button btn_delete;
    private Button btn_alterar_senha;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncios);

        btn_sair = (Button) findViewById(R.id.btn_sair);
        btn_alterar = (Button) findViewById(R.id.alterar);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_alterar_senha = (Button) findViewById(R.id.alterar_senha);

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

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletarPerfil();

            }
        });

        if (FirebaseAuth.getInstance().getCurrentUser().getProviders().get(0).equals("google.com")) {
            btn_alterar_senha.setVisibility(View.GONE);
        }

        btn_alterar_senha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(AnunciosActivity.this, AlterarSenhaActivity.class);
                startActivity(it);
                finish();
            }
        });
    }

    public void deletarPerfil() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Deletar Perfil")
                .setMessage("Tem certeza que deseja excluir seu perfil? Todos os dados serão excluídos!")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseLogin.deleteAccount(AnunciosActivity.this);
                    }
                })
                .setNegativeButton("Não", null)
                .show();
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        System.out.println("onConnectionFailed:" + connectionResult);
    }


}
