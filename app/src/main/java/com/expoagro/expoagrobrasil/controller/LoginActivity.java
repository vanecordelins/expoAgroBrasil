package com.expoagro.expoagrobrasil.controller;

import android.app.ProgressDialog;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.text.TextUtils;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.expoagro.expoagrobrasil.R;
import com.expoagro.expoagrobrasil.util.FirebaseLogin;
import com.expoagro.expoagrobrasil.util.GoogleSignIn;
import com.expoagro.expoagrobrasil.util.Regex;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private SignInButton googleButton;
    private static GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.campoEmail);

        mPasswordView = (EditText) findViewById(R.id.campoSenha);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(LoginActivity.this)
                .enableAutoManage(LoginActivity.this /* FragmentActivity */, LoginActivity.this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        googleButton =  (SignInButton) findViewById(R.id.sign_in_button);

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress();
                GoogleSignIn.signIn(mGoogleApiClient, LoginActivity.this); // Google Sign In
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.btnEntrar);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin(); //Aqui chamar o metodo para login *****************
            }
        });

        //Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            // Usuario ja esta logado, nÃ£o ir para a tela de login
            Intent it = new Intent(LoginActivity.this, AnunciosActivity.class);
            startActivity(it);
            finish();
        }

        TextView t2 = (TextView) findViewById(R.id.textoNovoCadastro);
        t2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent telaCadastro = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
                startActivity(telaCadastro);
                finish();
            }
        });

        TextView t3 = (TextView) findViewById(R.id.textoRecuperarSenha);
        t3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(LoginActivity.this, RecuperarSenhaActivity.class);
                startActivity(it);
                finish();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                GoogleSignIn.firebaseAuthWithGoogle(mAuth, this, account, mProgressDialog);

            } else {
                mProgressDialog.dismiss();
                System.out.println("Não foi possível realizar o Login. Tente Novamente");
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reseta os erros.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Armazena os valores no momento da chamada do login.
        String email = mEmailView.getText().toString();
        String senha = mPasswordView.getText().toString();

        boolean cancelar = false;
        View focusView = null;

        // Verifica se o usuÃ¡rio digitou uma.
        if (TextUtils.isEmpty(senha)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancelar = true;
        } else if (!Regex.isPasswordValid(senha)) {
            mPasswordView.setError(getString(R.string.error_senha_invalida));
            focusView = mPasswordView;
            cancelar = true;
        }

        // Verifica se Ã© um email vÃ¡lido.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancelar = true;
        } else if (!Regex.isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_email_invalido));
            focusView = mEmailView;
            cancelar = true;
        }

        if (cancelar) {
            // Existe um erro; nÃ£o Ã© chamado o login
            focusView.requestFocus(); //foco no primeiro campo com um erro
        } else {


            // Mostra um spinner de progresso, and kick off a background task to
            // perform the user login attempt.
            showProgress();

            //authenticate user pelo firebase
            FirebaseLogin.firebaseAuthentication(LoginActivity.this, mAuth, email, senha, mProgressDialog);

        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */

     private void showProgress() {
         if (mProgressDialog == null) {
             mProgressDialog = new ProgressDialog(this);
             mProgressDialog.setCancelable(false);
             mProgressDialog.setMessage("Verificando Dados...");
             mProgressDialog.setIndeterminate(true);
         }
         mProgressDialog.show();
     }

      @Override
      public void onDestroy() {
          super.onDestroy();
          if ( mProgressDialog!=null && mProgressDialog.isShowing() ){
              mProgressDialog.dismiss();
          }
      }

    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        System.out.println("onConnectionFailed:" + connectionResult);
    }

}

