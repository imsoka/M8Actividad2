package com.example.actividad2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
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
                //Abrimos conexión
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                //Lanzamos petición
                connection.connect();

                StringBuilder respuesta;
                //Cogemos la respuesta
                try(BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))){
                    respuesta = new StringBuilder();
                    String respuestaLinea;
                    while((respuestaLinea = br.readLine()) != null) {
                        respuesta.append(respuestaLinea.trim());
                    }
                    result = respuesta.toString();
                 }
            } catch (Exception exception) {
                //Si no hay conexión a internet o la petición falla, cogemos los datos de BBDD.
                coins = getAllCoins();
                renderCoins();
                progressDialog.dismiss();
            } finally {
                return result;
            }
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            //Si por alguna razón el resultado está vacio, nos quedamos aquí.
            if(resultado.isEmpty()) return;

            //Transformamos el XML en un objeto que podamos iterar
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                InputSource inputSource = new InputSource(new StringReader(resultado));
                Document document = documentBuilder.parse(inputSource);

                NodeList nodeList = document.getElementsByTagName("Cube");
                coins = new ArrayList<Coin>();
                //Iteramos el XML y creamos todas las instancias de Coin que haga falta
                for(int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);

                    if(node.hasAttributes() && !node.hasChildNodes()) {
                        Element element = (Element) node;
                        String coinName = element.getAttribute("currency");
                        String rate = element.getAttribute("rate");

                        Coin coin = new Coin(coinName, rate);

                        //Si la moneda no existe en base de datos, la creamos, si existe, la actualizamos
                        if(!coinExist(coin.name)) {
                            saveCoin(coin);
                        } else {
                            updateCoin(coin);
                        }
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

    //Renderizamos con el CoinAdapter, todas las monedas en la lista.
    void renderCoins(){
        ListView listViewCoins = (ListView) findViewById(R.id.listViewCoins);
        TextView tvError = (TextView) findViewById(R.id.tvError);
        if(!coins.isEmpty()) {
            CoinAdapter coinAdapter = new CoinAdapter(this, coins);
            listViewCoins.setAdapter(coinAdapter);
            listViewCoins.setVisibility(View.VISIBLE);
            tvError.setVisibility(View.INVISIBLE);
            return;
        }
        listViewCoins.setVisibility(View.INVISIBLE);
        tvError.setVisibility(View.VISIBLE);

    }

    //Metodo para guardar moneda
    void saveCoin(Coin coin){

        //Instanciamos base de datos y helper
        SQLiteOpenHelper dbHelper = new CoinRepositoryDbHelper(this);
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        //Los valores que vamos a insertar
        contentValues.put(CoinRepositoryContract.FeedEntry.COLUMN_NAME_NAME, coin.name);
        contentValues.put(CoinRepositoryContract.FeedEntry.COLUMN_NAME_RATE, coin.ratio);

        //Insertamos los valores
        database.insert(CoinRepositoryContract.FeedEntry.TABLE_NAME, null, contentValues);
    }

    //Recupera una moneda de base de datos a partir del nombre
    Coin getCoin(String name) {
        //Instanciamos la base de datos y el helper
        SQLiteOpenHelper dbHelper = new CoinRepositoryDbHelper(this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        //Los valores que vamos a recuperar
        String[] projection = {
                BaseColumns._ID,
                CoinRepositoryContract.FeedEntry.COLUMN_NAME_NAME,
                CoinRepositoryContract.FeedEntry.COLUMN_NAME_RATE,
        };

        //La condicion y el valor de esta
        String selection = CoinRepositoryContract.FeedEntry.COLUMN_NAME_NAME + " = ?";
        String[] selectionArgs = {
                name
        };

        //Recuperamos en un cursor el resultado
        Cursor cursor = database.query(
                CoinRepositoryContract.FeedEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        //Si hay resultados, creamos la instancia de moneda y la devolvemos
        if(cursor.moveToNext()) {

            String coinName = cursor.getString(cursor.getColumnIndexOrThrow(CoinRepositoryContract.FeedEntry.COLUMN_NAME_NAME));
            String coinRate = cursor.getString(cursor.getColumnIndexOrThrow(CoinRepositoryContract.FeedEntry.COLUMN_NAME_RATE));
            return new Coin(
                    coinName,
                    coinRate
            );
        }

        //Si no hay resultado devolvemos null
        return null;
    }

    //Recuperamos todas las monedas
    ArrayList<Coin> getAllCoins() {
        ArrayList<Coin> coins = new ArrayList<Coin>();

        //Instanciamos base de datos y helper
        SQLiteOpenHelper dbHelper = new CoinRepositoryDbHelper(this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        //Las columnas que vamos a recuperar
        String[] projection = {
                BaseColumns._ID,
                CoinRepositoryContract.FeedEntry.COLUMN_NAME_NAME,
                CoinRepositoryContract.FeedEntry.COLUMN_NAME_RATE,
        };

        //Hacemos la query
        Cursor cursor = database.query(
                CoinRepositoryContract.FeedEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        //Por cada resultado, insertamos en el arrayList una moneda, que instanciamos
        while(cursor.moveToNext()) {
            String coinName = cursor.getString(cursor.getColumnIndexOrThrow(CoinRepositoryContract.FeedEntry.COLUMN_NAME_NAME));
            String coinRate = cursor.getString(cursor.getColumnIndexOrThrow(CoinRepositoryContract.FeedEntry.COLUMN_NAME_RATE));
            Coin coin = new Coin(
                    coinName,
                    coinRate
            );

            coins.add(coin);
        }

        //Devuelve las monedas en un ArrayList o uno vacío si no hay resultados
        return coins;
    }

    //Determina si una moneda exist en base de datos por su nombre
    boolean coinExist(String name) {
        //Intenamos recuperar la moneda
        Coin coin = getCoin(name);

        //Devuelve true o false si la moneda ha sido recuperada
        return coin != null;
    }

    //Actualizar el ratio de una moneda en base de datos
    void updateCoin(Coin coin) {

        //Instanciamos base de datos y helper
        SQLiteOpenHelper dbHelper = new CoinRepositoryDbHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        //Recuperamos la moneda que existe en la base de datos
        Coin oldCoin = getCoin(coin.name);

        //Si el ratio es diferente, lo actualizamos
        if(!coin.ratio.equals(oldCoin.ratio)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(CoinRepositoryContract.FeedEntry.COLUMN_NAME_RATE, coin.ratio);
            String selection = CoinRepositoryContract.FeedEntry.COLUMN_NAME_RATE + "LIKE ?";
            String[] selectionArgs = { coin.ratio };

            database.update(
                    CoinRepositoryContract.FeedEntry.TABLE_NAME,
                    contentValues,
                    selection,
                    selectionArgs
            );
        }
    }
}


