package com.ksyun.media.streamer.demo;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.aiyaapp.aiya.R;
import com.aiyaapp.camera.sdk.AiyaEffects;
import com.aiyaapp.camera.sdk.base.Event;
import com.aiyaapp.camera.sdk.base.ActionObserver;
import com.aiyaapp.camera.sdk.base.ISdkManager;
import com.aiyaapp.camera.sdk.filter.EffectFilter;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.ksyun.media.streamer.encoder.VideoEncodeFormat;
import com.ksyun.media.streamer.framework.AVConst;
import com.ksyun.media.streamer.kit.StreamerConstants;
import com.ksyun.media.streamer.util.device.DeviceInfo;
import com.ksyun.media.streamer.util.device.DeviceInfoTools;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;

public class DemoActivity extends Activity
        implements OnClickListener, RadioGroup.OnCheckedChangeListener,Runnable{
    private static final String TAG = DemoActivity.class.getSimpleName();
    private Button mConnectButton;
    private EditText mUrlEditText;
    private EditText mFrameRateEditText;
    private EditText mVideoBitRateEditText;
    private EditText mAudioBitRateEditText;
    private RadioButton mRes360Button;
    private RadioButton mRes480Button;
    private RadioButton mRes540Button;
    private RadioButton mRes720Button;

    private RadioButton mLandscapeButton;
    private RadioButton mPortraitButton;

    private RadioGroup mEncodeGroup;
    private RadioButton mSWButton;
    private RadioButton mHWButton;
    private RadioButton mSW1Button;

    private RadioGroup mEncodeTypeGroup;
    private RadioButton mEncodeWithH264;
    private RadioButton mEncodeWithH265;

    private RadioGroup mSceneGroup;
    private RadioButton mSceneDefaultButton;
    private RadioButton mSceneShowSelfButton;
    private RadioGroup mProfileGroup;
    private RadioButton mProfileLowPowerButton;
    private RadioButton mProfileBalanceButton;
    private RadioButton mProfileHighPerfButton;

    private CheckBox mAutoStartCheckBox;
    private CheckBox mShowDebugInfoCheckBox;

    private DeviceInfo mDeviceInfo;
    private static boolean mShowDeviceToast = false;

    private BlockingQueue<String> effectQueue=new LinkedBlockingDeque<>();
    private boolean isDestroyed=false;
    private Semaphore mGiftSem=new Semaphore(1,true);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity);

        EMClient.getInstance().chatManager().addMessageListener(msgListener);

        final ActionObserver observer = new ActionObserver() {
            @Override
            public void onAction(Event state) {
                switch (state.eventType){
                    case Event.RESOURCE_FAILED:
                        com.aiyaapp.camera.sdk.base.Log.e("resource failed");
                        break;
                    case Event.INIT_FAILED:
                        com.aiyaapp.camera.sdk.base.Log.e("init failed");
                        Toast.makeText(DemoActivity.this, "注册失败，请检查网络", Toast.LENGTH_SHORT)
                            .show();
                        break;
                    case Event.INIT_SUCCESS:
                        com.aiyaapp.camera.sdk.base.Log.e("init success");
                        break;
                    case Event.PROCESS_END:
                        mGiftSem.release();
                        break;
                }
            }
        };
        AiyaEffects.getInstance().registerObserver(observer);
        AiyaEffects.getInstance().init(DemoActivity.this, getExternalFilesDir(null)
                .getAbsolutePath() + "/146-563-918-415-578-677-783-748-043-705-956.vlc", "");
        AiyaEffects.getInstance().set(ISdkManager.SET_MODE,ISdkManager.MODE_GIFT);

        mConnectButton = (Button) findViewById(R.id.connectBT);
        mConnectButton.setOnClickListener(this);

        mUrlEditText = (EditText) findViewById(R.id.rtmpUrl);
        mFrameRateEditText = (EditText) findViewById(R.id.frameRatePicker);
        mFrameRateEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        mVideoBitRateEditText = (EditText) findViewById(R.id.videoBitratePicker);
        mVideoBitRateEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        mAudioBitRateEditText = (EditText) findViewById(R.id.audioBitratePicker);
        mAudioBitRateEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        mRes360Button = (RadioButton) findViewById(R.id.radiobutton1);
        mRes480Button = (RadioButton) findViewById(R.id.radiobutton2);
        mRes540Button = (RadioButton) findViewById(R.id.radiobutton3);
        mRes720Button = (RadioButton) findViewById(R.id.radiobutton4);
        mLandscapeButton = (RadioButton) findViewById(R.id.orientationbutton1);
        mPortraitButton = (RadioButton) findViewById(R.id.orientationbutton2);
        mEncodeGroup = (RadioGroup) findViewById(R.id.encode_group);
        mSWButton = (RadioButton) findViewById(R.id.encode_sw);
        mHWButton = (RadioButton) findViewById(R.id.encode_hw);
        mSW1Button = (RadioButton) findViewById(R.id.encode_sw1);
        mEncodeTypeGroup = (RadioGroup) findViewById(R.id.encode_type);
        mEncodeWithH264 = (RadioButton) findViewById(R.id.encode_h264);
        mEncodeWithH265 = (RadioButton) findViewById(R.id.encode_h265);
        mSceneGroup = (RadioGroup) findViewById(R.id.encode_scene);
        mSceneDefaultButton = (RadioButton) findViewById(R.id.encode_scene_default);
        mSceneShowSelfButton = (RadioButton) findViewById(R.id.encode_scene_show_self);
        mProfileGroup = (RadioGroup) findViewById(R.id.encode_profile);
        mProfileLowPowerButton = (RadioButton) findViewById(R.id.encode_profile_low_power);
        mProfileBalanceButton = (RadioButton) findViewById(R.id.encode_profile_balance);
        mProfileHighPerfButton = (RadioButton) findViewById(R.id.encode_profile_high_perf);
        mAutoStartCheckBox = (CheckBox) findViewById(R.id.autoStart);
        mShowDebugInfoCheckBox = (CheckBox) findViewById(R.id.print_debug_info);

        updateUI();
        mEncodeTypeGroup.setOnCheckedChangeListener(this);
        mEncodeGroup.setOnCheckedChangeListener(this);

        isDestroyed=false;
        Thread mThread=new Thread(this);
        mThread.start();
    }

    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            //收到消息
            for (EMMessage msg:messages){
                if(msg.getBody() instanceof EMTextMessageBody){
                    if(msg.getStringAttribute("gift","").equals("gift")){
                        String giftId=((EMTextMessageBody)msg.getBody()).getMessage();
                        Log.e("aiya",giftId);
                        //AiyaEffects.getInstance().setEffect("assets/modelsticker/"+giftId+"/meta.json");
                        effectQueue.add("assets/modelsticker/"+giftId+"/meta.json");
                    }
                }
            }
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            //收到已读回执
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
            //收到已送达回执
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        //init encode info
        //若在硬编白名单中存在设备信息，则参考白名单信息进行配置
        DeviceInfo lastDeviceInfo = mDeviceInfo;
        mDeviceInfo = DeviceInfoTools.getInstance().getDeviceInfo();
        Log.i(TAG, "deviceInfo:" +  mDeviceInfo.printDeviceInfo());
        if(!mShowDeviceToast ||lastDeviceInfo==null|| !mDeviceInfo.compareDeviceInfo(lastDeviceInfo)) {
            if (mDeviceInfo.encode_h264 == DeviceInfo.ENCODE_HW_SUPPORT) {
                //支持硬编，建议使用硬编
                mHWButton.setChecked(true);
                Toast.makeText(this, "该设备支持h264硬编，建议您使用硬编", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "该设备可能不在硬编白名单中\n或者不支持硬编\n或者服务器还未返回" +
                        "\n如果支持硬编，欢迎一起更新白名单", Toast.LENGTH_SHORT).show();
            }
            mShowDeviceToast = true;
        }
    }
    
    private void setEnableRadioGroup(RadioGroup radioGroup, boolean enable) {
        for (int i=0; i<radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setEnabled(enable);
        }
    }

    private void updateUI() {
        if (mHWButton.isChecked() || mEncodeWithH265.isChecked()) {
            setEnableRadioGroup(mSceneGroup, false);
            setEnableRadioGroup(mProfileGroup, false);
        } else {
            setEnableRadioGroup(mSceneGroup, true);
            setEnableRadioGroup(mProfileGroup, true);
        }

        mUrlEditText.setText(getString(R.string.test_addr,Build.SERIAL));
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        updateUI();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.connectBT:
                int frameRate = 0;
                int videoBitRate = 0;
                int audioBitRate = 0;
                int videoResolution;
                int encodeType;
                int encodeMethod;
                int encodeScene;
                int encodeProfile;
                int orientation;
                boolean startAuto;
                boolean showDebugInfo;

                if (!TextUtils.isEmpty(mUrlEditText.getText())
					&& mUrlEditText.getText().toString().startsWith("rtmp")) {
                    if (!TextUtils.isEmpty(mFrameRateEditText.getText().toString())) {
                        frameRate = Integer.parseInt(mFrameRateEditText.getText()
                                .toString());
                    }

                    if (!TextUtils.isEmpty(mVideoBitRateEditText.getText().toString())) {
                        videoBitRate = Integer.parseInt(mVideoBitRateEditText.getText()
                                .toString());
                    }

                    if (!TextUtils.isEmpty(mAudioBitRateEditText.getText().toString())) {
                        audioBitRate = Integer.parseInt(mAudioBitRateEditText.getText()
                                .toString());
                    }

                    if (mRes360Button.isChecked()) {
                        videoResolution = StreamerConstants.VIDEO_RESOLUTION_360P;
                    } else if (mRes480Button.isChecked()) {
                        videoResolution = StreamerConstants.VIDEO_RESOLUTION_480P;
                    } else if (mRes540Button.isChecked()) {
                        videoResolution = StreamerConstants.VIDEO_RESOLUTION_540P;
                    } else {
                        videoResolution = StreamerConstants.VIDEO_RESOLUTION_720P;
                    }

                    if (mEncodeWithH265.isChecked()) {
                        encodeType = AVConst.CODEC_ID_HEVC;
                    } else {
                        encodeType = AVConst.CODEC_ID_AVC;
                    }

                    if (mHWButton.isChecked()) {
                        encodeMethod = StreamerConstants.ENCODE_METHOD_HARDWARE;
                        mSceneGroup.setClickable(false);
                    } else if (mSWButton.isChecked()) {
                        mSceneGroup.setClickable(true);
                        encodeMethod = StreamerConstants.ENCODE_METHOD_SOFTWARE;
                    } else {
                        mSceneGroup.setClickable(true);
                        encodeMethod = StreamerConstants.ENCODE_METHOD_SOFTWARE_COMPAT;
                    }

                    //TODO
                    if (mSceneDefaultButton.isChecked()) {
                        encodeScene = VideoEncodeFormat.ENCODE_SCENE_DEFAULT;
                    } else {
                        encodeScene = VideoEncodeFormat.ENCODE_SCENE_SHOWSELF;
                    }

                    if (mProfileLowPowerButton.isChecked()) {
                        encodeProfile = VideoEncodeFormat.ENCODE_PROFILE_LOW_POWER;
                    } else if (mProfileBalanceButton.isChecked()) {
                        encodeProfile = VideoEncodeFormat.ENCODE_PROFILE_BALANCE;
                    } else {
                        encodeProfile = VideoEncodeFormat.ENCODE_PROFILE_HIGH_PERFORMANCE;
                    }

                    if (mLandscapeButton.isChecked()) {
                        orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    } else if (mPortraitButton.isChecked()) {
                        orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    } else {
                        orientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR;
                    }

                    startAuto = mAutoStartCheckBox.isChecked();
                    showDebugInfo = mShowDebugInfoCheckBox.isChecked();

                    CameraActivity.startActivity(getApplicationContext(), 0,
                            mUrlEditText.getText().toString(), frameRate, videoBitRate,
                            audioBitRate, videoResolution, orientation, encodeType,  encodeMethod,
                            encodeScene, encodeProfile, startAuto, showDebugInfo);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroyed=true;
        effectQueue.clear();
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }

    @Override
    public void run() {
        while (!isDestroyed){
            try {
                mGiftSem.acquire();
                String gift=effectQueue.take();
                AiyaEffects.getInstance().setEffect(gift);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
