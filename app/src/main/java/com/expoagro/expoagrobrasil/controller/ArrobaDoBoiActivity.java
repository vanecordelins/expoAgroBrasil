package com.expoagro.expoagrobrasil.controller;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.expoagro.expoagrobrasil.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by joao on 03/07/17.
 */

public class ArrobaDoBoiActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arroba_do_boi);
        Arrobas arrobas = new Arrobas();
        arrobas.execute();


    }

        private class Arrobas extends AsyncTask<Void, Void, Void> {
            private String url = "http://www.canalrural.com.br/cotacao/boi-gordo/";
            private ProgressDialog mProgressDialog;
            private String valorArrobas;
            private String lugarArrobas;
            private String ufArrobas;
            private TextView textValorArrobas = (TextView) findViewById(R.id.valorArrobatext);
            private TextView textLugaresArrobas = (TextView) findViewById(R.id.lugaraArrobatext);
            private TextView textUfArrobas = (TextView) findViewById(R.id.ufArrobatext);


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgressDialog = new ProgressDialog(ArrobaDoBoiActivity.this);
                mProgressDialog.setTitle("Boi Gordo – Cotações - SCOT CONSULTORIA");
                mProgressDialog.setMessage("Carregando...");
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {

                    Document doc = Jsoup.connect(url).get();
                    Elements table = doc.select("table[class=cotacao-table cotacao-table--data-type-top]");
                    Elements rows = table.select("tbody");

                    for (int i = 0; i < rows.size(); i++) {
                        org.jsoup.nodes.Element row = rows.get(i);
                        Elements cols = row.select("td[data-th=à vista]");
                        Elements cols2 = row.select("td[data-th=Praça]");
                        Elements cols3 = row.select("td[data-th=UF]");
                        valorArrobas = cols.html();
                        lugarArrobas = cols2.html();
                        ufArrobas = cols3.html();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {

                textValorArrobas.setText("Valor R$\n\n"+valorArrobas);
                textLugaresArrobas.setText("Localidade\n\n"+lugarArrobas);
                textUfArrobas.setText("UF\n\n"+ufArrobas);
                mProgressDialog.dismiss();

            }
        }
    }
