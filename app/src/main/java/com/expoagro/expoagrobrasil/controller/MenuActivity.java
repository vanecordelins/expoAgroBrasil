package com.expoagro.expoagrobrasil.controller;

import android.content.Intent;

import android.os.Bundle;

import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.expoagro.expoagrobrasil.R;

import com.expoagro.expoagrobrasil.dao.UserDAO;
import com.expoagro.expoagrobrasil.model.Usuario;
import com.expoagro.expoagrobrasil.util.GoogleSignIn;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        uid = "";

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(MenuActivity.this)
                .enableAutoManage(MenuActivity.this /* FragmentActivity */, MenuActivity.this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

 /*       ProdutoDAO.getDatabaseReference().orderByChild("idUsuario").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot produto : dataSnapshot.getChildren()) {
                    Produto p = produto.getValue(Produto.class);
                    System.out.println(p.getId());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }); */

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        //getMenuInflater().inflate(R.menu.menu, menu);

        final TextView nomeUsuarioLogado = (TextView) findViewById(R.id.menu_nome);
        final TextView emailUsuarioLogado = (TextView) findViewById(R.id.menu_email);

        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        emailUsuarioLogado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    Intent it = new Intent(MenuActivity.this, LoginActivity.class);
                    startActivity(it);
                    finish();
                }
            }
        });


        UserDAO.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot user : dataSnapshot.getChildren()) {
                    if(user.getKey().equals(uid)){
                        Usuario target = user.getValue(Usuario.class);
                        nomeUsuarioLogado.setText(target.getNome());
                        emailUsuarioLogado.setText(target.getEmail());

                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_meu_perfil) {
            if(FirebaseAuth.getInstance().getCurrentUser() != null) { // Ja esta logado
                Intent telaVisualizar = new Intent(MenuActivity.this, VisualizarUsuarioActivity.class);
                startActivity(telaVisualizar);
                finish();
            } else {
                Intent telaLogin = new Intent(MenuActivity.this, LoginActivity.class);
                startActivity(telaLogin);
                finish();
            }
        } else if (id == R.id.menu_novo_anuncio) {

            if(FirebaseAuth.getInstance().getCurrentUser() != null) { // Ja esta logado
                Intent telaCadastrarAnuncio = new Intent(MenuActivity.this, CadastroProdutoActivity.class);
                startActivity(telaCadastrarAnuncio);
                finish();
            } else {
                Intent telaLogin = new Intent(MenuActivity.this, LoginActivity.class);
                startActivity(telaLogin);
                finish();
            }
        } else if (id == R.id.menu_meus_anuncios) {
            if(FirebaseAuth.getInstance().getCurrentUser() != null) {
                System.out.println("MENU MEUS FAVORITOS"); // Ja esta logado
            } else {
                Intent telaLogin = new Intent(MenuActivity.this, LoginActivity.class);
                startActivity(telaLogin);
                finish();
            }
        } else if (id == R.id.menu_hoje) {
            finish();
        } else if (id == R.id.menu_favoritos) {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                System.out.println("MENU FAVORITOS"); // Ja esta logado
            } else {
                Intent telaLogin = new Intent(MenuActivity.this, LoginActivity.class);
                startActivity(telaLogin);
                finish();
            }
        } else if (id == R.id.menu_sair) {
            GoogleSignIn.signOut(MenuActivity.this, mGoogleApiClient);
        } /* else if (id == R.id.menu_sobre) {

          }*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        System.out.println("onConnectionFailed:" + connectionResult);
    }

}

