package com.example.diet;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class AyarlarDB extends RealmObject {
    @Required
    String gunlukpay;

    public String getGunlukpay() {
        return gunlukpay;
    }

    public void setGunlukpay(String gunlukpay) {
        this.gunlukpay = gunlukpay;
    }
}
