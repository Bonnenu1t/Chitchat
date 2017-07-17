package com.bwie.chitchat.model;


import com.bwie.chitchat.bean.RegisterBean;

/**
 * Created by Bonnenu1t丶 on 2017/7/6.
 */

public interface RegisterInforFragmentModel {

    public void getData(String phone, String nickname, String sex, String age, String area, String introduce, String password, RegisterInforFragmentDataListener listener);




    public interface RegisterInforFragmentDataListener {


        public void onSuccess(RegisterBean registerBean);

        public void onFailed(int code);

    }

}
