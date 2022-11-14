package com.example.actividad2;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class FruitAdapter extends ArrayAdapter<Fruit> {

    public FruitAdapter(@NonNull Context context, ArrayList<Fruit> fruits) {
        super(context, 0, fruits);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Fruit fruit = this.getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.fruit_item, parent, false);
        }

        ImageView ivFruit = (ImageView) convertView.findViewById(R.id.ivFruit);
        TextView tvFruitName = (TextView) convertView.findViewById(R.id.tvFruitName);
        EditText etFruitQuantity = (EditText) convertView.findViewById(R.id.etFruitQuantity);

        Bitmap imgFrutas = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.frutas);

        imgFrutas = Bitmap.createBitmap(
                imgFrutas,
                fruit.imgXPosition*imgFrutas.getWidth()/4,
                fruit.imgYPosition*imgFrutas.getHeight()/4,
                imgFrutas.getWidth()/4,
                imgFrutas.getHeight()/4);

        ivFruit.setImageBitmap(imgFrutas);
        tvFruitName.setText(fruit.name);

        Button butIncrease = (Button) convertView.findViewById(R.id.butIncrease);
        Button butDecrease = (Button) convertView.findViewById(R.id.butDecrease);

        butIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = 0;
                if(etFruitQuantity.getText() != null) {
                    quantity = Integer.parseInt(etFruitQuantity.getText().toString());
                }
                quantity++;
                etFruitQuantity.setText(Integer.toString(quantity));
            }
        });

        View finalConvertView = convertView;
        butDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = 0;
                if(etFruitQuantity.getText() == null) {
                    return;
                }
                quantity = Integer.parseInt(etFruitQuantity.getText().toString());
                if(quantity > 0) {
                    quantity--;
                    etFruitQuantity.setText(Integer.toString(quantity));
                    return;
                }
                Toast notification = Toast.makeText(
                        finalConvertView.getContext(),
                        "No se puede bajar de 0",
                        Toast.LENGTH_LONG
                );
                notification.show();
            }
        });

        return convertView;
    }
}
