package com.expoagro.expoagrobrasil.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.expoagro.expoagrobrasil.R;
import com.expoagro.expoagrobrasil.dao.UserDAO;
import com.expoagro.expoagrobrasil.model.Usuario;
import com.expoagro.expoagrobrasil.util.PhoneEditText;
import com.expoagro.expoagrobrasil.util.Regex;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private Spinner spinner;
    private AutoCompleteTextView mNomeView;
    private AutoCompleteTextView mEmailView;
    private PhoneEditText mTelefoneView;
    private TextView mSenhaView;
    private TextView mRepetirSenhaView;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_cadastro_usuario);

        mNomeView = (AutoCompleteTextView) findViewById(R.id.campoNome);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.campoEmail);
        mTelefoneView = (PhoneEditText) findViewById(R.id.campoTelefone);
        mSenhaView = (EditText) findViewById(R.id.campoSenha);
        mRepetirSenhaView = (EditText) findViewById(R.id.campoRepetir);

        progress = new ProgressDialog(this);
        progress.setCancelable(false);

        // Cria um ArrayAdapter usando um array de string e um layout default do spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.cidades_PE, android.R.layout.simple_spinner_item); //simple_spinner_dropdown_item
        // Especifica o layout que será usado quando a lista de opções aparecer
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner = (Spinner) findViewById(R.id.spinner);
        // Aplica o adapter ao spinner
        spinner.setAdapter(adapter);

        Button mCadastrarButton = (Button) findViewById(R.id.btnCadastrar);
        mCadastrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrar(); //Aqui chamar o método para cadastrar *****************
            }
        });

        Button mCancelarButton = (Button) findViewById(R.id.btnCancelar);
        mCancelarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent telaLogin = new Intent(CadastroUsuarioActivity.this, LoginActivity.class);
                startActivity(telaLogin);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void cadastrar(){

        // Reseta os erros.
        mNomeView.setError(null);
        mEmailView.setError(null);
        mTelefoneView.setError(null);
        mSenhaView.setError(null);
        mRepetirSenhaView.setError(null);

        // Armazena os valores no momento da chamada do cadastro.
        String nome = mNomeView.getText().toString();
        String email = mEmailView.getText().toString();
        String telefone = mTelefoneView.getText().toString();
        String cidade = spinner.getSelectedItem().toString();
        String senha = mSenhaView.getText().toString();
        String repetirSenha = mRepetirSenhaView.getText().toString();

        boolean cancelar = false;
        View focusView = null;

        if ( TextUtils.isEmpty(nome) ) {
            mNomeView.setError(getString(R.string.error_field_required));
            focusView = mNomeView;
            cancelar = true;
        } else if (!Regex.isNameValid(nome)) {
            mNomeView.setError(getString(R.string.error_nome_invalido));
            focusView = mNomeView;
            cancelar = true;
        }

        if ( TextUtils.isEmpty(email) ) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancelar = true;
        } else if (!Regex.isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_email_invalido));
            focusView = mEmailView;
            cancelar = true;
        }

        if ( TextUtils.isEmpty(telefone) ) {
            mTelefoneView.setError(getString(R.string.error_field_required));
            focusView = mTelefoneView;
            cancelar = true;
        } else if(!Regex.isTelephoneValid(telefone)) {
            mTelefoneView.setError(getString(R.string.error_telefone_invalido));
            focusView = mTelefoneView;
            cancelar = true;
        }

        if ( cidade.equals("Selecione...") ) {
            Toast.makeText(CadastroUsuarioActivity.this, R.string.error_cidade_nao_selecionada, Toast.LENGTH_SHORT).show();
            cancelar = true;
        }

        if ( TextUtils.isEmpty(senha) ) {
            mSenhaView.setError(getString(R.string.error_field_required));
            focusView = mSenhaView;
            cancelar = true;
        } else if (!Regex.isPasswordValid(senha)) {
            mSenhaView.setError(getString(R.string.error_senha_invalida));
            focusView = mSenhaView;
            cancelar = true;
        }

        if ( TextUtils.isEmpty(repetirSenha) ) {
            mRepetirSenhaView.setError(getString(R.string.error_field_required));
            focusView = mRepetirSenhaView;
            cancelar = true;
        } else if ( !senha.equals(repetirSenha) ) {
            mRepetirSenhaView.setError(getString(R.string.error_repetir_senha_invalida));
            focusView = mRepetirSenhaView;
            cancelar = true;
        }

        if (cancelar) {
            if (focusView != null) {
                focusView.requestFocus();
            }
        } else {
            progress.setMessage("Cadastrando Dados");
            progress.show();
            final Usuario usuario = new Usuario();
            usuario.setNome(nome);
            usuario.setEmail(email);
            usuario.setTelefone(telefone);
            usuario.setCidade(cidade);
            usuario.setSenha(senha);

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            System.out.println("createUserWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    Toast.makeText(CadastroUsuarioActivity.this, "E-mail já cadastrado.", Toast.LENGTH_SHORT).show();
                                    progress.dismiss();
                                } else {
                                    Toast.makeText(CadastroUsuarioActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    System.out.println(task.getException().getMessage());
                                    progress.dismiss();
                                }
                            } else {
                                System.out.println("Authentication sucessul.");

                                usuario.setId(task.getResult().getUser().getUid());

                                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    System.out.println("E-mail Verification Sent.");
                                                    UserDAO userDAO = new UserDAO();
                                                    userDAO.save(usuario);
                                                    progress.dismiss();
                                                    Toast.makeText(CadastroUsuarioActivity.this, R.string.msg_cadastro_sucesso, Toast.LENGTH_SHORT).show();
                                                    FirebaseAuth.getInstance().signOut();

                                                    Intent it = new Intent(CadastroUsuarioActivity.this, LoginActivity.class);
                                                    startActivity(it);
                                                    finish();
                                                } else {
                                                    Toast.makeText(CadastroUsuarioActivity.this, "E-mail inválido", Toast.LENGTH_SHORT);
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
