package com.example.diet;

import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AlertDialog;

public class AramaItemClick implements AdapterView.OnItemClickListener {

    AramaDiyalog dia;
    AlertDialog alrt;
    AramaItemClick(AramaDiyalog d){
        dia=d;
    }
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(dia.prnt instanceof Bugun ) {
            Yiyecek gelen = (Yiyecek) dia.liste.getItemAtPosition(i);
            Bugun prnt = (Bugun) dia.prnt;
            prnt.seciliYiyecek = gelen;
            prnt.yiyecekSecYukle();
            if (!prnt.eklenebilir)
                prnt.eklenebilirlik(true);
            prnt.payGir.setText("");
            prnt.gramGir.setText("");
            dia.dismiss();
        }
        else if(dia.prnt instanceof  YiyecekEkleSil ){
            Yiyecek gelen = (Yiyecek)dia.liste.getItemAtPosition(i);
            YiyecekEkleSil prnt = (YiyecekEkleSil)dia.prnt;
            prnt.seciliYiyecek = gelen;
            prnt.yiyecekSecYukle();
            prnt.duzenlead.setText(gelen.getIsim());
            prnt.duzenlegram.setText(gelen.getBirimgram());
            prnt.setDuzenleEditTextAktif(true);
            dia.dismiss();
        }
        else if(dia.prnt instanceof  YKarisimEkleSil && dia!=((YKarisimEkleSil) dia.prnt).yekledia.dia){
            YarimYiyecek gelen = (YarimYiyecek)dia.liste.getItemAtPosition(i);
            YKarisimEkleSil prnt = (YKarisimEkleSil)dia.prnt;
            prnt.seciliyarim=gelen;
            prnt.olusturYarımYukle();
            AramaListYarimAdapter adptr=(AramaListYarimAdapter)prnt.liste.getAdapter();
            adptr.aramaliste= gelen.eklenenler;
            adptr.notifyDataSetChanged();
            prnt.toplamgram.setText(gelen.toplamgram);
            alrt.dismiss();
            dia.dismiss();
        }
        else if(dia.prnt instanceof  YKarisimEkleSil){
            Yiyecek gelen = (Yiyecek) dia.liste.getItemAtPosition(i);
            KarisimYiyecekEkle prnt = (KarisimYiyecekEkle) ((YKarisimEkleSil) dia.prnt).yekledia;
            prnt.seciliYiyecek = gelen;
            prnt.yiyecekSecYukle();
            if (!prnt.eklenebilir)
                prnt.eklenebilirlik(true);
            prnt.payGir.setText("");
            prnt.gramGir.setText("");
            dia.dismiss();
        }
    }
}
