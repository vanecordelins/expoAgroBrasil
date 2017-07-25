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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;

import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.expoagro.expoagrobrasil.R;


import com.expoagro.expoagrobrasil.dao.UserDAO;


import com.expoagro.expoagrobrasil.model.Usuario;
import com.expoagro.expoagrobrasil.util.GoogleSignIn;
import com.expoagro.expoagrobrasil.util.Lista;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.auth.api.Auth;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.List;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private String uid;
    private RecyclerView recyclerView;

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

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(MenuActivity.this)
                .enableAutoManage(MenuActivity.this, MenuActivity.this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // ----------------------------------RecyclerView-----------------------------------------------------------

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DatabaseReference myref = FirebaseDatabase.getInstance().getReference("Produto");
            FirebaseRecyclerAdapter<Lista, ListaViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<Lista, ListaViewHolder>(
                    Lista.class,
                    R.layout.linha,
                    ListaViewHolder.class,
                    myref


            ) {

                @Override
                protected void populateViewHolder(ListaViewHolder viewHolder, Lista model, int position) {

                    final String key = getRef(position).getKey();

                    viewHolder.setCategoria(model.getCategoria());
                    viewHolder.setData(model.getData());
                    viewHolder.setValor(model.getValor());
                    viewHolder.setFoto(model.getFoto());
                    viewHolder.setNome(model.getNome());


                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(MenuActivity.this, key, Toast.LENGTH_SHORT).show();
                        }
                    });


                }


            };

            recyclerView.setAdapter(recyclerAdapter);
    }

    public static class ListaViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView textView_nome;
        TextView textView_data;
        TextView textView_valor;
        TextView textView_categoria;
        ImageView imageView;

        public ListaViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            textView_nome = (TextView) itemView.findViewById(R.id.nome);
            textView_data = (TextView) itemView.findViewById(R.id.data);
            textView_valor = (TextView) itemView.findViewById(R.id.valor);
            textView_categoria = (TextView) itemView.findViewById(R.id.categoria);
            imageView = (ImageView) itemView.findViewById(R.id.foto);
        }


        public void setNome(String nome) {
            textView_nome.setText(nome);
        }

        public void setData(String data) {
            textView_data.setText(data);
        }

        public void setValor(String valor) {
            textView_valor.setText(valor);
        }

        public void setCategoria(String categoria) {
            textView_categoria.setText(categoria);
        }


        public void setFoto(List<String> foto) {
            if (foto == null) {
            } else {
                Picasso.with(mView.getContext())
                        .load(foto.get(0))
                        .resize(50,50)
                        .into(imageView);
            }
        }
    }

//---------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        //getMenuInflater().inflate(R.menu.menu, menu);

        final TextView nomeUsuarioLogado = (TextView) findViewById(R.id.menu_nome);
        final TextView emailUsuarioLogado = (TextView) findViewById(R.id.menu_email);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }


        UserDAO.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    if (user.getKey().equals(uid)) {
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
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
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

