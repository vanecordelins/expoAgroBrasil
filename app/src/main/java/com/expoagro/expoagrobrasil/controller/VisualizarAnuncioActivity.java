package com.expoagro.expoagrobrasil.controller;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.expoagro.expoagrobrasil.R;
import com.expoagro.expoagrobrasil.dao.ProdutoDAO;
import com.expoagro.expoagrobrasil.model.Produto;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Samir on 24/07/2017.
 */

public class VisualizarAnuncioActivity extends AppCompatActivity{
    private ViewPager viewPager;
    private AnuncioViewPager testeViewPager;
    private DatabaseReference mDatabase;

//    FirebaseStorage storage = FirebaseStorage.getInstance();
//    StorageReference storageRef = storage.getReferenceFromUrl("gs://expoagro-brasil.appspot.com/teste").child("DILMA.png");

    private ArrayList<String> img = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_anuncio);

        img.add("https://firebasestorage.googleapis.com/v0/b/expoagro-brasil.appspot.com/o/yziH8PEqaIUS8wPCBi9qY3Tgh393%2FTeste%2FTeste1.png?alt=media&token=b68d8893-48bd-401a-bd84-a3feed89083a");
        recuperarAuncio();
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        testeViewPager = new AnuncioViewPager(this, img);
        viewPager.setAdapter(testeViewPager);

    }

    public void recuperarAuncio(){

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String nome = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        ProdutoDAO.getDatabaseReference().orderByChild("idUsuario").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot prod : dataSnapshot.getChildren()){
                    Produto produto = prod.getValue(Produto.class);
                    ((TextView) findViewById(R.id.dataAnuncio)).setText("Data: "+produto.getData());
                    ((TextView) findViewById(R.id.vendedor)).setText("Vendedor: "+nome);
                    ((TextView) findViewById(R.id.descricao)).setText("Descrição: " + produto.getDescricao());
//                    ((TextView) findViewById(R.id.editText12)).setText(produto.getCidade());
                    System.out.println(produto.getFoto().get(0));

//                    produto.getFoto();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
