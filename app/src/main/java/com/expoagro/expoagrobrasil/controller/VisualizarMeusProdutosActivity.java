package com.expoagro.expoagrobrasil.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.expoagro.expoagrobrasil.R;
import com.expoagro.expoagrobrasil.model.Produto;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Samir on 25/07/2017.
 */

public class VisualizarMeusProdutosActivity extends AppCompatActivity {

    private static String idClicado;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_anuncios);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progress = new ProgressDialog(VisualizarMeusProdutosActivity.this);
        progress.setCancelable(false);
        progress.setIndeterminate(true);
        progress.setMessage("Carregando anúncios...");

        RadioButton rdoBtnServico = (RadioButton) findViewById(R.id.rdoBtnServico3);
        rdoBtnServico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent telaCadastrarServico = new Intent(VisualizarMeusProdutosActivity.this, VisualizarMeusServicosActivity.class);
                startActivity(telaCadastrarServico);
                finish();
            }
        });

        RadioButton rdoBtnProduto = (RadioButton) findViewById(R.id.rdoBtnProduto3);
        rdoBtnProduto.setChecked(true);

        // ----------------------------------RecyclerView-----------------------------------------------------------
        progress.show();
        Thread mThread = new Thread() {
            @Override
            public void run() {
                final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(VisualizarMeusProdutosActivity.this));
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Query myref = FirebaseDatabase.getInstance().getReference("Produto").orderByChild("idUsuario").equalTo(uid);

                myref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            progress.dismiss();
                            Toast.makeText(VisualizarMeusProdutosActivity.this, "Você não possui produtos cadastrados.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) { databaseError.getMessage(); }
                });
                final FirebaseRecyclerAdapter<Produto, VisualizarMeusProdutosActivity.ListaViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<Produto, VisualizarMeusProdutosActivity.ListaViewHolder>(
                        Produto.class,
                        R.layout.linha,
                        VisualizarMeusProdutosActivity.ListaViewHolder.class,
                        myref
                ) {
                    @Override
                    protected void populateViewHolder(VisualizarMeusProdutosActivity.ListaViewHolder viewHolder, final Produto model, int position) {
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
                                Intent intent = new Intent(VisualizarMeusProdutosActivity.this, VisualizarMeuProdutoClicadoActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                };
                VisualizarMeusProdutosActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() { recyclerView.setAdapter(recyclerAdapter); }
                });
            }
        };
        mThread.start();
        checkForFirebaseConn();
    }

    private void checkForFirebaseConn() {
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");

        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (!connected) {
                    Toast.makeText(VisualizarMeusProdutosActivity.this, "Você não está conectado a Internet", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Error");
            }
        });
    }
    public static String getId() {
        return idClicado;
    }

    public static void setId(String id) {
        idClicado = id;
    }

    public static class ListaViewHolder extends RecyclerView.ViewHolder{
        private View mView;
        private TextView textView_nome;
        private TextView textView_data;
        private TextView textView_valor;
        private TextView textView_categoria;
        private ImageView imageView;

        public ListaViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            textView_nome = (TextView) itemView.findViewById(R.id.nomeProduto);
            textView_data = (TextView) itemView.findViewById(R.id.dataProduto);
            textView_valor = (TextView) itemView.findViewById(R.id.valorProduto);
            textView_categoria = (TextView) itemView.findViewById(R.id.categoriaProduto);
            imageView = (ImageView) itemView.findViewById(R.id.fotoProduto);
        }


        public void setNome(String nome) { textView_nome.setText(nome); }

        public void setData(String data) {
            textView_data.setText(data);
        }

        public void setValor(String valor) {
            textView_valor.setText(valor);
        }

        public void setCategoria(String categoria) { textView_categoria.setText(categoria); }

        public void setFoto(List<String> foto) {
            if (foto != null) {
                Picasso.with(mView.getContext())
                        .load(foto.get(0))
                        .fit()
                        .into(imageView);
            } else {
                imageView.setImageResource(R.drawable.sem_foto);
            }
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(VisualizarMeusProdutosActivity.this, MenuProdutoActivity.class);
        startActivity(intent);
        finish();
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        System.out.println("onConnectionFailed:" + connectionResult);
    }
}
