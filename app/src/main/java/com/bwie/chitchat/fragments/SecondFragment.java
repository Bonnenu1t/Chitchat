package com.bwie.chitchat.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bwie.chitchat.R;
import com.bwie.chitchat.adapter.Mylistadapter;
import com.bwie.chitchat.bean.DataBean;
import com.bwie.chitchat.bean.IndexBean;
import com.bwie.chitchat.core.JNICore;
import com.bwie.chitchat.core.SortUtils;
import com.bwie.chitchat.network.BaseObserver;
import com.bwie.chitchat.network.RetrofitManager;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class SecondFragment extends Fragment {


    @BindView(R.id.select_image)
    ImageView selectImage;
    @BindView(R.id.select_title)
    TextView selectTitle;
    @BindView(R.id.select_login)
    Button selectLogin;
    Unbinder unbinder;
    @BindView(R.id.second_recycler)
    ListView secondRecycler;

    private List<DataBean> data;
    private Mylistadapter mylistadapter;
    private SwipeRefreshLayout springview_indexfragment_id;
    private List<DataBean> data1;

    public SecondFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        unbinder = ButterKnife.bind(this, view);
        springview_indexfragment_id = (SwipeRefreshLayout) view.findViewById(R.id.springview_indexfragment_id);
        selectImage.setVisibility(View.GONE);
        selectTitle.setVisibility(View.VISIBLE);
        selectTitle.setText("联系人");
        getdatesecond();
        data();
        return view;
    }
    private void data() {
        springview_indexfragment_id.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getdatesecond();
                springview_indexfragment_id .setRefreshing(false);     //  false:设置为false是不要一直刷新

            }
        });
    }

    public void getdatesecond() {
        Map<String,String> map = new HashMap<String, String>();
        map.put("user.currenttimer",System.currentTimeMillis()+"");

        String sign =  JNICore.getSign(SortUtils.getMapResult(SortUtils.sortString(map))) ;
        map.put("user.sign", sign);


        RetrofitManager.post("http://qhb.2dyt.com/MyInterface/userAction_selectAllUserAndFriend.action ",
                map, new BaseObserver() {

                    @Override
                    public void onSuccess(String result) {
                        System.out.println("myresult"+result);

                        Gson gson=new Gson();
                        IndexBean indexbean = gson.fromJson(result, IndexBean.class);

                        data1 = indexbean.getData();

                        mylistadapter = new Mylistadapter(getActivity(), data1);

                        secondRecycler.setAdapter(mylistadapter);


                    }

                    @Override
                    public void onFailed(int code) {

                    }
                });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
