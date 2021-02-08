package com.example.diet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class Diyet extends AppCompatActivity {
    TextView anamenu;
    Button bbugun, byiyecek, bykarisim, bbiray,bkayitaktar,bayarlar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diyet);
        realmBaslat();
        tanimla();
        buttonlaraClickListenerEkle();
        Veritabani.onCreate();
    }
    public void realmBaslat(){
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(".diyetdb").schemaVersion(4)
                .allowWritesOnUiThread(true)
                .allowQueriesOnUiThread(true)
                .build();
        Realm.setDefaultConfiguration(config);
        Veritabani.db=Realm.getDefaultInstance();
    }
    public void tanimla() {
        anamenu = findViewById(R.id.baslik);
        bbugun = findViewById(R.id.butBugun);
        byiyecek = findViewById(R.id.butYiyecek);
        bbiray = findViewById(R.id.butSonAy);
        bykarisim = findViewById(R.id.butYiyecekKarisim);
        bkayitaktar = findViewById(R.id.butkayitaktar);
        bayarlar= findViewById(R.id.butAyarlar);
    }


    public void buttonlaraClickListenerEkle() {
        final AppCompatActivity ths = this;
        final Intent intent1 = new Intent(ths, Bugun.class);
        bbugun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent1);
            }
        });
        final Intent intent2 = new Intent(ths, YiyecekEkleSil.class);
        byiyecek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent2);
            }
        });
        final Intent intent3 = new Intent(ths, YKarisimEkleSil.class);
        bykarisim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent3);
            }
        });
        final Intent intent4 = new Intent(ths, SonBirAy.class);
        bbiray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent4);
            }
        });
        final Intent intent5 = new Intent(ths, KayitAktar.class);
        bkayitaktar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent5);
            }
        });
        final Intent intent6 = new Intent(ths,Ayarlar.class);
        bayarlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent6);
            }
        });
    }



}