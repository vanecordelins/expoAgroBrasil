package com.expoagro.expoagrobrasil.controller;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.expoagro.expoagrobrasil.R;
import com.expoagro.expoagrobrasil.dao.ProdutoDAO;
import com.expoagro.expoagrobrasil.model.Produto;
import com.expoagro.expoagrobrasil.util.AnuncioViewPager;
import com.expoagro.expoagrobrasil.util.GoogleSignIn;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Samir on 25/07/2017.
 */

public class VisualizarMeuProdutoClicadoActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private ViewPager viewPager;
    private AnuncioViewPager testeViewPager;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_anuncio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final ArrayList<String> img = new ArrayList<>();

        final String keyProduto = VisualizarMeusProdutosActivity.getId();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        mGoogleApiClient = new GoogleApiClient.Builder(VisualizarMeuProdutoClicadoActivity.this)
                .enableAutoManage(VisualizarMeuProdutoClicadoActivity.this, VisualizarMeuProdutoClicadoActivity.this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();


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
                        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);

                        if(produto.getFoto() != null){
                            if (!produto.getFoto().isEmpty()) {
                                viewPager.setBackground(null);
                            }
                            for (int i =0; i<produto.getFoto().size(); i++){
                                img.add(produto.getFoto().get(i));
                            }
                        }
                        testeViewPager = new AnuncioViewPager(VisualizarMeuProdutoClicadoActivity.this, img);
                        viewPager.setAdapter(testeViewPager);
                        indicator.setViewPager(viewPager);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(VisualizarMeuProdutoClicadoActivity.this, "Erro ao recuperar produto", Toast.LENGTH_SHORT);
            }
        });

        Button alterar = (Button) findViewById(R.id.alterarProduto);
        alterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkForConnection()) {
                    Intent intent = new Intent(VisualizarMeuProdutoClicadoActivity.this, AlterarProdutoActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        Button excluir = (Button) findViewById(R.id.excluirProduto);
        excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkForConnection()) {
                    VisualizarMeusProdutosActivity.setId(null);
                    Dialog alertDialog = new AlertDialog.Builder(VisualizarMeuProdutoClicadoActivity.this).setIcon(android.R.drawable.ic_delete).setTitle("EXCLUIR PRODUTO")
                            .setMessage("Deseja realmente EXCLUIR este produto? Todos os seus dados serão perdidos!")
                            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialog, int which) {
                                    ProdutoDAO pdao = new ProdutoDAO();
                                    pdao.delete(keyProduto);
                                    Toast.makeText(VisualizarMeuProdutoClicadoActivity.this, "Produto deletado com sucesso.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(VisualizarMeuProdutoClicadoActivity.this, VisualizarMeusProdutosActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }).setNegativeButton("Não", null).show();
                    alertDialog.setCanceledOnTouchOutside(true);
                }
            }
        });

        TextView verComentarios = (TextView) findViewById(R.id.textoComentarios);
        verComentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent telaComentarios = new Intent(VisualizarMeuProdutoClicadoActivity.this, ComentariosActivity.class);
                startActivity(telaComentarios);
            }
        });

        checkForConnection();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(VisualizarMeuProdutoClicadoActivity.this, VisualizarMeusProdutosActivity.class);
        startActivity(intent);
        VisualizarMeusProdutosActivity.setId(null);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                VisualizarMeusProdutosActivity.setId(null);
                Intent intent = new Intent(VisualizarMeuProdutoClicadoActivity.this, VisualizarMeusProdutosActivity.class);
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
            Toast.makeText(VisualizarMeuProdutoClicadoActivity.this, "Você não está conectado a Internet", Toast.LENGTH_SHORT).show();
            VisualizarMeusProdutosActivity.setId(null);
            finish();
            return false;
        }
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        System.out.println("Error API CLIENT");
    }

}
