package com.example.rss;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ShowNews extends AppCompatActivity {
    ListView channelList;
    ImageView logoBao;
    TextView tieu;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_news);

        Intent callingIntent = getIntent();
        Bundle bun = callingIntent.getExtras();
//        urlAddress = myBundle.getString("urlAddress"); urlCaption = myBundle.getString(“urlCaption”);
        logoBao = findViewById(R.id.logo_bao);
        tieu = findViewById(R.id.tieu);
        String newsName = bun.getString("newsName");
        String title = "Channels in " + newsName;
        tieu.setText(title);

        logoBao.setScaleType(ImageView.ScaleType.FIT_CENTER);
        if (newsName.equals("Thanh Niên"))
            logoBao.setImageResource(R.drawable.thanhnien_logo);
        else if (newsName.equals("Tiền Phong"))
            logoBao.setImageResource(R.drawable.tienphong_logo);
        else if (newsName.equals("Vietnamnet"))
            logoBao.setImageResource(R.drawable.vietnamnet);

        ArrayList<String> channels = bun.getStringArrayList("channelNames");
        ArrayList<String> urls = bun.getStringArrayList("channelUrl");

//        channelList.setAdapter();
        channelList = findViewById(R.id.channels);
        if (channels != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, channels);
            channelList.setAdapter(adapter);
        }
        channelList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent callShowIndividualNews = new Intent(ShowNews.this, ShowArticle.class);
                Bundle myData = new Bundle();
                myData.putString("caption", "Items in channel " + channels.get(i) + " - "  +  bun.getString("newsName"));
                myData.putString("channel", channels.get(i));
                myData.putString("newsName", bun.getString("newsName"));
                System.out.println(urls.get(i));
                myData.putString("url", urls.get(i));
                callShowIndividualNews.putExtras(myData);
                startActivity(callShowIndividualNews);
            }
        });
    }
}
