package com.engineer.panorama.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.lbsapi.BMapManager;
import com.baidu.lbsapi.MKGeneralListener;
import com.baidu.lbsapi.model.BaiduPoiPanoData;
import com.baidu.lbsapi.panoramaview.PanoramaRequest;
import com.baidu.lbsapi.panoramaview.PanoramaView;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.pano.platform.plugin.indooralbum.IndoorAlbumCallback;
import com.baidu.pano.platform.plugin.indooralbum.IndoorAlbumPlugin;
import com.engineer.panorama.PanoDemoApplication;
import com.engineer.panorama.R;
import com.engineer.panorama.bean.IndoorPano;
import com.engineer.panorama.bean.Pano;

import java.util.List;


public class IndoorPanoViewActivity extends AppCompatActivity {
    private PanoramaView mPanoView;
    private IndoorPano indoorPano;
    private String pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor_pano_view);
        mPanoView = (PanoramaView) findViewById(R.id.indoor_panorama);

        Intent intent = this.getIntent();
        indoorPano = (IndoorPano) intent.getSerializableExtra("pid");
        pid = indoorPano.getPid();

        // 默认相册
        IndoorAlbumPlugin.getInstance().init();
        IndoorAlbumCallback.EntryInfo info = new IndoorAlbumCallback.EntryInfo();
//        info.setEnterPid("0900220000141205144547300IN");
        info.setEnterPid(pid);
        IndoorAlbumPlugin.getInstance().loadAlbumView(mPanoView, info);

//        getpid("厦门");




//        PoiIndoorOption option = new PoiIndoorOption().poiIndoorBid("09002200121902231123208758O").poiIndoorWd("酒店");
//        mPoiSearch.searchPoiIndoor(option);
//
//
//        BMapManager mBMapManager = new BMapManager(PanoDemoApplication.getInstance().getApplicationContext());
//        mBMapManager.init(new MKGeneralListener() {
//            @Override
//            public void onGetPermissionState(int iError) {
//                // 非零值表示key验证未通过
//                if (iError != 0) {
//                    // 授权Key错误：
//                    Toast.makeText(PanoDemoApplication.getInstance().getApplicationContext(),
//                            "请在AndoridManifest.xml中输入正确的授权Key,并检查您的网络连接是否正常！error: " + iError, Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(PanoDemoApplication.getInstance().getApplicationContext(), "key认证成功", Toast.LENGTH_LONG).show();
//                    // 初始化成功 设置加载全景
//
//                }
//            }
//        });
    }
    public void getpid(String city){
        new Thread(new Runnable() {
            @Override
            public void run() {
//                //数据库操作
//                String CLS="com.mysql.jdbc.Driver";
//                String URL="jdbc:mysql://localhost:3306/login?serverTimezone=UTC&characterEncoding=utf-8&useSSL=false&useUnicode=true&autoReconnect=true";
//                String USER="root";
//                String PWD="123456";
//                try{
//                    Class.forName(CLS);
//                    Connection connection = DriverManager.getConnection(URL,USER,PWD);
//                    String sql="select * from user";
//                    Statement statement = connection.createStatement();
//                    ResultSet resultSet = statement.executeQuery(sql);
//                    Log.d("iidobjstr=====",resultSet.toString());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Log.d("iidobjstr=====",e.toString());
//                }

                //创建POI检索实例
                PoiSearch mPoiSearch = PoiSearch.newInstance();
                //创建POI检索监听器
                OnGetPoiSearchResultListener listener = new OnGetPoiSearchResultListener() {
                    @Override
                    public void onGetPoiResult(PoiResult poiResult) {
                        //PoiInfo 检索到的第一条信息
                        List<PoiInfo> pois = poiResult.getAllPoi();
                        PanoramaRequest request = PanoramaRequest.getInstance(IndoorPanoViewActivity.this);
                        for (int i =0;i<=pois.size()-1;i++){
                            BaiduPoiPanoData panoramaInfoByUid = request.getPanoramaInfoByUid(pois.get(i).getUid());
//        request.getPanoramaByIIdWithJson(iid);
//                    Log.d("PoiSearch","======"+panoramaInfoByUid.getIid());
//                    Log.d("iidobjstr=====",panoramaInfoByUid.getName()+"pid:"+panoramaInfoByUid.getPid()+"==Iid:"+panoramaInfoByUid.getIid());
                            if(!panoramaInfoByUid.getIid().equals("")){
                                String iidobjstr = request.getPanoramaByIIdWithJson(panoramaInfoByUid.getIid());
                                JSONObject jsonObject1 = JSONObject.parseObject(iidobjstr);
                                JSONArray contentArray = jsonObject1.getJSONArray("content");
                                JSONObject jsonObject2 = contentArray.getJSONObject(0);
                                JSONObject jsonObject3 = jsonObject2.getJSONObject("interinfo");
                                JSONArray floorsArray = jsonObject3.getJSONArray("Floors");
                                JSONObject jsonObject4 = floorsArray.getJSONObject(0);
                                JSONArray pointsArray = jsonObject4.getJSONArray("Points");
                                JSONObject pointsJson = pointsArray.getJSONObject(0);
                                String pid = pointsJson.getString("PID");
                                Log.d("iidobjstr=====",panoramaInfoByUid.getName()+"--:"+pid);
                            }
                        }
                    }
                    @Override
                    public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {
                    }
                    @Override
                    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
                    }
                    //废弃
                    @Override
                    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
                    }
                };
                //创建POI检索监听器
                mPoiSearch.setOnGetPoiSearchResultListener(listener);
                //设置PoiCitySearchOption，发起检索请求
                for (int i = 0; i < 13; i++) {
                    mPoiSearch.searchInCity(new PoiCitySearchOption()
                            .city(city) //必填
                            .keyword("景点") //必填
                            .pageNum(i));
                }
                mPoiSearch.destroy();
            }
        }).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPanoView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPanoView.onResume();
    }

    @Override
    protected void onDestroy() {
        mPanoView.destroy();
        super.onDestroy();
    }
}
