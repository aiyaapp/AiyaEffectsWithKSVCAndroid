/*
 *
 * AiyaWrapFilter.java
 * 
 * Created by Wuwang on 2017/3/14
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.ksyun.media.streamer.demo.aiya;

import android.content.Context;
import android.util.Log;

import com.aiyaapp.aavt.gl.LazyFilter;
import com.aiyaapp.aiya.AiyaBeauty;
import com.aiyaapp.aiya.filter.AyBigEyeFilter;
import com.aiyaapp.aiya.filter.AyThinFaceFilter;
import com.aiyaapp.aiya.filter.AyTrackFilter;
import com.aiyaapp.aiya.render.AiyaGiftFilter;
import com.ksyun.media.streamer.filter.imgtex.ImgTexFilterBase;
import com.ksyun.media.streamer.framework.ImgTexFormat;
import com.ksyun.media.streamer.framework.ImgTexFrame;
import com.ksyun.media.streamer.util.gles.GLRender;
import com.aiyaapp.aiya.filter.AyBeautyFilter;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glDisable;

public class AiyaWrapFilter extends ImgTexFilterBase {

    private AyBeautyFilter mBeautyFilter;
    private AyTrackFilter mTrackerFilter;
    private AyBigEyeFilter mBigEyeFilter;
    private AyThinFaceFilter mThinFilter;
    private AiyaGiftFilter mGiftFilter;
    private AiyaVertFlipFilter mVertFlipFilter;
    private LazyFilter mShowFilter;

    private Context mContext;

    private boolean isStartDraw=false;

    private ImgTexFormat mSrcFormat;

    private final String tag=getClass().getName();

    public AiyaWrapFilter(Context context, GLRender glRender) {
        super(glRender);
        mContext = context;
        mTrackerFilter = new AyTrackFilter(context);
        mBeautyFilter = new AyBeautyFilter(AiyaBeauty.TYPE5);
        mThinFilter = new AyThinFaceFilter();
        mBigEyeFilter = new AyBigEyeFilter();
        mGiftFilter = new AiyaGiftFilter(context, null);
        mVertFlipFilter = new AiyaVertFlipFilter();
        mShowFilter = new LazyFilter();
    }


    @Override
    protected ImgTexFormat getSrcPinFormat() {
        return mSrcFormat;
    }

    @Override
    protected void onGLContextReady() {
        super.onGLContextReady();
        Log.e(tag,"onGLContextReady");
    }

    @Override
    protected void onFormatChanged(int i, ImgTexFormat imgTexFormat) {
        mSrcFormat=new ImgTexFormat(1,imgTexFormat.width,imgTexFormat.height);
        Log.e(tag,"onFormatChanged");
        isStartDraw=false;

        mShowFilter.create();
        mShowFilter.sizeChanged(imgTexFormat.width, imgTexFormat.height);

        mVertFlipFilter.create();
        mVertFlipFilter.sizeChanged(imgTexFormat.width, imgTexFormat.height);

        mTrackerFilter.create();
        mTrackerFilter.sizeChanged(imgTexFormat.width, imgTexFormat.height);

        mBeautyFilter.create();
        mBeautyFilter.sizeChanged(imgTexFormat.width,imgTexFormat.height);
        mBeautyFilter.setDegree(0.5f);

        mThinFilter.create();
        mThinFilter.sizeChanged(imgTexFormat.width, imgTexFormat.height);
        mThinFilter.setDegree(0.8f);

        mBigEyeFilter.create();
        mBigEyeFilter.sizeChanged(imgTexFormat.width, imgTexFormat.height);
        mBigEyeFilter.setDegree(0.8f);

        mGiftFilter.create();
        mGiftFilter.sizeChanged(imgTexFormat.width, imgTexFormat.height);
        //mGiftFilter.setEffect("assets/modelsticker/mogulin/meta.json");
        mGiftFilter.setEffect(null);
    }

    @Override
    protected void onDraw(ImgTexFrame[] imgTexFrames, boolean b) {
        if(!isStartDraw){
            isStartDraw=true;
            Log.e(tag,"onDraw");
        }

        int texture = mVertFlipFilter.drawToTexture(imgTexFrames[0].textureId);

        // track first
        mTrackerFilter.drawToTexture(texture);

        // do beauty
        texture = mBeautyFilter.drawToTexture(imgTexFrames[0].textureId);

        mGiftFilter.setFaceDataID(mTrackerFilter.getFaceDataID());
        texture = mGiftFilter.drawToTexture(texture);

        // make eye bigger
        mBigEyeFilter.setFaceDataID(mTrackerFilter.getFaceDataID());
        texture = mBigEyeFilter.drawToTexture(texture);

        // make face thinner
        mThinFilter.setFaceDataID(mTrackerFilter.getFaceDataID());
        texture = mThinFilter.drawToTexture(texture);

        // draw to output
        mShowFilter.draw(texture);

        glDisable(GL_BLEND);
        glDisable(GL_DEPTH_TEST);
    }

    @Override
    protected void onRelease() {
        super.onRelease();
        isStartDraw=false;
        mTrackerFilter.destroy();
        mBeautyFilter.destroy();
        mBigEyeFilter.destroy();
        mThinFilter.destroy();

        Log.e(tag,"onRelease");
    }

    @Override
    public int getSinkPinNum() {
        return 1;
    }
}
