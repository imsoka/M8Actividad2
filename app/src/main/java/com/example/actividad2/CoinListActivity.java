package com.example.actividad2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CoinListActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_list);
        String url = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
        CoinTask coinTask = new CoinTask();
        coinTask.execute(url);
    }

    public class CoinTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CoinListActivity.this);
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            String result = "";
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                StringBuilder respuesta;
                try(BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))){
                    respuesta = new StringBuilder();
                    String respuestaLinea;
                    while((respuestaLinea = br.readLine()) != null) {
                        respuesta.append(respuestaLinea.trim());
                    }
                    result = respuesta.toString();
                 }
            } catch (Exception exception) {
                result = "Error";
            } finally {
                return result;
            }
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            TextView ejemploXML = (TextView) findViewById(R.id.ejemploXML);
            ejemploXML.setText(resultado);
            progressDialog.dismiss();
        }
    }
}


