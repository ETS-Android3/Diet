package com.example.diet;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

import io.realm.RealmResults;
import io.realm.Sort;

public class SonBirAy extends AppCompatActivity {
    ListView list,dialist;
    AlertDialog dia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sonbiray);
        tanimla();
    }
    public void  tanimla(){
        list = findViewById(R.id.biraylist);
        BirAyListAdapter adp = new BirAyListAdapter(Veritabani.db.where(GunlukKayit.class).sort(new String[]{"yil","ay","gun"},new Sort[]{Sort.DESCENDING,Sort.DESCENDING,Sort.DESCENDING}).findAll(),this);
        list.setAdapter(adp);
        BirAyListOnItemClick birayOIC = new BirAyListOnItemClick(this);
        list.setOnItemClickListener(birayOIC);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.sonbirayyiyecekler);
        dia = builder.create();
        dia.show();
        dialist = dia.findViewById(R.id.biraydialist);
        BugunAdapter diaadp = new BugunAdapter(new ArrayList<OzelYiyecek>(),this);
        dialist.setAdapter(diaadp);
        dia.dismiss();
    }
}
class BirAyListAdapter extends BaseAdapter {

    RealmResults<GunlukKayit> aramaliste;
    Context ctx;
    public BirAyListAdapter(RealmResults<GunlukKayit> aramaliste, Context ctx) {
        this.aramaliste = aramaliste;
        this.ctx=ctx;
    }

    @Override
    public int getCount() {
        return aramaliste.size();
    }

    @Override
    public Object getItem(int i) {
        return aramaliste.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(ctx).inflate(R.layout.sonbiraylist,viewGroup,false);
        TextView tarih,yiyeceksayi,toplam,kullanilan,kalan;
        tarih=view.findViewById(R.id.biraylisttarih);
        yiyeceksayi=view.findViewById(R.id.biraylistadet);
        toplam=view.findViewById(R.id.biraylisttoplam);
        kullanilan=view.findViewById(R.id.biraylistkullanilan);
        kalan=view.findViewById(R.id.biraylistkalan);
        tarih.setText(""+aramaliste.get(i).getGun()+"."+aramaliste.get(i).getAy()+"."+aramaliste.get(i).getYil());
        yiyeceksayi.setText(""+aramaliste.get(i).getYiyecekler().size());
        toplam.setText(""+aramaliste.get(i).getToplampay());
        kullanilan.setText(""+aramaliste.get(i).getKullanilanpay());
        kalan.setText(String.format(Locale.ROOT,"%.2f",Float.valueOf(aramaliste.get(i).getToplampay())-Float.valueOf(aramaliste.get(i).getKullanilanpay())));

        return view;
    }
}

class BirAyListOnItemClick implements AdapterView.OnItemClickListener{

    SonBirAy bay;

    public BirAyListOnItemClick(SonBirAy bay) {
        this.bay = bay;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        BirAyListAdapter adp = (BirAyListAdapter)bay.list.getAdapter();
        GunlukKayit gelen = adp.aramaliste.get(i);
        BugunAdapter bga =((BugunAdapter)bay.dialist.getAdapter());
        bga.yiyecekList=gelen.getYiyecekler();
        bga.notifyDataSetChanged();
        bay.dia.show();
    }
}