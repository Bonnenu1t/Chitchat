package com.bwie.chitchat.fragments;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bwie.chitchat.R;
import com.bwie.chitchat.activitys.ChatMeActivity;
import com.bwie.chitchat.adapter.MyListAdapter;
import com.bwie.chitchat.base.IApplication;
import com.bwie.chitchat.bean.DataBean;
import com.bwie.chitchat.bean.IndexBean;
import com.bwie.chitchat.core.JNICore;
import com.bwie.chitchat.core.SortUtils;
import com.bwie.chitchat.network.BaseObserver;
import com.bwie.chitchat.network.RetrofitManager;
import com.google.gson.Gson;

import java.util.ArrayList;
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

    private MyListAdapter adapter;
    private SwipeRefreshLayout springview_indexfragment_id;
    List<DataBean> data1 = new ArrayList<>();
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

        adapter = new MyListAdapter(getActivity(), data1);
        secondRecycler.setAdapter(adapter);

        getDateSecond();
        data();
        initListener();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        secondRecycler.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChatMeActivity.class);
                intent.putExtra("uid",id);

                startActivity(intent);
            }
        });
    }

    private void initListener() {

    }

    private void data() {
        springview_indexfragment_id.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDateSecond();
                springview_indexfragment_id .setRefreshing(false);     //  false:设置为false是不要一直刷新

            }
        });
    }

    public void getDateSecond() {

        //获取判断是否有网络的管理器
        ConnectivityManager connectivityManager = (ConnectivityManager) IApplication.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取可用网络
        NetworkInfo infor = connectivityManager.getActiveNetworkInfo();
        //获取wifi状态
        NetworkInfo.State wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        //获取网络状态
        NetworkInfo.State interstat = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();

        if (infor == null) {
            //  List<DataBean1> lista = application.daoSession.getDataBean1Dao().queryBuilder().list();
            // list.addAll(lista);
            adapter.notifyDataSetChanged();
        } else {
            Map<String, String> map = new HashMap<String, String>();
            map.put("user.currenttimer", System.currentTimeMillis() + "");

            String sign = JNICore.getSign(SortUtils.getMapResult(SortUtils.sortString(map)));
            map.put("user.sign", sign);


            RetrofitManager.post("http://qhb.2dyt.com/MyInterface/userAction_selectAllUserAndFriend.action ",
                    map, new BaseObserver() {

                        @Override
                        public void onSuccess(String result) {
                            System.out.println("myresult" + result);

                            Gson gson = new Gson();
                            IndexBean indexbean = gson.fromJson(result, IndexBean.class);

                            List<DataBean> list = indexbean.getData();
                            if (list == null){
                                return;
                            }else {
                                data1.addAll(list);
                                adapter.notifyDataSetChanged();
                            }


                            //data.addAll(data1);
//                            mylistadapter = new MyListAdapter(getActivity(), data1);
//                            secondRecycler.setAdapter(mylistadapter);

                        }

                        @Override
                        public void onFailed(int code) {

                        }
                    });
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
