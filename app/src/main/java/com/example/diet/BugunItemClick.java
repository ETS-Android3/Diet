package com.example.diet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import io.realm.Realm;

public class BugunItemClick implements AdapterView.OnItemClickListener {

    AppCompatActivity act;
    BugunItemClick(AppCompatActivity act){
        this.act=act;
    }
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(act instanceof Bugun) {
            final Bugun bgn =(Bugun)act;
            final OzelYiyecek gelen = (OzelYiyecek) bgn.liste.getItemAtPosition(i);
            AlertDialog.Builder builder = new AlertDialog.Builder(bgn);
            AlertDialog.OnClickListener lst = new AlertDialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    switch (i) {
                        case DialogInterface.BUTTON_POSITIVE:
                            final int index = bgn.bugun.getYiyecekler().indexOf(gelen);
                            if (index != -1) {
                                bgn.bugunGuncelle();
                                BugunAdapter badpt = ((BugunAdapter) bgn.liste.getAdapter());
                                badpt.yiyecekList = bgn.bugun.yiyecekler;
                                Veritabani.db.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        bgn.bugun.setKullanilanpay(String.format(Locale.ROOT,"%.2f", Float.valueOf(bgn.bugun.getKullanilanpay()) - Float.valueOf(gelen.getPay())));
                                        bgn.bugun.getYiyecekler().deleteFromRealm(index);
                                    }
                                });
                            }
                            ((BaseAdapter) bgn.liste.getAdapter()).notifyDataSetChanged();
                            bgn.detayGuncelle();
                            return;
                        case DialogInterface.BUTTON_NEGATIVE:
                            return;
                    }
                }
            };
            builder.setMessage(String.format(Locale.ROOT,"İsim: %s\nPay: %s\nGram: %.2f\n Bilgilerine Sahip Kayıdı Silmek İstiyor Musunuz?", gelen.getIsim(), gelen.getPay(), Float.valueOf(gelen.getPay()) * Float.valueOf(gelen.getBirimgram())))
                    .setPositiveButton("Evet", lst).setNegativeButton("Hayır", lst).show();
        }
        else if(act instanceof YKarisimEkleSil){
            final YKarisimEkleSil ykar =(YKarisimEkleSil) act;
            final OzelYiyecek gelen = (OzelYiyecek) ykar.liste.getItemAtPosition(i);
            AlertDialog.Builder builder = new AlertDialog.Builder(ykar);
            final AramaListYarimAdapter badpt = ((AramaListYarimAdapter) ykar.liste.getAdapter());
            AlertDialog.OnClickListener lst = new AlertDialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    switch (i) {
                        case DialogInterface.BUTTON_POSITIVE:
                            final int index = ykar.seciliyarim.getEklenenler().indexOf(gelen);
                            if (index != -1) {
                                Veritabani.db.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        if(ykar.seciliyarim.getEklenenler().get(index).isManaged()) {
                                            ykar.seciliyarim.getEklenenler().deleteFromRealm(index);
                                        }
                                    }
                                });
                            }
                            badpt.aramaliste=ykar.seciliyarim.getEklenenler();
                            badpt.notifyDataSetChanged();
                            return;
                        case DialogInterface.BUTTON_NEGATIVE:
                            return;
                    }
                }
            };
            builder.setMessage(String.format(Locale.ROOT,"İsim: %s\nPay: %s\nGram: %.2f\n Bilgilerine Sahip Kayıdı Silmek İstiyor Musunuz?", gelen.getIsim(), gelen.getPay(), Float.valueOf(gelen.getPay()) * Float.valueOf(gelen.getBirimgram())))
                    .setPositiveButton("Evet", lst).setNegativeButton("Hayır", lst).show();
        }
    }
}
