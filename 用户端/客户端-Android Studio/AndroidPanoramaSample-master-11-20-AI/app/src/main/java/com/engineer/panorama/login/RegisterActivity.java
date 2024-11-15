package com.engineer.panorama.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.engineer.panorama.R;
import com.engineer.panorama.util.RequestInfo;
import com.engineer.panorama.util.Result;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class RegisterActivity extends Activity implements View.OnClickListener {

    public static final String TAG = RegisterActivity.class.getSimpleName();
    private EditText mAccount, mPwd, mPwdCheck;
    private OwlView2 owlView2;
    private Button mSureButton;
    private Button mCancelButton;
    OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAccount = (EditText) findViewById(R.id.account);
        mPwd = (EditText) findViewById(R.id.password);
        mPwdCheck = (EditText) findViewById(R.id.password_check);
        mSureButton = (Button) findViewById(R.id.btn_register);
        mCancelButton = (Button) findViewById(R.id.btn_cacel);

        owlView2 = (OwlView2) findViewById(R.id.owl_view);
        owlView2.addWatchEditText(mAccount);
        owlView2.addWatchEditText(mPwd);
        owlView2.addWatchEditText(mPwdCheck);

        mSureButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                register();
                break;
            case R.id.btn_cacel:
                Intent intent_Register_to_Login = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent_Register_to_Login);
                finish();
                break;
        }
    }

    public void register() {
        String username = mAccount.getText().toString().trim();//获取用户名
        String password = mPwd.getText().toString().trim();     //获取密码
        Log.i(TAG, "regist: " + username + password);
        String BASE_URL = RequestInfo.BASEURL + "/user/";
        if (isUserNameAndPwdValid(username, password)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String append = RequestInfo.REGIST + "?username=" + username + "&password=" + password;
                        Request request = new Request.Builder()
                                .url(BASE_URL + append) //后端请求接口的路径
                                .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), "")) //发送JSON格式的body
                                .build(); //创造http请求
                        Log.i(TAG, "request: " + request.toString());
                        executeRequest(request, username, password);
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegisterActivity.this, "网络请求失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).start();
        }
    }

    private void executeRequest(Request request, String username, String password) {
        Log.d(TAG, "executeRequest request:" + request.toString());
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure:" + e.getMessage());
                Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG, "onResponse:" + response.toString());
                String str = response.body().string();//想拿到字符串，可以从response-body-string
                Log.e(TAG, str);
                try {
                    List<Result> result = JSON.parseArray('[' + str + ']', Result.class);
                    if (!result.isEmpty()) {
                        if (result.get(0).isSuccess()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegisterActivity.this, result.get(0).getMsg(), Toast.LENGTH_SHORT).show();
                                    Intent intent_Register_to_Login = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent_Register_to_Login);
                                    findUser(username, password);
                                    finish();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegisterActivity.this, result.get(0).getMsg(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void findUser(String username, String password) {
        boolean isExist = LitePal.isExist(UserInfo.class, "userName = ? and userPwd = ?", username, password);
        if (isExist) {
            return;
        }
        long id = System.currentTimeMillis();
        UserInfo userInfo = new UserInfo(username, password, id);
        boolean flag = userInfo.save();
        Log.e(TAG, "userInfo save flag : " + flag);
    }

    public boolean isUserNameAndPwdValid(String username, String password) {
        String userName = mAccount.getText().toString().trim();
        String userPwd = mPwd.getText().toString().trim();
        if (userName.equals("")) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (userPwd.equals("")) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
