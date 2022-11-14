package com.example.actividad2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CoinAdapter extends ArrayAdapter<Coin> {

    public CoinAdapter(@NonNull Context context, ArrayList<Coin> coins) {
        super(context, 0, coins);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Coin coin = this.getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.coin_item, parent, false);
        }

        TextView tvCoinName = (TextView) convertView.findViewById(R.id.tvCoinName);
        TextView tvRatio = (TextView) convertView.findViewById(R.id.tvRatio);

        tvCoinName.setText(coin.name);
        tvRatio.setText(coin.ratio);

        return convertView;
    }
}
