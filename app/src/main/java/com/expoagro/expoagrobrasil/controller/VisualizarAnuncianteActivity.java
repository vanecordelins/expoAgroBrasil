package com.expoagro.expoagrobrasil.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.expoagro.expoagrobrasil.R;
import com.expoagro.expoagrobrasil.dao.UserDAO;
import com.expoagro.expoagrobrasil.model.Usuario;
import com.expoagro.expoagrobrasil.util.GoogleSignIn;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class VisualizarAnuncianteActivity extends AppCompatActivity {

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_anunciante);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progress = new ProgressDialog(VisualizarAnuncianteActivity.this);
        progress.setMessage("Carregando dados...");
        progress.setCancelable(false);

        if (VisualizarProdutoActivity.getIdAnunciante() != null) {
            String idAnunciante = VisualizarProdutoActivity.getIdAnunciante();
            getAnuncianteInfo(idAnunciante);
        } else if (VisualizarServicoActivity.getIdAnunciante() != null){
            String idAnunciante = VisualizarServicoActivity.getIdAnunciante();
            getAnuncianteInfo(idAnunciante);
        }
        checkForConnection();
    }

    private void getAnuncianteInfo(final String id) {
        progress.show();
        UserDAO.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    if (user.getKey().equals(id)) {
                        final Usuario target = user.getValue(Usuario.class);
                        ((EditText) findViewById(R.id.nomeEditText)).setText(target.getNome());
                        ((EditText) findViewById(R.id.telefoneEditText)).setText(target.getTelefone());
                        ((EditText) findViewById(R.id.emailEditText)).setText(target.getEmail());
                        ((EditText) findViewById(R.id.cidadeEditText)).setText(target.getCidade());
                        progress.dismiss();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        VisualizarProdutoActivity.setIdAnunciante(null);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                VisualizarProdutoActivity.setIdAnunciante(null);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void checkForConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        boolean isConnected =  netInfo != null && netInfo.isConnectedOrConnecting();
        if (!isConnected) {
            Toast.makeText(VisualizarAnuncianteActivity.this, "Você não está conectado a Internet", Toast.LENGTH_SHORT).show();
            progress.dismiss();
            finish();
        }
    }
}
