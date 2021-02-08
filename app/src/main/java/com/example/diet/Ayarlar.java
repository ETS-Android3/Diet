package com.example.diet;

import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import io.realm.Realm;

public class Ayarlar extends AppCompatActivity{

    EditText gunlukpay;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ayarlar);
        tanimla();
    }
    public void tanimla(){
        gunlukpay = findViewById(R.id.editGunlukPay);
        gunlukpay.setText(Veritabani.ayarlar.getGunlukpay());
    }
    @Override
    protected void onStop() {
        super.onStop();
        Veritabani.db.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Veritabani.ayarlar.setGunlukpay(gunlukpay.getText().toString());
            }
        });
    }
}
