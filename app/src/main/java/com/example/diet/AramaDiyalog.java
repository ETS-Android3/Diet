package com.example.diet;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class AramaDiyalog extends Dialog {
    AppCompatActivity prnt;
    EditText arama;
    ListView liste;

    public AramaDiyalog(@NonNull Context context) {
        super(context, true, null);
        prnt = (AppCompatActivity) context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aramadiyalog);
        tanimla();
    }

    public void tanimla() {
        arama = findViewById(R.id.aramatext);
        liste = findViewById(R.id.aramalist);
    }

}
