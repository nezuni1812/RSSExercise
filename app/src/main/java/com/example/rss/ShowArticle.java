package com.example.rss;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ShowArticle extends AppCompatActivity {
    ListView newsList;
    TextView head;

    ArrayList<String> headlines;
    ArrayList<SingleItem> allNews;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_article);

        Intent callingIntent = getIntent();
        Bundle bun = callingIntent.getExtras();



        head = findViewById(R.id.head);
        newsList = findViewById(R.id.newsList);

        head.setText(bun.getString("caption"));

        if (headlines != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, headlines);
            newsList.setAdapter(adapter);
        }

        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowArticle.this);

                builder.setTitle(allNews.get(i).getTitle()).setMessage(allNews.get(i).getDescription());
                builder.setPositiveButton("More", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ShowArticle.this, "More", Toast.LENGTH_SHORT).show();
                        adapterView.getItemIdAtPosition(i);
                        Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(allNews.get(i).getLink()));
                        startActivity(browser);
                    }
                });
                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ShowArticle.this, "Close", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                builder.create();
                builder.show();

            }
        });

        DownloadRssFeed downloader = new DownloadRssFeed(ShowArticle.this);
        downloader.execute(bun.getString("url"), bun.getString("caption"));
    }
}
