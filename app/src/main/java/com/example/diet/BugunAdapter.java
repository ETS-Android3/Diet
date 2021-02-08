package com.example.diet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class BugunAdapter extends BaseAdapter {
    List<OzelYiyecek> yiyecekList;
    Context context;
    public BugunAdapter(List<OzelYiyecek> list, Context con){
        yiyecekList=list;
        context=con;
    }
    @Override
    public int getCount() {
        if(yiyecekList==null)
            return 0;
        return yiyecekList.size();
    }

    @Override
    public Object getItem(int i) {
        if(yiyecekList==null)
            return null;
        return yiyecekList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(yiyecekList==null)
            return null;
        view = LayoutInflater.from(context).inflate(R.layout.bugunlist,null);
        TextView isim,pay,gram,silbut;

        isim = view.findViewById(R.id.listisim);
        pay = view.findViewById(R.id.listpay);
        gram = view.findViewById(R.id.listgram);
        silbut= view.findViewById(R.id.silbut);

        isim.setText(yiyecekList.get(i).getIsim());
        pay.setText(yiyecekList.get(i).getPay());
        gram.setText(String.valueOf(Float.valueOf(yiyecekList.get(i).getPay()) * Float.valueOf(yiyecekList.get(i).getBirimgram())));
        if(context instanceof SonBirAy){
            silbut.setLayoutParams(new LinearLayout.LayoutParams(0,0,0));
        }
        return view;
    }
}
