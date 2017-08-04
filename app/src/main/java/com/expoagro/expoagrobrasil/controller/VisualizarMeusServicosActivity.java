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
import com.expoagro.expoagrobrasil.util.Servico;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by joao on 03/08/17.
 */

public class VisualizarMeusServicosActivity extends AppCompatActivity {

    private static String idClicado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_meus_servicos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RadioButton rdoBtnServico = (RadioButton) findViewById(R.id.rdoBtnProduto3);
        rdoBtnServico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent telaCadastrarServico = new Intent(VisualizarMeusServicosActivity.this, VisualizarMeusProdutosActivity.class);
                startActivity(telaCadastrarServico);
                finish();
            }
        });


        // ----------------------------------RecyclerView-----------------------------------------------------------


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview4);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query myref = FirebaseDatabase.getInstance().getReference("Serviço").orderByChild("idUsuario").equalTo(uid);

        FirebaseRecyclerAdapter<Servico, VisualizarMeusServicosActivity.ListaViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<Servico, VisualizarMeusServicosActivity.ListaViewHolder>(
                Servico.class,
                R.layout.linha,
                VisualizarMeusServicosActivity.ListaViewHolder.class,
                myref
        ) {

            @Override
            protected void populateViewHolder(VisualizarMeusServicosActivity.ListaViewHolder viewHolder, final Servico model, int posit) {

                final String keyServico = getRef(posit).getKey();

                viewHolder.setCategoria(model.getCategoria());
                viewHolder.setData(model.getData());
                viewHolder.setValor(model.getValor());
                viewHolder.setNome(model.getNome());


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setId(keyServico);
                        //  TextView i = (TextView) findViewById(R.id.vendedor);
                        //   i.setText(model.getNome());
                        Intent intent = new Intent(VisualizarMeusServicosActivity.this, VisualizarMeuServicoClicadoActivity.class);
                        startActivity(intent);
                        finish();
                        //     Toast.makeText(VisualizarMeusAnunciosActivitty.this, key, Toast.LENGTH_LONG).show();

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

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(VisualizarMeusServicosActivity.this, MenuProdutoActivity.class);
        startActivity(intent);
        finish();
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        System.out.println("onConnectionFailed:" + connectionResult);
    }
}
