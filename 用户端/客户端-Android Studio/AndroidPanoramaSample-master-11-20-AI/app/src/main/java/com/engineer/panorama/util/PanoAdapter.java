package com.engineer.panorama.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.engineer.panorama.R;
import com.engineer.panorama.bean.Pano;
import com.engineer.panorama.ui.PanoViewActivity;



import java.util.List;

public class PanoAdapter extends BaseAdapter implements AdapterView.OnItemClickListener{
    private Context context;
    private ListView listView;
    private List<Pano> panoList;

    public PanoAdapter(Context context, ListView listView) {
        this.context = context;
        this.listView = listView;
    }


    public void setData(List<Pano> data){
        this.panoList = data;
    }


    @Override
    public int getCount() {
        return panoList.size();
    }

    @Override
    public Object getItem(int position) {
        return panoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(context, R.layout.item_pano_layout,null);
        ViewHolder viewHolder = new ViewHolder(convertView);
        Pano pano = panoList.get(position);
        String panonam = pano.getPanoName();
        if (panonam.length()>=14){
            panonam = panonam.substring(0,14)+"...";
        }
        viewHolder.name.setText(panonam);
        viewHolder.addr.setText(pano.getPanoAddr());

        ((ListView)parent).setOnItemClickListener(this);//设置点击事件
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Pano panobean = panoList.get(position);
        Intent intent = new Intent(context, PanoViewActivity.class);
        intent.putExtra("uid",panobean);
        context.startActivity(intent);

        Log.d("uid","信息展现点击 ：" + panobean.toString());
    }

    static class ViewHolder{
        View baseView;
        TextView name;
        TextView addr;

        public ViewHolder(View baseView) {
            this.baseView = baseView;
            this.name = (TextView) baseView.findViewById(R.id.pano_name);
            this.addr = (TextView) baseView.findViewById(R.id.pano_addr);
        }
    }
}


