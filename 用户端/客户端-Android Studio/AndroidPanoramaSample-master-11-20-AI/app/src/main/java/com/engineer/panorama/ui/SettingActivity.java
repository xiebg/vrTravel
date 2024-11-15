package com.engineer.panorama.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.engineer.panorama.MainActivity;
import com.engineer.panorama.R;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        LinearLayout feedback = findViewById(R.id.ll_1);
        LinearLayout my = findViewById(R.id.ll_2);
        LinearLayout edition = findViewById(R.id.ll_3);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingActivity.this,"有什么问题可以尽快反馈给我们，我们会第一时间给您答复！",Toast.LENGTH_SHORT).show();
//                openEmail();
                sendEmail(SettingActivity.this,"问题反馈","以下功能有问题，希望优化：\n1.","2262752545@qq.com", null);
            }
        });
        my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingActivity.this,"本软件是来自三明学院经济与管理学院电子商务团队开发！",Toast.LENGTH_SHORT).show();
            }
        });
        edition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingActivity.this,"亲，当前版本是v1.0",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void sendEmail(Context context, String title,
                          String content, String address, String[] ccArr) {
        Uri uri = Uri.parse("mailto:" + address);
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
        // 设置对方邮件地址
        emailIntent.putExtra(Intent.EXTRA_EMAIL, address);
        // 抄送
        emailIntent.putExtra(Intent.EXTRA_CC, ccArr);
        // 设置标题内容
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        // 设置邮件文本内容
        emailIntent.putExtra(Intent.EXTRA_TEXT, content);
        context.startActivity(Intent.createChooser(emailIntent, "选择邮箱"));
    }


    //
//    /**
//     * ACTION_SEND + message/rfc822
//     * @param context
//     * @param title
//     * @param content
//     * @param emailArr
//     * @param ccArr
//     */
    public void sendEmail2(Context context, String title,
                           String content, String[] emailArr, String[] ccArr) {
        // 调用系统发邮件
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        // 设置邮件格式
        emailIntent.setType("message/rfc822");
        // 设置对方邮件地址
        emailIntent.putExtra(Intent.EXTRA_EMAIL, emailArr);
        // 抄送人
        emailIntent.putExtra(Intent.EXTRA_CC, ccArr);
        // 设置标题内容
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        // 设置邮件文本内容
        emailIntent.putExtra(Intent.EXTRA_TEXT, content);
        context.startActivity(Intent.createChooser(emailIntent, "选择邮箱"));
    }
    //
//    /**
//     * 打开邮箱客户端
//     */
    private void openEmail() {
        Uri uri = Uri.parse("mailto:" + "");
        List<ResolveInfo> packageInfos = getPackageManager()
                .queryIntentActivities(new Intent(Intent.ACTION_SENDTO, uri), 0);
        List<String> tempPkgNameList = new ArrayList<>();
        List<Intent> emailIntents = new ArrayList<>();
        for (ResolveInfo info : packageInfos) {
            String pkgName = info.activityInfo.packageName;
            if (!tempPkgNameList.contains(pkgName)) {
                tempPkgNameList.add(pkgName);
                Intent intent = getPackageManager().getLaunchIntentForPackage(pkgName);
                emailIntents.add(intent);
            }
        }
        if (!emailIntents.isEmpty()) {
            Intent chooserIntent = Intent.createChooser(emailIntents.remove(0), "选择邮箱");
            if (chooserIntent != null) {
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                        emailIntents.toArray(new Parcelable[]{}));
                startActivity(chooserIntent);
            } else {
                Toast.makeText(this, "没有找到可用的邮件客户端", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "没有找到可用的邮件客户端", Toast.LENGTH_SHORT).show();
        }
    }
}

