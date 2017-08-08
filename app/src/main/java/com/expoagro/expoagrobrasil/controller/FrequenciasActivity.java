package com.expoagro.expoagrobrasil.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.expoagro.expoagrobrasil.R;

/**
 * Created by Fabricio on 8/4/2017.
 */

public class FrequenciasActivity extends AppCompatActivity {
    private static String uid;
    private static boolean isClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frequencia);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button diaria = (Button) findViewById(R.id.diaria);
        diaria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUid("Por Dia");
                setIsClick(true);
                acao();
            }
        });
        Button horaria = (Button) findViewById(R.id.horaria);
        horaria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUid("Por Hora");
                setIsClick(true);
                acao();
            }
        });
        Button semanal = (Button) findViewById(R.id.semanal);
        semanal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUid("Por Semana");
                setIsClick(true);
                acao();
            }
        });
        Button quinzenal = (Button) findViewById(R.id.quinzenal);
        quinzenal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUid("Por Quinzena");
                setIsClick(true);
                acao();
            }
        });

        Button outros = (Button) findViewById(R.id.outra);
        outros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUid("Outra");
                setIsClick(true);
                acao();
            }
        });
        Button servico = (Button) findViewById(R.id.servico);
        servico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUid("Serviço");
                setIsClick(false);
                acao();
            }
        });
    }

    public void acao(){
        Intent intent = new Intent(FrequenciasActivity.this, MenuServicoActivity.class);
        startActivity(intent);
        finish();
    }

    public static String getUid() {
        return uid;
    }

    public static void setUid(String frequencia) {
        uid = frequencia;
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
                Intent it = new Intent(FrequenciasActivity.this, MenuServicoActivity.class);
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
        Intent intent = new Intent(FrequenciasActivity.this, MenuServicoActivity.class);
        startActivity(intent);
        finish();
    }
}