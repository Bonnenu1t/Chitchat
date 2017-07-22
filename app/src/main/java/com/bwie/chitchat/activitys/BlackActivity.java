package com.bwie.chitchat.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bwie.chitchat.R;
import com.bwie.chitchat.adapter.BlackAdapter;
import com.bwie.chitchat.base.IActivity;
import com.bwie.chitchat.bean.IndexBean;
import com.bwie.chitchat.bean.UserBean;
import com.bwie.chitchat.bean.UserInfoBean;
import com.bwie.chitchat.core.JNICore;
import com.bwie.chitchat.core.SortUtils;
import com.bwie.chitchat.daoutils.FirstFragmentDaoUtils;
import com.bwie.chitchat.horizontal.HorizontalScrollViewAdapter;
import com.bwie.chitchat.horizontal.MyHorizontalScrollView;
import com.bwie.chitchat.network.BaseObserver;
import com.bwie.chitchat.network.RetrofitManager;
import com.bwie.chitchat.widget.MyToast;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BlackActivity extends IActivity {

    @BindView(R.id.id_content)
    PhotoView blackImage;
    @BindView(R.id.id_gallery)
    LinearLayout idGallery;
    @BindView(R.id.id_horizontalScrollView)
    MyHorizontalScrollView mHorizontalScrollView;
    @BindView(R.id.black_recycler)
    RecyclerView blackRecycler;
    @BindView(R.id.black_scroll)
    ScrollView blackScroll;
    @BindView(R.id.black_name)
    TextView blackName;
    @BindView(R.id.black_sex)
    TextView blackSex;
    @BindView(R.id.black_city)
    TextView blackCity;
    @BindView(R.id.black_age)
    TextView blackAge;
    @BindView(R.id.black_start)
    Button blackStart;
    @BindView(R.id.black_end)
    Button blackEnd;
    private String ok;
    private int userid;


    private HorizontalScrollViewAdapter mAdapter;
    String[] name = {" 我的资料", "昵称", "年龄", "星座", "居住地"
            , "身高", "体重", "血型", "籍贯", "职业", "学历"
            , "收入", "魅力部位", "婚姻状况", "住房情况", "接收异地恋", "喜欢的异性"
            , "接收亲密行为", "和父母同住", "是否要小孩", "我的标签"};

    String[] ziliao = {"", " 高贵的银耳汤", "24岁", "摩羯座", "北京市-市区", " 0cm"
            , " 0斤", "保密", "保密", "保密", "保密", "保密"
            , "保密", "保密", "保密", "保密", "保密", "保密"
            , "保密", "保密", ""};
    List<UserInfoBean.DataBean.PhotolistBean> photolist = new ArrayList<>();
    private List<Integer> mDatas = new ArrayList<Integer>(Arrays.asList(
            R.drawable.woman1, R.drawable.gril_photo_one, R.drawable.gril_photo_two,
            R.drawable.gril_photo_three, R.drawable.cover_1, R.drawable.cover_2,
            R.drawable.cover_3, R.drawable.woman1, R.drawable.gril_photo_one));
    private UserInfoBean.DataBean data;
    private List<UserBean> others = new ArrayList<UserBean>();
    private int uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black);
        ButterKnife.bind(this);

        SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);

        ok = sp.getString("sure", "");
        Intent intent = getIntent();
        uid = intent.getIntExtra("uid", 0);
        getData(String.valueOf(uid));
        //  userid = intent.getIntExtra("userid", 1);

        for (int i = 0; i < 21; i++) {
            //一定要记住吧数组里的[i]加上
            UserBean userBean = new UserBean();
            userBean.setName(name[i] + "");
            userBean.setZiLiao(ziliao[i] + "");
            others.add(userBean);
        }

        blackScroll.smoothScrollTo(0, 20);

        BlackAdapter adapter = new BlackAdapter(this, others);
        blackRecycler.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        blackRecycler.setLayoutManager(linearLayoutManager);


