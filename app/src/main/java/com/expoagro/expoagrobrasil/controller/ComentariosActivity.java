package com.expoagro.expoagrobrasil.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.expoagro.expoagrobrasil.R;
import com.expoagro.expoagrobrasil.model.Comentario;
import com.expoagro.expoagrobrasil.model.Produto;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class ComentariosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progress = new ProgressDialog(ComentariosActivity.this);
        progress.setCancelable(false);
        progress.setMessage("Carregando comentários...");
        progress.show();

        final String key;
        if(MenuProdutoActivity.getId() != null) {
            key = MenuProdutoActivity.getId();
        } else if (VisualizarMeusProdutosActivity.getId() != null) {
            key = VisualizarMeusProdutosActivity.getId();
        } else if (VisualizarMeusServicosActivity.getId() != null) {
            key = VisualizarMeusServicosActivity.getId();
        } else {
            key = MenuServicoActivity.getId();
        }

        Button btn_comentar = (Button) findViewById(R.id.btnComentar);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            btn_comentar.setVisibility(View.GONE);
        } else {
            btn_comentar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(ComentariosActivity.this, NovoComentarioActivity.class);
                    startActivity(it);
                }
            });
        }

        Thread mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(ComentariosActivity.this));
                final Query q = FirebaseDatabase.getInstance().getReference("Comentário").orderByChild("idProduto").equalTo(key);

                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            ComentariosActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progress.dismiss();
                                    Toast.makeText(ComentariosActivity.this, "Sem comentários.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println(databaseError.getMessage());
                    }
                });
                final FirebaseRecyclerAdapter<Comentario, ComentarioViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<Comentario, ComentarioViewHolder>(
                        Comentario.class,
                        R.layout.comentario,
                        ComentarioViewHolder.class,
                        q
                ) {
                    @Override
                    protected void populateViewHolder(ComentarioViewHolder viewHolder, Comentario model, int position) {
                        viewHolder.setComentario(model.getComentario());
                        viewHolder.setData(model.getData());
                        viewHolder.setNomeUsuario(model.getNomeUsuario());
                        progress.dismiss();
                    }
                };
                ComentariosActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(recyclerAdapter);
                    }
                });
            }
        });
        mThread.start();
    }

    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class ComentarioViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView textView_nomeUsuario;
        private TextView textView_comentario;
        private TextView textView_data;

        public ComentarioViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            textView_comentario = (TextView) itemView.findViewById(R.id.comentarioTexto);
            textView_data = (TextView) itemView.findViewById(R.id.comentarioData);
            textView_nomeUsuario = (TextView) itemView.findViewById(R.id.comentarioUsuario);
        }

        public void setComentario(String comentario) {
            textView_comentario.setText(comentario);
        }

        public void setData(String data) {
            textView_data.setText(data);
        }

        public void setNomeUsuario(String nomeUsuario) {textView_nomeUsuario.setText(nomeUsuario);}
    }

}
