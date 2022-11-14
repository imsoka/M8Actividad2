package com.example.actividad2;

import android.provider.BaseColumns;

public class CoinRepositoryContract {
    private CoinRepositoryContract() {}

    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "Coins";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_RATE = "rate";
    }
}
