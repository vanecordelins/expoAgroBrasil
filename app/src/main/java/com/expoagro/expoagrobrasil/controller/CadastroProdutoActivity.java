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

public class CadastroProdutoActivity extends AppCompatActivity {

    private AutoCompleteTextView mNomeView;
    private TextView mValorView;
    private Spinner spinnerCategoria;
    private TextView mDescricaoView;
    private TextView mObservacaoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_cadastro_produto);

        mNomeView = (AutoCompleteTextView) findViewById(R.id.campoNomeProduto);
        mValorView = (TextView) findViewById(R.id.campoValor);
        mDescricaoView = (TextView) findViewById(R.id.campoDescricao);
        mObservacaoView = (TextView) findViewById(R.id.campoObservacao);

        // Cria um ArrayAdapter usando um array de string e um layout default do spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categorias, android.R.layout.simple_spinner_item); //simple_spinner_dropdown_item
        // Especifica o layout que será usado quando a lista de opções aparecer
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCategoria = (Spinner) findViewById(R.id.spinnerCategoria);
        // Aplica o adapter ao spinner
        spinnerCategoria.setAdapter(adapter);

        RadioButton rdoBtnServico = (RadioButton) findViewById(R.id.rdoBtnServico);
        rdoBtnServico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent telaCadastrarServico = new Intent(CadastroProdutoActivity.this, CadastroServicoActivity.class);
                startActivity(telaCadastrarServico);
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
                Intent telaMenu = new Intent(CadastroProdutoActivity.this, MenuActivity.class);
                startActivity(telaMenu);
                finish();
            }
        });

    }

    public void cadastrar(){
        String nome = mNomeView.getText().toString();
        String valor = mValorView.getText().toString();
        String categoria = spinnerCategoria.getSelectedItem().toString();
        String descricao = mDescricaoView.getText().toString();
        String observacao = mObservacaoView.getText().toString();

    }

}
