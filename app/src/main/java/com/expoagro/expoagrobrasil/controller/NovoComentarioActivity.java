package com.expoagro.expoagrobrasil.controller;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.expoagro.expoagrobrasil.R;
import com.expoagro.expoagrobrasil.dao.ProdutoDAO;
import com.expoagro.expoagrobrasil.dao.ServicoDAO;
import com.expoagro.expoagrobrasil.dao.UserDAO;
import com.expoagro.expoagrobrasil.model.Comentario;
import com.expoagro.expoagrobrasil.model.Produto;
import com.expoagro.expoagrobrasil.model.Servico;
import com.expoagro.expoagrobrasil.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NovoComentarioActivity extends AppCompatActivity {

    private EditText comentarioText;
    private String key;
    private String nomeUsuario;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_comentario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        comentarioText = (EditText) findViewById(R.id.campoComentario);

        progress = new ProgressDialog(NovoComentarioActivity.this);
        progress.setMessage("Carregando dados...");
        progress.setCancelable(false);
        progress.show();

        if(MenuProdutoActivity.getId() != null) {
            key = MenuProdutoActivity.getId();
            ProdutoDAO.getDatabaseReference().addListenerForSingleValueEvent(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot prod : dataSnapshot.getChildren()) {
                        if (prod.getKey().equals(key)) {
                            final Produto produto = prod.getValue(Produto.class);
                            ((TextView) findViewById(R.id.nomeAnuncio)).setText(produto.getNome().toUpperCase());
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) { System.out.println(databaseError.getMessage()); }
            });
        } else {
            key = MenuServicoActivity.getId();
            ServicoDAO.getDatabaseReference().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot serv : dataSnapshot.getChildren()) {
                        if (serv.getKey().equals(key)) {
                            final Servico servico = serv.getValue(Servico.class);
                            ((TextView) findViewById(R.id.nomeAnuncio)).setText(servico.getNome().toUpperCase());
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) { System.out.println(databaseError.getMessage()); }
            });
        }

        UserDAO.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    if (user.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        Usuario target = user.getValue(Usuario.class);
                        ((TextView) findViewById(R.id.nomeUsuario)).setText(target.getNome() + ": ");
                        nomeUsuario = target.getNome();
                        progress.dismiss();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { System.out.println(databaseError.getMessage()); }
        });

        Button btn_cancelar = (Button) findViewById(R.id.btnCancelar);
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btn_cadastrar = (Button) findViewById(R.id.btnCadastrar);
        btn_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String comentario = comentarioText.getText().toString();

                if(comentario.isEmpty()) {
                    Toast.makeText(NovoComentarioActivity.this, "Não é permitido comentário vazio.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Dialog alertDialog = new AlertDialog.Builder(NovoComentarioActivity.this).setIcon(android.R.drawable.ic_dialog_info).setTitle("Confirmar Comentário")
                        .setMessage("Deseja continuar?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                                String date = df.format(calendar.getTime());

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Comentário");
                                String id = ref.push().getKey();
                                Comentario comment = new Comentario(id, comentario, date, key, nomeUsuario);
                                ref.child(id).setValue(comment);

                                Toast.makeText(NovoComentarioActivity.this, "Comentário realizado con sucesso.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).setNegativeButton("Não", null).show();
                alertDialog.setCanceledOnTouchOutside(true);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
