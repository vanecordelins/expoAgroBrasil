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
import android.widget.Spinner;
import android.widget.Toast;

import com.expoagro.expoagrobrasil.R;
import com.expoagro.expoagrobrasil.dao.UserDAO;
import com.expoagro.expoagrobrasil.model.Usuario;
import com.expoagro.expoagrobrasil.util.GoogleSignIn;
import com.expoagro.expoagrobrasil.util.PhoneEditText;
import com.expoagro.expoagrobrasil.util.Regex;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class CompletarCadastroActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private AutoCompleteTextView mNomeView;
    private AutoCompleteTextView mEmailView;
    private PhoneEditText mTelefoneView;
    private Button mCancelarButton;
    private Button mCadastrarButton;
    private Spinner spinner;
    private ProgressDialog progress;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completar_cadastro);

        mNomeView = (AutoCompleteTextView) findViewById(R.id.campoNome);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.campoEmail);
        mTelefoneView = (PhoneEditText) findViewById(R.id.campoTelefone);

        progress = new ProgressDialog(this);
        progress.setMessage("Carregando Dados...");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(CompletarCadastroActivity.this)
                .enableAutoManage(CompletarCadastroActivity.this /* FragmentActivity */, CompletarCadastroActivity.this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Cria um ArrayAdapter usando um array de string e um layout default do spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.cidades_PE, android.R.layout.simple_spinner_item); //simple_spinner_dropdown_item
        // Especifica o layout que será usado quando a lista de opções aparecer
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner = (Spinner) findViewById(R.id.spinner);
        // Aplica o adapter ao spinner
        spinner.setAdapter(adapter);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider
                String uid = profile.getUid();

                mNomeView.setText(profile.getDisplayName());
                mEmailView.setText(profile.getEmail());

                break;
            }
            progress.hide();
        }

        mCadastrarButton = (Button) findViewById(R.id.btn_confirm);
        mCadastrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmar();
            }
        });

        mCancelarButton = (Button) findViewById(R.id.btn_cancel);
        mCancelarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignIn.signOut(CompletarCadastroActivity.this, mGoogleApiClient);
                Intent telaLogin = new Intent(CompletarCadastroActivity.this, LoginActivity.class);
                startActivity(telaLogin);
                finish();
            }
        });
    }

    private void confirmar() {
        progress.show();
        mTelefoneView.setError(null);

        // Armazena os valores no momento da chamada do cadastro.
        String nome = mNomeView.getText().toString();
        String email = mEmailView.getText().toString();
        String telefone = mTelefoneView.getText().toString();
        String cidade = spinner.getSelectedItem().toString();

        boolean cancelar = false;
        View focusView = null;

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
            Toast.makeText(CompletarCadastroActivity.this, R.string.error_cidade_nao_selecionada, Toast.LENGTH_LONG).show();
            cancelar = true;
        }

        if (cancelar) {
            focusView.requestFocus();
            progress.hide();
        } else {
            final Usuario usuario = new Usuario();
            usuario.setId(FirebaseAuth.getInstance().getCurrentUser().getUid());
            usuario.setEmail(email);
            usuario.setNome(nome);
            usuario.setCidade(cidade);
            usuario.setTelefone(telefone);

            FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                System.out.println("E-mail Verification Sent.");
                                UserDAO userDAO = new UserDAO();
                                userDAO.save(usuario);
                                progress.hide();

                                Toast.makeText(CompletarCadastroActivity.this, R.string.msg_cadastro_sucesso, Toast.LENGTH_SHORT).show();

                                Intent it = new Intent(CompletarCadastroActivity.this, AnunciosActivity.class);
                                startActivity(it);
                                finish();
                            } else {
                                Toast.makeText(CompletarCadastroActivity.this, "E-mail inválido", Toast.LENGTH_SHORT).show();
                                progress.hide();
                            }
                        }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Sem Conexão com a Internet.", Toast.LENGTH_SHORT).show();
    }
}
