package com.expoagro.expoagrobrasil.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.expoagro.expoagrobrasil.R;
import com.expoagro.expoagrobrasil.model.Produto;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Samir on 25/07/2017.
 */

public class VisualizarMeusAnunciosActivitty extends AppCompatActivity {

    private static String idClicado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_meus_anuncios);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RadioButton rdoBtnServico = (RadioButton) findViewById(R.id.rdoBtnServico3);
        rdoBtnServico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent telaCadastrarServico = new Intent(VisualizarMeusAnunciosActivitty.this, VisualizarMeusServicosActivity.class);
                startActivity(telaCadastrarServico);
                finish();
            }
        });


        // ----------------------------------RecyclerView-----------------------------------------------------------


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query myref = FirebaseDatabase.getInstance().getReference("Produto").orderByChild("idUsuario").equalTo(uid);

        FirebaseRecyclerAdapter<Produto, VisualizarMeusAnunciosActivitty.ListaViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<Produto, VisualizarMeusAnunciosActivitty.ListaViewHolder>(
                Produto.class,
                R.layout.linha,
                VisualizarMeusAnunciosActivitty.ListaViewHolder.class,
                myref
        ) {

            @Override
            protected void populateViewHolder(VisualizarMeusAnunciosActivitty.ListaViewHolder viewHolder, final Produto model, int position) {

                final String keyProduto = getRef(position).getKey();

                viewHolder.setCategoria(model.getCategoria());
                viewHolder.setData(model.getData());
                viewHolder.setValor(model.getValor());
                viewHolder.setFoto(model.getFoto());
                viewHolder.setNome(model.getNome());


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setId(keyProduto);
                        //  TextView i = (TextView) findViewById(R.id.vendedor);
                        //   i.setText(model.getNome());
                        Intent intent = new Intent(VisualizarMeusAnunciosActivitty.this, VisualizarMeuProdutoClicadoActivity.class);
                        startActivity(intent);
                        finish();
                   //     Toast.makeText(VisualizarMeusAnunciosActivitty.this, key, Toast.LENGTH_LONG).show();

                    }
                });

            }

        };

        recyclerView.setAdapter(recyclerAdapter);
    }

    public static String getId() {
        return idClicado;
    }

    public static void setId(String id) {
        idClicado = id;
    }

    public static class ListaViewHolder extends RecyclerView.ViewHolder{
        private View mView;
        private TextView textView_nome;
        private TextView textView_data;
        private TextView textView_valor;
        private TextView textView_categoria;
        private ImageView imageView;

        public ListaViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            textView_nome = (TextView) itemView.findViewById(R.id.nomeProduto);
            textView_data = (TextView) itemView.findViewById(R.id.dataProduto);
            textView_valor = (TextView) itemView.findViewById(R.id.valorProduto);
            textView_categoria = (TextView) itemView.findViewById(R.id.categoriaProduto);
            imageView = (ImageView) itemView.findViewById(R.id.fotoProduto);
            //textView_nome2 = (TextView) itemView.findViewById(R.id.vendedorProduto);
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
                        .resize(100,100)
                        .into(imageView);
            }
        }

    }

//---------------------------------------------------------------------------------------

//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//
//        //getMenuInflater().inflate(R.menu.menu, menu);
//
//        final TextView nomeUsuarioLogado = (TextView) findViewById(R.id.menu_nome);
//        final TextView emailUsuarioLogado = (TextView) findViewById(R.id.menu_email);
//
//        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
//            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        }
//
//        emailUsuarioLogado.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
//                    Intent it = new Intent(VisualizarMeusAnunciosActivitty.this, LoginActivity.class);
//                    startActivity(it);
//                    finish();
//                }
//            }
//        });
//
//
//        UserDAO.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot user : dataSnapshot.getChildren()) {
//                    if(user.getKey().equals(uid)){
//                        Usuario target = user.getValue(Usuario.class);
//                        nomeUsuarioLogado.setText(target.getNome());
//                        emailUsuarioLogado.setText(target.getEmail());
//
//                        break;
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                System.out.println("The read failed: " + databaseError.getMessage());
//            }
//        });
//        return true;
//    }
//
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(VisualizarMeusAnunciosActivitty.this, MenuProdutoActivity.class);
        startActivity(intent);
        finish();
    }
//
//
//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.menu_meu_perfil) {
//            if(FirebaseAuth.getInstance().getCurrentUser() != null) { // Ja esta logado
//                Intent telaVisualizar = new Intent(VisualizarMeusAnunciosActivitty.this, VisualizarUsuarioActivity.class);
//                startActivity(telaVisualizar);
//                finish();
//            } else {
//                Intent telaLogin = new Intent(VisualizarMeusAnunciosActivitty.this, LoginActivity.class);
//                startActivity(telaLogin);
//                finish();
//            }
//        } else if (id == R.id.menu_novo_anuncio) {
//
//            if(FirebaseAuth.getInstance().getCurrentUser() != null) { // Ja esta logado
//                Intent telaCadastrarAnuncio = new Intent(VisualizarMeusAnunciosActivitty.this, CadastroProdutoActivity.class);
//                startActivity(telaCadastrarAnuncio);
//                finish();
//            } else {
//                Intent telaLogin = new Intent(VisualizarMeusAnunciosActivitty.this, LoginActivity.class);
//                startActivity(telaLogin);
//                finish();
//            }
//        } else if (id == R.id.menu_meus_anuncios) {
//            if(FirebaseAuth.getInstance().getCurrentUser() != null) {
//                System.out.println("MENU MEUS FAVORITOS"); // Ja esta logado
//                Intent intent = new Intent(VisualizarMeusAnunciosActivitty.this, )
//
//            } else {
//                Intent telaLogin = new Intent(VisualizarMeusAnunciosActivitty.this, LoginActivity.class);
//                startActivity(telaLogin);
//                finish();
//            }
//        } else if (id == R.id.menu_hoje) {
//            finish();
//        } else if (id == R.id.menu_favoritos) {
//            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
//                System.out.println("MENU FAVORITOS"); // Ja esta logado
//            } else {
//                Intent telaLogin = new Intent(VisualizarMeusAnunciosActivitty.this, LoginActivity.class);
//                startActivity(telaLogin);
//                finish();
//            }
//        } else if (id == R.id.menu_sair) {
//            GoogleSignIn.signOut(VisualizarMeusAnunciosActivitty.this, mGoogleApiClient);
//        } /* else if (id == R.id.menu_sobre) {
//          }*/
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
//
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        System.out.println("onConnectionFailed:" + connectionResult);
    }
}
