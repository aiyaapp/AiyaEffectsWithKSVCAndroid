package com.ksyun.live.demo.player.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aiyaapp.aiya.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.exceptions.HyphenateException;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private LocalFragment localFragment;
    private Button media_net;
    private Button media_setting;
    private Button media_history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setActionBarLayout(R.layout.media_actionbar,this);

        setDefaultFragment();
        //EMOptions options = new EMOptions();
        //// 默认添加好友时，是不需要验证的，改成需要验证
        //options.setAcceptInvitationAlways(true);
        ////初始化
        //EMClient.getInstance().init(getApplicationContext(), options);
        ////在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        //EMClient.getInstance().setDebugMode(true);
        //new Thread(new Runnable() {
        //    @Override
        //    public void run() {
        //        try {
        //            EMClient.getInstance().createAccount(Build.SERIAL, "123456");
        //        } catch (HyphenateException e) {
        //            e.printStackTrace();
        //        }
        //        EMClient.getInstance().login(Build.SERIAL, "123456", new EMCallBack() {
        //            @Override
        //            public void onSuccess() {
        //                Log.e("aiya","登录成功");
        //                //sendGift("dabai","583d9b30");
        //            }
        //
        //            @Override
        //            public void onError(int code, String error) {
        //                Log.e("aiya","登录出错");
        //            }
        //
        //            @Override
        //            public void onProgress(int progress, String status) {
        //                Log.e("aiya","process-->"+progress+status);
        //            }
        //        });
        //    }
        //}).start();
    }

    //public void sendGift(String giftName,String roomId){
    //    EMMessage message = EMMessage.createTxtSendMessage(giftName, roomId);
    //    message.setAttribute("gift","gift");
    //    EMClient.getInstance().chatManager().sendMessage(message);
    //}


    public void setActionBarLayout(int layoutId, Context mContext) {
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            LayoutInflater inflator = (LayoutInflater) this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflator.inflate(layoutId, new LinearLayout(mContext), false);
            ActionBar.LayoutParams layout = new ActionBar.LayoutParams(
                    android.support.v7.app.ActionBar.LayoutParams.MATCH_PARENT, android.support.v7.app.ActionBar.LayoutParams.MATCH_PARENT);
            actionBar.setCustomView(v, layout);
            media_net = (Button)findViewById(R.id.media_network);
            media_history = (Button)findViewById(R.id.media_history);
            media_setting = (Button)findViewById(R.id.media_setting);
            media_net.setOnClickListener(this);
            media_setting.setOnClickListener(this);
            media_history.setOnClickListener(this);
            findViewById(R.id.media_aiyatest).setOnClickListener(this);
        }else{
            Toast.makeText(MainActivity.this, "ActionBar不存在", Toast.LENGTH_SHORT).show();
        }

    }

    private void setDefaultFragment() {

        FragmentManager fm = this.getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        localFragment = new LocalFragment();
        localFragment.setSettings(getSharedPreferences("SETTINGS", Context.MODE_PRIVATE));
        transaction.replace(R.id.contentFrame,localFragment);
        transaction.commit();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch(view.getId()){
            case R.id.media_network:
                Toast.makeText(MainActivity.this, "media_net", Toast.LENGTH_SHORT).show();
                intent = new Intent(MainActivity.this,NetMediaActivty.class);
                startActivity(intent);
                break;
            case R.id.media_history:
                Intent intent2 = new Intent(this,HistoryActivity.class);
                startActivity(intent2);
                Toast.makeText(MainActivity.this, "media_history", Toast.LENGTH_SHORT).show();
                break;
            case R.id.media_aiyatest:
                Intent intent3 = new Intent(this,GiftTestActivity.class);
                startActivity(intent3);
                Toast.makeText(MainActivity.this, "gift_test", Toast.LENGTH_SHORT).show();
                break;
            case R.id.media_setting:
                intent = new Intent(this,SettingActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "media_setting", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        localFragment.onBackPressed();
    }
}
