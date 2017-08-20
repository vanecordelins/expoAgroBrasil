package com.expoagro.expoagrobrasil.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.expoagro.expoagrobrasil.R;

public class NovoComentarioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_comentario);

        Button btn_cancelar = (Button) findViewById(R.id.btnCancelar);
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(NovoComentarioActivity.this, ComentariosActivity.class);
                startActivity(it);
                finish();
            }
        });

        Button btn_cadastrar = (Button) findViewById(R.id.btnCadastrar);
    }
}
