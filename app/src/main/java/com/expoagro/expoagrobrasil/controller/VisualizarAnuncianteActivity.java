package com.expoagro.expoagrobrasil.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.expoagro.expoagrobrasil.R;
import com.expoagro.expoagrobrasil.dao.UserDAO;
import com.expoagro.expoagrobrasil.model.Avaliacao;
import com.expoagro.expoagrobrasil.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VisualizarAnuncianteActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private RatingBar ratingBar2;
    private TextView avaliacaoText;
    private String idAnunciante;
    private String ui;


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

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar2 = (RatingBar) findViewById(R.id.ratingBar2);
        avaliacaoText = (TextView) findViewById(R.id.avaliacaoText);

        ratingBar2.setActivated(false);

        if (VisualizarProdutoActivity.getIdAnunciante() != null) {
            idAnunciante = VisualizarProdutoActivity.getIdAnunciante();
            getAnuncianteInfo(idAnunciante);
        } else if (VisualizarServicoActivity.getIdAnunciante() != null){
            idAnunciante = VisualizarServicoActivity.getIdAnunciante();
            getAnuncianteInfo(idAnunciante);
        }
        mediaAvalicaco();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
             ui = FirebaseAuth.getInstance().getCurrentUser().getUid();
        } else {
            ratingBar.setActivated(false);
            ratingBar.setEnabled(false);
        }

        FirebaseDatabase.getInstance().getReference("Avaliacao").child(idAnunciante).addValueEventListener(new ValueEventListener() {
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() == null || ratingBar2.getNumStars() != 0) {

                    ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

                        @Override
                        public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                            Avaliacao avaliacao = new Avaliacao(ui, v, idAnunciante);
                            FirebaseDatabase.getInstance().getReference("Avaliacao").child(idAnunciante).child(ui).setValue(avaliacao);
                            ratingBar.setRating(v);

                            mediaAvalicaco();
                        }
                    });
                }
            }
        });

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
    public void mediaAvalicaco() {
        FirebaseDatabase.getInstance().getReference("Avaliacao").child(idAnunciante).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }

            public void onDataChange(DataSnapshot dataSnapshot) {

                float ava = 0;
                ArrayList<Float> listAvaliacao = new ArrayList();
                for (DataSnapshot avaliacao : dataSnapshot.getChildren()) {
                    Avaliacao a = avaliacao.getValue(Avaliacao.class);
                    listAvaliacao.add(a.getAvaliacao());
                }
                for (int i = 0; i < listAvaliacao.size(); i++) {
                    ava = listAvaliacao.get(i) + ava;
                }
                float ava1 = ava / listAvaliacao.size();
                ratingBar2 = (RatingBar) findViewById(R.id.ratingBar2);
                ratingBar2.setRating(ava1);
                avaliacaoText.setText("" + ratingBar2.getRating());
                progress.dismiss();
            }

        });
    }
}
