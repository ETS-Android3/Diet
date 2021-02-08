package com.example.diet;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import java.util.Locale;

import io.realm.Realm;


public class KarisimYiyecekEkle extends Dialog {
    YKarisimEkleSil prnt;
    AramaDiyalog dia;
    Button bsec,bekle;
    EditText payGir,gramGir;
    Yiyecek seciliYiyecek;
    UyariDiyalog uyari;
    boolean eklenebilir;

    public KarisimYiyecekEkle(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.karisimyiyecekekle);
        prnt = (YKarisimEkleSil)context;
        tanimla();
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

    public void eklenebilirlik(boolean val){
        payGir.setFocusableInTouchMode(val);
        payGir.setCursorVisible(val);
        gramGir.setFocusableInTouchMode(val);
        gramGir.setCursorVisible(val);
        bekle.setClickable(val);
        eklenebilir=val;
    }
    public void tanimla(){
        bsec = findViewById(R.id.butSecKarisimEkle);
        bekle = findViewById(R.id.butEkleKarisimEkle);
        gramGir = findViewById(R.id.gramGirKarisimEkle);
        payGir = findViewById(R.id.payGirKarisimEkle);
        uyari = new UyariDiyalog(getContext(),"");

        eklenebilirlik(false);


        dia = new AramaDiyalog(prnt);
        AramaListAdapter adp = new AramaListAdapter(null, dia.getContext());
        dia.liste.setAdapter(adp);
        AramaTextWatcher aramaTW = new AramaTextWatcher(adp,dia);
        AramaItemClick aramaIOC = new AramaItemClick(dia);
        dia.arama.addTextChangedListener(aramaTW);
        dia.liste.setOnItemClickListener(aramaIOC);

        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                seciliYiyecek=null;
                yiyecekSecSil();
                payGir.setText("");
                gramGir.setText("");
            }
        });
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
                if(!eklenebilir) {
                    uyari.setMsg("Lütfen Önce Yiyecek Seçiniz.");
                    uyari.show();
                }
            }
        });
        gramGir.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!eklenebilir){
                    uyari.setMsg("Lütfen Önce Yiyecek Seçiniz.");
                    uyari.show();
                }
            }
        }));

        bekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!eklenebilir){
                    uyari.setMsg("Lütfen Önce Yiyecek Seçiniz.");
                    uyari.show();
                }
                else{
                    if(payGir.getText().toString().equals("") || gramGir.getText().toString().equals("") || Float.valueOf(payGir.getText().toString()).floatValue()==0.0 || Float.valueOf(gramGir.getText().toString()).floatValue()==0.0 ) {
                        uyari.setMsg("Doğru Değerler Giriniz. Pay ve gram değerleri boş bırakılamaz veya 0 yazılamaz.( Örnek: 52.12 )");
                        uyari.show();
                        return;
                    }

                    Veritabani.db.executeTransaction(new Realm.Transaction() {
                        public void execute(Realm realm) {
                            OzelYiyecek ozl = realm.createObject(OzelYiyecek.class);
                            ozl.isim=seciliYiyecek.getIsim();
                            ozl.birimgram=seciliYiyecek.getBirimgram();
                            ozl.pay=String.format(Locale.ROOT,"%.2f",Float.valueOf(payGir.getText().toString()));
                            prnt.seciliyarim.eklenenler.add(ozl);
                        }
                    });
                    ((BaseAdapter)prnt.liste.getAdapter()).notifyDataSetChanged();
                    seciliYiyecek=null;
                    yiyecekSecSil();
                    payGir.setText("");
                    gramGir.setText("");
                    dismiss();
                }
            }
        });

        PayDegisti pTW = new PayDegisti(prnt);
        GramDegisti gTW = new GramDegisti(prnt);
        pTW.gTW=gTW;
        gTW.pTW=pTW;

        payGir.addTextChangedListener(pTW);
        gramGir.addTextChangedListener(gTW);

    }

}

