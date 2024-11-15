package com.engineer.panorama.ui;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.baidu.lbsapi.BMapManager;
import com.baidu.lbsapi.MKGeneralListener;
import com.baidu.lbsapi.panoramaview.PanoramaView;
import com.engineer.panorama.PanoDemoApplication;
import com.engineer.panorama.R;

public class  MultiPanoViewActivity extends AppCompatActivity {
    private PanoramaView panorama1;
    private PanoramaView panorama2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_pano_view);
        panorama1 = (PanoramaView) findViewById(R.id.panorama1);
        panorama2 = (PanoramaView) findViewById(R.id.panorama2);

//        panorama1.setPanorama("0100220000130817164838355J5")
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
                    panorama1.setPanorama("0900220001150514054806738T5");
                    panorama2.setPanorama("0300220000131105191740485IN");
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        panorama1.onPause();
        panorama2.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        panorama1.onResume();
        panorama2.onResume();
    }

    @Override
    protected void onDestroy() {
        panorama1.destroy();
        panorama2.destroy();
        super.onDestroy();
    }
}
