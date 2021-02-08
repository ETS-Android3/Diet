package com.example.diet;

import io.realm.RealmObject;

public class OzelYiyecek extends RealmObject {
    String isim;
    String pay,birimgram;

    @Override
    public String toString() {
        return "OzelYiyecek{" +
                "isim='" + isim + '\'' +
                ", pay='" + pay + '\'' +
                ", birimgram='" + birimgram + '\'' +
                '}';
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getBirimgram() {
        return birimgram;
    }

    public void setBirimgram(String birimgram) {
        this.birimgram = birimgram;
    }


}
