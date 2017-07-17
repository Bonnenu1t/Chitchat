package com.bwie.chitchat.base;

/**
 * Created by Bonnenu1t丶 on 2017/7/4.
 */

/**
 * 定义泛型T
 * @param <T>
 */
public abstract class BasePresenter<T> {

    public T view;

    public void attach(T view){
        this.view = view;
    }

    public void detach(){
        this.view = null;
    }
}
