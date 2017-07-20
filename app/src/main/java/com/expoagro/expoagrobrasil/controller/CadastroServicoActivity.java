package com.expoagro.expoagrobrasil.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.expoagro.expoagrobrasil.R;

public class CadastroServicoActivity extends AppCompatActivity {

    private AutoCompleteTextView mNomeView;
    private TextView mValorView;
    private Spinner spinnerFrequencia;
    private TextView mDescricaoView;
    private TextView mObservacaoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_cadastro_servico);

        mNomeView = (AutoCompleteTextView) findViewById(R.id.campoNomeProduto);
        mValorView = (TextView) findViewById(R.id.campoValor);
        mDescricaoView = (TextView) findViewById(R.id.campoDescricao);
        mObservacaoView = (TextView) findViewById(R.id.campoObservacao);

        // Cria um ArrayAdapter usando um array de string e um layout default do spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.frequencias, android.R.layout.simple_spinner_item); //simple_spinner_dropdown_item
        // Especifica o layout que será usado quando a lista de opções aparecer
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFrequencia = (Spinner) findViewById(R.id.spinnerFrequencia);
        // Aplica o adapter ao spinner
        spinnerFrequencia.setAdapter(adapter);

        RadioButton rdoBtnProduto = (RadioButton) findViewById(R.id.rdoBtnProduto);
        rdoBtnProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent telaCadastrarProduto = new Intent(CadastroServicoActivity.this, CadastroProdutoActivity.class);
                startActivity(telaCadastrarProduto);
                finish();
            }
        });

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
                Intent telaMenu = new Intent(CadastroServicoActivity.this, MenuActivity.class);
                startActivity(telaMenu);
                finish();
            }
        });
    }

    public void cadastrar(){
        String nome = mNomeView.getText().toString();
        String valor = mValorView.getText().toString();
        String frequencia = spinnerFrequencia.getSelectedItem().toString();
        String descricao = mDescricaoView.getText().toString();
        String observacao = mObservacaoView.getText().toString();
    }

}
