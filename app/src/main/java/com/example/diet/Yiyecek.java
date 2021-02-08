package com.example.diet;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Yiyecek extends RealmObject {
    @PrimaryKey
    String isim;

    String birimgram;

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public String getBirimgram() {
        return birimgram;
    }

    public void setBirimgram(String birimgram) {
        this.birimgram = birimgram;
    }

    @Override
    public String toString() {
        return "Yiyecek{" +
                "isim='" + isim + '\'' +
                ", birimgram='" + birimgram + '\'' +
                '}';
    }
}
