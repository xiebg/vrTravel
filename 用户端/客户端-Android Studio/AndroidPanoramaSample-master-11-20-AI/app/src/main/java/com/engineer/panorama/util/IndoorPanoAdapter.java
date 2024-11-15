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
import com.engineer.panorama.bean.IndoorPano;
import com.engineer.panorama.bean.Pano;
import com.engineer.panorama.ui.IndoorPanoViewActivity;
import com.engineer.panorama.ui.PanoViewActivity;

import java.util.List;

public class IndoorPanoAdapter extends BaseAdapter implements AdapterView.OnItemClickListener{
    private Context context;
    private ListView listView;
    private List<IndoorPano> indoorPanoList;

    public IndoorPanoAdapter(Context context, ListView listView) {
        this.context = context;
        this.listView = listView;
    }


    public void setData(List<IndoorPano> data){
        this.indoorPanoList = data;
    }


    @Override
    public int getCount() {
        return indoorPanoList.size();
    }

    @Override
    public Object getItem(int position) {
        return indoorPanoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(context, R.layout.item_indoor_pano_layout,null);
        ViewHolder viewHolder = new ViewHolder(convertView);
        IndoorPano indoorPano = indoorPanoList.get(position);
        Log.d("indoorPano1",indoorPano.toString());
        String panonam = indoorPano.getAddrname();
        if (panonam.length()>=14){
            panonam = panonam.substring(0,14)+"...";
        }
        viewHolder.addrname.setText(panonam);
//        viewHolder.addr.setText(pano.getPanoAddr());

        ((ListView)parent).setOnItemClickListener(this);//设置点击事件
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        IndoorPano indoorPanobean = indoorPanoList.get(position);
        Intent intent = new Intent(context, IndoorPanoViewActivity.class);
        intent.putExtra("pid",indoorPanobean);
        context.startActivity(intent);

        Log.d("pid","信息展现 点击 ：" + indoorPanobean.toString());
    }

    static class ViewHolder{
        View baseView;
        TextView addrname;
//        TextView addr;

        public ViewHolder(View baseView) {
            this.baseView = baseView;
            this.addrname = (TextView) baseView.findViewById(R.id.indoor_pano_name);
//            this.addr = (TextView) baseView.findViewById(R.id.pano_addr);
        }
    }
}


