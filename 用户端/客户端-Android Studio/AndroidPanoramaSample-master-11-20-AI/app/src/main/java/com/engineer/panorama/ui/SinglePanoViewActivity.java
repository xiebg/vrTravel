package com.engineer.panorama.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.lbsapi.BMapManager;
import com.baidu.lbsapi.MKGeneralListener;
import com.baidu.lbsapi.panoramaview.PanoramaView;
import com.engineer.panorama.PanoDemoApplication;
import com.engineer.panorama.R;

public class SinglePanoViewActivity extends AppCompatActivity {
    private PanoramaView mPanoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_pano_view);
        mPanoView = (PanoramaView) findViewById(R.id.panorama);
//        panorama.setPanorama("0100220000130817164838355J5")
//        mPanoView.setPanorama("0900220001150514054806738T5");
//        panorama.setPanorama("0300220000131105191740485IN");

        BMapManager mBMapManager = new BMapManager(PanoDemoApplication.getInstance().getApplicationContext());
        mBMapManager.init(new MKGeneralListener() {
            @Override
            public void onGetPermissionState(int iError) {
                // 非零值表示key验证未通过
                if (iError != 0) {
                    // 授权Key错误：
                    Toast.makeText(PanoDemoApplication.getInstance().getApplicationContext(),
                            "请在AndoridManifest.xml中输入正确的授权Key,并检查您的网络连接是否正常！error: " + iError, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(PanoDemoApplication.getInstance().getApplicationContext(), "key认证成功", Toast.LENGTH_LONG).show();
                    // 初始化成功 设置加载全景
                    mPanoView.setPanorama("0100220000130817164838355J5");
                }
            }
        });
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
