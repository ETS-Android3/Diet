package com.example.diet;

import android.util.Log;

import io.realm.Realm;
import io.realm.RealmResults;

public class Veritabani {
    static Realm db;;
    static AyarlarDB ayarlar;
    public static void onCreate(){
        RealmResults<AyarlarDB> res = db.where(AyarlarDB.class).findAll();
        if(res.size()==0)
        {
            db.beginTransaction();
            ayarlar = db.createObject(AyarlarDB.class);
            ayarlar.setGunlukpay("24");
            db.commitTransaction();
        }
        else
            ayarlar = res.first();
    }

    public static <RealmModeli> void tumKayitlarLog(Class<RealmModeli> clss){
        RealmResults<RealmModeli> res=db.where((Class) clss).findAll();
        for(RealmModeli y : res){
            Log.i("Kayitlarr",y.toString());
        }
    }
}
