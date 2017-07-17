package com.bwie.chitchat.model;

import com.bwie.chitchat.bean.IndexBean;

public interface FirstFragmentModel {


    public void getData(DataListener dataListener,Long time);




    public interface DataListener{
        public void onSuccess(IndexBean indexBean);
        public void onFailed(int code);
    }

}