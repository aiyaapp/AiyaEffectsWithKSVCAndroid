/*
 *
 * AiyaMenu.java
 * 
 * Created by Wuwang on 2017/3/14
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.ksyun.media.streamer.demo.aiya;

import android.content.Context;
import android.util.JsonReader;
import android.util.Log;


import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Description:
 */
public class AiyaMenu {

    private Context context;

    private ArrayList<MenuBean> data;

    public AiyaMenu(Context context){
        this.context=context;
        data=new ArrayList<>();
    }

    public void readMenu(String menuPath){
        try {
            Log.e("AiyaMenu", "解析菜单->" +menuPath);
            JsonReader r = new JsonReader(new InputStreamReader(context.getAssets().open(menuPath)));
            r.beginArray();
            data.clear();
            while (r.hasNext()) {
                MenuBean menu = new MenuBean();
                r.beginObject();
                String name;
                while (r.hasNext()) {
                    name = r.nextName();
                    if (name.equals("name")) {
                        menu.name = r.nextString();
                    } else if (name.equals("path")) {
                        menu.path = r.nextString();
                    }
                }
                data.add(menu);
                Log.e("AiyaMenu", "增加菜单->" + menu.name);
                r.endObject();
            }
            r.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //初始化特效按钮菜单
    public ArrayList<MenuBean> getMenuList() {
        return data;
    }

    public class MenuBean {

        public String name;
        public String path;
        public String show;

        public int id;

    }

}
