package com.expoagro.expoagrobrasil.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.expoagro.expoagrobrasil.R;
import com.expoagro.expoagrobrasil.dao.ServicoDAO;
import com.expoagro.expoagrobrasil.dao.UserDAO;
import com.expoagro.expoagrobrasil.model.Servico;
import com.expoagro.expoagrobrasil.model.Usuario;
import com.expoagro.expoagrobrasil.util.GoogleSignIn;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Fabricio on 8/4/2017.
 */

public class VisualizarServicoActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private ProgressDialog progress;
    private GoogleApiClient mGoogleApiClient;
    private String shareServico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_servico);

        final String keyServico = MenuServicoActivity.getId();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        mGoogleApiClient = new GoogleApiClient.Builder(VisualizarServicoActivity.this)
                .enableAutoManage(VisualizarServicoActivity.this, VisualizarServicoActivity.this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();


        progress = new ProgressDialog(VisualizarServicoActivity.this);
        progress.setCancelable(false);
        progress.setIndeterminate(true);
        progress.setMessage("Carregando anúncio...");
        progress.show();

        ServicoDAO.getDatabaseReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot serv : dataSnapshot.getChildren()) {
                    if (serv.getKey().equals(keyServico)) {
                        final Servico servico = serv.getValue(Servico.class);
                        ((TextView) findViewById(R.id.dataServico)).setText("Data: " + servico.getData());
                        ((TextView) findViewById(R.id.descricaoServico)).setText("Descrição: " + servico.getDescricao());
                        ((TextView) findViewById(R.id.nomeServico)).setText("Nome: " + servico.getNome());
                        ((TextView) findViewById(R.id.observacaoServico)).setText("Observação: " + servico.getObservacao());
                        UserDAO.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot user : dataSnapshot.getChildren()) {
                                    if (user.getKey().equals(servico.getIdUsuario())) {
                                        Usuario target = user.getValue(Usuario.class);
                                        ((TextView) findViewById(R.id.vendedorServico)).setText("Vendedor: " + target.getNome());
                                        progress.dismiss();
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                System.out.println("Error - recuperar usuario - Visualizar Anuncio");
                            }
                        });

                        shareServico = "Confira " + servico.getNome().toUpperCase() +
                                " por " + servico.getValor() +
                                " no aplicativo ExpoAgro Brasil!";
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Visualizar Servico database error");
            }
        });

        ImageButton mBtnServico = (ImageButton) findViewById(R.id.btnCompartilharServico);
        mBtnServico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = shareServico + " https://play.google.com/store?hl=pt-BR&tab=w8";
                String shareSub = "Baixe o aplicativo ExpoAgro Brasil e confira a oferta!";
                myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                myIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                startActivity(Intent.createChooser(myIntent, "Compartilhar usando"));
            }
        });

        Button alterar = (Button) findViewById(R.id.alterarServico);
        Button excluir = (Button) findViewById(R.id.excluirServico);

        alterar.setVisibility(View.GONE);
        excluir.setVisibility(View.GONE);

        checkForConnection();
    }

    private void checkForConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        boolean isConnected =  netInfo != null && netInfo.isConnectedOrConnecting();
        if (!isConnected) {
            Toast.makeText(VisualizarServicoActivity.this, "Você não está conectado a Internet", Toast.LENGTH_SHORT).show();
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                GoogleSignIn.signOut(VisualizarServicoActivity.this, mGoogleApiClient);
            }
            finish();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        System.out.println("onConnection Failed Listener");
    }
}
