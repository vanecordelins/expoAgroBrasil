package com.expoagro.expoagrobrasil.controller;

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

import com.expoagro.expoagrobrasil.R;
import com.expoagro.expoagrobrasil.model.Produto;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Samir on 25/07/2017.
 */

public class VisualizarMeusAnunciosActivity extends AppCompatActivity {

    private static String idClicado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_meus_anuncios);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RadioButton rdoBtnServico = (RadioButton) findViewById(R.id.rdoBtnServico3);
        rdoBtnServico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent telaCadastrarServico = new Intent(VisualizarMeusAnunciosActivity.this, VisualizarMeusServicosActivity.class);
                startActivity(telaCadastrarServico);
                finish();
            }
        });


        // ----------------------------------RecyclerView-----------------------------------------------------------


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query myref = FirebaseDatabase.getInstance().getReference("Produto").orderByChild("idUsuario").equalTo(uid);

        FirebaseRecyclerAdapter<Produto, VisualizarMeusAnunciosActivity.ListaViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<Produto, VisualizarMeusAnunciosActivity.ListaViewHolder>(
                Produto.class,
                R.layout.linha,
                VisualizarMeusAnunciosActivity.ListaViewHolder.class,
                myref
        ) {

            @Override
            protected void populateViewHolder(VisualizarMeusAnunciosActivity.ListaViewHolder viewHolder, final Produto model, int position) {

                final String keyProduto = getRef(position).getKey();

                viewHolder.setCategoria(model.getCategoria());
                viewHolder.setData(model.getData());
                viewHolder.setValor(model.getValor());
                viewHolder.setFoto(model.getFoto());
                viewHolder.setNome(model.getNome());


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setId(keyProduto);
                        //  TextView i = (TextView) findViewById(R.id.vendedor);
                        //   i.setText(model.getNome());
                        Intent intent = new Intent(VisualizarMeusAnunciosActivity.this, VisualizarMeuProdutoClicadoActivity.class);
                        startActivity(intent);
                        finish();
                   //     Toast.makeText(VisualizarMeusAnunciosActivity.this, key, Toast.LENGTH_LONG).show();

                    }
                });

            }

        };

        recyclerView.setAdapter(recyclerAdapter);
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
            //textView_nome2 = (TextView) itemView.findViewById(R.id.vendedorProduto);
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
                        .resize(100,100)
                        .into(imageView);
            }
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(VisualizarMeusAnunciosActivity.this, MenuProdutoActivity.class);
        startActivity(intent);
        finish();
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        System.out.println("onConnectionFailed:" + connectionResult);
    }
}
