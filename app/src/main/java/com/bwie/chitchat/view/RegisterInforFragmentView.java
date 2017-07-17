package com.bwie.chitchat.view;

import com.bwie.chitchat.bean.RegisterBean;

public interface RegisterInforFragmentView {


    void registerSuccess(RegisterBean registerBean);
    void registerFailed(int code);

}
