package com.example.diet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public class YiyecekEkleSil extends AppCompatActivity {
    Button bsec, bsil, bkaydet,bduzenle;
    EditText ad, gram,duzenlead,duzenlegram;
    AramaDiyalog dia;
    Yiyecek seciliYiyecek;
    ScrollView scrollv;
    UyariDiyalog uyari;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yiyecekeklesil);
        tanimla();
        buttunlarListenerOlustur();
    }
    public void yiyecekSecSil(){
        bsec.setText("Yiyecek Seç");
        bsec.setBackgroundColor(Color.rgb(255,234,219));
    }

    public void setDuzenleEditTextAktif(boolean val){
        duzenlead.setFocusableInTouchMode(val);
        duzenlead.setFocusable(val);
        duzenlegram.setFocusableInTouchMode(val);
        duzenlegram.setFocusable(val);
    }

    public void yiyecekSecYukle(){
        bsec.setText("Seçili: "+seciliYiyecek.getIsim().toUpperCase());
        bsec.setBackgroundColor(Color.rgb(255,245,145));
    }

    public void tanimla() {
        bsec = findViewById(R.id.butSecSil);
        bsil = findViewById(R.id.butSil);
        bkaydet = findViewById(R.id.butKaydet);
        ad = findViewById(R.id.adText);
        gram = findViewById(R.id.gramText);
        bduzenle = findViewById(R.id.butDuzenle);
        duzenlead = findViewById(R.id.duzenlenenAdText);
        duzenlegram = findViewById(R.id.duzenlenenGramText);
        scrollv = findViewById(R.id.yiyecekeklesilscroll);
        uyari = new UyariDiyalog(this,"");

        setDuzenleEditTextAktif(false);
        View.OnClickListener duzenleler = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(seciliYiyecek==null) {
                    uyari.setMsg("Düzenlemek veya Silmek İçin Öncelikle Düzenlenecek Veya Silinecek Yiyeceği Yukarıdan Seç");
                    uyari.show();
                }
            }
        };
        duzenlead.setOnClickListener(duzenleler);
        duzenlegram.setOnClickListener(duzenleler);


        dia = new AramaDiyalog(this);
        AramaListAdapter adp = new AramaListAdapter(null, dia.getContext());
        dia.liste.setAdapter(adp);
        AramaTextWatcher aramaTW = new AramaTextWatcher(adp,dia);
        AramaItemClick aramaIOC = new AramaItemClick(dia);
        dia.arama.addTextChangedListener(aramaTW);
        dia.liste.setOnItemClickListener(aramaIOC);
    }

    public void buttunlarListenerOlustur() {
        bsec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dia.arama.setText("");
                dia.show();
            }
        });


        final AlertDialog.Builder builder = new AlertDialog.Builder(YiyecekEkleSil.this);
        final AlertDialog.OnClickListener lst = new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case DialogInterface.BUTTON_POSITIVE:
                        Veritabani.db.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                    seciliYiyecek.deleteFromRealm();
                            }
                        });
                        uyari.setMsg("Kayit Silindi.");
                        uyari.show();

                        seciliYiyecek=null;
                        yiyecekSecSil();
                        duzenlead.setText("");
                        duzenlegram.setText("");
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        return;
                }
            }
        };
        bsil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (seciliYiyecek == null) {
                    uyari.setMsg("Lütfen İlk Önce Yiyecek Seçiniz.");
                    uyari.show();
                }
                else {
                    builder.setMessage(String.format(Locale.ROOT,"İsim: %s\nPay: %s\nGram: %s\n Bilgilerine Sahip Kayıdı Silmek İstiyor Musunuz?", seciliYiyecek.getIsim(), "1" , seciliYiyecek.getBirimgram()))
                            .setPositiveButton("Evet", lst).setNegativeButton("Hayır", lst).show();
                }
            }
        });


        bkaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Pattern.compile("[0-9a-zA-Z]+").matcher(ad.getText().toString()).find() && !gram.getText().toString().equals("")){
                    Veritabani.db.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            try {
                                Yiyecek yeni = realm.createObject(Yiyecek.class, ad.getText().toString());
                                yeni.setBirimgram(gram.getText().toString());

                                uyari.setMsg("Kayıt Başarılı.");
                                uyari.show();

                                ad.setText("");
                                gram.setText("");
                            }catch (RealmPrimaryKeyConstraintException e){
                                uyari.setMsg("Kayit Başarısız! Bu İsime Sahip Kayıt Zaten Var.");
                                uyari.show();

                            }
                        }
                    });
                }
            }
        });
        bduzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (seciliYiyecek == null) {
                    uyari.setMsg("Lütfen İlk Önce Yiyecek Seçiniz.");
                    uyari.show();
                }
                else{
                    if(Pattern.compile("[0-9a-zA-Z]+").matcher(duzenlead.getText().toString()).find() && !duzenlegram.getText().toString().equals("")){
                        Veritabani.db.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                if(seciliYiyecek.getIsim().equals(duzenlead.getText().toString()))
                                    seciliYiyecek.setBirimgram(duzenlegram.getText().toString());
                                else{
                                    seciliYiyecek.deleteFromRealm();
                                    seciliYiyecek = Veritabani.db.createObject(Yiyecek.class,duzenlead.getText().toString());
                                    seciliYiyecek.setBirimgram(duzenlegram.getText().toString());
                                }
                            }
                        });
                        yiyecekSecYukle();

                        uyari.setMsg("Yiyecek Başarıyla Düzenlendi.");
                        uyari.show();
                    }
                }
            }
        });
    }
}