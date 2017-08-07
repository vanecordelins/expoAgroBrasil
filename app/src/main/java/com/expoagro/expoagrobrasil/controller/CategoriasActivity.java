package com.expoagro.expoagrobrasil.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.expoagro.expoagrobrasil.R;

public class CategoriasActivity extends AppCompatActivity {

    private static String uid;
    private static boolean isClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button animal = (Button) findViewById(R.id.animal);
        animal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUid("Animal");
                setIsClick(true);
                acao();
            }
        });
        Button ferramenta = (Button) findViewById(R.id.ferramenta);
        ferramenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUid("Ferramenta");
                setIsClick(true);
                acao();
            }
        });
        Button propriedade = (Button) findViewById(R.id.propriedade);
        propriedade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUid("Propriedade");
                setIsClick(true);
                acao();
            }
        });
        Button maquinario = (Button) findViewById(R.id.maquinarios);
        maquinario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUid("Maquinários");
                setIsClick(true);
                acao();
            }
        });
        Button outros = (Button) findViewById(R.id.outros);
        outros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUid("Outros");
                setIsClick(true);
                acao();
            }
        });
        Button produto = (Button) findViewById(R.id.produto);
        produto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUid("Produto");
                setIsClick(false);
                acao();
            }
        });

    }

    public void acao(){
        Intent intent = new Intent(CategoriasActivity.this, MenuProdutoActivity.class);
        startActivity(intent);
        finish();
    }

    public static String getUid() {
        return uid;
    }

    public static void setUid(String categoria) {
        uid = categoria;
    }

    public static boolean isClick() {
        return isClick;
    }

    public static void setIsClick(boolean isClicked) {
        isClick = isClicked;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent it = new Intent(CategoriasActivity.this, MenuProdutoActivity.class);
                startActivity(it);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CategoriasActivity.this, MenuProdutoActivity.class);
        startActivity(intent);
        finish();
    }

}
