package com.bwie.chitchat.model;

import com.bwie.chitchat.bean.IndexBean;
import com.bwie.chitchat.core.JNICore;
import com.bwie.chitchat.core.SortUtils;
import com.bwie.chitchat.network.BaseObserver;
import com.bwie.chitchat.network.RetrofitManager;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.HashMap;
import java.util.Map;

public class FirstFragmentModelImpl implements FirstFragmentModel {


    @Override
    public void getData( final DataListener listener,Long time) {

        Map<String,String> map = new HashMap<String, String>();

        map.put("user.currenttimer",time+"");
        String sign =  JNICore.getSign(SortUtils.getMapResult(SortUtils.sortString(map))) ;
        map.put("user.sign",sign);

        RetrofitManager.post("http://qhb.2dyt.com/MyInterface/userAction_selectAllUser.action", map, new BaseObserver() {
            @Override
            public void onSuccess(String result) {

                try {
                    System.out.println("===="+result);
                    Gson gson = new Gson();
                    IndexBean indexBean =   gson.fromJson(result, IndexBean.class);
                    if (indexBean.getResult_code()== 200 && indexBean.getData().size() != 0) {
                        System.out.println("===="+indexBean.toString());
                        listener.onSuccess(indexBean);

                        //FirstFragmentDaoUtils.insert(indexBean.getData());
                    }


                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int code) {
                listener.onFailed(code);
            }
        });


    }

}
