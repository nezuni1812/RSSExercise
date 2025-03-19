package com.example.rss;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    GridView bao;

    static final String[] news = new String[]{"Tiền Phong", "Thanh Niên", "Vietnamnet"};
    static final String[] logo = new String[]{"tienphong_logo", "thanhnien_logo", "vietnamnet"};

    // Thanhnien News
    String [][] thanhnienNews = {
            {"https://thanhnien.vn/rss/kinh-te.rss", "Kinh tế"},
            {"https://thanhnien.vn/rss/cong-nghe.rss", "Công nghệ"},
            {"https://thanhnien.vn/rss/giao-duc.rss", "Giáo dục"},
            {"https://thanhnien.vn/rss/du-lich.rss", "Du lịch"},
            {"https://thanhnien.vn/rss/van-hoa.rss", "Văn hóa"}
    };

    // Tienphong News
    String [][] tienphongNews = {
            {"https://tienphong.vn/rss/hanh-trang-nguoi-linh-182.rss", "Người lính"},
            {"https://tienphong.vn/rss/phap-luat-12.rss", "Pháp luật"},
            {"https://tienphong.vn/rss/khoa-hoc-45.rss", "Khoa học"},
            {"https://tienphong.vn/rss/dau-tu-165.rss", "Đầu tư"},
            {"https://tienphong.vn/rss/sach-429.rss", "Sách"}
    };

    // VietNamNet News
    String [][] vietnamnetNews = {
            {"https://infonet.vietnamnet.vn/rss/doi-song.rss", "Đời sống"},
            {"https://infonet.vietnamnet.vn/rss/thi-truong.rss", "Thị trường"},
            {"https://infonet.vietnamnet.vn/rss/the-gioi.rss", "Thế giới"},
            {"https://infonet.vietnamnet.vn/rss/gia-dinh.rss", "Gia đình"},
            {"https://infonet.vietnamnet.vn/rss/gioi-tre.rss", "Giới trẻ"}
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bao = findViewById(R.id.bao);
        NewAdapter adapter = new NewAdapter(this, Arrays.asList(news), Arrays.asList(logo));
        bao.setAdapter(adapter);
        bao.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, "Bao " + i, Toast.LENGTH_SHORT).show();
                Intent callShowHeadlines = new Intent(MainActivity.this, ShowNews.class);
                Bundle myData = new Bundle();
                myData.putString("newsName", "ThanhNien");

                ArrayList<String> channelNames = new ArrayList<>();
                for (int j = 0; j < thanhnienNews.length; j++) {
                    channelNames.add(thanhnienNews[j][1]); // Lấy cột thứ 2 (tên chuyên mục)
                }
                myData.putStringArrayList("channelNames", channelNames);

                ArrayList<String> channelUrl = new ArrayList<>();
                for (int j = 0; j < thanhnienNews.length; j++) {
                    channelUrl.add(thanhnienNews[j][0]); // Lấy cột thứ 2 (tên chuyên mục)
                }
                myData.putStringArrayList("channelUrl", channelUrl);
                callShowHeadlines.putExtras(myData);
                startActivity(callShowHeadlines);

            }
        });
    }

    class NewAdapter extends BaseAdapter {
        private Context context;
        private List<String> newsNames;
        private List<String> logoFileNames;
        private LayoutInflater inflater;

        public NewAdapter(Context context, List<String> newsNames, List<String> logoFileNames) {
            this.context = context;
            this.newsNames = newsNames;
            this.logoFileNames = logoFileNames;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return newsNames.size();
        }

        @Override
        public Object getItem(int position) {
            return newsNames.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_news, parent, false);
                holder = new ViewHolder();
                holder.newsName = convertView.findViewById(R.id.textViewNewsName);
                holder.newsLogo = convertView.findViewById(R.id.imageViewNewsLogo);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // Set text for news name
            holder.newsName.setText(newsNames.get(position));

            // Load logo from drawable resources
            int resId = context.getResources().getIdentifier(logoFileNames.get(position), "drawable", context.getPackageName());
            if (resId != 0) {
                holder.newsLogo.setImageResource(resId);
            }

            return convertView;
        }

        private class ViewHolder {
            TextView newsName;
            ImageView newsLogo;
        }
    }
}
