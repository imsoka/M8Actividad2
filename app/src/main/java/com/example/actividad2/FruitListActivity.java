package com.example.actividad2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class FruitListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit_list);
        renderFruitAdapter();
    }

    private void renderFruitAdapter() {
        // Creamos los datos
        ArrayList<Fruit> fruits = new ArrayList<Fruit>();
        fruits.add(new Fruit("Sandía", 0, 0));
        fruits.add(new Fruit("Piña", 1, 0));
        fruits.add(new Fruit("Melón", 2, 0));
        fruits.add(new Fruit("Granada", 3, 0));
        fruits.add(new Fruit("Uvas", 0, 1));
        fruits.add(new Fruit("Limón", 1, 1));
        fruits.add(new Fruit("Plátano", 2,1));
        fruits.add(new Fruit("Kiwi", 3,1));
        fruits.add(new Fruit("Fresa", 0,2));
        fruits.add(new Fruit("Naranja", 1,2));
        fruits.add(new Fruit("Coco", 2,2));
        fruits.add(new Fruit("Cerezas", 3,2));
        fruits.add(new Fruit("Manzana", 0,3));
        fruits.add(new Fruit("Aguacate", 1,3));
        fruits.add(new Fruit("Manzana", 2,3));
        fruits.add(new Fruit("Pera", 3,3));

        // Definimos el adaptador propio. En este caso no posee layout.
        FruitAdapter fruitAdapter = new FruitAdapter(this, fruits);
        // Attach the adapter to a ListView
        ListView listViewFrutas = (ListView) findViewById(R.id.listViewFrutas);
        listViewFrutas.setAdapter(fruitAdapter);
    }
}