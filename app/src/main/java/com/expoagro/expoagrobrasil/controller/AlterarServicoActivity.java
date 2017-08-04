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
import com.expoagro.expoagrobrasil.model.Servico;
import com.expoagro.expoagrobrasil.util.MoneyTextWatcher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class AlterarServicoActivity extends AppCompatActivity {

    private AutoCompleteTextView mNomeView;
    private EditText mValorView;
    private Spinner spinnerFrequencia;
    private TextView mDescricaoView;
    private TextView mObservacaoView;
    private String keyServico;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_servico);

        mNomeView = (AutoCompleteTextView) findViewById(R.id.campoNomeServico);
        mValorView = (EditText) findViewById(R.id.campoValor);
        mDescricaoView = (TextView) findViewById(R.id.campoDescricao);
        mObservacaoView = (TextView) findViewById(R.id.campoObservacao);

        mValorView.addTextChangedListener(new MoneyTextWatcher(mValorView));

        keyServico = VisualizarMeusServicosActivity.getId();

        progress = new ProgressDialog(AlterarServicoActivity.this);
        progress = new ProgressDialog(this);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.setMessage("Carregando dados...");
        progress.show();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.frequencias, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFrequencia = (Spinner) findViewById(R.id.spinnerFrequencia);
        spinnerFrequencia.setAdapter(adapter);

        carregarServico();

        Button mAlterarButton = (Button) findViewById(R.id.btnCadastrar);
        mAlterarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alterar();
            }
        });

        Button mCancelarButton = (Button) findViewById(R.id.btnCancelar);
        mCancelarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AlterarServicoActivity.this, VisualizarMeuServicoClicadoActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void carregarServico() {
        ServicoDAO.getDatabaseReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot serv: dataSnapshot.getChildren()) {
                    if(serv.getKey().equals(keyServico)) {
                        Servico servico = serv.getValue(Servico.class);

                        ((EditText) findViewById(R.id.campoDescricao)).setText(servico.getDescricao());
                        ((EditText) findViewById(R.id.campoNomeServico)).setText(servico.getNome());
                        ((EditText) findViewById(R.id.campoObservacao)).setText(servico.getObservacao());
                        ((EditText) findViewById(R.id.campoValor)).setText(servico.getValor());

                        mValorView.addTextChangedListener(new MoneyTextWatcher(mValorView));

                        Spinner spinner = ((Spinner) findViewById(R.id.spinnerFrequencia));
                        for (int i = 0; i < spinner.getCount(); i++) {
                            if (spinner.getItemAtPosition(i).toString().equals(servico.getFrequencia())) {
                                spinner.setSelection(i);
                                break;
                            }
                        }

                    }
                }
                progress.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void alterar() {
        progress.setMessage("Atualizando dados...");
        final String nome = mNomeView.getText().toString();
        final String valor = mValorView.getText().toString();
        final String frequencia = spinnerFrequencia.getSelectedItem().toString();
        final String descricao = mDescricaoView.getText().toString();
        final String observacao = mObservacaoView.getText().toString();

        boolean cancelar = validateInfo(nome, valor, frequencia);

        if (cancelar) {
            return;
        } else {
            Dialog alertDialog = new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_info).setTitle("Alterar Serviço")
                    .setMessage("Deseja continuar? Verifique se todos os dados estão corretos.")
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                           progress.show();
                           alterarServico(nome, valor, frequencia, descricao, observacao);
                        }
                    }).setNegativeButton("Não", null).show();
            alertDialog.setCanceledOnTouchOutside(true);
        }
    }

    private void alterarServico(final String nome, final String valor, final String frequencia, final String descricao, final String observacao) {
        final ServicoDAO servDAO = new ServicoDAO();

        ServicoDAO.getDatabaseReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot serv: dataSnapshot.getChildren()) {
                    if (serv.getKey().equals(keyServico)) {
                        Servico target = serv.getValue(Servico.class);
                        target.setNome(nome);
                        target.setFrequencia(frequencia);
                        target.setDescricao(descricao);
                        target.setObservacao(observacao);
                        target.setValor(valor);

                        servDAO.update(target);
                        Toast.makeText(AlterarServicoActivity.this, "Serviço atualizado com sucesso.", Toast.LENGTH_SHORT).show();
                        progress.dismiss();

                        Intent it = new Intent(AlterarServicoActivity.this, VisualizarMeuServicoClicadoActivity.class);
                        startActivity(it);
                        finish();
                        break;
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error - Alterar Serviço");
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(AlterarServicoActivity.this, VisualizarMeuServicoClicadoActivity.class);
        startActivity(it);
        finish();
    }

    private boolean validateInfo(String nome, String valor, String categoria) {
        // Reseta os erros.
        mNomeView.setError(null);
        mValorView.setError(null);

        boolean cancelar = false;
        View focusView = null;

        if (TextUtils.isEmpty(nome)) {
            mNomeView.setError(getString(R.string.error_field_required));
            focusView = mNomeView;
            cancelar = true;
        } else if (TextUtils.isEmpty(valor)) {
            mValorView.setError(getString(R.string.error_field_required));
            focusView = mValorView;
            cancelar = true;
        } else if ("Selecione a Frequência...".equals(categoria)) {
            Toast.makeText(AlterarServicoActivity.this, R.string.error_frequencia_nao_selecionada, Toast.LENGTH_SHORT).show();
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
