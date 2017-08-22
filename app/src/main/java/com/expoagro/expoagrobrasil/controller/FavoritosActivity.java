package com.expoagro.expoagrobrasil.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.expoagro.expoagrobrasil.R;
import com.expoagro.expoagrobrasil.dao.ProdutoDAO;
import com.expoagro.expoagrobrasil.model.Produto;
import com.expoagro.expoagrobrasil.util.GoogleSignIn;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FavoritosActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private RecyclerView recyclerView;
    private static String idClicado;
    private static String testeId;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progress = new ProgressDialog(FavoritosActivity.this);
        progress.setCancelable(false);
        progress.setIndeterminate(true);
        progress.setMessage("Carregando anúncios...");
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        progress.show();
        Thread mThread = new Thread() {
            @Override
            public void run() {
                recyclerView = (RecyclerView) findViewById(R.id.recyclerview3);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(FavoritosActivity.this));
                final Query myref = FirebaseDatabase.getInstance().getReference("Favoritos").child(uid);

                myref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            progress.dismiss();
                            FavoritosActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(FavoritosActivity.this, "Nenhum anúncio favoritado.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println(databaseError.getMessage());
                    }
                });

                final FirebaseRecyclerAdapter<Produto, FavoritosActivity.AnuncioViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<Produto, FavoritosActivity.AnuncioViewHolder>(
                        Produto.class,
                        R.layout.linha,
                        FavoritosActivity.AnuncioViewHolder.class,
                        myref
                ) {
                    @Override
                    protected void populateViewHolder(final FavoritosActivity.AnuncioViewHolder viewHolder, Produto model, int posit) {
                        final String keyAnuncio = getRef(posit).getKey();
                        testeId = keyAnuncio;
                        viewHolder.setCategoria(model.getCategoria());
                        viewHolder.setData(model.getData());
                        viewHolder.setValor(model.getValor());
                        viewHolder.setNome(model.getNome());
                        viewHolder.setFoto(model.getFoto());
                        progress.dismiss();

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final Query myQuery1 = ProdutoDAO.getDatabaseReference().child(keyAnuncio);
                                myQuery1.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        System.out.println("CONSULTA: " + myQuery1);
                                        System.out.println("NÃO Existe: " + !dataSnapshot.exists());
                                        if (!dataSnapshot.exists()) {
                                            MenuServicoActivity.setId(keyAnuncio);
                                            Intent intent = new Intent(FavoritosActivity.this, VisualizarServicoActivity.class);
                                            startActivity(intent);
                                        } else {
                                            MenuProdutoActivity.setId(keyAnuncio);
                                            Intent intent = new Intent(FavoritosActivity.this, VisualizarProdutoActivity.class);
                                            startActivity(intent);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        System.out.println(databaseError.getMessage());
                                    }
                                });
                            }
                        });
                    }
                };
                FavoritosActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(recyclerAdapter);
                    }
                });
            }
        };
        mThread.start();
        checkForConnection();
    }

    private void checkForConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        boolean isConnected = netInfo != null && netInfo.isConnectedOrConnecting();
        if (!isConnected) {
            Toast.makeText(FavoritosActivity.this, "Você não está conectado a Internet", Toast.LENGTH_LONG).show();
            progress.dismiss();
        }
    }

    public static String getId() {
        return idClicado;
    }

    public static void setId(String id) {
        idClicado = id;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        System.out.println("Erro");
    }

    public static class AnuncioViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView textView_nome;
        private TextView textView_data;
        private TextView textView_valor;
        private TextView textView_frequencia;
        private ImageView imageView;

        public AnuncioViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            textView_nome = (TextView) itemView.findViewById(R.id.nomeProduto);
            textView_data = (TextView) itemView.findViewById(R.id.dataProduto);
            textView_valor = (TextView) itemView.findViewById(R.id.valorProduto);
            textView_frequencia = (TextView) itemView.findViewById(R.id.categoriaProduto);
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

        public void setFrequencia(String frequencia) {
            textView_frequencia.setText(frequencia);
        }

        public void setCategoria(String categoria) {
            textView_frequencia.setText(categoria);
        }

        public void setFoto(List<String> foto) {
            if (foto != null) {
                Picasso.with(mView.getContext())
                        .load(foto.get(0))
                        .fit()
                        .into(imageView);
            } else {
                final Query myQuery1 = ProdutoDAO.getDatabaseReference().child(testeId);
                myQuery1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                         if (!dataSnapshot.exists()) {
                             imageView.setImageResource(R.drawable.services);
                         } else {
                             imageView.setImageResource(R.drawable.sem_foto);
                         }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println(databaseError.getMessage());
                    }
                });
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FavoritosActivity.this, MenuProdutoActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(FavoritosActivity.this, MenuProdutoActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
