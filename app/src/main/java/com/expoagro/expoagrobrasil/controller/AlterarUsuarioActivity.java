package com.expoagro.expoagrobrasil.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.expoagro.expoagrobrasil.R;
import com.expoagro.expoagrobrasil.dao.UserDAO;
import com.expoagro.expoagrobrasil.model.Usuario;
import com.expoagro.expoagrobrasil.util.PhoneEditText;
import com.expoagro.expoagrobrasil.util.Regex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class AlterarUsuarioActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private Spinner spinner;
    private ProgressDialog progress;
    private AutoCompleteTextView mNomeView;
    private PhoneEditText mTelefoneView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Recuperando Dados");
        progress.show();

        setContentView(R.layout.activity_alterar_usuario);

        mNomeView = (AutoCompleteTextView) findViewById(R.id.campoNome);
        mTelefoneView = (PhoneEditText) findViewById(R.id.campoTelefone);


        // Cria um ArrayAdapter usando um array de string e um layout default do spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.cidades_PE, android.R.layout.simple_spinner_item); //simple_spinner_dropdown_item
        // Especifica o layout que será usado quando a lista de opções aparecer
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner = (Spinner) findViewById(R.id.spinner);
        // Aplica o adapter ao spinner
        spinner.setAdapter(adapter);

        fillForm();

        Button mAtualizarButton = (Button) findViewById(R.id.btn_confirm);
        mAtualizarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.setMessage("Atualizando Dados...");
                progress.show();
                alterar(); //Aqui chamar o método para alterar usuario *****************
            }
        });

        Button mCancelarButton = (Button) findViewById(R.id.btn_cancel);
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
                            if (target.getSenha() == null) {
                                findViewById(R.id.campoNome).setEnabled(false);
                            }
                            ((EditText) findViewById(R.id.campoTelefone)).setText(target.getTelefone());
                            for (int i = 0; i < spinner.getCount(); i++) {
                                if (spinner.getItemAtPosition(i).toString().equals(target.getCidade())) {
                                    spinner.setSelection(i);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    progress.dismiss();
                }
            });
    }
    private void alterar() {
        // Armazena os valores no momento da chamada do cadastro.
        final String nome = mNomeView.getText().toString();
        final String telefone = mTelefoneView.getText().toString();
        final String cidade = spinner.getSelectedItem().toString();

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

        if ( TextUtils.isEmpty(telefone) ) {
            mTelefoneView.setError(getString(R.string.error_field_required));
            focusView = mTelefoneView;
            cancelar = true;
        } else if(!Regex.isTelephoneValid(telefone)) {
            mTelefoneView.setError(getString(R.string.error_telefone_invalido));
            focusView = mTelefoneView;
            cancelar = true;
        }

        if ( "Selecione...".equals(cidade) ) {
            Toast.makeText(AlterarUsuarioActivity.this, R.string.error_cidade_nao_selecionada, Toast.LENGTH_SHORT).show();
            cancelar = true;
        }

        if (cancelar) {
            if(focusView != null) {
                focusView.requestFocus();
            }
            progress.dismiss();
        } else {
            final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            UserDAO.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getMessage());
                }

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot user : dataSnapshot.getChildren()) {
                        if (user.getKey().equals(uid)) {
                            Usuario target = user.getValue(Usuario.class);
                            target.setCidade(cidade);
                            target.setTelefone(telefone);
                            target.setNome(nome);

                            UserDAO userDAO = new UserDAO();
                            userDAO.update(target);
                            Toast.makeText(AlterarUsuarioActivity.this, "Perfil atualizado com sucesso.", Toast.LENGTH_SHORT).show();
                            progress.dismiss();

                            Intent it = new Intent(AlterarUsuarioActivity.this, AnunciosActivity.class);
                            startActivity(it);
                            finish();
                            break;
                        }
                    }
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(AlterarUsuarioActivity.this, AnunciosActivity.class);
        startActivity(it);
        finish();
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        System.out.println("onConnectionFailed:" + connectionResult);
    }

}
