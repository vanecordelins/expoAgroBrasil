package com.expoagro.expoagrobrasil.controller;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.expoagro.expoagrobrasil.R;
import com.expoagro.expoagrobrasil.dao.ServicoDAO;
import com.expoagro.expoagrobrasil.model.Servico;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by joao on 03/08/17.
 */

public class VisualizarMeuServicoClicadoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_servico);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar7);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String keyServico = VisualizarMeusServicosActivity.getId();

        TextView verComentarios = (TextView) findViewById(R.id.textoComentarios);
        verComentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent telaComentarios = new Intent(VisualizarMeuServicoClicadoActivity.this, ComentariosActivity.class);
                startActivity(telaComentarios);
            }
        });

        ImageButton btnFavoritarServico = (ImageButton)findViewById(R.id.btnFavoritarServico);
        btnFavoritarServico.setEnabled(false);

        ServicoDAO.getDatabaseReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot serv : dataSnapshot.getChildren()) {
                    if (serv.getKey().equals(keyServico) ) {
                        final Servico servico = serv.getValue(Servico.class);
                        ((TextView) findViewById(R.id.dataServico)).setText("Data: " + servico.getData());
                        ((TextView) findViewById(R.id.descricaoServico)).setText("Descrição: " + servico.getDescricao());
                        ((TextView) findViewById(R.id.nomeServico)).setText("Nome: " + servico.getNome());
                        ((TextView) findViewById(R.id.observacaoServico)).setText("Observação: " + servico.getObservacao());
                        ((TextView) findViewById(R.id.vendedorServico)).setText("Frequência: " + servico.getFrequencia());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(VisualizarMeuServicoClicadoActivity.this, "Erro ao recuperar Servico.", Toast.LENGTH_SHORT);
            }
        });

        Button alterar = (Button) findViewById(R.id.alterarServico);
        alterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkForConnection()) {
                    Intent intent = new Intent(VisualizarMeuServicoClicadoActivity.this, AlterarServicoActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        Button excluir = (Button) findViewById(R.id.excluirServico);
        excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog alertDialog = new AlertDialog.Builder(VisualizarMeuServicoClicadoActivity.this).setIcon(android.R.drawable.ic_delete).setTitle("EXCLUIR SERVIÇO")
                        .setMessage("Deseja realmente EXCLUIR este serviço? Todos os seus dados serão perdidos!")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                VisualizarMeusServicosActivity.setId(null);
                                ServicoDAO pdao = new ServicoDAO();
                                pdao.delete(keyServico);
                                Toast.makeText(VisualizarMeuServicoClicadoActivity.this, "Serviço deletado com sucesso.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(VisualizarMeuServicoClicadoActivity.this, VisualizarMeusServicosActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).setNegativeButton("Não", null).show();
                alertDialog.setCanceledOnTouchOutside(true);
            }
        });

        checkForConnection();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        VisualizarMeusServicosActivity.setId(null);
        Intent intent = new Intent(VisualizarMeuServicoClicadoActivity.this, VisualizarMeusServicosActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                VisualizarMeusServicosActivity.setId(null);
                Intent intent = new Intent(VisualizarMeuServicoClicadoActivity.this, VisualizarMeusServicosActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean checkForConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        boolean isConnected =  netInfo != null && netInfo.isConnectedOrConnecting();
        if (!isConnected) {
            Toast.makeText(VisualizarMeuServicoClicadoActivity.this, "Você não está conectado a Internet", Toast.LENGTH_SHORT).show();
            finish();
            return false;
        }
        return true;
    }

}
