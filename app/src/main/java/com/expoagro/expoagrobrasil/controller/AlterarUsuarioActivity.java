package com.expoagro.expoagrobrasil.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.expoagro.expoagrobrasil.R;
import com.expoagro.expoagrobrasil.dao.UserDAO;
import com.expoagro.expoagrobrasil.model.Usuario;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class AlterarUsuarioActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private Spinner spinner;
    private ProgressDialog progress;
    private Button mCancelarButton;
    private Button mCadastrarButton;
    private AutoCompleteTextView mNomeView;
    private AutoCompleteTextView mEmailView;
    private AutoCompleteTextView mTelefoneView;
    private TextView mSenhaView;
    private TextView mRepetirSenhaView;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progress = new ProgressDialog(this);
        progress.setMessage("Recuperando Dados");
        progress.show();

        setContentView(R.layout.activity_alterar_usuario);

        mNomeView = (AutoCompleteTextView) findViewById(R.id.campoNome);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.campoEmail);
        mTelefoneView = (AutoCompleteTextView) findViewById(R.id.campoTelefone);
        mSenhaView = (EditText) findViewById(R.id.campoSenha);
        mRepetirSenhaView = (EditText) findViewById(R.id.campoRepetir);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(AlterarUsuarioActivity.this)
                .enableAutoManage(AlterarUsuarioActivity.this /* FragmentActivity */, AlterarUsuarioActivity.this /* OnConnectionFailedListener */)
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

        fillForm();

        mCadastrarButton = (Button) findViewById(R.id.btn_confirm);
        mCadastrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alterar(); //Aqui chamar o método para alterar usuario *****************
            }
        });

        mCancelarButton = (Button) findViewById(R.id.btn_cancel);
        mCancelarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent telaLogin = new Intent(AlterarUsuarioActivity.this, LoginActivity.class);
                startActivity(telaLogin);
                finish();
            }
        });

    }

    private void fillForm() {

        if (mGoogleApiClient.isConnected()) {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (user != null) {
                for (UserInfo profile : user.getProviderData()) {
                    // Id of the provider (ex: google.com)
                    String providerId = profile.getProviderId();

                    // UID specific to the provider
                    String uid = profile.getUid();

                    break;
                }
            } else {
                System.out.println("O usuário não está logado.");
            }
        } else {
            final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            UserDAO.getReference().addValueEventListener(new ValueEventListener() {
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getMessage());
                }

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot user : dataSnapshot.getChildren()) {
                        if (user.getKey().equals(uid)) {
                            Usuario target = user.getValue(Usuario.class);
                            ((EditText) findViewById(R.id.campoEmail)).setText(target.getEmail());
                            ((EditText) findViewById(R.id.campoNome)).setText(target.getNome());
                            ((EditText) findViewById(R.id.campoTelefone)).setText(target.getTelefone());
                            for (int i = 0; i < spinner.getCount(); i++) {
                                if (spinner.getItemAtPosition(i).toString().equals(target.getCidade())) {
                                    spinner.setSelection(i);
                                    break;
                                }
                            }
                            progress.hide();
                            break;
                        }
                    }
                }
            });
        }
    }
    private void alterar() {
        // Armazena os valores no momento da chamada do cadastro.
        String nome = mNomeView.getText().toString();
        String email = mEmailView.getText().toString();
        String telefone = mTelefoneView.getText().toString();
        String cidade = spinner.getSelectedItem().toString();

        String senha = mSenhaView.getText().toString();
        String repetirSenha = mRepetirSenhaView.getText().toString();

    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        System.out.println("onConnectionFailed:" + connectionResult);
    }

}
