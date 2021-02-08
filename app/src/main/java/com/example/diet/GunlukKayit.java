package com.example.diet;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class GunlukKayit extends RealmObject {
    @PrimaryKey
    String key;
    int yil,ay,gun;
    RealmList<OzelYiyecek> yiyecekler;
    String toplampay;
    String kullanilanpay;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getYil() {
        return yil;
    }

    public void setYil(int yil) {
        this.yil = yil;
    }

    public int getAy() {
        return ay;
    }

    public void setAy(int ay) {
        this.ay = ay;
    }

    public int getGun() {
        return gun;
    }

    public void setGun(int gun) {
        this.gun = gun;
    }


    public RealmList<OzelYiyecek> getYiyecekler() {
        return yiyecekler;
    }

    public void setYiyecekler(RealmList<OzelYiyecek> yiyecekler) {
        this.yiyecekler = yiyecekler;
    }

    public String getToplampay() {
        return toplampay;
    }

    public void setToplampay(String toplampay) {
        this.toplampay = toplampay;
    }

    public String getKullanilanpay() {
        return kullanilanpay;
    }

    public void setKullanilanpay(String kullanilanpay) {
        this.kullanilanpay = kullanilanpay;
    }

}

