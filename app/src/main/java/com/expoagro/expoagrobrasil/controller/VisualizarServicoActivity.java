package com.expoagro.expoagrobrasil.controller;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.expoagro.expoagrobrasil.R;
import com.expoagro.expoagrobrasil.dao.ServicoDAO;
import com.expoagro.expoagrobrasil.util.AnuncioViewPager;
import com.expoagro.expoagrobrasil.util.Servico;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by joao on 03/08/17.
 */

public class VisualizarServicoActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private AnuncioViewPager testeViewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_servico);
      /*  final ArrayList<String> img = new ArrayList<>();*/

        final String keyServico = MenuServicoActivity.getId();
        System.out.println("VISUALIZOU");
//        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        final String nome = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        ServicoDAO.getDatabaseReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot serv : dataSnapshot.getChildren()) {
                    if (serv.getKey().equals(keyServico) ) {
                        Servico servico = serv.getValue(Servico.class);
                        ((TextView) findViewById(R.id.dataServico)).setText("Data: " + servico.getData());
//                        ((TextView) findViewById(R.id.vendedorProduto)).setText("Vendedor: " + nome);
                        ((TextView) findViewById(R.id.descricaoServico)).setText("Descrição: " + servico.getDescricao());
                        ((TextView) findViewById(R.id.nomeServico)).setText("Nome: " + servico.getNome());
                        ((TextView) findViewById(R.id.observacaoServico)).setText("Observação: " + servico.getObservacao());
                        ((TextView) findViewById(R.id.fresquenciaServico)).setText("Fresquência: " + servico.getFrequencia());

/*                        if(servico.getFoto() != null){
                            for (int i =0; i<servico.getFoto().size(); i++){
                                img.add(servico.getFoto().get(i));
                            }
                        }
                        viewPager = (ViewPager)findViewById(R.id.viewPager);
                        testeViewPager = new AnuncioViewPager(VisualizarServicoActivity.this, img);
                        viewPager.setAdapter(testeViewPager);*/
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(VisualizarServicoActivity.this, "Erro ao recuperar Servico.", Toast.LENGTH_SHORT);
            }
        });

        //System.out.println(img);
        Button alterar = (Button) findViewById(R.id.alterarServico);
        Button excluir = (Button) findViewById(R.id.excluirServico);

        alterar.setVisibility(View.GONE);
        excluir.setVisibility(View.GONE);



    }

}
