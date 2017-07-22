package com.expoagro.expoagrobrasil.controller;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.expoagro.expoagrobrasil.R;
import com.expoagro.expoagrobrasil.dao.ProdutoDAO;
import com.expoagro.expoagrobrasil.dao.UserDAO;
import com.expoagro.expoagrobrasil.model.Produto;
import com.expoagro.expoagrobrasil.model.Usuario;
import com.expoagro.expoagrobrasil.util.ImagePicker;
import com.expoagro.expoagrobrasil.util.MoneyTextWatcher;
import com.expoagro.expoagrobrasil.util.Regex;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CadastroProdutoActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_ID = 234;

    private AutoCompleteTextView mNomeView;
    private EditText mValorView;
    private Spinner spinnerCategoria;
    private TextView mDescricaoView;
    private TextView mObservacaoView;
    private ImageView imView;
    private List<Bitmap> fotos;
    private List<String> fotosURL;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_cadastro_produto);

        mNomeView = (AutoCompleteTextView) findViewById(R.id.campoNomeProduto);
        mValorView = (EditText) findViewById(R.id.campoValor);
        mDescricaoView = (TextView) findViewById(R.id.campoDescricao);
        mObservacaoView = (TextView) findViewById(R.id.campoObservacao);
        imView = (ImageView) findViewById(R.id.imageView);

        mValorView.addTextChangedListener(new MoneyTextWatcher(mValorView));

        fotos = new ArrayList<>();
        fotosURL = new ArrayList<>();

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Cadastrando novo produto...");

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

        Button mAddMoreButton = (Button) findViewById(R.id.btn_add_mais);
        mAddMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseImageIntent = ImagePicker.getPickImageIntent(CadastroProdutoActivity.this);
                if (chooseImageIntent != null) {
                    startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
                }
            }
        });

        imView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseImageIntent = ImagePicker.getPickImageIntent(CadastroProdutoActivity.this);
                if (chooseImageIntent != null) {
                    startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent telaMenu = new Intent(CadastroProdutoActivity.this, MenuActivity.class);
        startActivity(telaMenu);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case PICK_IMAGE_ID:
                    Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, imView.getWidth(), imView.getHeight(), false);
                    fotos.add(resizedBitmap);

                    imView.setImageBitmap(resizedBitmap);
                    break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
                    break;
            }
        }
    }

    public void cadastrar() {
        final String nome = mNomeView.getText().toString();
        final String valor = mValorView.getText().toString();
        final String categoria = spinnerCategoria.getSelectedItem().toString();
        final String descricao = mDescricaoView.getText().toString();
        final String observacao = mObservacaoView.getText().toString();

        boolean cancelar = validateInfo(nome, valor, categoria);

        if (cancelar) {
            return;
        } else {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            final String date = df.format(calendar.getTime());

            SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm");
            final String time = dfTime.format(Calendar.getInstance().getTime());

            new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_info).setTitle("Confirmar Cadastro")
                    .setMessage("Deseja continuar? Verifique se todos os dados estão corretos. ")
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {

                            registrarProduto(nome, observacao, descricao, date, time, valor, categoria);
                        }
                    }).setNegativeButton("Não", null).show();

        }
    }


    public void registrarProduto(final String nome, final String observacao, final String descricao, final String date, final String time,
                                 final String valor, final String categoria) {
        dialog.show();
        Thread mThread = new Thread() {
            @Override
            public void run() {

                final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                final ProdutoDAO pdao = new ProdutoDAO();

                for (int i = 0; i < fotos.size(); i++) {

                    Bitmap bitmap = fotos.get(i);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] data = baos.toByteArray();

                    StorageReference ref = FirebaseStorage.getInstance().getReference().child(FirebaseAuth.getInstance()
                            .getCurrentUser().getUid()).child(nome).child(nome + "" + (i + 1) + ".png");

                    UploadTask uploadTask = ref.putBytes(data);

                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(CadastroProdutoActivity.this, "Ocorreu um erro ao cadastrar o produto.", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            fotosURL.add(downloadUrl.toString());

                            UserDAO.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot user : dataSnapshot.getChildren()) {
                                        if (user.getKey().equals(uid)) {
                                            Usuario target = user.getValue(Usuario.class);
                                            Produto produto = new Produto(nome, observacao, descricao, date, time, valor, target.getCidade(), categoria, fotosURL);
                                            pdao.save(produto, uid);
                                            Toast.makeText(CadastroProdutoActivity.this, R.string.msg_cadastro_sucesso, Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                            Intent it = new Intent(CadastroProdutoActivity.this, MenuActivity.class);
                                            startActivity(it);
                                            finish();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    System.out.println("The read failed: " + databaseError.getMessage());
                                    dialog.dismiss();
                                }
                            });
                        }
                    });
                }
            }
        };
        mThread.start();

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
            Toast.makeText(CadastroProdutoActivity.this, R.string.error_categoria_nao_selecionada, Toast.LENGTH_SHORT).show();
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