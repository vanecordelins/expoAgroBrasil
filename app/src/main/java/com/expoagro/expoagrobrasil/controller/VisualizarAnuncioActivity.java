package com.expoagro.expoagrobrasil.controller;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.expoagro.expoagrobrasil.R;
import com.expoagro.expoagrobrasil.dao.ProdutoDAO;
import com.expoagro.expoagrobrasil.dao.UserDAO;
import com.expoagro.expoagrobrasil.model.Produto;
import com.expoagro.expoagrobrasil.model.Usuario;
import com.expoagro.expoagrobrasil.util.AnuncioViewPager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Samir on 24/07/2017.
 */

public class VisualizarAnuncioActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private AnuncioViewPager testeViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_anuncio);
        final ArrayList<String> img = new ArrayList<>();

        final String keyProduto = MenuProdutoActivity.getId();

        ProdutoDAO.getDatabaseReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot prod : dataSnapshot.getChildren()) {
                    if (prod.getKey().equals(keyProduto) ) {
                        final Produto produto = prod.getValue(Produto.class);
                        ((TextView) findViewById(R.id.dataProduto)).setText("Data: " + produto.getData());
                        UserDAO.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot user : dataSnapshot.getChildren()) {
                                    if (user.getKey().equals(produto.getIdUsuario())) {
                                        Usuario target = user.getValue(Usuario.class);
                                        System.out.println(target.getNome());
                                        ((TextView) findViewById(R.id.vendedorProduto)).setText("Vendedor: " + target.getNome());
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                System.out.println("Erro ao pesquisar vendedor");
                            }
                        });
                        ((TextView) findViewById(R.id.descricaoProduto)).setText("Descrição: " + produto.getDescricao());
                        ((TextView) findViewById(R.id.nomeProduto)).setText("Nome: " + produto.getNome());
                        ((TextView) findViewById(R.id.observacaoProduto)).setText("Observação: " + produto.getObservacao());

                        viewPager = (ViewPager)findViewById(R.id.viewPager);
                        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);

                        if(produto.getFoto() != null){
                            if (!produto.getFoto().isEmpty()) {
                                viewPager.setBackground(null);
                            }
                            for (int i =0; i<produto.getFoto().size(); i++){
                                img.add(produto.getFoto().get(i));
                            }
                        }
                        testeViewPager = new AnuncioViewPager(VisualizarAnuncioActivity.this, img);
                        viewPager.setAdapter(testeViewPager);
                        indicator.setViewPager(viewPager);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(VisualizarAnuncioActivity.this, "Erro ao recuperar produto.", Toast.LENGTH_SHORT);
            }
        });

        //System.out.println(img);
        Button alterar = (Button) findViewById(R.id.alterarProduto);
        Button excluir = (Button) findViewById(R.id.excluirProduto);

        alterar.setVisibility(View.GONE);
        excluir.setVisibility(View.GONE);



    }

}
