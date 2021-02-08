package com.example.diet;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.regex.Pattern;

public class PayDegisti implements TextWatcher {

    AppCompatActivity act;
    GramDegisti gTW;
    boolean etkin=true;
    public PayDegisti(AppCompatActivity act) {
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
                gTW.etkin = false;
                etkin = false;

                if (Pattern.compile("[0-9]{1,4}(.[0-9]{1,4})?").matcher(editable.toString()).matches()) {
                    String val = String.format(Locale.ROOT,"%.2f", Float.valueOf(editable.toString()) * Float.valueOf(bgn.seciliYiyecek.getBirimgram()));
                    if (9999.99 >= Float.valueOf(val)) {
                        bgn.gramGir.setText(val);
                    } else {
                        bgn.gramGir.setText("9999.99");
                        String topay = String.format(Locale.ROOT,"%.2f", 9999.99 / Float.valueOf(bgn.seciliYiyecek.getBirimgram()));
                        if(Float.valueOf(topay).floatValue()!=Float.valueOf(bgn.payGir.getText().toString()).floatValue())
                            bgn.payGir.setText((topay.length()>5 && topay.charAt(5)=='.')?topay.substring(0,5):topay);
                        bgn.payGir.setSelection(bgn.payGir.getText().length());
                    }
                } else
                    bgn.gramGir.setText("");
                gTW.etkin = true;
                etkin = true;
            }
        }
        else if(act instanceof YKarisimEkleSil){
            YKarisimEkleSil ykar = (YKarisimEkleSil) act;
            if (etkin && ykar.yekledia.seciliYiyecek != null) {
                gTW.etkin = false;
                etkin = false;

                if (Pattern.compile("[0-9]{1,4}(.[0-9]{1,2})?").matcher(editable.toString()).matches()) {
                    String val = String.format(Locale.ROOT,"%.2f", Float.valueOf(editable.toString()) * Float.valueOf(ykar.yekledia.seciliYiyecek.getBirimgram()));
                    if (9999.99 >= Float.valueOf(val)) {
                        ykar.yekledia.gramGir.setText(val);
                    } else {
                        ykar.yekledia.gramGir.setText("9999.99");
                        String topay = String.format(Locale.ROOT,"%.2f", 9999.99 / Float.valueOf(ykar.yekledia.seciliYiyecek.getBirimgram()));
                        if(Float.valueOf(topay).floatValue()!=Float.valueOf(ykar.yekledia.payGir.getText().toString()).floatValue())
                            ykar.yekledia.payGir.setText((topay.length()>5 && topay.charAt(5)=='.')?topay.substring(0,5):topay);
                        ykar.yekledia.payGir.setSelection(ykar.yekledia.payGir.getText().length());
                    }
                } else
                    ykar.yekledia.gramGir.setText("");
                gTW.etkin = true;
                etkin = true;
            }
        }
    }
}
