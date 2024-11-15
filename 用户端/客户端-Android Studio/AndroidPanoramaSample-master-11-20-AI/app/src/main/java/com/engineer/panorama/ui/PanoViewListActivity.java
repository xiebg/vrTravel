package com.engineer.panorama.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.engineer.panorama.MainActivity;
import com.engineer.panorama.MainActivity;
import com.engineer.panorama.R;
import com.engineer.panorama.bean.Pano;
import com.engineer.panorama.util.PanoAdapter;
import com.engineer.panorama.util.RequestInfo;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PanoViewListActivity extends AppCompatActivity {
    private ListView panoitem;
    private PanoAdapter panoAdapter;
    private List<Pano> panoList = new ArrayList<Pano>();
    private EditText et_search;
    private Button bt_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pano_view_list);
        et_search = findViewById(R.id.et_search);
        bt_search = findViewById(R.id.bt_search);
        getPanosAll();

        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                panoList.clear();
                String name = et_search.getText().toString().trim();
                String NameURL = RequestInfo.BASEURL + "/pano/panoname"+"?name="+name;
                Log.d("ddd",NameURL);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Request request = new Request.Builder()
                                    .url(NameURL) //后端请求接口的路径
                                    .get() //发送JSON格式的body
                                    .build(); //创造http请求
                            OkHttpClient okHttpClient = new OkHttpClient();
                            Response response = okHttpClient.newCall(request).execute();
                            String panolistStr = response.body().string();

                            JSONArray jsonArray = new JSONArray(panolistStr);
                            for (int i = 0; i <= jsonArray.length()-1; i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Gson gson = new Gson();
                                Pano pano = gson.fromJson(jsonObject.toString(), Pano.class);
                                Log.d("abbb",pano.toString());
                                panoList.add(pano);
                            }
                            Log.d("abbb",panoList.toString());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    panoitem = findViewById(R.id.pano_listview);
                                    panoAdapter = new PanoAdapter(PanoViewListActivity.this,panoitem);
                                    panoAdapter.setData(panoList);
                                    panoitem.setAdapter(panoAdapter);
                                }
                            });

                        } catch (Exception e) {
                            Log.d("abbb",e.toString());
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(PanoViewListActivity.this, "pano,网络请求失败！", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();

            }
        });

        /*
        // 对编辑框添加文本改变监听，搜索的具体功能在这里实现,很简单，文本该变的时候进行搜索。关键方法是重写的onTextChanged（）方法。
        et_search.addTextChangedListener(new TextWatcher() {
          //编辑框内容改变的时候会执行该方法
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 如果adapter不为空的话就根据编辑框中的内容来过滤数据
                String NameURL = RequestInfo.BASEURL + "/pano/panoname"+"?name="+s.toString();
                Log.d("ddd",NameURL);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });*/


    }




    private void getPanosAll(){
        String URL = RequestInfo.BASEURL + "/pano/all";
        Log.d("HP",URL);
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
                    for (int i = 0; i <= 500; i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Gson gson = new Gson();
                        Pano pano = gson.fromJson(jsonObject.toString(), Pano.class);
                        Log.d("abbb",pano.toString());
                        panoList.add(pano);
                    }
                    Log.d("abbb",panoList.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            panoitem = findViewById(R.id.pano_listview);
                            panoAdapter = new PanoAdapter(PanoViewListActivity.this,panoitem);
                            panoAdapter.setData(panoList);
                            panoitem.setAdapter(panoAdapter);
                        }
                    });

                } catch (Exception e) {
                    Log.d("abbb",e.toString());
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PanoViewListActivity.this, "pano,网络请求失败！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();

    }



    private void getPano(int id){
        String URL = RequestInfo.BASEURL + "/pano/1?uid="+id;
        Log.d("HP",URL);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Request request = new Request.Builder()
                            .url(URL) //后端请求接口的路径
                            .get() //发送JSON格式的body
                            .build(); //创造http请求
                    Log.i("HP", "request: " + request.toString());
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Response response = okHttpClient.newCall(request).execute();
                    String panolistStr = response.body().string();
                    Log.d("HP2",panolistStr);

                    Gson gson = new Gson();
                    Pano pano = gson.fromJson(panolistStr, Pano.class);
                    Log.d("HP2",pano.toString());
                    panoList.add(pano);

                } catch (Exception e) {
                    Log.d("HP3",e.toString());
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PanoViewListActivity.this, "pano,网络请求失败！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();

    }
    @Override
    protected void onStart() {
        super.onStart();
    }
}

