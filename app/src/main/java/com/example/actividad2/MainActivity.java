package com.example.actividad2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.LlamadaAdaptadorSimpleSpinner();
    }

    public void LLamadaAdaptadorSimpleListViewConstructor1(View view) {
        // Este es el array en donde están los datos a visualizar
        String[] jmh_opciones = {"hola","adios","Viernes"};
        //Definimos el adaptador
        ArrayAdapter<String> jmh_adapter1 = new ArrayAdapter <String>
                (getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,jmh_opciones);
        //Creamos variable que apunte al ListView del Layout
        ListView jmh_origen = (ListView) findViewById(R.id.listviewjhm);
        //Inflamos los valores del ListView usando el adaptador
        jmh_origen.setAdapter(jmh_adapter1);
    }
    public void LLamadaAdaptadorSimpleListViewConstructor2(View view) {
        // Este es el array en donde están los datos a visualizar
        String[] jmh_opciones = {"hola","adios"};
        //Definimos el adaptador
        ArrayAdapter<String>adapter_jmh=new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_2,android.R.id.text1, jmh_opciones);
        //Creamos variable que apunte al ListView del Layout
        ListView jmh_origen = (ListView) findViewById(R.id.listviewjhm);
        //Inflamos los valores del ListView usando el adaptador
        jmh_origen.setAdapter(adapter_jmh);
    }
    public void LlamadaAdaptadorSimpleSpinner() {
        // Este es el array en donde están los datos a visualizar
        String[] jmh_opciones = {"hola","adios"};
        //Definimos el adaptador
        ArrayAdapter<String> jmh_adapter1 = new ArrayAdapter <String> (MainActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,jmh_opciones);
        //Creamos variable que apunte al spinner del Layout
        Spinner jmh_origen = (Spinner) findViewById(R.id.spinnerjmh);
        //Inflamos los valores del spinner usando el adaptador
        jmh_origen.setAdapter(jmh_adapter1);


    }

    public void LlamadaAdaptadorPropioListView(View view) {
        // Creamos los datos
        ArrayList<User> arrayOfUsers_jmh = new ArrayList<User>();
        arrayOfUsers_jmh.add(new User("Nathan", "San Diego"));
        arrayOfUsers_jmh.add(new User("Nathan", "San Diego"));
        arrayOfUsers_jmh.add(new User("Nathan", "San Diego"));
        arrayOfUsers_jmh.add(new User("Nathan", "San Diego"));
        arrayOfUsers_jmh.add(new User("Nathan", "San Diego"));
        arrayOfUsers_jmh.add(new User("Nathan", "San Diego"));
        arrayOfUsers_jmh.add(new User("Nathan", "San Diego"));
        arrayOfUsers_jmh.add(new User("Nathan", "San Diego"));
        arrayOfUsers_jmh.add(new User("Nathan", "San Diego"));
        arrayOfUsers_jmh.add(new User("Nathan", "San Diego"));
        arrayOfUsers_jmh.add(new User("Nathan", "San Diego"));
        arrayOfUsers_jmh.add(new User("Nathan", "San Diego"));
        arrayOfUsers_jmh.add(new User("Nathan", "San Diego"));
        arrayOfUsers_jmh.add(new User("Nathan", "San Diego"));
        arrayOfUsers_jmh.add(new User("Nathan", "San Diego"));
        arrayOfUsers_jmh.add(new User("Nathan", "San Diego"));
        arrayOfUsers_jmh.add(new User("Nathan", "San Diego"));
        // Definimos el adaptador propio. En este caso no posee layout.
        UsersAdapter adapter_jmh = new UsersAdapter(this, arrayOfUsers_jmh);
        // Attach the adapter to a ListView
        ListView listView_jmh = (ListView) findViewById(R.id.listviewjhm);
        listView_jmh.setAdapter(adapter_jmh);
        // Limpiar el adaptador
        //adapter_jmh.clear();
    }

    public void renderFruits(View view){
        Intent intent = new Intent(this, FruitListActivity.class);
        this.startActivity(intent);
    }

    public void renderCoins(View view){
        Intent intent = new Intent(this, CoinListActivity.class);
        this.startActivity(intent);
    }
}









