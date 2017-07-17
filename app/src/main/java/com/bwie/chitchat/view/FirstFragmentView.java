package com.bwie.chitchat.view;

import com.bwie.chitchat.bean.IndexBean;

/**
 * Created by Bonnenu1t丶 on 2017/7/11.
 */

public interface FirstFragmentView {

    public void success(IndexBean indexBean);
    public void failed(int code);
}
