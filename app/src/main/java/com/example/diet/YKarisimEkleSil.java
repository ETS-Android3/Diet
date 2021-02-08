package com.example.diet;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.RealmList;

public class YKarisimEkleSil extends AppCompatActivity {
    Button olustur, yekle, kaydet, sil;
    EditText toplamgram;
    ListView liste;
    YarimYiyecek seciliyarim;
    AramaDiyalog aramadia2;
    KarisimYiyecekEkle yekledia;
    UyariDiyalog uyari;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ykarisimeklesil);
        tanimla();
        buttonClickListenerlarOluştur();
    }

    public void olusturSil(){
        olustur.setText("Yeni Karışım Oluştur Veya Yarıda Kalan Karışım Seç");
        olustur.setBackgroundColor(Color.rgb(255,234,219));
    }

    public void olusturYarımYukle(){
        olustur.setText("Seçili Karışım: "+seciliyarim.getIsim().toUpperCase());
        olustur.setBackgroundColor(Color.rgb(255,245,145));
    }
    public void olusturYeniYukle(){
        olustur.setText("Yeni Karışım: "+seciliyarim.getIsim().toUpperCase());
        olustur.setBackgroundColor(Color.rgb(255,245,145));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (seciliyarim != null) {
            Veritabani.db.beginTransaction();
            if (!toplamgram.getText().toString().equals(""))
                seciliyarim.setToplamgram(toplamgram.getText().toString());
            if (seciliyarim.eklenenler.size() == 0 && seciliyarim.toplamgram == null) {
                YarimYiyecek gln = Veritabani.db.where(YarimYiyecek.class).equalTo("isim", seciliyarim.getIsim()).findFirst();
                if (gln != null)
                    gln.deleteFromRealm();
            } else
                Veritabani.db.insertOrUpdate(seciliyarim);
            Veritabani.db.commitTransaction();
        }
    }

    public void tanimla() {
        olustur = findViewById(R.id.butOlustur);
        yekle = findViewById(R.id.butKarisimaYiyecekEkle);
        kaydet = findViewById(R.id.karisimKaydet);
        toplamgram = findViewById(R.id.toplamGramGir);
        liste = findViewById(R.id.karisimList);
        sil = findViewById(R.id.karisimSil);
        uyari = new UyariDiyalog(this,"");
        yekledia=new KarisimYiyecekEkle(this);

        AramaListYarimAdapter adp = new AramaListYarimAdapter(null, this);
        liste.setAdapter(adp);
        BugunItemClick bic = new BugunItemClick(this);
        liste.setOnItemClickListener(bic);
    }

    public void buttonClickListenerlarOluştur() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(YKarisimEkleSil.this);
        View layout = getLayoutInflater().inflate(R.layout.karisimolustur, null);
        builder.setView(layout);

        Button yarimsec = layout.findViewById(R.id.butYarimSec), olusturdialog = layout.findViewById(R.id.butKarisimOlusturDialog);
        final EditText isimdialog = layout.findViewById(R.id.karisimisimText);

        final AramaDiyalog aramadia = new AramaDiyalog(YKarisimEkleSil.this);
        AramaListYarimAdapter adp = new AramaListYarimAdapter(null, aramadia.prnt);
        aramadia.liste.setAdapter(adp);
        AramaTextWatcher aramaTW = new AramaTextWatcher(adp, aramadia);
        final AramaItemClick aramaIOC = new AramaItemClick(aramadia);
        aramadia.arama.addTextChangedListener(aramaTW);
        aramadia.liste.setOnItemClickListener(aramaIOC);
        TextView tw = aramadia.findViewById(R.id.payTextView);
        tw.setText("Toplam Gramı");
        tw = aramadia.findViewById(R.id.gramTextView);
        tw.setText("Yiyecek Adet");


        yarimsec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aramadia.arama.setText("");
                aramadia.show();
            }
        });
        olusturdialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt = isimdialog.getText().toString();

                if (Pattern.compile("[0-9a-zA-Z]+").matcher(txt).find()) {
                    YarimYiyecek dbobj = Veritabani.db.where(YarimYiyecek.class).equalTo("isim", txt).findFirst();
                    Yiyecek dbobj2 = Veritabani.db.where(Yiyecek.class).equalTo("isim",txt).findFirst();
                    if (dbobj == null && dbobj2==null) {
                        if (seciliyarim != null && seciliyarim.isim.equals(txt)) {
                            uyari.setMsg("Zaten Bu İsme Sahip Karışımı Oluşturmaktasınız.");
                            uyari.show();
                            return;
                        } else {
                            Veritabani.db.beginTransaction();
                            seciliyarim = Veritabani.db.createObject(YarimYiyecek.class,txt);
                            seciliyarim.eklenenler= new RealmList<OzelYiyecek>();
                            seciliyarim.toplamgram=null;
                            Veritabani.db.commitTransaction();
                            ((AramaListYarimAdapter)liste.getAdapter()).aramaliste=seciliyarim.eklenenler;
                            toplamgram.setText("");
                            olusturYeniYukle();
                        }
                    } else {
                        uyari.setMsg("Bu İsime Sahip Başka Bir Kayıt Var. Lütfen Başka Bir İsim Yazınız.");
                        uyari.show();
                        return;
                    }

                    aramaIOC.alrt.dismiss();
                } else {
                    uyari.setMsg("Lütfen Karışım İsmini Boş Bırakmayınız.");
                    uyari.show();
                }
            }
        });
        aramaIOC.alrt = builder.create();
        olustur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aramaIOC.alrt.show();
            }
        });





        aramadia2 = new AramaDiyalog(YKarisimEkleSil.this);
        AramaListAdapter adp2 = new AramaListAdapter(null, aramadia2.prnt);
        aramadia2.liste.setAdapter(adp2);
        AramaTextWatcher aramaTW2 = new AramaTextWatcher(adp2, aramadia2);
        final AramaItemClick aramaIOC2 = new AramaItemClick(aramadia2);
        aramadia2.arama.addTextChangedListener(aramaTW2);
        aramadia2.liste.setOnItemClickListener(aramaIOC2);

        yekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (seciliyarim == null) {
                    uyari.setMsg("Öncelikle Yarım Kalan Bir Karışım Seçmeli Yada Yeni Bir Karışım Oluşturmalısınız.");
                    uyari.show();
                }
                else {
                    yekledia.show();
                }

            }
        });




        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (seciliyarim == null) {
                    uyari.setMsg("Öncelikle Yarım Kalan Bir Karışım Seçmeli Yada Yeni Bir Karışım Oluşturmalısınız.");
                    uyari.show();
                }
                else {
                    if(toplamgram.getText().toString().equals("")) {
                        uyari.setMsg("Kaydı Tamamlamak İçin Öncelikle Toplam Yemek Gramı Değerini Yazmanız Gerekmektedir.");
                        uyari.show();
                        return;
                    }
                    if(seciliyarim.getEklenenler().size()==0){
                        uyari.setMsg("Kaydı Tamamlamak İçin En Az 1 Adet Yiyecek Eklenmiş Olmalıdır.");
                        uyari.show();
                        return;
                    }

                    float toplampay=0;
                    for(OzelYiyecek oy : seciliyarim.eklenenler)
                        toplampay+=Float.valueOf(oy.pay);
                    final Float yenibirimgram = Float.valueOf(toplamgram.getText().toString())/toplampay;
                    Veritabani.db.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            Yiyecek y = realm.createObject(Yiyecek.class,seciliyarim.getIsim());
                            y.setBirimgram(String.format(Locale.ROOT,"%.0f",Math.ceil(yenibirimgram)));
                            if(seciliyarim.isManaged())
                                seciliyarim.deleteFromRealm();
                        }
                    });
                    seciliyarim=null;
                    ((AramaListYarimAdapter)liste.getAdapter()).aramaliste=null;
                    ((AramaListYarimAdapter)liste.getAdapter()).notifyDataSetChanged();
                    toplamgram.setText("");
                    olusturSil();
                    uyari.setMsg("Kayıt Başarılı");
                    uyari.show();
                }
            }
        });




        DialogInterface.OnClickListener dlst = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case AlertDialog.BUTTON_POSITIVE:
                        Veritabani.db.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                try {
                                    seciliyarim.deleteFromRealm();
                                } catch (IllegalArgumentException e) {
                                }
                            }
                        });
                        seciliyarim = null;
                        olusturSil();
                        toplamgram.setText("");
                        ((AramaListYarimAdapter) liste.getAdapter()).aramaliste = null;
                        ((AramaListYarimAdapter) liste.getAdapter()).notifyDataSetChanged();
                        uyari.setMsg("Bu Karışım Başarıyla Silindi.");
                        uyari.show();
                        break;
                    case AlertDialog.BUTTON_NEGATIVE:
                        return;

                }
            }
        };
        final AlertDialog alrt = new AlertDialog.Builder(this).setPositiveButton("Evet",dlst).setNegativeButton("Hayır",dlst).create();
        sil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (seciliyarim == null) {
                    uyari.setMsg("Öncelikle Yarım Kalan Bir Karışım Seçmeli Yada Yeni Bir Karışım Oluşturmalısınız.");
                    uyari.show();
                }
                else {
                    alrt.setMessage(String.format(Locale.ROOT,"İsim: %s\n Bilgisine Sahip Yemek Karışımı Kaydını Silmek İstediğinize Emin Misiniz?",seciliyarim.getIsim()));
                    alrt.show();
                }
            }
        });
    }
}