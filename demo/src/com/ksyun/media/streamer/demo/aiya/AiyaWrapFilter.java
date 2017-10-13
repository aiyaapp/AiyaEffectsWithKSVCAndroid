/*
 *
 * AiyaWrapFilter.java
 * 
 * Created by Wuwang on 2017/3/14
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.ksyun.media.streamer.demo.aiya;

import android.util.Log;

import com.aiyaapp.camera.sdk.filter.AFilter;
import com.ksyun.media.streamer.filter.imgtex.ImgTexFilterBase;
import com.ksyun.media.streamer.framework.ImgTexFormat;
import com.ksyun.media.streamer.framework.ImgTexFrame;
import com.ksyun.media.streamer.util.gles.GLRender;

public class AiyaWrapFilter extends ImgTexFilterBase {

    private AFilter mFilter;

    private boolean isStartDraw=false;

    private ImgTexFormat mSrcFormat;

    private final String tag=getClass().getName();

    public AiyaWrapFilter(GLRender glRender, final AFilter filter) {
        super(glRender);
        this.mFilter=filter;
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
        mFilter.create();
        mFilter.setSize(imgTexFormat.width,imgTexFormat.height);
    }

    @Override
    protected void onDraw(ImgTexFrame[] imgTexFrames, boolean b) {
        if(!isStartDraw){
            isStartDraw=true;
            Log.e(tag,"onDraw");
        }

        mFilter.setTextureId(imgTexFrames[0].textureId);
        mFilter.draw();
    }

    @Override
    protected void onRelease() {
        super.onRelease();
        isStartDraw=false;
        Log.e(tag,"onRelease");
    }

    @Override
    public int getSinkPinNum() {
        return 1;
    }
}
