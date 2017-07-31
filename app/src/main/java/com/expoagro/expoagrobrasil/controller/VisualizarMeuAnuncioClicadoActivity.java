package com.expoagro.expoagrobrasil.controller;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.expoagro.expoagrobrasil.R;
import com.expoagro.expoagrobrasil.dao.ProdutoDAO;
import com.expoagro.expoagrobrasil.model.Produto;
import com.expoagro.expoagrobrasil.util.AnuncioViewPager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Samir on 25/07/2017.
 */

public class VisualizarMeuAnuncioClicadoActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private AnuncioViewPager testeViewPager;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_anuncio);
        final ArrayList<String> img = new ArrayList<>();

        final String keyProduto = VisualizarMeusAnunciosActivity.getId();

        ProdutoDAO.getDatabaseReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot prod : dataSnapshot.getChildren()) {
                    if (prod.getKey().equals(keyProduto) ) {
                        Produto produto = prod.getValue(Produto.class);
                        ((TextView) findViewById(R.id.dataProduto)).setText("Data: " + produto.getData());
                        ((TextView) findViewById(R.id.descricaoProduto)).setText("Descrição: " + produto.getDescricao());
                        ((TextView) findViewById(R.id.nomeProduto)).setText("Nome: " + produto.getNome());
                        ((TextView) findViewById(R.id.observacaoProduto)).setText("Observação: " + produto.getObservacao());
                        findViewById(R.id.vendedorProduto).setVisibility(View.GONE);

                        viewPager = (ViewPager)findViewById(R.id.viewPager);

                        if(produto.getFoto() != null){
                            if (!produto.getFoto().isEmpty()) {
                                viewPager.setBackground(null);
                            }
                            for (int i =0; i<produto.getFoto().size(); i++){
                                img.add(produto.getFoto().get(i));
                            }
                        }
                        testeViewPager = new AnuncioViewPager(VisualizarMeuAnuncioClicadoActivity.this, img);
                        viewPager.setAdapter(testeViewPager);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(VisualizarMeuAnuncioClicadoActivity.this, "Erro ao recuperar produto", Toast.LENGTH_SHORT);
            }
        });

        Button alterar = (Button) findViewById(R.id.alterarProduto);
        alterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VisualizarMeuAnuncioClicadoActivity.this, AlterarProdutoActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button excluir = (Button) findViewById(R.id.excluirProduto);
        excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog alertDialog = new AlertDialog.Builder(VisualizarMeuAnuncioClicadoActivity.this).setIcon(android.R.drawable.ic_delete).setTitle("EXCLUIR PRODUTO")
                        .setMessage("Deseja realmente EXCLUIR este produto? Todos os seus dados serão perdidos!")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                ProdutoDAO pdao = new ProdutoDAO();
                                pdao.delete(keyProduto);
                                Toast.makeText(VisualizarMeuAnuncioClicadoActivity.this, "Produto deletado com sucesso.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(VisualizarMeuAnuncioClicadoActivity.this, VisualizarMeusAnunciosActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).setNegativeButton("Não", null).show();
                alertDialog.setCanceledOnTouchOutside(true);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(VisualizarMeuAnuncioClicadoActivity.this, VisualizarMeusAnunciosActivity.class);
        startActivity(intent);
        finish();
    }
}
