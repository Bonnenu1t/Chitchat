package com.bwie.chitchat.model;

import cn.smssdk.SMSSDK;

/**
 * Created by Bonnenu1t丶 on 2017/7/6.
 */

public class RegisterSmsModelImpl implements RegisterSmsModel {



    public void getVerificationCode(String phone){

        SMSSDK.getVerificationCode("86", phone);

    }



}
