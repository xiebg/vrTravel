package com.engineer.panorama;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.engineer.panorama.bean.News;
import com.engineer.panorama.location.BNaviMainActivity;
import com.engineer.panorama.ui.EmepyActivity;
import com.engineer.panorama.ui.HotelPanoViewListActivity;
import com.engineer.panorama.ui.IndoorPanoViewListActivity;
import com.engineer.panorama.ui.MapActivity;
import com.engineer.panorama.ui.MapViewActivity;
import com.engineer.panorama.ui.PanoPagerActivity;
import com.engineer.panorama.ui.PanoViewListActivity;
import com.engineer.panorama.ui.SettingActivity;
import com.engineer.panorama.ui.SinglePanoViewActivity;
import com.engineer.panorama.ui.TFSelectActivity;
import com.engineer.panorama.util.LocSdkClient;
import com.engineer.panorama.util.NewsAdapter;
import com.engineer.panorama.util.PermissionsChecker;
import com.engineer.panorama.util.RequestInfo;
import com.engineer.panorama.view.BottomNavigationViewHelper;
import com.engineer.panorama.view.ViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

//import org.json.JSONArray;
//import org.json.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ViewPagerAdapter mAdapter;
    private LinearLayout navi_click,ar_click,panorama_click,indoor_click,map_click,spot_click, hotel_click;

    //定义一个队列来保存各个布局文件
    private ArrayList<View> views;

    //新闻相关
    private ListView newsitem;
    private NewsAdapter newsAdapter;
    private List<News> newsList = new ArrayList<News>();
    private int showcount = 300;

    private MenuItem menuItem;
    private BottomNavigationView bottomNavigationView;

    private TextView tvLocation;
    private TextView tvTemperature;
    private LinearLayout oneItem, twoItem, thereItem, fourItem, fiveItem;
    private EditText et_search_news;
    private Button bt_search_news;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        //队列来保存各个布局文件
        views=new ArrayList<View>();
        //获取界面的布局加载器，将布局文件加载到程序中
        LayoutInflater li=getLayoutInflater();
        //将布局加载到程序中
        View viewPager_home = li.inflate(R.layout.activity_main_local, null,false);
        View viewPager_news = li.inflate(R.layout.activity_main_news, null,false);
//        View viewPager_local =li.inflate(R.layout.activity_main_local, null,false);
        View viewPager_find = li.inflate(R.layout.activity_main_find, null,false);
        View viewPager_mine = li.inflate(R.layout.activity_main_mine, null,false);

        views.add(viewPager_home);
        views.add(viewPager_news);
//        views.add(viewPager_local);
        views.add(viewPager_find);
        views.add(viewPager_mine);

        mAdapter = new ViewPagerAdapter(views);
        viewPager.setAdapter(mAdapter);


        initHomeView(viewPager_home);
        initNewsView(viewPager_news);
//        initLocalView(viewPager_local);
        initFindView(viewPager_find);
        initMineView(viewPager_mine);

        initPermissions();

        ListView news_listview = (ListView) viewPager_news.findViewById(R.id.news_listview);
        WebView web_find = (WebView) viewPager_find.findViewById(R.id.find_web);
        WebSettings webSettings_find = web_find.getSettings();
//        WebSettings webSettings_news = web_news.getSettings();

        ViewGroup.LayoutParams params = news_listview.getLayoutParams();

        params.height = 80 * showcount;
        news_listview.setLayoutParams(params);

//        web_news.setWebViewClient(new WebViewClient() {
//            public boolean shouldOverrideUrlLoading(WebView view, String url)
//            { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
//                view.loadUrl(url);
//                return true;
//            }
//            @SuppressWarnings("deprecation")
//            @Override
//            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                super.onReceivedError(view, errorCode, description, failingUrl);
//                // Handle the error
//                if(errorCode==-2) {
//                    view.loadUrl("file:///android_asset/time_out.html");
////                    view.loadUrl("file:///mbd.baidu.com/ma/s/uWOMOVBc");
//                }
//            }
//
//            @TargetApi(Build.VERSION_CODES.M)
//            @Override
//            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
//                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
//            }
//        });

        web_find.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                // Handle the error
                if(errorCode==-2) {
                    view.loadUrl("file:///android_asset/time_out.html");
                }
            }

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings_find.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//            webSettings_news.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

