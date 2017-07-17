package com.bwie.chitchat.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bwie.chitchat.R;
import com.bwie.chitchat.activitys.BlackActivity;
import com.bwie.chitchat.activitys.TabActivity;
import com.bwie.chitchat.adapter.IndexAdapter;
import com.bwie.chitchat.bean.DataBean;
import com.bwie.chitchat.bean.IndexBean;
import com.bwie.chitchat.core.JNICore;
import com.bwie.chitchat.core.SortUtils;
import com.bwie.chitchat.daoutils.FirstFragmentDaoUtils;
import com.bwie.chitchat.network.BaseObserver;
import com.bwie.chitchat.network.RetrofitManager;
import com.bwie.chitchat.view.FirstFragmentView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

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
public class FirstFragment extends Fragment implements FirstFragmentView {


    Unbinder unbinder;
    int page = 1;
    @BindView(R.id.select_image)
    ImageView selectImage;
    @BindView(R.id.select_title)
    TextView selectTitle;
    @BindView(R.id.select_login)
    Button selectLogin;
    @BindView(R.id.first_recycler_view)
    RecyclerView firstRecyclerView;
    @BindView(R.id.springview_indexfragment)
    SpringView springviewIndexfragment;
    private TabActivity activity;
    private IndexAdapter adapter;

    private List<DataBean> list = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    private HorizontalDividerItemDecoration horizontalDividerItemDecoration;
    private View view;
    private long time;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        activity = (TabActivity) getActivity() ;
        view = inflater.inflate(R.layout.fragment_first, container, false);
        unbinder = ButterKnife.bind(this, view);

        initView(view);

        return view;
    }


    private void initView(View view) {
        selectTitle.setVisibility(View.VISIBLE);
        selectTitle.setText("发现");
        selectLogin.setVisibility(View.VISIBLE);
        selectLogin.setTag(1);
        selectLogin.setText("切换模式");

        getData(System.currentTimeMillis());

        selectLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (int) v.getTag();
                if (tag == 1) {
                    selectLogin.setTag(2);
                    toStaggeredGridLayoutManager();
                } else {
                    selectLogin.setTag(1);
                    toLinearLayoutManager();
                }
            }
        });

        horizontalDividerItemDecoration = new HorizontalDividerItemDecoration.Builder(getActivity()).build();

        adapter = new IndexAdapter(getActivity(),list);
        toLinearLayoutManager();

        springviewIndexfragment.setHeader(new DefaultHeader(getActivity()));
        springviewIndexfragment.setFooter(new DefaultFooter(getActivity()));




        springviewIndexfragment.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                //System.out.println("onRefresh = " );
               // presenter.getData(System.currentTimeMillis());
                list.clear();
                getData(System.currentTimeMillis());
                adapter.notifyDataSetChanged();

                springviewIndexfragment.onFinishFreshAndLoad();

            }

            @Override
            public void onLoadmore() {
                //System.out.println("onLoadmore = " );
               // presenter.getData(time);
                getData(list.get(list.size()-1).getLasttime());
//                adapter.setData(indexBean);
//                time = indexBean.getData().get(indexBean.getData().size()-1).getCreatetime();
//                adapter.notifyDataSetChanged();
                adapter.adapterData(list);
                adapter.notifyDataSetChanged();
                        springviewIndexfragment.onFinishFreshAndLoad();

            }
        });




    }

    public void toLinearLayoutManager() {
        if (linearLayoutManager == null) {
            linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        }
        adapter.dataChange(1);
        firstRecyclerView.setLayoutManager(linearLayoutManager);
        firstRecyclerView.setAdapter(adapter);
        firstRecyclerView.addItemDecoration(horizontalDividerItemDecoration);

    }
    public void toStaggeredGridLayoutManager() {
        if (staggeredGridLayoutManager == null) {
            staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        }
        adapter.dataChange(2);
        firstRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        firstRecyclerView.setAdapter(adapter);
        firstRecyclerView.removeItemDecoration(horizontalDividerItemDecoration);

    }


    @Override
    public void success(IndexBean indexBean) {
        //adapter.setData(indexBean);

    }

    @Override
    public void failed(int code) {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }



    public void getData(Long time) {

        Map<String, String> map = new HashMap<String, String>();

        map.put("user.currenttimer", time + "");
        String sign = JNICore.getSign(SortUtils.getMapResult(SortUtils.sortString(map)));
        map.put("user.sign", sign);

        RetrofitManager.post("http://qhb.2dyt.com/MyInterface/userAction_selectAllUser.action", map, new BaseObserver() {
            @Override
            public void onSuccess(String result) {

                try {
                    Gson gson = new Gson();
                    final IndexBean indexBean = gson.fromJson(result, IndexBean.class);
                    if (indexBean.getResult_code() == 200 && indexBean.getData().size() != 0) {
                        FirstFragmentDaoUtils.insert(indexBean.getData());
                        list.addAll(indexBean.getData());

                        adapter.setOnItemClickListener(new IndexAdapter.MyItemClickListener() {
                            @Override
                            public void onItemClick(View view, int postion) {

                                Intent intent = new Intent(getActivity(), BlackActivity.class);

                                intent.putExtra("uid",postion);
                                //System.out.println("===== " + postion);
                                getActivity().startActivity(intent);
                            }
                        });

                    }


                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int code) {

            }
        });
    }

}
