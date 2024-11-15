package com.engineer.panorama.ui;

import androidx.appcompat.app.AppCompatActivity;
import com.engineer.panorama.R;
import com.engineer.panorama.bean.News;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class NewsDetailsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        TextView title = (TextView)findViewById(R.id.news_detalis_title);
        TextView time = (TextView)findViewById(R.id.news_detalis_time);
        TextView source = (TextView)findViewById(R.id.news_detalis_source);
        TextView contents = (TextView)findViewById(R.id.news_detalis_content);

        Intent intent = this.getIntent();
        News news=(News)intent.getSerializableExtra("news");
        title.setText(news.getNewsTitle());
        time.setText(news.getCreateTime());
        source.setText(news.getNewsSource());
        contents.setText(news.getNewsContent());

    }
}