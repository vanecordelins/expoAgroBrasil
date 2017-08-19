package com.expoagro.expoagrobrasil.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.expoagro.expoagrobrasil.R;
import com.expoagro.expoagrobrasil.dao.UserDAO;
import com.expoagro.expoagrobrasil.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class VisualizarAnuncianteActivity extends AppCompatActivity {
        RatingBar ratingBar;
        RatingBar ratingBar2;
        TextView avaliacaoText;
        String avaliacao;;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_anunciante);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (VisualizarProdutoActivity.getIdAnunciante() != null) {
            String idAnunciante = VisualizarProdutoActivity.getIdAnunciante();
            getAnuncianteInfo(idAnunciante);
        } else if (VisualizarServicoActivity.getIdAnunciante() != null){
            String idAnunciante = VisualizarServicoActivity.getIdAnunciante();
            getAnuncianteInfo(idAnunciante);
        }
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar2 = (RatingBar) findViewById(R.id.ratingBar2);
        avaliacaoText = (TextView) findViewById(R.id.avaliacaoText);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){

            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                Toast.makeText(VisualizarAnuncianteActivity.this,"" + v,Toast.LENGTH_SHORT).show();
                ratingBar2.setRating(v/2);
                    
            }
        });
    }

    private void getAnuncianteInfo(final String id) {
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
}
