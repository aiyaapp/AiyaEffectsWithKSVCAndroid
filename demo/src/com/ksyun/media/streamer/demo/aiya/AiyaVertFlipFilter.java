package com.ksyun.media.streamer.demo.aiya;

import android.content.res.Resources;
import com.aiyaapp.aavt.gl.BaseFilter;


/**
 * 特效滤镜，使用此类处理纹理，为一张纹理添加上特效
 *
 * @author wuwang
 */
public class AiyaVertFlipFilter extends BaseFilter {

    public AiyaVertFlipFilter(Resources resource) {
        super(resource,"shader/base.vert","shader/base.frag");
    }

    public AiyaVertFlipFilter(String vert,String frag){
        super(null,vert,frag);
    }

    public AiyaVertFlipFilter(){
        super(null,"attribute vec4 aVertexCo;\n" +
                        "attribute vec2 aTextureCo;\n" +
                        "\n" +
                        "uniform mat4 uVertexMatrix;\n" +
                        "uniform mat4 uTextureMatrix;\n" +
                        "\n" +
                        "varying vec2 vTextureCo;\n" +
                        "\n" +
                        "void main(){\n" +
                        "    gl_Position = uVertexMatrix*aVertexCo;\n" +
                        "    vTextureCo = (uTextureMatrix*vec4(aTextureCo,0,1)).xy;\n" +
                        "}",
                "precision mediump float;\n" +
                        "varying vec2 vTextureCo;\n" +
                        "uniform sampler2D uTexture;\n" +
                        "void main() {\n" +
                        "    gl_FragColor = texture2D( uTexture, vec2(vTextureCo.x, 1.0-vTextureCo.y));\n" +
                        "}");
    }

    @Override
    protected void onCreate() {
        super.onCreate();
    }

}
