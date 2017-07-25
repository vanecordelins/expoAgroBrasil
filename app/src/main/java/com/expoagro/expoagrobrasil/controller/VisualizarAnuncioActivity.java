package com.expoagro.expoagrobrasil.controller;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.expoagro.expoagrobrasil.R;
import com.expoagro.expoagrobrasil.util.TesteViewPager;
import com.google.firebase.storage.FirebaseStorage;

public class VisualizarAnuncioActivity extends AppCompatActivity {


    // FirebaseStorage storage = FirebaseStorage.getInstance();
   // StorageReference storageRef = storage.getReferenceFromUrl("gs://expoagro-brasil.appspot.com/teste").child("DILMA.png");


 /*   private int[] img = {
            R.drawable.eab_logocnome,
            R.drawable.eab_logocnome_in,
            R.drawable.eab_logosnome
    }; */

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_anuncio);

        ViewPager viewPager = (ViewPager)findViewById(R.id.viewPager);
        TesteViewPager testeViewPager = new TesteViewPager(this);
        viewPager.setAdapter(testeViewPager);

        //PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);
        //photoView.setImageResource(R.drawable.eab_logocnome);

    }

}