//        web_news.loadUrl("https://m.baidu.com/from=1000953f/bd_page_type=1/ssid=2774c3fcd4cbc3bbd3d0b5bcd1ddf52e/uid=0/pu=usm%402%2Csz%40320_1002%2Cta%40iphone_2_6.0_2_12137.1/baiduid=973EEF52D3DEC4DFD024270ADE701750/w=0_10_/t=iphone/l=3/tc?ref=www_iphone&lid=7382171454823398260&order=1&fm=alop&tj=www_normal_1_0_10_title&vit=osres&m=8&srd=1&cltj=cloud_title&asres=1&title=%E6%97%85%E6%B8%B8%E5%8A%A8%E6%80%81%E8%B5%84%E8%AE%AF%E5%88%97%E8%A1%A8%2C%E8%9C%82%E7%AA%9D%E8%B5%84%E8%AE%AF-%E9%A9%AC%E8%9C%82%E7%AA%9D&dict=32&wd=&eqid=6672bdd363c6fc00100000025abb4f73&w_qd=IlPT2AEptyoA_ykzquMb5vixJUtVkoC&tcplug=1&sec=28640&di=9c9c2610d7a61bf0&bdenc=1&tch=124.0.283.182.0.0&nsrc=IlPT2AEptyoA_yixCFOxXnANedT62v3IEQGG_yFZ2Du5mlasxP4lZQRAViHuRnqKXkfws8H0sqcFrXS7_8km6so4g43&clk_info=%7B%22srcid%22%3A1599%2C%22tplname%22%3A%22www_normal%22%2C%22t%22%3A1522225014944%2C%22xpath%22%3A%22div-a-h3%22%7D");
//        web_news.loadUrl("https://news.cncn.com/");

        web_find.loadUrl("https://map.baidu.com/search/%E6%99%AF%E7%82%B9/");

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        //默认 >3 的选中效果会影响ViewPager的滑动切换时的效果，故利用反射去掉
        BottomNavigationViewHelper.removeNavigationShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_home:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.item_news:
                                viewPager.setCurrentItem(1);
                                break;
//                            case R.id.item_local:
//                                viewPager.setCurrentItem(2);
//                                break;
                            case R.id.item_find:
                                viewPager.setCurrentItem(2);
                                break;
                            case R.id.item_mine:
                                viewPager.setCurrentItem(3);
                                break;
                        }
                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //    menuItem = bottomNavigationView.getMenu().getItem(2).setChecked(true);
            }
            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //禁止ViewPager滑动
//        viewPager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });

      //  setupViewPager(viewPager);
    }

    private void initMineView(View viewPager_mine) {
        TextView accountTv = (TextView) viewPager_mine.findViewById(R.id.account);
        oneItem =  (LinearLayout) viewPager_mine.findViewById(R.id.one_item);
        twoItem = (LinearLayout)viewPager_mine.findViewById(R.id.two_item);
        thereItem = (LinearLayout) viewPager_mine.findViewById(R.id.there_item);
        fourItem = (LinearLayout)viewPager_mine.findViewById(R.id.four_item);
        fiveItem = (LinearLayout) viewPager_mine.findViewById(R.id.five_item);
        Intent intent = getIntent();
        accountTv.setText(intent.getStringExtra("userName"));
        oneItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "这个真的很好玩，快来和我一起玩吧：\nhttps://www.xxxx.com");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        twoItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EmepyActivity.class);
                startActivity(intent);
            }
        });
        thereItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EmepyActivity.class);
                startActivity(intent);
            }
        });
        fourItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EmepyActivity.class);
                startActivity(intent);
            }
        });
        fiveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });



