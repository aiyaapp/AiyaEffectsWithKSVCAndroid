package com.ksyun.live.demo.player.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.aiyaapp.aiya.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

/**
 * Created by aiya on 2017/3/29.
 */

public class GiftTestActivity extends AppCompatActivity {

    private EditText mRoom;
    private EditText mOtherGift;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mRoom=(EditText) findViewById(R.id.mRoom);
        mOtherGift=(EditText) findViewById(R.id.mOtherGift);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.mDabai:
                sendGift("dabai");
                break;
            case R.id.mTaoyuan:
                sendGift("shiwaitaoyuan");
                break;
            case R.id.mXiaocao:
                sendGift("grass");
                break;
            case R.id.mLanse:
                sendGift("lanseyaoji");
                break;
            case R.id.mSend:
                String gift=mOtherGift.getText().toString();
                if(gift.length()>0){
                    sendGift(gift);
                }
                break;
        }
    }

    public void sendGift(String giftName){
        String room=mRoom.getText().toString();
        if(room.length()>0){
            EMMessage message = EMMessage.createTxtSendMessage(giftName, room);
            message.setAttribute("gift","gift");
            EMClient.getInstance().chatManager().sendMessage(message);
            Toast.makeText(this,"礼物发送成功",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"请输入直播房间ID",Toast.LENGTH_SHORT).show();
        }
    }
}
