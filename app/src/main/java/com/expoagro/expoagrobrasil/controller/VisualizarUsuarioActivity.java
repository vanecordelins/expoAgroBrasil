package com.expoagro.expoagrobrasil.controller;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.expoagro.expoagrobrasil.util.FirebaseLogin;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class VisualizarUsuarioActivity extends AppCompatActivity {

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_usuario);

        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Carregando Dados...");
        progress.show();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recuperarUsuario();

        Button btn_alterar_senha = (Button) findViewById(R.id.btnAlterarSenha);

        if (FirebaseAuth.getInstance().getCurrentUser().getProviders().get(0).equals("google.com")) {
            btn_alterar_senha.setVisibility(View.GONE);
        }

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

        UserDAO.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
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
                progress.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
                progress.dismiss();
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
    public void onBackPressed() {
        Intent it = new Intent(VisualizarUsuarioActivity.this, MenuActivity.class);
        startActivity(it);
        finish();
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
            case R.id.acao_excluir:
                Dialog alertDialog = new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_delete).setTitle("EXCLUIR CONTA")
                        .setMessage("Tem certeza que deseja excluir sua conta? Todos os seus dados e anúncios serão excluídos!")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseLogin.deleteAccount(VisualizarUsuarioActivity.this);
                            }
                        })
                        .setNegativeButton("Não", null).show();
                alertDialog.setCanceledOnTouchOutside(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
