package com.example.diet;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.BaseAdapter;

import io.realm.Case;
import io.realm.RealmQuery;

public class AramaTextWatcher implements TextWatcher {
    BaseAdapter adp;
    AramaDiyalog dia;
    public AramaTextWatcher(BaseAdapter adp,AramaDiyalog dia) {
        this.adp = adp;this.dia=dia;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void afterTextChanged(Editable editable) {
        RealmQuery rquery;
        String query = editable.toString();
        if(dia.prnt instanceof  YKarisimEkleSil && dia!=((YKarisimEkleSil) dia.prnt).aramadia2 && dia!=(((YKarisimEkleSil) dia.prnt).yekledia.dia)){
            rquery=Veritabani.db.where(YarimYiyecek.class).contains("isim",query,Case.INSENSITIVE);
            ((AramaListYarimAdapter)adp).aramaliste=rquery.findAll();

        }
        else {
            rquery = Veritabani.db.where(Yiyecek.class).contains("isim", query, Case.INSENSITIVE);
            ((AramaListAdapter)adp).aramaliste=rquery.findAll();
        }
        adp.notifyDataSetChanged();
    }
}
