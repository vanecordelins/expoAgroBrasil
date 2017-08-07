package com.expoagro.expoagrobrasil.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.expoagro.expoagrobrasil.R;
import com.expoagro.expoagrobrasil.dao.UserDAO;
import com.expoagro.expoagrobrasil.model.Produto;
import com.expoagro.expoagrobrasil.model.Usuario;
import com.expoagro.expoagrobrasil.util.GoogleSignIn;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MenuProdutoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private String uid;
    private RecyclerView recyclerView;
    private static String idClicado;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        uid = "";
        progress = new ProgressDialog(MenuProdutoActivity.this);
        progress.setCancelable(false);
        progress.setIndeterminate(true);
        progress.setMessage("Carregando anúncios...");

        ((NavigationView) findViewById(R.id.nav_view)).setNavigationItemSelectedListener(MenuProdutoActivity.this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        mGoogleApiClient = new GoogleApiClient.Builder(MenuProdutoActivity.this)
                .enableAutoManage(MenuProdutoActivity.this, MenuProdutoActivity.this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();


        RadioButton rdoBtnServico = (RadioButton) findViewById(R.id.rdoBtnServico2);
        rdoBtnServico.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
            Intent telaCadastrarServico = new Intent(MenuProdutoActivity.this, MenuServicoActivity.class);
            startActivity(telaCadastrarServico);
            finish();
                                             }
                                         });

                // ----------------------------------RadioButton-----------------------------------------------------------

        RadioButton rdoBtnProduto = (RadioButton) findViewById(R.id.rdoBtnProduto2);
        rdoBtnProduto.setChecked(true);
        // ----------------------------------RecyclerView-----------------------------------------------------------
        progress.show();
        Thread mThread = new Thread() {
            @Override
            public void run() {
                recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(MenuProdutoActivity.this));
                Query myref = FirebaseDatabase.getInstance().getReference("Produto");
                if (CategoriasActivity.isClick()) {
                    myref = FirebaseDatabase.getInstance().getReference("Produto").orderByChild("categoria").equalTo(CategoriasActivity.getUid());
                    myref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() == null) {
                                Toast.makeText(MenuProdutoActivity.this, "Produtos não encontrados", Toast.LENGTH_SHORT).show();
                                progress.dismiss();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) { databaseError.getMessage(); }
                    });
                }
                final FirebaseRecyclerAdapter<Produto, ProdutoViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<Produto, ProdutoViewHolder>(
                        Produto.class,
                        R.layout.linha,
                        ProdutoViewHolder.class,
                        myref
                ) {
                    @Override
                    protected void populateViewHolder(ProdutoViewHolder viewHolder, Produto model, int position) {
                        final String key = getRef(position).getKey();
                        viewHolder.setCategoria(model.getCategoria());
                        viewHolder.setData(model.getData());
                        viewHolder.setValor(model.getValor());
                        viewHolder.setFoto(model.getFoto());
                        viewHolder.setNome(model.getNome());
                        progress.dismiss();

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                setId(key);
                                Intent intent = new Intent(MenuProdutoActivity.this, VisualizarProdutoActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                };
                MenuProdutoActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() { recyclerView.setAdapter(recyclerAdapter); }
                });
            }
        };
        mThread.start();
        checkForConnection();
    }

    private void checkForConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        boolean isConnected =  netInfo != null && netInfo.isConnectedOrConnecting();
        if (!isConnected) {
            Toast.makeText(MenuProdutoActivity.this, "Você não está conectado a Internet", Toast.LENGTH_LONG).show();
            progress.dismiss();
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                GoogleSignIn.signOut(MenuProdutoActivity.this, mGoogleApiClient);
            }
        }
    }

    public static String getId() {
        return idClicado;
    }

    public static void setId(String id) {
        idClicado = id;
    }

    public static class ProdutoViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView textView_nome;
        private TextView textView_data;
        private TextView textView_valor;
        private TextView textView_categoria;
        private ImageView imageView;

        public ProdutoViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            textView_nome = (TextView) itemView.findViewById(R.id.nomeProduto);
            textView_data = (TextView) itemView.findViewById(R.id.dataProduto);
            textView_valor = (TextView) itemView.findViewById(R.id.valorProduto);
            textView_categoria = (TextView) itemView.findViewById(R.id.categoriaProduto);
            imageView = (ImageView) itemView.findViewById(R.id.fotoProduto);
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
            if (foto != null) {
                    Picasso.with(mView.getContext())
                            .load(foto.get(0))
                            .fit()
                            //.resize(100,100)
                            .into(imageView);
            } else {
                imageView.setImageResource(R.drawable.sem_foto);
            }
        }
    }

//---------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.teste_filtro, menu);

        final TextView nomeUsuarioLogado = (TextView) findViewById(R.id.menu_nome);
        final TextView emailUsuarioLogado = (TextView) findViewById(R.id.menu_email);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        emailUsuarioLogado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    Intent it = new Intent(MenuProdutoActivity.this, LoginActivity.class);
                    startActivity(it);
                    finish();
                }
            }
        });

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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        finish();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_meu_perfil) {

            if(FirebaseAuth.getInstance().getCurrentUser() != null) { // Ja esta logado
                Intent telaVisualizar = new Intent(MenuProdutoActivity.this, VisualizarUsuarioActivity.class);
                startActivity(telaVisualizar);
                finish();
            } else {
                Intent telaLogin = new Intent(MenuProdutoActivity.this, LoginActivity.class);
                startActivity(telaLogin);
                finish();
            }
        } else if (id == R.id.menu_novo_anuncio) {

            if(FirebaseAuth.getInstance().getCurrentUser() != null) { // Ja esta logado
                Intent telaCadastrarAnuncio = new Intent(MenuProdutoActivity.this, CadastroProdutoActivity.class);
                startActivity(telaCadastrarAnuncio);
                finish();
            } else {
                Intent telaLogin = new Intent(MenuProdutoActivity.this, LoginActivity.class);
                startActivity(telaLogin);
                finish();
            }
        } else if (id == R.id.menu_meus_anuncios) {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                Intent telaLogin = new Intent(MenuProdutoActivity.this, VisualizarMeusProdutosActivity.class);
                startActivity(telaLogin);
                finish();
            } else {
                Intent telaLogin = new Intent(MenuProdutoActivity.this, LoginActivity.class);
                startActivity(telaLogin);
                finish();
            }
        } else if (id == R.id.menu_hoje) {
            finish();
        } else if (id == R.id.menu_favoritos) {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                System.out.println("MENU FAVORITOS"); // Ja esta logado
            } else {
                Intent telaLogin = new Intent(MenuProdutoActivity.this, LoginActivity.class);
                startActivity(telaLogin);
                finish();
            }
        } else if (id == R.id.menu_sair) {
            GoogleSignIn.signOut(MenuProdutoActivity.this, mGoogleApiClient);
        } /* else if (id == R.id.menu_sobre) {
          }*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.app_bar_filter:
                Intent intent = new Intent(MenuProdutoActivity.this, CategoriasActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        System.out.println("onConnectionFailed:" + connectionResult);
    }

}


