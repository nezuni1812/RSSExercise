package com.example.rss;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ShowNews extends AppCompatActivity {
    ListView channelList;
    TextView tieu;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_news);

        Intent callingIntent = getIntent();
        Bundle bun = callingIntent.getExtras();
//        urlAddress = myBundle.getString("urlAddress"); urlCaption = myBundle.getString(“urlCaption”);

        tieu = findViewById(R.id.tieu);
        tieu.setText(bun.getString("newsName"));

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
                myData.putString("caption", "ThanhNien - "  + channels.get(i));
                System.out.println(urls.get(i));
                myData.putString("url", urls.get(i));
                callShowIndividualNews.putExtras(myData);
                startActivity(callShowIndividualNews);
            }
        });
    }
}
