package com.bwie.chitchat.base;

import android.app.Application;

import com.bwie.chitchat.db.DaoMaster;
import com.bwie.chitchat.db.DaoSession;
import com.bwie.chitchat.utils.AMapUtils;

import cn.smssdk.SMSSDK;


/**
 * Created by muhanxi on 17/6/19.
 */

public class IApplication extends Application {

    public static DaoSession daoSession ;
    public static IApplication application ;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        SMSSDK.initSDK(application,"1f337ab220920","0ad4986ad8e865b4b9097a451489d935");

        initJNI();
        aMap();
        initGreendao();

    }



    public static IApplication getApplication(){
        if(application == null){
            application = getApplication() ;
        }
        return application;
    }



    private void initJNI(){
        System.loadLibrary("core");
    }

    public void aMap(){
        AMapUtils.getInstance().startUtils(this);
    }



    public void initGreendao(){


        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,"dliao.db");
        DaoMaster master = new DaoMaster(helper.getWritableDatabase());
        //   加密
//        DaoMaster master = new DaoMaster(helper.getEncryptedWritableDb("1111"));

        daoSession = master.newSession() ;

    }


}
