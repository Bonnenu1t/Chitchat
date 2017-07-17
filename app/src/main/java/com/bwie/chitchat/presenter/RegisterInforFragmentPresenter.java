package com.bwie.chitchat.presenter;


import com.bwie.chitchat.base.BasePresenter;
import com.bwie.chitchat.bean.RegisterBean;
import com.bwie.chitchat.model.RegisterInforFragmentModel;
import com.bwie.chitchat.model.RegisterInforFragmentModelImpl;
import com.bwie.chitchat.view.RegisterInforFragmentView;



public class RegisterInforFragmentPresenter extends BasePresenter<RegisterInforFragmentView> {


    private RegisterInforFragmentModel registerInforFragmentModel ;
    public RegisterInforFragmentPresenter(){
        registerInforFragmentModel = new RegisterInforFragmentModelImpl();
    }



    public void vaildInfor(String phone,String nickname,String sex,String age,String area,String introduce,String password){


        //非空判断

        registerInforFragmentModel.getData(phone, nickname, sex, age, area, introduce, password, new RegisterInforFragmentModel.RegisterInforFragmentDataListener() {
            @Override
            public void onSuccess(RegisterBean registerBean) {


                view.registerSuccess(registerBean);

            }

            @Override
            public void onFailed(int code) {

                view.registerFailed(code);
            }
        });






    }


}
