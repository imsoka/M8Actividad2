package com.example.actividad2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class CoinListActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    ArrayList<Coin> coins;
    CoinAdapter coinAdapter;
    Context context;
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
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                InputSource inputSource = new InputSource(new StringReader(resultado));
                Document document = documentBuilder.parse(inputSource);

                NodeList nodeList = document.getElementsByTagName("Cube");
                coins = new ArrayList<Coin>();
                for(int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);

                    if(node.hasAttributes() && !node.hasChildNodes()) {
                        Element element = (Element) node;
                        String coinName = element.getAttribute("currency");
                        String rate = element.getAttribute("rate");

                        Coin coin = new Coin(coinName, rate);
                        coins.add(coin);
                    }
                }
                renderCoins();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            progressDialog.dismiss();
        }
    }

    void renderCoins(){
        ListView listViewCoins = (ListView) findViewById(R.id.listViewCoins);
        CoinAdapter coinAdapter = new CoinAdapter(this, coins);
        listViewCoins.setAdapter(coinAdapter);
    }
}


