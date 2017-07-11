package com.expoagro.expoagrobrasil.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.expoagro.expoagrobrasil.R;
import com.expoagro.expoagrobrasil.dao.UserDAO;
import com.expoagro.expoagrobrasil.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class VisualizarUsuarioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_usuario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recuperarUsuario();

        Button btn_alterar_senha = (Button) findViewById(R.id.btnAlterarSenha);
        btn_alterar_senha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(VisualizarUsuarioActivity.this, AlterarSenhaActivity.class);
                startActivity(it);
                finish();
            }
        });

    }

    public void recuperarUsuario() {

        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        UserDAO.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot user : dataSnapshot.getChildren()) {
                    if(user.getKey().equals(uid)){
                        Usuario target = user.getValue(Usuario.class);
                        ((TextView) findViewById(R.id.nome)).setText(target.getNome());
                        ((TextView) findViewById(R.id.menu_email)).setText(target.getEmail());
                        ((TextView) findViewById(R.id.telefone)).setText(target.getTelefone());
                        ((TextView) findViewById(R.id.cidade)).setText(target.getCidade());

                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            // Id correspondente ao botão Up/Home da actionbar
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
            case R.id.acao_editar:
                Intent itAlt = new Intent(VisualizarUsuarioActivity.this, AlterarUsuarioActivity.class);
                startActivity(itAlt);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

}
