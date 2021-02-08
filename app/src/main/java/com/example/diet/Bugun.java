package com.example.diet;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

public class Bugun extends AppCompatActivity {
    AramaDiyalog dia;
    Button bsec,bekle;
    EditText payGir,gramGir;
    TextView detay;
    ListView liste;
    Yiyecek seciliYiyecek;
    GunlukKayit bugun;
    UyariDiyalog uyari;
    boolean eklenebilir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bugun);
        tanimla();
        listeOlustur();
        clickListenerlerOlustur();
    }

    public void yiyecekSecSil(){
        bsec.setText("Yiyecek Seç");
        bsec.setBackgroundColor(Color.rgb(255,234,219));
    }

    public void yiyecekSecYukle(){
        bsec.setText("Seçili: "+seciliYiyecek.getIsim().toUpperCase());
        bsec.setBackgroundColor(Color.rgb(255,245,145));
    }

    public void bugunGuncelle(){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Istanbul"));
        cal.setTime(new Date());
        cal.add(Calendar.MONTH,+1);
        RealmResults<GunlukKayit> res= Veritabani.db.where(GunlukKayit.class)
                .equalTo("yil",cal.get(Calendar.YEAR))
                .and()
                .equalTo("ay",cal.get(Calendar.MONTH))
                .and()
                .equalTo("gun",cal.get(Calendar.DAY_OF_MONTH)).findAll();
        if(res.size()!=0) {
            bugun = res.first();
            Veritabani.db.beginTransaction();
                bugun.setToplampay(Veritabani.ayarlar.getGunlukpay());
            Veritabani.db.commitTransaction();
        }
        else
        {
            Veritabani.db.beginTransaction();
            bugun = Veritabani.db.createObject(GunlukKayit.class,String.format(Locale.ROOT,"%d%d%d",cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)));
            bugun.setYil(cal.get(Calendar.YEAR));
            bugun.setAy(cal.get(Calendar.MONTH));
            bugun.setGun(cal.get(Calendar.DAY_OF_MONTH));
            bugun.setToplampay(Veritabani.ayarlar.getGunlukpay());
            bugun.setKullanilanpay("0");
            bugun.setYiyecekler(new RealmList<OzelYiyecek>());
            String[] flds = {"yil","ay","gun"};
            Sort[] srts = {Sort.DESCENDING,Sort.DESCENDING,Sort.DESCENDING};
            RealmResults<GunlukKayit> sonuclar = Veritabani.db.where(GunlukKayit.class).sort(flds,srts).findAll();
            if(sonuclar.size()>30)
                for(int i=30;i<sonuclar.size();i++)
                    sonuclar.get(i).deleteFromRealm();
            Veritabani.db.commitTransaction();
        }
    }

    public void detayGuncelle(){
        detay.setText(String.format(Locale.ROOT,"Tarih: %d.%d.%d\nBugünki Toplam Pay: %s\nToplam Kullanılan Pay: %s\nKalan Pay: %.2f",bugun.getGun(),bugun.getAy(),bugun.getYil(),bugun.getToplampay(),bugun.getKullanilanpay(),Float.valueOf(bugun.getToplampay())-Float.valueOf(bugun.getKullanilanpay())));
    }

    public void listeOlustur(){
        if(bugun!=null) {
            BugunAdapter adapter = new BugunAdapter(bugun.getYiyecekler(), this.getApplicationContext());
            liste.setAdapter(adapter);
        }
    }

    public void eklenebilirlik(boolean val){
        payGir.setFocusableInTouchMode(val);
        payGir.setCursorVisible(val);
        gramGir.setFocusableInTouchMode(val);
        gramGir.setCursorVisible(val);
        bekle.setClickable(val);
        eklenebilir=val;
    }

    public void tanimla(){
        bsec = findViewById(R.id.butSec);
        bekle = findViewById(R.id.butEkle);
        gramGir = findViewById(R.id.gramGir);
        payGir = findViewById(R.id.payGir);
        liste = findViewById(R.id.liste);
        detay = findViewById(R.id.detay);
        uyari = new UyariDiyalog(this,"");

        eklenebilirlik(false);


        bugunGuncelle();
        detayGuncelle();

        dia = new AramaDiyalog(Bugun.this);
        AramaListAdapter adp = new AramaListAdapter(null, dia.getContext());
        dia.liste.setAdapter(adp);
        AramaTextWatcher aramaTW = new AramaTextWatcher(adp,dia);
        AramaItemClick aramaIOC = new AramaItemClick(dia);
        dia.arama.addTextChangedListener(aramaTW);
        dia.liste.setOnItemClickListener(aramaIOC);

    }

    public void clickListenerlerOlustur(){
        bsec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dia.arama.setText("");
                dia.show();
            }
        });

        payGir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!eklenebilir){
                    uyari.setMsg("Lütfen Önce Yiyecek Seçiniz.");
                    uyari.show();
                }
            }
        });
        gramGir.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!eklenebilir)
                {
                    uyari.setMsg("Lütfen Önce Yiyecek Seçiniz.");
                    uyari.show();
                }
            }
        }));

        bekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!eklenebilir)
                {
                    uyari.setMsg("Lütfen Önce Yiyecek Seçiniz.");
                    uyari.show();
                }
                else{
                    if(payGir.getText().toString().equals("") || gramGir.getText().toString().equals("") || Float.valueOf(payGir.getText().toString()).floatValue()==0.0 || Float.valueOf(gramGir.getText().toString()).floatValue()==0.0 ) {
                        uyari.setMsg("Doğru Değerler Giriniz. Pay ve gram değerleri boş bırakılamaz veya 0 yazılamaz.( Örnek: 52.12 )");
                        uyari.show();
                        return;
                    }
                    bugunGuncelle();
                    BugunAdapter badpt=((BugunAdapter)liste.getAdapter());
                    badpt.yiyecekList=bugun.yiyecekler;

                    OzelYiyecek ozl = new OzelYiyecek();
                    ozl.isim=seciliYiyecek.getIsim();
                    ozl.birimgram=seciliYiyecek.getBirimgram();
                    ozl.pay=String.format(Locale.ROOT,"%.2f",Float.valueOf(payGir.getText().toString()));
                    final OzelYiyecek fozl =ozl;
                    if(Float.valueOf(bugun.getKullanilanpay())+Float.valueOf(ozl.getPay())>Float.valueOf(bugun.getToplampay())){
                        AlertDialog.Builder builder = new AlertDialog.Builder(Bugun.this);
                        DialogInterface.OnClickListener lst =  new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        Veritabani.db.executeTransaction(new Realm.Transaction() {
                                            @Override
                                            public void execute(Realm realm) {
                                                if(bugun.getYiyecekler().add(fozl)) {
                                                    bugun.setKullanilanpay(String.format(Locale.ROOT,"%.2f",Float.valueOf(bugun.getKullanilanpay())+Float.valueOf(fozl.getPay())));
                                                    uyari.setMsg("Yiyecek Başarıyla Kaydedildi.");
                                                    uyari.show();
                                                }
                                            }
                                        });
                                        break;
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        return;
                                }
                                ((BaseAdapter)liste.getAdapter()).notifyDataSetChanged();
                                detayGuncelle();
                            }
                        };
                        builder.setMessage("Bugünki Sınırınızı "
                                +String.format(Locale.ROOT,"%.2f",Float.valueOf(bugun.getToplampay())-Float.valueOf(bugun.getKullanilanpay())-Float.valueOf(ozl.getPay()))
                                +" Pay Kadar Aşıyorsunuz Yiyeceği Eklemek İstediğinize Emin Misiniz?").setPositiveButton("Evet",lst).setNegativeButton("Hayır",lst).show();
                    }
                    else{
                        Veritabani.db.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                if(bugun.getYiyecekler().add(fozl)) {
                                    bugun.setKullanilanpay(String.format(Locale.ROOT,"%.2f",Float.valueOf(bugun.getKullanilanpay())+Float.valueOf(fozl.getPay())));
                                    uyari.setMsg("Yiyecek Başarıyla Kaydedildi");
                                    uyari.show();
                                }
                            }
                        });
                        ((BaseAdapter)liste.getAdapter()).notifyDataSetChanged();
                        detayGuncelle();
                    }
                    payGir.setText("");
                    yiyecekSecSil();
                    seciliYiyecek=null;
                    eklenebilirlik(false);
                }
            }
        });

        PayDegisti pTW = new PayDegisti(this);
        GramDegisti gTW = new GramDegisti(this);
        pTW.gTW=gTW;
        gTW.pTW=pTW;

        payGir.addTextChangedListener(pTW);
        gramGir.addTextChangedListener(gTW);

        liste.setOnItemClickListener(new BugunItemClick(this));

    }
}

