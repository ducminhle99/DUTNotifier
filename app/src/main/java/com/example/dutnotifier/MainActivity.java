package com.example.dutnotifier;

import android.os.AsyncTask;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.dutnotifier.model.modelNoti;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String MY_URL = "http://sv.dut.udn.vn/G_Thongbao_LopHP.aspx";

    private RecyclerView recycler;
    private NotiAdapter notiAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recycler = (RecyclerView) findViewById(R.id.recyler_category);
        configRecyclerView();
        new DownloadTask().execute(MY_URL);
    }

    private void configRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,1);
        recycler.hasFixedSize();
        recycler.setLayoutManager(layoutManager);
    }

    //Download HTML bằng AsynTask
    private class DownloadTask extends AsyncTask<String, Void, ArrayList<modelNoti>> {

        private static final String TAG = "DownloadTask";

        @Override
        protected ArrayList<modelNoti> doInBackground(String... strings) {
            Document document = null;
            ArrayList<modelNoti> listNoti = new ArrayList<>();
            try {
                document = (Document) Jsoup.connect(strings[0]).get();
                if (document != null) {
                    Elements sub = document.select("body");
                    Element element = sub.first();

                    for(int i=0;i<25;i++) {
                        modelNoti noti = new modelNoti();
                        Element element1 = element.getElementsByClass("MsoNormal").get(i);
                        Element date = element1.getElementsByTag("span").first();
                        Element title = element1.getElementsByTag("span").get(2);

                        noti.setDate(date.text());
                        noti.setTitle(title.text());
                        noti.setContent("Unknown");
                        listNoti.add(noti);

                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return listNoti;
        }

        @Override
        protected void onPostExecute(ArrayList<modelNoti> notis) {
            super.onPostExecute(notis);
            //Setup data recyclerView
            notiAdapter = new NotiAdapter(MainActivity.this,notis);
            recycler.setAdapter(notiAdapter);
        }
    }
}