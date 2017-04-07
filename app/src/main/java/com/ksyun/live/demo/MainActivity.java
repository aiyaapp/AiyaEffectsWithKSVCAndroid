package com.ksyun.live.demo;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.aiyaapp.aiya.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.exceptions.HyphenateException;
import com.ksyun.live.demo.player.activity.PlayerActivity;
import com.ksyun.media.streamer.demo.DemoActivity;


public class MainActivity extends AppCompatActivity {

    private Button btn_stream;
    private Button btn_player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_player = (Button)findViewById(R.id.btn_player);
        btn_stream = (Button)findViewById(R.id.btn_stream);

        btn_stream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,DemoActivity.class);
                startActivity(intent);
            }
        });
        btn_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,PlayerActivity.class);
                startActivity(intent);
            }
        });


        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(true);
        //初始化
        EMClient.getInstance().init(getApplicationContext(), options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().createAccount(Build.SERIAL, "123456");
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
                EMClient.getInstance().login(Build.SERIAL, "123456", new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        Log.e("aiya","登录成功");
                        //sendGift("dabai","583d9b30");
                    }

                    @Override
                    public void onError(int code, String error) {
                        Log.e("aiya","登录出错");
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                        Log.e("aiya","process-->"+progress+status);
                    }
                });
            }
        }).start();
    }
}
