package com.expoagro.expoagrobrasil.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.expoagro.expoagrobrasil.R;
import com.expoagro.expoagrobrasil.util.Regex;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AlterarSenhaActivity extends AppCompatActivity {

    private TextView mSenhaAtual;
    private TextView mNovaSenha;
    private TextView mConfirmaNovaSenha;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_senha);

        mSenhaAtual = (EditText) findViewById(R.id.campoSenha);
        mNovaSenha = (EditText) findViewById(R.id.campoNovaSenha);
        mConfirmaNovaSenha = (EditText) findViewById(R.id.campoConfirmaNovaSenha);
        Button btn_confirm = (Button) findViewById(R.id.btn_confirm);
        Button btn_cancel = (Button) findViewById(R.id.btn_cancelar);

        progress = new ProgressDialog(this);
        progress.setCancelable(false);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirma();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(AlterarSenhaActivity.this, AnunciosActivity.class);
                startActivity(it);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(AlterarSenhaActivity.this, AnunciosActivity.class);
        startActivity(it);
        finish();
    }

    public void confirma() {
        progress.setMessage("Atualizando Dados...");
        progress.setIndeterminate(true);
        progress.show();

        mNovaSenha.setError(null);
        mSenhaAtual.setError(null);
        mConfirmaNovaSenha.setError(null);

        String senhaAtual = mSenhaAtual.getText().toString();
        final String novaSenha = mNovaSenha.getText().toString();
        String confirmaNovaSenha = mConfirmaNovaSenha.getText().toString();

        boolean cancelar = false;
        View focusView = null;

        if ( TextUtils.isEmpty(senhaAtual) ) {
            mSenhaAtual.setError(getString(R.string.error_field_required));
            focusView = mSenhaAtual;
            cancelar = true;
        } else if (!Regex.isPasswordValid(senhaAtual)) {
            mSenhaAtual.setError(getString(R.string.error_senha_invalida));
            focusView = mSenhaAtual;
            cancelar = true;
        }

        if ( TextUtils.isEmpty(novaSenha) ) {
            mNovaSenha.setError(getString(R.string.error_field_required));
            focusView = mNovaSenha;
            cancelar = true;
        } else if (!Regex.isPasswordValid(novaSenha)) {
            mNovaSenha.setError(getString(R.string.error_senha_invalida));
            focusView = mNovaSenha;
            cancelar = true;
        }

        if ( TextUtils.isEmpty(confirmaNovaSenha) ) {
            mConfirmaNovaSenha.setError(getString(R.string.error_field_required));
            focusView = mConfirmaNovaSenha;
            cancelar = true;
        } else if (!Regex.isPasswordValid(confirmaNovaSenha)) {
            mConfirmaNovaSenha.setError(getString(R.string.error_senha_invalida));
            focusView = mConfirmaNovaSenha;
            cancelar = true;
        } else if ( !novaSenha.equals(confirmaNovaSenha) ) {
            mConfirmaNovaSenha.setError(getString(R.string.error_repetir_senha_invalida));
            focusView = mConfirmaNovaSenha;
            cancelar = true;
        }

        if (cancelar) {
            if (focusView != null) {
                focusView.requestFocus();
            }
            progress.dismiss();
        } else {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseAuth.getInstance().signInWithEmailAndPassword(user.getEmail(), senhaAtual)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                // there was an error
                                Toast.makeText(AlterarSenhaActivity.this, "Senha Atual Incorreta.", Toast.LENGTH_SHORT).show();
                                progress.dismiss();
                            } else {
                                user.updatePassword(novaSenha).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(AlterarSenhaActivity.this, "Senha atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                                            progress.dismiss();
                                            Intent it = new Intent(AlterarSenhaActivity.this, AnunciosActivity.class);
                                            startActivity(it);
                                            finish();
                                        } else {
                                            Toast.makeText(AlterarSenhaActivity.this, "Falha ao atualizar senha", Toast.LENGTH_SHORT).show();
                                            progress.dismiss();
                                        }
                                    }
                                });
                            }
                        }
                    });
        }

    }
}
