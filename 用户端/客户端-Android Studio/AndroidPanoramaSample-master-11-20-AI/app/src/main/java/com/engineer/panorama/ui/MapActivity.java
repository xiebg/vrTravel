package com.engineer.panorama.ui;

import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.engineer.panorama.PanoDemoApplication;
import com.engineer.panorama.R;
import com.engineer.panorama.util.LocSdkClient;


public class MapActivity extends AppCompatActivity {
    MapView mMapView;
    RadioGroup mMapType;
    BaiduMap mBaiduMap;
    boolean isFirstLocate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_local_map);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mMapType = (RadioGroup) findViewById(R.id.radioGroup);
        mBaiduMap = mMapView.getMap();
        //卫星地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);

        mMapType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.btnNORMAL:
                        if(mBaiduMap.isTrafficEnabled()) {
                            mBaiduMap.setTrafficEnabled(false);
                        }
                        if(mBaiduMap.isBaiduHeatMapEnabled()) {
                            mBaiduMap.setBaiduHeatMapEnabled(false);
                        }
                        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                        break;
                    case R.id.btnSATELLITE:
                        if(mBaiduMap.isTrafficEnabled()) {
                            mBaiduMap.setTrafficEnabled(false);
                        }
                        if(mBaiduMap.isBaiduHeatMapEnabled()) {
                            mBaiduMap.setBaiduHeatMapEnabled(false);
                        }
                        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                        break;
                    case R.id.btnTraffic:
                        //开启交通图
                        if(mBaiduMap.isBaiduHeatMapEnabled()) {
                            mBaiduMap.setBaiduHeatMapEnabled(false);
                        }
                        if(!mBaiduMap.isTrafficEnabled()) {
                            mBaiduMap.setTrafficEnabled(true);
                        }
                        mBaiduMap.setCustomTrafficColor("#ffba0101", "#fff33131", "#ffff9e19", "#00000000");
                        //  对地图状态做更新，否则可能不会触发渲染，造成样式定义无法立即生效。
                        MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(13);
                        mBaiduMap.animateMapStatus(u);
                        break;
                    case R.id.btnHeatMap:
                        if(mBaiduMap.isTrafficEnabled()) {
                            mBaiduMap.setTrafficEnabled(false);
                        }
                        //开启热力图
                        if(!mBaiduMap.isBaiduHeatMapEnabled()) {
                            mBaiduMap.setBaiduHeatMapEnabled(true);
                        }
                        break;
                }
            }
        });

        LocationClient mLocationClient = LocSdkClient.getInstance(PanoDemoApplication.getInstance().getBaseContext()).getLocationStart();
        //定位初始化
        if(mLocationClient != null) {
            mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
                @Override
                public void onReceiveLocation(BDLocation location) {
                    //mapView 销毁后不在处理新接收的位置
                    if (location == null || mMapView == null){
                        return;
                    }
                    MyLocationData locData = new MyLocationData.Builder()
                            .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                            .direction(location.getDirection()).latitude(location.getLatitude())
                            .longitude(location.getLongitude()).build();
                    mBaiduMap.setMyLocationData(locData);

                    if(location.getLocType()==BDLocation.TypeGpsLocation || location.getLocType()==BDLocation.TypeNetWorkLocation){
                        navigateTo(location);
                    }
                }
            });

            //通过LocationClientOption设置LocationClient相关参数
            LocationClientOption option = new LocationClientOption();
            option.setOpenGps(true); // 打开gps
            option.setCoorType("bd09ll"); // 设置坐标类型
            option.setScanSpan(1000);

            //设置locationClientOption
            mLocationClient.setLocOption(option);
            //开启地图定位图层
            mLocationClient.start();
        }
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mMapView == null){
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);

            if(location.getLocType()==BDLocation.TypeGpsLocation || location.getLocType()==BDLocation.TypeNetWorkLocation){
                navigateTo(location);
            }
        }
    }

    private void navigateTo(BDLocation bdLocation) {
        if(isFirstLocate){
            LatLng ll = new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.animateMapStatus(update);
            isFirstLocate = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mLocationClient.stop();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }
}

