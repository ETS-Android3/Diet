package com.example.diet;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class YarimYiyecek extends RealmObject {
    @PrimaryKey
    String isim;
    RealmList<OzelYiyecek> eklenenler;
    String toplamgram;
    public YarimYiyecek(){}
    public YarimYiyecek(String isim, RealmList<OzelYiyecek> eklenenler, String toplamgram) {
        this.isim = isim;
        this.eklenenler = eklenenler;
        this.toplamgram = toplamgram;
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public RealmList<OzelYiyecek> getEklenenler() {
        return eklenenler;
    }

    public void setEklenenler(RealmList<OzelYiyecek> eklenenler) {
        this.eklenenler = eklenenler;
    }

    public String getToplamgram() {
        return toplamgram;
    }

    public void setToplamgram(String toplamgram) {
        this.toplamgram = toplamgram;
    }
}
