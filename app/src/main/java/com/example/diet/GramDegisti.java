package com.example.diet;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.regex.Pattern;

public class GramDegisti implements TextWatcher {

    AppCompatActivity act;
    PayDegisti pTW;
    boolean etkin=true;
    public GramDegisti(AppCompatActivity act) {
        this.act = act;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(act instanceof Bugun) {
            Bugun bgn = (Bugun)act;
            if (etkin && bgn.seciliYiyecek != null) {
                pTW.etkin = false;
                etkin = false;
                if (Pattern.compile("[0-9]{1,5}(.[0-9]{1,4})?").matcher(editable.toString()).matches()) {
                    String val = String.format(Locale.ROOT,"%.2f", Float.valueOf(editable.toString()) / Float.valueOf(bgn.seciliYiyecek.getBirimgram()));
                    if (999.99 >= Float.valueOf(val)) {
                        bgn.payGir.setText(val);
                    } else {
                        bgn.payGir.setText("999.99");
                        String togram = String.format(Locale.ROOT,"%.2f", 999.99 * Float.valueOf(bgn.seciliYiyecek.getBirimgram()));
                        if(Float.valueOf(togram).floatValue()!=Float.valueOf(bgn.gramGir.getText().toString()).floatValue())
                            bgn.gramGir.setText((togram.length()>6 && togram.charAt(6)=='.')?togram.substring(0,6):togram);
                        bgn.gramGir.setSelection(bgn.gramGir.getText().length());

                    }
                } else
                    bgn.payGir.setText("");
                pTW.etkin = true;
                etkin = true;
            }
        }
        else if(act instanceof YKarisimEkleSil){
            YKarisimEkleSil ykar = (YKarisimEkleSil)act;
            if (etkin && ykar.yekledia.seciliYiyecek != null) {
                pTW.etkin = false;
                etkin = false;
                if (Pattern.compile("[0-9]{1,5}(.[0-9]{1,2})?").matcher(editable.toString()).matches()) {
                    String val = String.format(Locale.ROOT,"%.2f", Float.valueOf(editable.toString()) / Float.valueOf(ykar.yekledia.seciliYiyecek.getBirimgram()));
                    if (999.99 >= Float.valueOf(val)) {
                        ykar.yekledia.payGir.setText(val);
                    } else {
                        ykar.yekledia.payGir.setText("999.99");
                        String togram=String.format(Locale.ROOT,"%.2f", 999.99 * Float.valueOf(ykar.yekledia.seciliYiyecek.getBirimgram()));
                        if(Float.valueOf(togram).floatValue()!=Float.valueOf(ykar.yekledia.gramGir.getText().toString()).floatValue())
                            ykar.yekledia.gramGir.setText((togram.length()>6 && togram.charAt(6)=='.')?togram.substring(0,6):togram);
                        ykar.yekledia.gramGir.setSelection(ykar.yekledia.gramGir.getText().length());
                    }
                } else
                    ykar.yekledia.payGir.setText("");
                pTW.etkin = true;
                etkin = true;
            }
        }
    }
}
