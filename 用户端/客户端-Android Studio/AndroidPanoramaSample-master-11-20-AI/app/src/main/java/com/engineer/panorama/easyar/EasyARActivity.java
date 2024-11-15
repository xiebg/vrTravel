package com.engineer.panorama.easyar;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Random;

import cn.easyar.CameraDevice;
import cn.easyar.Engine;
import cn.easyar.ObjectTracker;
import com.engineer.panorama.R;

public class EasyARActivity extends Activity
{
    /*
     * Steps to create the key for this sample:
     *  1. login www.easyar.com
     *  2. create app with
     *      Name: HelloAR
     *      Package Name: cn.easyar.samples.helloar
     *  3. find the created item in the list and show key
     *  4. set key string bellow
     */
    private static String key = "DFSVOAhHjSQQIR2uiGMB4QVHfork4Rtvuvi9QzxmoxMIdqUOPHuyQ3M39FN/J/FUeyDyVAlkt08qeqtDZTerADphoxMCcL8oLTf8UGU3qggqcKgSLGbkWxJu5AM8e6INLFyiEmsvnTxlN7AAO3ynDz1m5FsSN6UOJHizDyBhv0MUOeQRJXSyByZnqxJrL51DPnyoBSZitUNlN6sAKjebTWt4qQU8eaMSay+dQzpwqBIsO48MKHKjNTt0pQoge6FDZTe1BCdmo08KeakULUejAiZyqAg9fKkPaznkEix7tQRnR6MCJmeiCCdy5E1rZqMPOnDoLit/owI9QbQAKn6vDy436kM6cKgSLDuVFDtzpwIsQbQAKn6vDy436kM6cKgSLDuVEShntQQaZacVIHSqLChl5E1rZqMPOnDoLCZhrw4nQbQAKn6vDy436kM6cKgSLDuCBCdmozI5dLIIKHmLADk36kM6cKgSLDuFIA1BtAAqfq8PLjebTWtwvhEgZ6M1IHijMj10qxFrL6gUJXnqQyBmig4qdKpDc3OnDTpwu00yN6QUJ3GqBABxtUNzTuQCJnjoBCdyrw8scLRPOXSoDjt0qwBrSOpDP3S0CCh7shJrL51DKnqrDDx7rxUwN5tNa2WqAD1zqRMkZuRbEjenDy1nqQgtN5tNa3ipBTx5oxJrL51DOnCoEiw7jwwocqM1O3SlCiB7oUNlN7UEJ2ajTwp5qRQtR6MCJnKoCD18qQ9rOeQSLHu1BGdHowImZ6IIJ3LkTWtmow86cOguK3+jAj1BtAAqfq8PLjfqQzpwqBIsO5UUO3OnAixBtAAqfq8PLjfqQzpwqBIsO5URKGe1BBplpxUgdKosKGXkTWtmow86cOgsJmGvDidBtAAqfq8PLjfqQzpwqBIsO4IEJ2ajMjl0sggoeYsAOTfqQzpwqBIsO4UgDUG0ACp+rw8uN5tNa3C+ESBnozUgeKMyPXSrEWsvqBQleepDIGaKDip0qkNzc6cNOnC7TTI3pBQncaoEAHG1Q3NO5EMUOeQXKGevACdhtUNzTuQCJnirFCd8shhrSOpDOXmnFS96tAw6N/w6a3ypEmtI6kMkeqIUJXC1Q3NO5BIse7UEZ1yrAC5wkhModq0IJ3LkTWtmow86cOgiJXqzBRtwpQ4ue68VIHqoQ2U3tQQnZqNPG3ClDjtxrw8uN+pDOnCoEiw7iQMjcKUVHWenAiJ8qAZrOeQSLHu1BGdGsxMvdKUEHWenAiJ8qAZrOeQSLHu1BGdGtgA7ZqMyOXSyCCh5iwA5N+pDOnCoEiw7iw49fKkPHWenAiJ8qAZrOeQSLHu1BGdRow86cJURKGGvACVYpxFrOeQSLHu1BGdWhyUdZ6cCInyoBmtI6kMsbbYIO3CSCCRwlRUoeLZDc3uzDSU55Ag6WakCKHnkWy90qhIsaJsc24P0l+dxxZj6G/sCSp0CENSkAL0Kkog2nPr33kj0Og4Zhw8iJGXjdw1/H+3l2BTSAWw6JtZioM8amBmhF64xc+XfZxWI0DW8qCEKSTco2mg3XQVkvZvd+SBINhCvtdWOTK3edWcGf1u6IO1ISk7o4ZnGZr8DuolMRZBnJWmUiuWPzXH/a5I6y4mPnqDf9a7jOmiCgVZw0aoiVDIUdxcj5NUClbVyr0Yur6HcJa+AC8IFG9kVp3VTEk2RQGNXukHCRVe6nc7rm+Bi3i/3Cifg6OyQz/feh/WWPCcJGSo043CROXKd/YHM6GUscE34hUZAhyvMxeKc5bRFNuM1SRXGYQ==";
    private GLView glView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        TextView visitorNum = findViewById(R.id.visitor_num);
        Random random = new Random();
        int num = random.nextInt(1000 - 500 + 1) + 500;
        visitorNum.setText("当前客流量：" + num + "人");

        if (!Engine.initialize(this, key)) {
            Log.e("EasyARActivity", "Initialization Failed.");
            Toast.makeText(EasyARActivity.this, Engine.errorMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        if (!CameraDevice.isAvailable()) {
            Toast.makeText(EasyARActivity.this, "CameraDevice not available.", Toast.LENGTH_LONG).show();
            return;
        }
        if (!ObjectTracker.isAvailable()) {
            Toast.makeText(EasyARActivity.this, "ObjectTracker not available.", Toast.LENGTH_LONG).show();
            return;
        }

        glView = new GLView(this);

        requestCameraPermission(new PermissionCallback() {
            @Override
            public void onSuccess() {
                ((ViewGroup) findViewById(R.id.preview)).addView(glView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }

            @Override
            public void onFailure() {
            }
        });
    }

    private interface PermissionCallback
    {
        void onSuccess();
        void onFailure();
    }
    private HashMap<Integer, PermissionCallback> permissionCallbacks = new HashMap<Integer, PermissionCallback>();
    private int permissionRequestCodeSerial = 0;
    @TargetApi(23)
    private void requestCameraPermission(PermissionCallback callback)
    {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                int requestCode = permissionRequestCodeSerial;
                permissionRequestCodeSerial += 1;
                permissionCallbacks.put(requestCode, callback);
                requestPermissions(new String[]{Manifest.permission.CAMERA}, requestCode);
            } else {
                callback.onSuccess();
            }
        } else {
            callback.onSuccess();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if (permissionCallbacks.containsKey(requestCode)) {
            PermissionCallback callback = permissionCallbacks.get(requestCode);
            permissionCallbacks.remove(requestCode);
            boolean executed = false;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    executed = true;
                    callback.onFailure();
                }
            }
            if (!executed) {
                callback.onSuccess();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (glView != null) { glView.onResume(); }
    }

    @Override
    protected void onPause()
    {
        if (glView != null) { glView.onPause(); }
        super.onPause();
    }
}
