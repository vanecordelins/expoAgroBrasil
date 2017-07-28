package com.expoagro.expoagrobrasil.controller;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.expoagro.expoagrobrasil.dao.ProdutoDAO;
import com.expoagro.expoagrobrasil.model.Produto;
import com.expoagro.expoagrobrasil.util.AnuncioViewPager;
import com.expoagro.expoagrobrasil.util.ImagePicker;
import com.expoagro.expoagrobrasil.util.MoneyTextWatcher;

import com.expoagro.expoagrobrasil.util.ProdutoViewPager;
import com.expoagro.expoagrobrasil.util.Regex;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samir on 25/07/2017.
 */



public class AlterarProdutoActivity extends AppCompatActivity {

    private AutoCompleteTextView mNomeView;
    private EditText mValorView;
    private Spinner spinnerCategoria;
    private TextView mDescricaoView;
    private TextView mObservacaoView;
    private ArrayList<String> fotosURL;
    private List<Bitmap> fotos;
    private ProgressDialog dialog;
    private static final int PICK_IMAGE_ID = 234;
    private ProdutoViewPager produtoViewPager;
    private String keyProduto;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_produto);

        mNomeView = (AutoCompleteTextView) findViewById(R.id.campoNomeProduto);
        mValorView = (EditText) findViewById(R.id.campoValor);
        mDescricaoView = (TextView) findViewById(R.id.campoDescricao);
        mObservacaoView = (TextView) findViewById(R.id.campoObservacao);
        viewPager = (ViewPager) findViewById(R.id.viewProduto);

        mValorView.addTextChangedListener(new MoneyTextWatcher(mValorView));

        fotosURL = new ArrayList<>();
        fotos = new ArrayList<>();

        keyProduto = VisualizarMeusAnunciosActivity.getId();

        dialog = new ProgressDialog(AlterarProdutoActivity.this);
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Atualizando dados...");


        // Cria um ArrayAdapter usando um array de string e um layout default do spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categorias, android.R.layout.simple_spinner_item); //simple_spinner_dropdown_item
        // Especifica o layout que será usado quando a lista de opções aparecer
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCategoria = (Spinner) findViewById(R.id.spinnerCategoria);
        // Aplica o adapter ao spinner
        spinnerCategoria.setAdapter(adapter);

        Button mAlterarButton = (Button) findViewById(R.id.btnAlterar);
        mAlterarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alterar(); //Aqui chamar o método para cadastrar *****************
            }
        });

        Button mCancelarButton = (Button) findViewById(R.id.btnCancelar);
        mCancelarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AlterarProdutoActivity.this, VisualizarMeuAnuncioClicadoActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button mAddMoreButton = (Button) findViewById(R.id.btn_add_mais);
        mAddMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseImageIntent = ImagePicker.getPickImageIntent(AlterarProdutoActivity.this);
                if (chooseImageIntent != null) {
                    startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
                }
            }
        });

        ProdutoDAO.getDatabaseReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot prod : dataSnapshot.getChildren()) {
                    if (prod.getKey().equals(keyProduto)) {
                        Produto produto = prod.getValue(Produto.class);
                        ((EditText) findViewById(R.id.campoDescricao)).setText(produto.getDescricao());
                        ((EditText) findViewById(R.id.campoNomeProduto)).setText(produto.getNome());
                        ((EditText) findViewById(R.id.campoObservacao)).setText(produto.getObservacao());
                        ((EditText) findViewById(R.id.campoValor)).setText(produto.getValor());

                        mValorView.addTextChangedListener(new MoneyTextWatcher(mValorView));

                        Spinner spinner = ((Spinner) findViewById(R.id.spinnerCategoria));
                        for (int i = 0; i < spinner.getCount(); i++) {
                            if (spinner.getItemAtPosition(i).toString().equals(produto.getCategoria())) {
                                spinner.setSelection(i);
                                break;
                            }
                        }

                        if (produto.getFoto() != null) {
                            for (int i = 0; i < produto.getFoto().size(); i++) {
                                fotosURL.add(produto.getFoto().get(i));
                            }
                            if (!produto.getFoto().isEmpty()) {
                                viewPager.setBackground(null);
                            }
                        }
                        produtoViewPager = new ProdutoViewPager(AlterarProdutoActivity.this, null, fotosURL);
                        viewPager.setAdapter(produtoViewPager);

                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                AlterarProdutoActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AlterarProdutoActivity.this, "Erro de Alteração. Tente Novamente.", Toast.LENGTH_SHORT);
                    }
                });
            }
        });

    }

    public void alterar() {
        final String nome = mNomeView.getText().toString();
        final String valor = mValorView.getText().toString();
        final String categoria = spinnerCategoria.getSelectedItem().toString();
        final String descricao = mDescricaoView.getText().toString();
        final String observacao = mObservacaoView.getText().toString();

        boolean cancelar = validateInfo(nome, valor, categoria);

        if (cancelar) {
            return;
        } else {

//            if(fotos.isEmpty()) {
//                new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_info).setTitle("Alterar Cadastro")
//                        .setMessage("Deseja continuar sem adicionar fotos?")
//                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(final DialogInterface dialog, int which) {
//                                alterarProduto(nome, observacao, descricao, date, time, valor, categoria);
//                            }
//                        }).setNegativeButton("Não", null).show();
//            } else {
                Dialog alertDialog = new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_info).setTitle("Alterar Produto")
                        .setMessage("Deseja continuar? Verifique se todos os dados estão corretos. ")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {

                                alterarProduto(nome, observacao, descricao, valor, categoria);
                            }
                        }).setNegativeButton("Não", null).show();
                alertDialog.setCanceledOnTouchOutside(true);
             //}

        }
    }

    public void alterarProduto(final String nome, final String observacao, final String descricao,
                                 final String valor, final String categoria) {

        dialog.show();
        final ProdutoDAO pdao = new ProdutoDAO();

        ProdutoDAO.getDatabaseReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot produto : dataSnapshot.getChildren()) {
                    if (produto.getKey().equals(keyProduto)) {
                        Produto target = produto.getValue(Produto.class);
                        target.setCategoria(categoria);
                        target.setDescricao(descricao);
                        target.setObservacao(observacao);
                        target.setValor(valor);
                        target.setNome(nome);

                        pdao.update(target);
                        Toast.makeText(AlterarProdutoActivity.this, "Produto atualizado com sucesso.", Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                        Intent it = new Intent(AlterarProdutoActivity.this, VisualizarMeuAnuncioClicadoActivity.class);
                        startActivity(it);
                        finish();
                        break;
                    }
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AlterarProdutoActivity.this, VisualizarMeuAnuncioClicadoActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean validateInfo(String nome, String valor, String categoria) {

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
        } else if ("Selecione a Categoria...".equals(categoria)) {
            Toast.makeText(AlterarProdutoActivity.this, R.string.error_categoria_nao_selecionada, Toast.LENGTH_SHORT).show();
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case PICK_IMAGE_ID:
                    Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);

                    Bitmap resizedBitmap = ImagePicker.resize(bitmap, 600, 400);
                  //  viewPager.setBackground(null);

                    fotos.add(resizedBitmap);

                    if (fotos.size() > 4) {
                        findViewById(R.id.btn_add_mais).setEnabled(false);
                        Toast.makeText(this, "Você adicionou a quantidade máxima permitida de fotos.", Toast.LENGTH_SHORT).show();
                    }

                //    produtoViewPager = new ProdutoViewPager(this, fotos, null);

                //    viewPager.setAdapter(produtoViewPager);


                    break;
                default:
                    break;
            }
        }
    }
}
