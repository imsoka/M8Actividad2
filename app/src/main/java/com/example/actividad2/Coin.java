package com.example.actividad2;

public class Coin {
    public String name;
    public String ratio;
    public String image;

    public Coin(String name, String ratio) {
        this.name = name;
        this.ratio = ratio;

        StringBuilder image = new StringBuilder();

        image.append(this.name);
        image.append(".png");

        this.image = image.toString();
    }
}
