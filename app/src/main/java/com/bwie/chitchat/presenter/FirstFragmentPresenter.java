package com.bwie.chitchat.presenter;

import com.bwie.chitchat.base.BasePresenter;
import com.bwie.chitchat.bean.IndexBean;
import com.bwie.chitchat.model.FirstFragmentModel;
import com.bwie.chitchat.model.FirstFragmentModelImpl;
import com.bwie.chitchat.view.FirstFragmentView;

/**
 * Created by Bonnenu1t丶 on 2017/7/11.
 */

public class FirstFragmentPresenter extends BasePresenter<FirstFragmentView>{

    private FirstFragmentModel firstFragmentModel ;

    public FirstFragmentPresenter(){
        firstFragmentModel = new FirstFragmentModelImpl();
    }

    public void getData(Long time){

        firstFragmentModel.getData(new FirstFragmentModel.DataListener() {
            @Override
            public void onSuccess(IndexBean indexBean) {
                view.success(indexBean);


            }

            @Override
            public void onFailed(int code) {
                view.failed(code);
            }
        },System.currentTimeMillis());

    }
}
