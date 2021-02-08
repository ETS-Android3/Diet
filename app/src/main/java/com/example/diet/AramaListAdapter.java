package com.example.diet;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import io.realm.RealmResults;

public class AramaListAdapter extends BaseAdapter {
        public RealmResults<Yiyecek> aramaliste;
        public Context ctx;

        AramaListAdapter(RealmResults<Yiyecek> l,Context c){
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
            TextView isim,pay,gram, silbut;

            isim = view.findViewById(R.id.listisim);
            pay = view.findViewById(R.id.listpay);
            gram = view.findViewById(R.id.listgram);
            silbut = view.findViewById(R.id.silbut);

            silbut.setText("Seç");
            silbut.setBackgroundColor(Color.rgb(0xA3,0xF7,0xBF));
            isim.setText(aramaliste.get(i).getIsim());
            pay.setText("1");
            gram.setText(aramaliste.get(i).getBirimgram());
            return view;
        }
    }
