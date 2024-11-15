package com.engineer.panorama.ui;

import androidx.appcompat.app.AppCompatActivity;
import com.engineer.panorama.R;
import com.engineer.panorama.bean.IndoorPano;
import com.engineer.panorama.util.IndoorPanoAdapter;
import com.engineer.panorama.util.RequestInfo;
import com.google.gson.Gson;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HotelPanoViewListActivity extends AppCompatActivity {

    private ListView indoorpanoitem;
    private IndoorPanoAdapter indoorPanoAdapter;
    private List<IndoorPano> indoorPanoList = new ArrayList<IndoorPano>();
    private EditText indoor_et_search;
    private Button indoor_bt_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor_pano_view_list);
        indoor_et_search = findViewById(R.id.indoor_et_search);
        indoor_bt_search = findViewById(R.id.indoor_bt_search);
        String AllURL = RequestInfo.BASEURL + "/indoor/findhotelall";
        getIndoorPanos(AllURL);
        indoor_bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indoorPanoList.clear();
                String name = indoor_et_search.getText().toString().trim();
                String NameURL = RequestInfo.BASEURL + "/indoor/findhotelname"+"?name="+name;
                getIndoorPanos(NameURL);
            }
        });
    }


    private void getIndoorPanos(String URL){
//        String URL = RequestInfo.BASEURL + "/indoor/findhotelall";
//        Log.d("HP",URL);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Request request = new Request.Builder()
                            .url(URL) //后端请求接口的路径
                            .get() //发送JSON格式的body
                            .build(); //创造http请求
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Response response = okHttpClient.newCall(request).execute();
                    String panolistStr = response.body().string();

                    JSONArray jsonArray = new JSONArray(panolistStr);
                    for (int i = 0; i <= jsonArray.length()-1; i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Gson gson = new Gson();
                        IndoorPano indoorPano = gson.fromJson(jsonObject.toString(), IndoorPano.class);
//                        Log.d("indoorPano",indoorPano.toString());
                        indoorPanoList.add(indoorPano);
                    }
                    Log.d("indoorPano",indoorPanoList.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                    indoorpanoitem = findViewById(R.id.indoor_pano_listview);
                    indoorPanoAdapter = new IndoorPanoAdapter(HotelPanoViewListActivity.this,indoorpanoitem);
                    indoorPanoAdapter.setData(indoorPanoList);
                    indoorpanoitem.setAdapter(indoorPanoAdapter);
                        }
                    });

                } catch (Exception e) {
                    Log.d("indoorPano",e.toString());
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            Toast.makeText(IndoorPanoViewListActivity.this, "pano,网络请求失败！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();

    }
}