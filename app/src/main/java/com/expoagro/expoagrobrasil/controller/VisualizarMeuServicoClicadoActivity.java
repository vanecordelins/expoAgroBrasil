package com.expoagro.expoagrobrasil.controller;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.expoagro.expoagrobrasil.R;
import com.expoagro.expoagrobrasil.dao.ProdutoDAO;
import com.expoagro.expoagrobrasil.dao.ServicoDAO;
import com.expoagro.expoagrobrasil.util.AnuncioViewPager;
import com.expoagro.expoagrobrasil.util.Servico;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by joao on 03/08/17.
 */

public class VisualizarMeuServicoClicadoActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private AnuncioViewPager testeViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_servico);
      /*  final ArrayList<String> img = new ArrayList<>();*/

        final String keyServico = VisualizarMeusServicosActivity.getId();

//        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        final String nome = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        ServicoDAO.getDatabaseReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot serv : dataSnapshot.getChildren()) {
                    if (serv.getKey().equals(keyServico) ) {
                        Servico servico = serv.getValue(Servico.class);
                        ((TextView) findViewById(R.id.dataServico)).setText("Data: " + servico.getData());
                     //   ((TextView) findViewById(R.id.vendedorProduto)).setText("Vendedor: " + servico.get);
                        ((TextView) findViewById(R.id.descricaoServico)).setText("Descrição: " + servico.getDescricao());
                        ((TextView) findViewById(R.id.nomeServico)).setText("Nome: " + servico.getNome());
                        ((TextView) findViewById(R.id.observacaoServico)).setText("Observação: " + servico.getObservacao());
                        ((TextView) findViewById(R.id.frequenciaServico)).setText("Frequência: " + servico.getFrequencia());

/*                        if(servico.getFoto() != null){
                            for (int i =0; i<servico.getFoto().size(); i++){
                                img.add(servico.getFoto().get(i));
                            }
                        }
                        viewPager = (ViewPager)findViewById(R.id.viewPager);
                        testeViewPager = new AnuncioViewPager(VisualizarMeuServicoClicadoActivity.this, img);
                        viewPager.setAdapter(testeViewPager);*/
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(VisualizarMeuServicoClicadoActivity.this, "Erro ao recuperar Servico.", Toast.LENGTH_SHORT);
            }
        });

        Button alterar = (Button) findViewById(R.id.alterarServico);
        alterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VisualizarMeuServicoClicadoActivity.this, AlterarProdutoActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button excluir = (Button) findViewById(R.id.excluirServico);
        excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog alertDialog = new AlertDialog.Builder(VisualizarMeuServicoClicadoActivity.this).setIcon(android.R.drawable.ic_delete).setTitle("EXCLUIR SERVIÇO")
                        .setMessage("Deseja realmente EXCLUIR este serviço? Todos os seus dados serão perdidos!")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                ServicoDAO pdao = new ServicoDAO();
                                pdao.delete(keyServico);
                                Toast.makeText(VisualizarMeuServicoClicadoActivity.this, "Serviço deletado com sucesso.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(VisualizarMeuServicoClicadoActivity.this, VisualizarMeusServicosActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).setNegativeButton("Não", null).show();
                alertDialog.setCanceledOnTouchOutside(true);
            }
        });

    }

}
