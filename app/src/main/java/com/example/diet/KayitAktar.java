package com.example.diet;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.RealmResults;

public class KayitAktar extends AppCompatActivity {
    EditText ipedit;
    Button bgonder, bal;
    UyariDiyalog uyari;
    Handler h;
    internetKabul ik;
    internetGonder ig;
    boolean active;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        active=false;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.kayitaktar);
        tanimla();
        listenerlarAyarla();
        internetislem();

    }

    private void internetislem() {
        h = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(active) {
                    Bundle datas = msg.getData();
                    if (datas.getBoolean("gonder")) {
                        if (!datas.getBoolean("basarili")) {
                            uyari.setMsg("Kayıtları Gönderme İşlemi Başarısız Oldu.");
                            uyari.show();
                        } else {
                            uyari.setMsg("Kayıtlar Başarıyla Gönderildi.");
                            uyari.show();
                        }
                    } else {
                        if (datas.getBoolean("basarili")) {
                            String satirlar[] = msg.getData().getString("gelen").split("\n");
                            for (String satir : satirlar) {
                                final String gelen[] = satir.split("#");
                                Veritabani.db.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        Yiyecek y = new Yiyecek();
                                        y.isim = gelen[0];
                                        y.birimgram = gelen[1];
                                        Veritabani.db.insertOrUpdate(y);
                                    }
                                });
                                uyari.setMsg("Kayıtlar Başarıyla Alındı.");
                                uyari.show();
                            }
                        } else {
                            uyari.setMsg("Kayitlari Alma Başarısız.");
                            uyari.show();
                        }
                    }
                }
            }
        };
    }

    private void listenerlarAyarla() {
        bgonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ipstr = ipedit.getText().toString();
                if(!Pattern.compile("[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}").matcher(ipstr).matches())
                {
                    uyari.setMsg("Lütfen Geçerli Bir Ip Değeri Giriniz.");
                    uyari.show();
                    return;
                }
                uyari.setMsg("Kayıtlar Gönderiliyor Lütfen Bekleyiniz.");
                uyari.show();
                Message msg = h.obtainMessage();
                StringBuilder res = new StringBuilder();
                RealmResults<Yiyecek> rs = Veritabani.db.where(Yiyecek.class).findAll();
                for (Yiyecek y : rs)
                    res.append(y.isim + "#" + y.birimgram + "\n");
                ig = new internetGonder(h,ipstr, res.toString());
                ig.start();
            }
        });
        bal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uyari.setMsg("Kayıtlar Alınabilir. Diğer Cihazdan Gönderme Sinyali Bekleniyor.");
                uyari.show();
                ik = new internetKabul(h);
                ik.start();
            }
        });
    }

    public void tanimla() {
        ipedit = findViewById(R.id.ipedit);
        bgonder = findViewById(R.id.kayitgonder);
        bal = findViewById(R.id.kayital);
        uyari = new UyariDiyalog(this, "");
        active=true;
    }

    class internetKabul extends Thread {
        ServerSocket ss;
        Socket s;
        DataInputStream input;
        Handler h;

        public internetKabul(Handler h) {
            this.h = h;
        }

        @Override
        public void run() {

                Message msg = h.obtainMessage();
            try {
                ss = new ServerSocket(9980);
                Timer t = new Timer();
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            ss.close();
                        } catch (IOException e) {}
                    }
                },10000);
                s = ss.accept();
                input = new DataInputStream(s.getInputStream());
                String gelen =input.readUTF();
                input.close();
                ss.close();


                msg.getData().putString("gelen", gelen);
                msg.getData().putBoolean("gonder", false);
                msg.getData().putBoolean("basarili", true);

            }catch (SocketException e){
                msg.getData().putBoolean("basarili", false);
            }
            catch (IOException e) {
                e.getStackTrace();
            }
            h.sendMessage(msg);


        }
    }

    class internetGonder extends Thread {
        Socket s;
        DataOutputStream output;
        Handler h;
        String data,ip;

        public internetGonder(Handler h, String ip,String dat) {
            this.h = h;
            data=dat;
            this.ip=ip;
        }

        @Override
        public void run() {
                Message msg = h.obtainMessage();
                msg.getData().putBoolean("gonder", true);
            try {
                s = new Socket(ip, 9980);
                output = new DataOutputStream(s.getOutputStream());
                output.writeUTF(data);
                output.close();
                s.close();

                msg.getData().putBoolean("basarili",true);

            }catch (ConnectException e){
                msg.getData().putBoolean("basarili",false);
            }
            catch (IOException e) {
                e.getStackTrace();
            }
            h.sendMessage(msg);
        }
    }
}
