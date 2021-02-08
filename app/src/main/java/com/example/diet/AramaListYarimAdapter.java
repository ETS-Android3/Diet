package com.example.diet;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class AramaListYarimAdapter<listeleman> extends BaseAdapter {
    public List<listeleman> aramaliste;
    public Context ctx;

    AramaListYarimAdapter(List<listeleman> l,Context c){
        aramaliste=l;
        ctx=c;
    }
    @Override
    public int getCount() {
        if (aramaliste==null)
            return 0;
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
        if(aramaliste==null)
            return null;
        view= LayoutInflater.from(ctx).inflate(R.layout.bugunlist,null);
        TextView isim,pay,gram;
        isim = view.findViewById(R.id.listisim);
        pay = view.findViewById(R.id.listpay);
        gram = view.findViewById(R.id.listgram);
        TextView silbut = view.findViewById(R.id.silbut);
        if(aramaliste.get(0) instanceof YarimYiyecek) {
            YarimYiyecek y = (YarimYiyecek)aramaliste.get(i);
            isim.setText(y.getIsim());
            pay.setText(y.getToplamgram());
            gram.setText("" + y.eklenenler.size());
            silbut.setText("Seç");
            silbut.setBackgroundColor(Color.rgb(0xA3,0xF7,0xBF));

        }
        else if(aramaliste.get(0) instanceof OzelYiyecek){
            OzelYiyecek y = (OzelYiyecek)aramaliste.get(i);
            isim.setText(y.getIsim());
            pay.setText(y.getPay());
            gram.setText(String.format(Locale.ROOT,"%.2f",Float.valueOf(y.getPay())*Float.valueOf(y.getBirimgram())));
        }
        return view;
    }
}
