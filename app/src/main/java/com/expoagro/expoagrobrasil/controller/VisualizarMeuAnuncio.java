package com.expoagro.expoagrobrasil.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
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

public class VisualizarMeuAnuncio extends AppCompatActivity {

    private ViewPager viewPager;
    private AnuncioViewPager testeViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_anuncio);
        final ArrayList<String> img = new ArrayList<>();

        final String keyProduto = VisualizarMeusAnunciosActivitty.getId();


        System.out.println(keyProduto);
        final String nome = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        ProdutoDAO.getDatabaseReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot prod : dataSnapshot.getChildren()) {
                    if (prod.getKey().equals(keyProduto) ) {
                        Produto produto = prod.getValue(Produto.class);
                        ((TextView) findViewById(R.id.dataProduto)).setText("Data: " + produto.getData());
                        ((TextView) findViewById(R.id.vendedorProduto)).setText("Vendedor: " + nome);
                        ((TextView) findViewById(R.id.descricaoProduto)).setText("Descrição: " + produto.getDescricao());
                        ((TextView) findViewById(R.id.nomeProduto)).setText("Nome: " + produto.getNome());
                        ((TextView) findViewById(R.id.observacaoProduto)).setText("Observação: " + produto.getObservacao());

                        if(produto.getFoto() != null){
                            for (int i =0; i<produto.getFoto().size(); i++){
                                img.add(produto.getFoto().get(i));
                            }
                        }
                        viewPager = (ViewPager)findViewById(R.id.viewPager);
                        testeViewPager = new AnuncioViewPager(VisualizarMeuAnuncio.this, img);
                        viewPager.setAdapter(testeViewPager);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(VisualizarMeuAnuncio.this, "Erro ao recuperar produto", Toast.LENGTH_SHORT);
            }
        });

        //System.out.println(img);
        Button alterar = (Button) findViewById(R.id.alterarProduto);
        alterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VisualizarMeuAnuncio.this, AlterarProdutoActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(VisualizarMeuAnuncio.this, VisualizarMeusAnunciosActivitty.class);
        startActivity(intent);
        finish();
    }
}
