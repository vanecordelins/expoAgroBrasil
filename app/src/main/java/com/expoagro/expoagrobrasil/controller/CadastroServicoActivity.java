package com.expoagro.expoagrobrasil.controller;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
import com.expoagro.expoagrobrasil.dao.ServicoDAO;
import com.expoagro.expoagrobrasil.dao.UserDAO;
import com.expoagro.expoagrobrasil.model.Servico;
import com.expoagro.expoagrobrasil.model.Usuario;
import com.expoagro.expoagrobrasil.util.MoneyTextWatcher;
import com.expoagro.expoagrobrasil.util.Regex;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CadastroServicoActivity extends AppCompatActivity {

    private AutoCompleteTextView mNomeView;
    private EditText mValorView;
    private Spinner spinnerFrequencia;
    private TextView mDescricaoView;
    private TextView mObservacaoView;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_cadastro_servico);

        mNomeView = (AutoCompleteTextView) findViewById(R.id.campoNomeServico);
        mValorView = (EditText) findViewById(R.id.campoValor);
        mDescricaoView = (TextView) findViewById(R.id.campoDescricao);
        mObservacaoView = (TextView) findViewById(R.id.campoObservacao);

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setMessage("Cadastrando Novo Serviço...");
        dialog.setCancelable(false);

        mValorView.addTextChangedListener(new MoneyTextWatcher(mValorView));

        // Cria um ArrayAdapter usando um array de string e um layout default do spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.frequencias, android.R.layout.simple_spinner_item); //simple_spinner_dropdown_item
        // Especifica o layout que será usado quando a lista de opções aparecer
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFrequencia = (Spinner) findViewById(R.id.spinnerFrequencia);
        // Aplica o adapter ao spinner
        spinnerFrequencia.setAdapter(adapter);

        /*RadioButton rdoBtnProduto = (RadioButton) findViewById(R.id.rdoBtnProduto);
        rdoBtnProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent telaCadastrarProduto = new Intent(CadastroServicoActivity.this, CadastroProdutoActivity.class);
                startActivity(telaCadastrarProduto);
                finish();
            }
        });*/

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
                Intent telaMenu = new Intent(CadastroServicoActivity.this, MenuProdutoActivity.class);
                startActivity(telaMenu);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent telaMenu = new Intent(CadastroServicoActivity.this, MenuProdutoActivity.class);
        startActivity(telaMenu);
        finish();
    }

    public void cadastrar() {

        final String nome = mNomeView.getText().toString();
        final String valor = mValorView.getText().toString();
        final String frequencia = spinnerFrequencia.getSelectedItem().toString();
        final String descricao = mDescricaoView.getText().toString();
        final String observacao = mObservacaoView.getText().toString();

        boolean cancelar = validateInfo(nome, valor, frequencia);

        if (cancelar) {

            return;
        } else {
            dialog.show();
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            final String date = df.format(calendar.getTime());
            final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm");
            final String time = dfTime.format(Calendar.getInstance().getTime());

            Dialog alertDialog = new AlertDialog.Builder(CadastroServicoActivity.this).setIcon(android.R.drawable.ic_dialog_info).setTitle("Confirmar Cadastro")
                    .setMessage("Deseja continuar? Verifique se todos os dados estão corretos. ")
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            Thread mThread = new Thread() {
                                @Override
                                public void run() {
                                    UserDAO.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot user : dataSnapshot.getChildren()) {
                                                if (user.getKey().equals(uid)) {
                                                    Usuario target = user.getValue(Usuario.class);
                                                    Servico servico = new Servico(nome, observacao, descricao, date, time, target.getCidade(), valor, uid, frequencia);
                                                    ServicoDAO servicoDAO = new ServicoDAO();
                                                    servicoDAO.save(servico);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            System.out.println("The read failed: " + databaseError.getMessage());
                                        }
                                    });
                                }
                            };
                            mThread.start();
                            onSucessoCadastro();

                        }
                    })
                    .setNegativeButton("Não", null).show();
            alertDialog.setCanceledOnTouchOutside(true);
            dialog.dismiss();
        }

    }

    public void onSucessoCadastro() {
        Toast.makeText(CadastroServicoActivity.this, R.string.msg_cadastro_sucesso, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
        Intent it = new Intent(CadastroServicoActivity.this, MenuProdutoActivity.class);
        startActivity(it);
        finish();
    }

    public boolean validateInfo(String nome, String valor, String frequencia) {

        // Reseta os erros.
        mNomeView.setError(null);
        mValorView.setError(null);

        boolean cancelar = false;
        View focusView = null;

        if (TextUtils.isEmpty(nome)) {
            mNomeView.setError(getString(R.string.error_field_required));
            focusView = mNomeView;
            cancelar = true;
        } else if (!Regex.isNameValid(nome)) {
            mNomeView.setError(getString(R.string.error_nome_invalido));
            focusView = mNomeView;
            cancelar = true;
        } else if (TextUtils.isEmpty(valor)) {
            mValorView.setError(getString(R.string.error_field_required));
            focusView = mValorView;
            cancelar = true;
        } else if ("Selecione a Frequência...".equals(frequencia)) {
            Toast.makeText(CadastroServicoActivity.this, R.string.error_frequencia_nao_selecionada, Toast.LENGTH_SHORT).show();
            cancelar = true;
        }

        if (cancelar) {
            if (focusView != null) {
                focusView.requestFocus();
            }
            return true;
        }
        return false;
    }


}
