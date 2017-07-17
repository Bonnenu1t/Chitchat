package com.bwie.chitchat.presenter;

import android.text.TextUtils;

import com.bwie.chitchat.base.BasePresenter;
import com.bwie.chitchat.model.RegisterSmsModelImpl;
import com.bwie.chitchat.utils.PhoneCheckUtils;
import com.bwie.chitchat.view.RegisterSmsView;




public class RegisterSmsPresenter extends BasePresenter<RegisterSmsView> {


    private RegisterSmsModelImpl registerSmsModel ;

    public RegisterSmsPresenter(){
        registerSmsModel = new RegisterSmsModelImpl();
    }


    public void getVerificationCode(String phone){


        if(TextUtils.isEmpty(phone)){

            view.phoneError(1);
            return;
        }



        if(!PhoneCheckUtils.isChinaPhoneLegal(phone)){
            view.phoneError(2);
            return;
        }

        registerSmsModel.getVerificationCode(phone);


        view.showTimer();

    }



    public void nextStep(String phone,String sms){

        if(TextUtils.isEmpty(phone)){

            view.phoneError(1);
            return;
        }



        if(!PhoneCheckUtils.isChinaPhoneLegal(phone)){
            view.phoneError(2);
            return;
        }




        //判断验证码是否合法  验证码是4为数字 \\d{4} sms 非空

        view.toNextPage();

    }


}
