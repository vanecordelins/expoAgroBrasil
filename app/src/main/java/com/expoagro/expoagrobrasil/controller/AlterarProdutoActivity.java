package com.expoagro.expoagrobrasil.controller;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.expoagro.expoagrobrasil.R;
import com.expoagro.expoagrobrasil.dao.ProdutoDAO;
import com.expoagro.expoagrobrasil.dao.UserDAO;
import com.expoagro.expoagrobrasil.model.Produto;
import com.expoagro.expoagrobrasil.util.ImagePicker;
import com.expoagro.expoagrobrasil.util.MoneyTextWatcher;

import com.expoagro.expoagrobrasil.util.ProdutoViewPager;
import com.expoagro.expoagrobrasil.util.Regex;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
    private List<String> fotosURLUp;

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
        fotosURLUp = new ArrayList<>();

        keyProduto = VisualizarMeusAnunciosActivity.getId();

        dialog = new ProgressDialog(AlterarProdutoActivity.this);
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Alterando dados...");

        // Cria um ArrayAdapter usando um array de string e um layout default do spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categorias, android.R.layout.simple_spinner_item); //simple_spinner_dropdown_item
        // Especifica o layout que será usado quando a lista de opções aparecer
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCategoria = (Spinner) findViewById(R.id.spinnerCategoria);
        // Aplica o adapter ao spinner
        spinnerCategoria.setAdapter(adapter);

        carregarProduto();

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

        final ImageView mAddMoreButton = (ImageView) findViewById(R.id.btn_add_mais);
        mAddMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseImageIntent = ImagePicker.getPickImageIntent(AlterarProdutoActivity.this);
                if (chooseImageIntent != null) {
                    startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
                }
            }
        });

        ImageView mRemoveButton = (ImageView) findViewById(R.id.btn_remove);
        mRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fotos.isEmpty()) {
                    Dialog alertDialog = new AlertDialog.Builder(AlterarProdutoActivity.this).setIcon(android.R.drawable.ic_input_delete).setTitle("Remover")
                            .setMessage("Deseja remover esta foto?")
                            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void onClick(final DialogInterface dialog, int which) {
                                    fotos.remove(viewPager.getCurrentItem());

                                    try {
                                        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(fotosURL.get(viewPager.getCurrentItem()));
                                        ref.delete().addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                System.out.println("Warning - Photo to delete does not exist");
                                            }
                                        });
                                    } catch(IndexOutOfBoundsException  ex) {
                                        System.out.println(ex.getMessage());
                                    }

                                    produtoViewPager = new ProdutoViewPager(AlterarProdutoActivity.this, fotos, null);

                                    viewPager.setAdapter(produtoViewPager);

                                    if (fotos.isEmpty()) {
                                        viewPager.setBackground(AlterarProdutoActivity.this.getResources().getDrawable(R.drawable.sem_foto, null));
                                    }

                                    if (!mAddMoreButton.isEnabled()) {
                                        mAddMoreButton.setEnabled(true);
                                    }

                                }
                            }).setNegativeButton("Não", null).show();
                    alertDialog.setCanceledOnTouchOutside(true);
                }
            }
        });

    }

    private void carregarProduto() {
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
                                final String fotoURL = produto.getFoto().get(i);
                                fotosURL.add(fotoURL);
                                new BackgroundLoading(AlterarProdutoActivity.this).execute(fotoURL);
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
                Toast.makeText(AlterarProdutoActivity.this, "Erro de Alteração. Tente Novamente.", Toast.LENGTH_SHORT);
            }
        });

    }

    public class BackgroundUpload extends AsyncTask<String, Produto, Void> {

        private ProdutoDAO pdao;

        public BackgroundUpload(ProdutoDAO pdao) {
            this.pdao = pdao;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(String... params) {
            final String nome = params[0];
            final String categoria = params[1];
            final String descricao = params[2];
            final String observacao = params[3];
            final String valor = params[4];

            final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            ProdutoDAO.getDatabaseReference().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getMessage());
                }

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot produto : dataSnapshot.getChildren()) {
                        if (produto.getKey().equals(keyProduto)) {
                            final Produto target = produto.getValue(Produto.class);
                            target.setCategoria(categoria);
                            target.setDescricao(descricao);
                            target.setObservacao(observacao);
                            target.setValor(valor);
                            target.setNome(nome);

                            if (fotos.isEmpty()) {
                                target.setFoto(null);
                                pdao.update(target);
                            }

                            for (int i = 0; i < fotos.size(); i++) {
                                Bitmap bitmap = fotos.get(i);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                byte[] data = baos.toByteArray();

                                UploadTask uploadTask = FirebaseStorage.getInstance().getReference().child(uid).child(nome).child(nome + "" + (i + 1) + ".png").putBytes(data);

                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        System.out.println(exception.getMessage());
                                        Toast.makeText(AlterarProdutoActivity.this, "Ocorreu um erro ao cadastrar o produto.", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                        fotosURLUp.add(downloadUrl.toString());
                                        target.setFoto(fotosURLUp);
                                        pdao.update(target);
                                    }
                                });
                            }
                            break;
                        }
                    }
                }
            });
            while (fotosURLUp.size() != fotos.size()) { }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {

            Toast.makeText(AlterarProdutoActivity.this, "Produto atualizado com sucesso.", Toast.LENGTH_SHORT).show();

            dialog.dismiss();
            Intent it = new Intent(AlterarProdutoActivity.this, VisualizarMeuAnuncioClicadoActivity.class);
            startActivity(it);
            finish();
        }
    }


    public class BackgroundLoading extends AsyncTask<String, Void, Bitmap> {

        private ProgressDialog dialog;

        public BackgroundLoading(Activity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected  void onPreExecute() {
            this.dialog.setMessage("Carregando Dados");
            this.dialog.setIndeterminate(true);
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected  void onPostExecute(Bitmap sucess) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            fotos.add(sucess);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

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
            if(fotos.isEmpty()) {
                Dialog alertDialog = new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_info).setTitle("Alterar Cadastro")
                        .setMessage("Deseja continuar sem adicionar fotos?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                alterarProduto(nome, observacao, descricao, valor, categoria);
                            }
                        }).setNegativeButton("Não", null).show();
                alertDialog.setCanceledOnTouchOutside(true);
            } else {
                Dialog alertDialog = new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_info).setTitle("Alterar Produto")
                        .setMessage("Deseja continuar? Verifique se todos os dados estão corretos. ")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                alterarProduto(nome, observacao, descricao, valor, categoria);
                            }
                        }).setNegativeButton("Não", null).show();
                alertDialog.setCanceledOnTouchOutside(true);
             }

        }
    }

    public void alterarProduto(final String nome, final String observacao, final String descricao,
                                 final String valor, final String categoria) {
        dialog.show();
        ProdutoDAO pdao = new ProdutoDAO();
        new BackgroundUpload(pdao).execute(nome, categoria, descricao, observacao, valor);

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

                    if (fotos.isEmpty()) {
                        viewPager.setBackground(null);
                    }

                    fotos.add(resizedBitmap);

                    if (fotos.size() > 4) {
                        findViewById(R.id.btn_add_mais).setEnabled(false);
                        Toast.makeText(this, "Você adicionou a quantidade máxima permitida de fotos.", Toast.LENGTH_SHORT).show();
                    }

                    produtoViewPager = new ProdutoViewPager(this, fotos, null);

                    viewPager.setAdapter(produtoViewPager);

                    break;
                default:
                    break;
            }
        }
    }
}
