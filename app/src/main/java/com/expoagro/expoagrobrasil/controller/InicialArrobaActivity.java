package com.expoagro.expoagrobrasil.controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.expoagro.expoagrobrasil.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;


/**
 * Created by joao on 08/07/17.
 */

public class InicialArrobaActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial_arroba);
        ArrobaNacional arrobaNacional = new ArrobaNacional();
        arrobaNacional.execute();

        TextView dataatual = (TextView) findViewById(R.id.textData);
        String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
        dataatual.setText(currentDateTimeString);
    }

    public void onArrobaDoBoi (View v) {
        Intent it = new Intent(this, ArrobaDoBoiActivity.class);
        startActivity(it);
    }

    public void onAnuncios (View v) {
        Intent it = new Intent(this, MenuActivity.class);
        startActivity(it);
    }

    private class ArrobaNacional extends AsyncTask<Void, Void, Void> {
        private String url = "http://www.cepea.esalq.usp.br/br/indicador/boi-gordo.aspx";
        private ProgressDialog mProgressDialog;
        private String valorNacional;
        private TextView textValorNacional = (TextView) findViewById(R.id.valorNacionalText);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(InicialArrobaActivity.this);
            mProgressDialog.setTitle("Boi gordo - CEPEA-Esalq/USP");
            mProgressDialog.setMessage("Carregando...");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);

            mProgressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                Document doc = Jsoup.connect(url).get();
                Elements table = doc.select("table[id=imagenet-indicador1]");
                Elements rows = table.select("td");

                for (int i = 0; i < rows.size(); i++) {
                    org.jsoup.nodes.Element row = rows.get(1);
                    Elements cols = row.select("td");
                    valorNacional = cols.text();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if(valorNacional == null){
                textValorNacional.setText(" @Nacional R$ "+"126,00");
            }else {
                textValorNacional.setText(" @Nacional R$ " + valorNacional);
            }
                mProgressDialog.dismiss();

        }
    }
}