//        oneItem.initMine(R.drawable.share, "分享", "", true)
//                .setOnRootClickListener(new MyOneLineView.OnRootClickListener() {
//            @Override
//            public void onRootClick(View view) {
//            }
//        }, 1);
//        twoItem.initMine(R.drawable.collection, "我的收藏", "", true);
//        thereItem.initMine(R.drawable.history, "历史关注", "", true);
//        fourItem.initMine(R.drawable.follow2, "我的关注", "", true);
//        fiveItem.initMine(R.drawable.setting, "设置", "", true);
    }

    private void initFindView(View viewPager_find) {
    }

    private void initHomeView(View viewPager_local) {
        tvLocation = viewPager_local.findViewById(R.id.tv_location);
        tvTemperature = viewPager_local.findViewById(R.id.tv_temperature);
        String districtId = null;
        String weatherUrl = null;
        LocationClient mLocationClient = LocSdkClient.getInstance(PanoDemoApplication.getInstance().getBaseContext()).getLocationStart();
        if(mLocationClient != null) {
            mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
                @Override
                public void onReceiveLocation(BDLocation bdLocation) {
                    try {
                        //当前设备位置所在的省
                        String province = bdLocation.getProvince();
                        //当前设备位置所在的市
                        String city = bdLocation.getCity();
                        tvLocation.setText(province + city);
                        String dist= bdLocation.getDistrict();
                        String districtUrl = RequestInfo.BASEURL + "/district/findid?city="+dist.substring(0,dist.length()-1);
                        Log.d("districtId",districtUrl);

                        //http://localhost:9090/district/findid?city=
                        //http://192.168.215.125:9090/district/findid?city=甘谷县
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Request request = new Request.Builder()
                                            .url(districtUrl) //后端请求接口的路径
                                            .get() //发送JSON格式的body
                                            .build(); //创造http请求
                                    OkHttpClient okHttpClient = new OkHttpClient();
                                    Response response = okHttpClient.newCall(request).execute();
                                    String districtId = response.body().string();
                                    Log.d("districtId",districtId);

                                } catch (Exception e) {
                                    Log.d("districtId",e.toString());
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            if (districtId != null){
                weatherUrl = "https://api.map.baidu.com/weather/v1/?district_id="+districtId+"&data_type=all&ak=lb24UtgyfPZwv3OoDOcaA7gBoPIFcBmD";
                getweather(weatherUrl);
            }else{
                String district_id = "620523";
                weatherUrl = "https://api.map.baidu.com/weather/v1/?district_id="+district_id+"&data_type=all&ak=lb24UtgyfPZwv3OoDOcaA7gBoPIFcBmD";
                getweather(weatherUrl);
            }
            LocationClientOption option = new LocationClientOption();
            option.setIsNeedAddress(true);
            option.setAddrType("all");
            mLocationClient.setLocOption(option);
            mLocationClient.start();
        }


        //地图
        map_click=(LinearLayout) viewPager_local.findViewById(R.id.map_click);
        map_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
        //AR景点浏览
        ar_click=(LinearLayout) viewPager_local.findViewById(R.id.ar_click);
        ar_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TFSelectActivity.class);
                startActivity(intent);
            }
        });
        //VR导航
        panorama_click=(LinearLayout) viewPager_local.findViewById(R.id.panorama_click);
        panorama_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, PanoViewActivity.class);
                Intent intent = new Intent(MainActivity.this, PanoViewListActivity.class);
                startActivity(intent);
            }
        });
        //交通导航
        navi_click=(LinearLayout) viewPager_local.findViewById(R.id.navi_click);
        navi_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BNaviMainActivity.class);
                startActivity(intent);
            }
        });
        //热门景点
        spot_click=(LinearLayout) viewPager_local.findViewById(R.id.spot_click);
        spot_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PanoPagerActivity.class);
                startActivity(intent);
            }
        });
        //酒店
        hotel_click=(LinearLayout) viewPager_local.findViewById(R.id.hotel_click);
        hotel_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HotelPanoViewListActivity.class);
                startActivity(intent);
            }
        });

        //AR内景
        indoor_click=(LinearLayout) viewPager_local.findViewById(R.id.indoor_click);
        indoor_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, IndoorPanoViewListActivity.class);
                startActivity(intent);
            }
        });
    }

    public void getweather(String weatherUrl){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("weatherResult",weatherUrl);
                    Request request = new Request.Builder()
                            .url(weatherUrl) //后端请求接口的路径
                            .get() //发送JSON格式的body
                            .build(); //创造http请求
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Response response = okHttpClient.newCall(request).execute();
                    String weatherResult = response.body().string();
//                        Log.d("weatherResult",weatherResult);
                    JSONObject jsonWeather = JSONObject.parseObject(weatherResult);
                    JSONObject result = jsonWeather.getJSONObject("result");
                    JSONObject now = result.getJSONObject("now");
                    String temp = now.getString("temp");
                    String text = now.getString("text");
                    String wind_dir = now.getString("wind_dir");
                    Log.d("weatherResult",now.toString()+":"+temp);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvTemperature.setText(temp+"°C"+text+" "+wind_dir);
                        }
                    });

                } catch (Exception e) {
                    Log.d("weatherResult",e.toString());
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

    private void initLocalView(View viewPager_home) {
        Button btDesc_pid = viewPager_home.findViewById(R.id.panorama_desc_pid);
        Button btDesc_uid_street = viewPager_home.findViewById(R.id.panorama_desc_uid_street);
        btDesc_pid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SinglePanoViewActivity.class));
            }
        });
        btDesc_uid_street.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MapViewActivity.class));
            }
        });
    }

    private void initNewsView(View viewPager_news) {
        getNewsAll(viewPager_news);

        et_search_news = viewPager_news.findViewById(R.id.et_search_news);
        bt_search_news = viewPager_news.findViewById(R.id.bt_search_news);
        bt_search_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newsList.clear();
                String title = et_search_news.getText().toString().trim();
                String newsURL = RequestInfo.BASEURL + "/news/newstitle"+"?title="+title;
                Log.d("eee",newsURL);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Request request = new Request.Builder()
                                    .url(newsURL) //后端请求接口的路径
                                    .get() //发送JSON格式的body
                                    .build(); //创造http请求
                            Log.i("HP", "request: " + request.toString());
                            OkHttpClient okHttpClient = new OkHttpClient();
                            Response response = okHttpClient.newCall(request).execute();
                            String newslistStr = response.body().string();


                            JSONArray jsonArray = JSONArray.parseArray(newslistStr);
                            for (int i = 0; i <= jsonArray.toArray().length-1; i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Gson gson = new Gson();
                                News news = gson.fromJson(jsonObject.toString(), News.class);
                                Log.d("HP2",news.toString());
                                newsList.add(news);
                            }
                            Log.d("abbb",newslistStr.toString());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    newsitem = viewPager_news.findViewById(R.id.news_listview);
                                    newsAdapter = new NewsAdapter(MainActivity.this,newsitem);
                                    newsAdapter.setData(newsList);
                                    newsitem.setAdapter(newsAdapter);
                                }
                            });
                        } catch (Exception e) {
                            Log.d("eee",e.toString());
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "news,网络请求失败！", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });

    }
    private void getNewsAll(View viewPager_news){
        String URL = RequestInfo.BASEURL + "/news/all";
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
                    String newslistStr = response.body().string();


                    JSONArray jsonArray =JSONArray.parseArray(newslistStr);
                    for (int i = 0; i <= jsonArray.toArray().length-1; i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Gson gson = new Gson();
                        News news = gson.fromJson(jsonObject.toString(), News.class);
                        Log.d("HP2",news.toString());
                        newsList.add(news);
                    }
                    Log.d("abbb",newslistStr.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            newsitem = viewPager_news.findViewById(R.id.news_listview);
                            newsAdapter = new NewsAdapter(MainActivity.this,newsitem);
                            newsAdapter.setData(newsList);
                            newsitem.setAdapter(newsAdapter);
                        }
                    });
                } catch (Exception e) {
                    Log.d("HP3",e.toString());
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "news,网络请求失败！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void getNews(int id){
        String URL = RequestInfo.BASEURL + "/news/1?uid="+id;
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
                    String newslistStr = response.body().string();
                    Log.d("HP1",newslistStr);

                    Gson gson = new Gson();
                    News news = gson.fromJson(newslistStr, News.class);
                    Log.d("HP2",news.toString());
                    newsList.add(news);

                } catch (Exception e) {
                    Log.d("HP3",e.toString());
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "news,网络请求失败！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void initPermissions() {
        PermissionsChecker permissionsChecker = new PermissionsChecker(this);
        if (permissionsChecker.lacksPermissions()) {
            Toast.makeText(this, "缺少权限，请开启权限！", Toast.LENGTH_LONG).show();
            openSetting();
        }
    }

    public void openSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    // 用来计算返回键的点击间隔时间
    private long exitTime = 0;
    //双击返回退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 1500) {
                //弹出提示，可以有多种方式
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
//    public static class WeiXinShareUtil {
//        public static void sharePhotoToWX(Context context, String text) {
//            if (!uninstallSoftware(context, "com.tencent.mm")) {
//                Toast.makeText(context, "微信没有安装！", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            Intent intent = new Intent();
//
//            ComponentName componentName = new ComponentName("com.tencent.mm",
//                    "com.tencent.mm.ui.tools.ShareToTimeLineUI");
//            intent.setComponent(componentName);
//
//            intent.setAction("android.intent.action.SEND");
//
//            intent.putExtra("Kdescription", text);
//
//            context.startActivity(intent);
//
//        }
//        private static boolean uninstallSoftware(Context context, String packageName) {
//            PackageManager packageManager = context.getPackageManager();
//            try {
//                PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
//                if (packageInfo != null) {
//                    return true;
//                }
//            } catch (PackageManager.NameNotFoundException e) {
//                e.printStackTrace();
//            }
//            return false;
//        }
//    }
}
