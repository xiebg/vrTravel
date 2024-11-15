package com.engineer.panorama.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.alibaba.fastjson.JSON;
import com.engineer.panorama.MainActivity;
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



public class LoginActivity extends Activity {

    public static final String TAG = LoginActivity.class.getSimpleName();
    private OwlView owlView;
    private EditText mAccount;
    private EditText mPassword;
    private Button login;
    private TextView signUp, forgetPassword,tiao;
    OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        owlView = (OwlView) findViewById(R.id.owl_view);
        mAccount = (EditText) findViewById(R.id.account);
        mPassword = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.btn_login);
        signUp = (TextView) findViewById(R.id.sign_up);
        forgetPassword = (TextView) findViewById(R.id.forget_password);

        tiao = (TextView) findViewById(R.id.tiao);
        tiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        initListener();
    }

    public void login() {
        String username = mAccount.getText().toString().trim();//获取用户名
        String password = mPassword.getText().toString().trim();     //获取密码
        Log.i(TAG, "login: " + username + password);
        String BASE_URL = RequestInfo.BASEURL + "/user/";
        if (isUserNameAndPwdValid(username, password)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String append = RequestInfo.LOGIN + "?username=" + username + "&password=" + password;
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
                                Toast.makeText(LoginActivity.this, "网络请求失败！", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
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
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("userName", username);
                                    startActivity(intent);
                                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                    findUser(username, password);
                                    finish();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, result.get(0).getMsg(), Toast.LENGTH_SHORT).show();
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

    public boolean isUserNameAndPwdValid(String username, String password) {
        if (username.equals("")) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.equals("")) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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

    private void initListener() {
        mPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    owlView.open();
                } else {
                    owlView.close();
                }
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
