package com.engineer.panorama.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.engineer.panorama.R;
import com.engineer.panorama.bean.News;
import com.engineer.panorama.ui.NewsDetailsActivity;

import java.util.List;

public class NewsAdapter extends BaseAdapter implements AdapterView.OnItemClickListener{
    private Context context;
    private ListView listView;
    private List<News> newsList;

    public NewsAdapter(Context context, ListView listView) {
        this.context = context;
        this.listView = listView;
    }

    public void setData(List<News> data){
        this.newsList = data;
    }


    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public Object getItem(int position) {
        return newsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(context, R.layout.item_news_layout,null);
        ViewHolder viewHolder = new ViewHolder(convertView);
        News news = newsList.get(position);
        String newstit = news.getNewsTitle();
        if (newstit.length()>=14){
            newstit = newstit.substring(0,14)+"...";
        }
        viewHolder.title.setText(newstit);
        viewHolder.time.setText(news.getCreateTime());

        ((ListView)parent).setOnItemClickListener(this);//设置点击事件
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        News bean = newsList.get(position);
        Intent intent = new Intent(context, NewsDetailsActivity.class);
        intent.putExtra("news",  bean);
        context.startActivity(intent);

        Log.d("XHP","信息展现 点击 ：" + bean.getNewsTitle());
    }

    static class ViewHolder{
        View baseView;
        TextView title;
        TextView time;

        public ViewHolder(View baseView) {
            this.baseView = baseView;
            this.title = (TextView) baseView.findViewById(R.id.news_title);
            this.time = (TextView) baseView.findViewById(R.id.news_time);
        }
    }
}


