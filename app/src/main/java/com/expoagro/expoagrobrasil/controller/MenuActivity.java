package com.expoagro.expoagrobrasil.controller;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.expoagro.expoagrobrasil.R;
import com.expoagro.expoagrobrasil.dao.ProdutoDAO;
import com.expoagro.expoagrobrasil.dao.UserDAO;
import com.expoagro.expoagrobrasil.model.Produto;
import com.expoagro.expoagrobrasil.model.Usuario;
import com.expoagro.expoagrobrasil.util.Lista;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private String uid;
    private static final int CALL_IMAGE = 12;
    private static final int CALL_CAMERA = 14;
    private RecyclerView recyclerView;
    private DatabaseReference myref;
    private DatabaseReference myref2;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        uid = "";

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(MenuActivity.this)
                .enableAutoManage(MenuActivity.this, MenuActivity.this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // ----------------------------------RecyclerView-----------------------------------------------------------

        mAuth = FirebaseAuth.getInstance();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myref = FirebaseDatabase.getInstance().getReference("Produto");
            FirebaseRecyclerAdapter<Lista, ListaViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<Lista, ListaViewHolder>(
                    Lista.class,
                    R.layout.linha,
                    ListaViewHolder.class,
                    myref


            ) {

                @Override
                protected void populateViewHolder(ListaViewHolder viewHolder, Lista model, int position) {

                    final String key = getRef(position).getKey();

                    viewHolder.setCategoria(model.getCategoria());
                    viewHolder.setData(model.getData());
                    viewHolder.setValor(model.getValor());
                    viewHolder.setFoto(model.getFoto());
                    viewHolder.setNome(model.getNome());


                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(MenuActivity.this, key, Toast.LENGTH_LONG).show();
                        }
                    });


                }


            };

            recyclerView.setAdapter(recyclerAdapter);
    }

    public static class ListaViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView textView_nome;
        TextView textView_data;
        TextView textView_valor;
        TextView textView_categoria;
        ImageView imageView;

        public ListaViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            textView_nome = (TextView) itemView.findViewById(R.id.nome);
            textView_data = (TextView) itemView.findViewById(R.id.data);
            textView_valor = (TextView) itemView.findViewById(R.id.valor);
            textView_categoria = (TextView) itemView.findViewById(R.id.categoria);
            imageView = (ImageView) itemView.findViewById(R.id.foto);
        }


        public void setNome(String nome) {
            textView_nome.setText(nome);
        }

        public void setData(String data) {
            textView_data.setText(data);
        }

        public void setValor(String valor) {
            textView_valor.setText(valor);
        }

        public void setCategoria(String categoria) {
            textView_categoria.setText(categoria);
        }


        public void setFoto(List<String> foto) {
            if (foto == null) {
            } else {
                Picasso.with(mView.getContext())
                        .load(foto.get(0))
                        .resize(50,50)
                        .into(imageView);
            }
        }
    }

//---------------------------------------------------------------------------------------


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        final TextView nomeUsuarioLogado = (TextView) findViewById(R.id.menu_nome);
        final TextView emailUsuarioLogado = (TextView) findViewById(R.id.menu_email);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }


        UserDAO.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    if (user.getKey().equals(uid)) {
                        Usuario target = user.getValue(Usuario.class);
                        nomeUsuarioLogado.setText(target.getNome());
                        emailUsuarioLogado.setText(target.getEmail());

                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_meu_perfil) {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                Intent telaVisualizar = new Intent(MenuActivity.this, VisualizarUsuarioActivity.class);
                startActivity(telaVisualizar);
                finish();
            } else {
                Intent telaLogin = new Intent(MenuActivity.this, LoginActivity.class);
                startActivity(telaLogin);
                finish();
            }
        } else if (id == R.id.menu_novo_anuncio) {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                System.out.println("MENU NOVO ANUNCIOS"); // Ja esta logado
            } else {
                Intent telaLogin = new Intent(MenuActivity.this, LoginActivity.class);
                startActivity(telaLogin);
                finish();
            }
        } else if (id == R.id.menu_meus_anuncios) {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                System.out.println("MENU MEUS FAVORITOS"); // Ja esta logado
            } else {
                Intent telaLogin = new Intent(MenuActivity.this, LoginActivity.class);
                startActivity(telaLogin);
                finish();
            }
        } else if (id == R.id.menu_hoje) {
            finish();
        } else if (id == R.id.menu_favoritos) {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                System.out.println("MENU FAVORITOS"); // Ja esta logado
            } else {
                Intent telaLogin = new Intent(MenuActivity.this, LoginActivity.class);
                startActivity(telaLogin);
                finish();
            }
        } /* else if (id == R.id.menu_sobre) {

          }*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        System.out.println("onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == CALL_IMAGE) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, CALL_IMAGE);
            }
        } else if (requestCode == CALL_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(cameraIntent, CALL_CAMERA);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == CALL_CAMERA && intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Bitmap bit = (Bitmap) bundle.get("data");

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bit.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                StorageReference ref = FirebaseStorage.getInstance().getReference().child("temer.jpg");
                UploadTask uploadTask = ref.putBytes(data);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        System.out.println("Unsucessful");
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        final List<String> lista = new ArrayList<String>();
                        lista.add(downloadUrl.toString());
                        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        Produto produto = new Produto("Boi", "obs", "desc", "data", "hora", 100, "categoria", lista);
                        ProdutoDAO pdao = new ProdutoDAO();
                        pdao.save(produto, uid);
                    }
                });

            }
        } else if (resultCode != RESULT_OK) {
            System.out.println("Cancelado pelo usuário");
        } else if (requestCode == CALL_IMAGE) {
            Cursor cursor = getContentResolver().query(intent.getData(), null, null, null, null);
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String fileSrc = cursor.getString(idx); // file source path

            Uri uri = Uri.fromFile(new File(fileSrc));
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(uri.getLastPathSegment());
            UploadTask uploadTask = ref.putFile(uri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    System.out.println("Unsucessful");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    final List<String> lista = new ArrayList<String>();
                    lista.add(downloadUrl.toString());
                    uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Produto produto = new Produto("Boi", "obs", "desc", "data", "hora", 100, "categoria", lista);
                    ProdutoDAO pdao = new ProdutoDAO();
                    pdao.save(produto, uid);
                }
            });
            //    Glide.with(this /* context */)
            //         .load("https://firebasestorage.googleapis.com/v0/b/expoagro-brasil.appspot.com/o/armadefogo.png?alt=media&token=35607849-7763-4967-a846-6ef775ae16ec")
            //         .into((ImageView) findViewById(R.id.imageView));
        }
    }
}
