package com.example.diet;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class UyariDiyalog extends Dialog {
    TextView tv;
    Button but;
    String msg;
    public UyariDiyalog(@NonNull Context context,String Mesaj) {
        super(context);
        msg=Mesaj;
        tanimla();
    }
    public void tanimla(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.uyaridiyalog);
        tv= findViewById(R.id.uyaritv);
        but = findViewById(R.id.uyaribut);

        tv.setText(msg);
        but.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                cancel();
            }
        });
    }

    public void setMsg(String msg) {
        this.msg = msg;
        tv.setText(msg);
    }

    public String getMsg() {
        return msg;
    }
}
