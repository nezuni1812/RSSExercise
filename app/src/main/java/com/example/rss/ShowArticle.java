package com.example.rss;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ShowArticle extends AppCompatActivity {
    ListView newsList;
    ImageView logoBao;
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

        logoBao = findViewById(R.id.logo_bao);
        String newsName = bun.getString("newsName");

        logoBao.setScaleType(ImageView.ScaleType.FIT_CENTER);
        if (newsName.equals("Thanh Niên"))
            logoBao.setImageResource(R.drawable.thanhnien_logo);
        else if (newsName.equals("Tiền Phong"))
            logoBao.setImageResource(R.drawable.tienphong_logo);
        else if (newsName.equals("Vietnamnet"))
            logoBao.setImageResource(R.drawable.vietnamnet);

        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowArticle.this);

                LinearLayout titleLayout = new LinearLayout(ShowArticle.this);
                titleLayout.setOrientation(LinearLayout.HORIZONTAL);
                titleLayout.setPadding(50, 20, 20, 0);
                titleLayout.setGravity(Gravity.CENTER_VERTICAL);

                String NewsName = bun.getString("newsName");
                String Channel = bun.getString("channel");

                ImageView icon = new ImageView(ShowArticle.this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(130, 130); // Width x Height (px)
                icon.setLayoutParams(layoutParams);
                icon.setScaleType(ImageView.ScaleType.FIT_CENTER);
                if (NewsName.equals("Thanh Niên"))
                    icon.setImageResource(R.drawable.thanhnien_logo);
                else if (NewsName.equals("Tiền Phong"))
                    icon.setImageResource(R.drawable.tienphong_logo);
                else if (NewsName.equals("Vietnamnet"))
                    icon.setImageResource(R.drawable.vietnamnet);

                icon.setPadding(20, 0, 20, 0);

                TextView title = new TextView(ShowArticle.this);
                String text = "Items in channel " + Channel + " - " + NewsName;
                title.setText(text);
                title.setTextSize(18);
                title.setTypeface(null, Typeface.BOLD);

                titleLayout.addView(icon);
                titleLayout.addView(title);

                String desc = allNews.get(i).getDescription().replaceAll("￼", "");
                String formattedMessage = "<b>" + allNews.get(i).getTitle() + "</b><br><br>" + desc;

                builder.setCustomTitle(titleLayout);
                builder.setMessage(Html.fromHtml(formattedMessage, Html.FROM_HTML_MODE_LEGACY));

                builder.setPositiveButton("More", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(allNews.get(i).getLink()));
                        startActivity(browser);
                    }
                });

                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