/*// 启用图片缩放功能
        blackImage.enable();
// 获取图片信息
        Info info = blackImage.getInfo();
// 从普通的ImageView中获取Info
// 从一张图片信息变化到现在的图片，用于图片点击后放大浏览，具体使用可以参照demo的使用
        blackImage.animaFrom(info);
// 获取/设置 动画持续时间
        blackImage.setAnimaDuring(2000);
        int d = blackImage.getAnimaDuring();
// 获取/设置 最大缩放倍数
        blackImage.setMaxScale(1);
        float maxScale = blackImage.getMaxScale();*/


        mHorizontalScrollView = (MyHorizontalScrollView) findViewById(R.id.id_horizontalScrollView);
        mAdapter = new HorizontalScrollViewAdapter(this, mDatas);

        //添加滚动回调
        mHorizontalScrollView
                .setCurrentImageChangeListener(new MyHorizontalScrollView.CurrentImageChangeListener() {
                    @Override
                    public void onCurrentImgChanged(int position,
                                                    View viewIndicator) {
                        blackImage.setImageResource(mDatas.get(position));
                        viewIndicator.setBackgroundColor(Color.parseColor("#AA024DA4"));
                    }
                });
        //添加点击回调
        mHorizontalScrollView.setOnItemClickListener(new MyHorizontalScrollView.OnItemClickListener() {

            @Override
            public void onClick(View view, int position) {
                blackImage.setImageResource(mDatas.get(position));
                view.setBackgroundColor(Color.parseColor("#AA024DA4"));
            }
        });
        //设置适配器
        mHorizontalScrollView.initDatas(mAdapter);
    }

    @OnClick(R.id.black_start)
    public void onViewClicked() {
        getTianjia(uid);
    }

    //打招呼添加好友
    public void getTianjia(final int userid) {

        if (ok != null && ok.length() != 0) {
            //加好友
            AlertDialog.Builder builder = new AlertDialog.Builder(BlackActivity.this);

            builder.setTitle("是否添加加好友");
            builder.setPositiveButton("添加好友", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // Toast.makeText(DataPageActivity.this,"添加好友成功",Toast.LENGTH_SHORT).show();

                    Map<String, String> map = new HashMap<String, String>();

                    map.put("relationship.friendId", userid + "");

                    String sign = JNICore.getSign(SortUtils.getMapResult(SortUtils.sortString(map)));
                    map.put("user.sign", sign);
                    RetrofitManager.post("http://qhb.2dyt.com/MyInterface/userAction_addFriends.action", map, new BaseObserver() {
                        @Override
                        public void onSuccess(String result) {

                            Gson gson = new Gson();
                            IndexBean indexBean = gson.fromJson(result, IndexBean.class);


                            FirstFragmentDaoUtils.insert(indexBean.getData());
                            //  gson.fromJson(result,)

                            System.out.println("result ========================== " + result);

                            Toast.makeText(BlackActivity.this, "添加成功", Toast.LENGTH_SHORT).show();

                            blackStart.setVisibility(View.GONE);
                            blackEnd.setVisibility(View.VISIBLE);

                        }

                        @Override
                        public void onFailed(int code) {

                        }
                    });


                }
            });
            builder.setNegativeButton("取消添加", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();

        } else {

            Intent intent = new Intent(BlackActivity.this, LoginActivity.class);

            startActivity(intent);

        }

    }

    public void getData(String userid) {
        Map map = new HashMap<String, String>();
        map.put("user.userId", userid);
        System.out.println("userid===== " + userid);
        String sign = JNICore.getSign(SortUtils.getMapResult(SortUtils.sortString(map)));
        map.put("user.sign", sign);
        RetrofitManager.post("http://qhb.2dyt.com/MyInterface/userAction_selectUserById.action", map, new BaseObserver() {
            @Override
            public void onSuccess(String result) {

                Gson gson = new Gson();
                UserInfoBean dataBean = gson.fromJson(result, UserInfoBean.class);
                data = dataBean.getData();
                System.out.println("data = " + data.toString());
                Glide.with(BlackActivity.this).load(data.getImagePath()).into(blackImage);
                blackName.setText(data.getNickname());

                //自定义随机数
                Random random = new Random();
                int num = (int) (Math.random() * 12 + 18);//产生70-120的随机数

                blackAge.setText(num + "岁");
                blackSex.setText(data.getGender());
                blackCity.setText(data.getArea());
//        blackAge.setText(age + "岁");


            }

            @Override
            public void onFailed(int code) {
                System.out.println("code = " + code);
                MyToast.makeText(BlackActivity.this, "请求失败", Toast.LENGTH_SHORT);
            }
        });
    }

    @OnClick(R.id.black_end)
    public void onViewClied() {
        Intent intent = new Intent(BlackActivity.this,ChatMeActivity.class);
        intent.putExtra("uid",uid);
        startActivity(intent);

    }
}



