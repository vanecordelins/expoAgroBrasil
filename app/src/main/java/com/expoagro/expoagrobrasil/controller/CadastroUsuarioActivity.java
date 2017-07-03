package com.expoagro.expoagrobrasil.controller;

import android.content.Intent;
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
import com.expoagro.expoagrobrasil.dao.FirebaseLogin;
import com.expoagro.expoagrobrasil.dao.UserDAO;
import com.expoagro.expoagrobrasil.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private Spinner spinner;
    private AutoCompleteTextView mNomeView;
    private AutoCompleteTextView mEmailView;
    private AutoCompleteTextView mTelefoneView;
    private TextView mSenhaView;
    private TextView mRepetirSenhaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_cadastro_usuario);

        mNomeView = (AutoCompleteTextView) findViewById(R.id.campoNome);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.campoEmail);
        mTelefoneView = (AutoCompleteTextView) findViewById(R.id.campoTelefone);
        mSenhaView = (EditText) findViewById(R.id.campoSenha);
        mRepetirSenhaView = (EditText) findViewById(R.id.campoRepetir);

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
                finish();
            }
        });
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
        }

        if ( TextUtils.isEmpty(email) ) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancelar = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_email_invalido));
            focusView = mEmailView;
            cancelar = true;
        }

        if ( TextUtils.isEmpty(telefone) ) {
            mTelefoneView.setError(getString(R.string.error_field_required));
            focusView = mTelefoneView;
            cancelar = true;
        }

        if ( cidade.equals("Selecione uma cidade...") ) {
            Toast.makeText(CadastroUsuarioActivity.this, R.string.error_cidade_nao_selecionada, Toast.LENGTH_LONG).show();
            cancelar = true;
        }

        if ( TextUtils.isEmpty(senha) ) {
            mSenhaView.setError(getString(R.string.error_field_required));
            focusView = mSenhaView;
            cancelar = true;
        } else if (!isPasswordValid(senha)) {
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
            focusView.requestFocus();
        } else {
            Usuario usuario = new Usuario();
            usuario.setNome(nome);
            usuario.setEmail(email);
            usuario.setTelefone(telefone);
            usuario.setCidade(cidade);
            usuario.setSenha(senha);

            FirebaseLogin.createFirebaseUser(CadastroUsuarioActivity.this, FirebaseAuth.getInstance(), usuario);

            UserDAO userDAO = new UserDAO();
            userDAO.save(usuario);

            Toast.makeText(CadastroUsuarioActivity.this, R.string.msg_cadastro_sucesso, Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6 && password.length() <= 15;
    }
}
