/*
 *
 * AiyaWrapFilter.java
 * 
 * Created by Wuwang on 2017/3/14
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.ksyun.media.streamer.demo.aiya;

import android.util.Log;

import com.aiyaapp.aiya.AiyaBeauty;
import com.ksyun.media.streamer.filter.imgtex.ImgTexFilterBase;
import com.ksyun.media.streamer.framework.ImgTexFormat;
import com.ksyun.media.streamer.framework.ImgTexFrame;
import com.ksyun.media.streamer.util.gles.GLRender;
import com.aiyaapp.aiya.filter.AyBeautyFilter;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glDisable;

public class AiyaWrapFilter extends ImgTexFilterBase {

    private AyBeautyFilter mFilter;

    private boolean isStartDraw=false;

    private ImgTexFormat mSrcFormat;

    private final String tag=getClass().getName();

    public AiyaWrapFilter(GLRender glRender) {
        super(glRender);

        this.mFilter= new AyBeautyFilter(AiyaBeauty.TYPE5);
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
        mFilter.sizeChanged(imgTexFormat.width,imgTexFormat.height);
        mFilter.setDegree(0.5f);
    }

    @Override
    protected void onDraw(ImgTexFrame[] imgTexFrames, boolean b) {
        if(!isStartDraw){
            isStartDraw=true;
            Log.e(tag,"onDraw");
        }

        int texture = mFilter.drawToTexture(imgTexFrames[0].textureId);
        mFilter.draw(texture);
        glDisable(GL_BLEND);
        glDisable(GL_DEPTH_TEST);
    }

    @Override
    protected void onRelease() {
        super.onRelease();
        isStartDraw=false;
        mFilter.destroy();
        Log.e(tag,"onRelease");
    }

    @Override
    public int getSinkPinNum() {
        return 1;
    }
}
