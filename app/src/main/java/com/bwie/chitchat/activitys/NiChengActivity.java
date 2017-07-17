package com.bwie.chitchat.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bwie.chitchat.R;
import com.bwie.chitchat.base.IActivity;
import com.bwie.chitchat.base.IApplication;
import com.bwie.chitchat.bean.RegisterBean;
import com.bwie.chitchat.cipher.Md5Utils;
import com.bwie.chitchat.core.JNICore;
import com.bwie.chitchat.core.SortUtils;
import com.bwie.chitchat.network.BaseObserver;
import com.bwie.chitchat.network.RetrofitManager;
import com.bwie.chitchat.utils.PreferencesUtils;
import com.bwie.chitchat.widget.MyToast;
import com.google.gson.Gson;
import com.jakewharton.rxbinding2.view.RxView;
import com.lljjcoder.citypickerview.widget.CityPickerView;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

import static com.bwie.chitchat.R.array.age;


public class NiChengActivity extends IActivity {
    double latitude;
    double accuracy;
    //声明AMapLocationClient类对象
    AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    AMapLocationClientOption mLocationOption = null;

    @BindView(R.id.select_image)
    ImageView selectImage;
    @BindView(R.id.select_title)
    TextView selectTitle;
    @BindView(R.id.ni_cheng_username)
    EditText niChengUsername;
    @BindView(R.id.ni_cheng_password)
    EditText niChengPassword;
    @BindView(R.id.ni_cheng_sex)
    TextView niChengSex;
    @BindView(R.id.ni_cheng_age)
    TextView niChengAge;
    @BindView(R.id.ni_cheng_city)
    TextView niChengCity;
    @BindView(R.id.ni_cheng_jie_shao)
    TextView niChengJieShao;
    @BindView(R.id.ni_cheng_next)
    Button niChengNext;
    @BindView(R.id.select_login)
    Button selectLogin;
    private Intent intent = null;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ni_cheng);
        ButterKnife.bind(this);

        selectTitle.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
//        String describle = intent.getStringExtra("describle");
        phone = intent.getStringExtra("phone");
//        String describle = PreferencesUtils.getValueByKey(getApplication(), "des", "da");
//        niChengJieShao.setText(describle);

        getMap();

    }

    private void getMap() {

        //声明定位回调监听器
        AMapLocationListener mLocationListener = new AMapLocationListener() {

            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                // TODO Auto-generated method stub
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        //可在其中解析amapLocation获取相应内容。
                        double locationType = amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表

                        latitude = amapLocation.getLatitude();//获取纬度
                        accuracy=amapLocation.getAccuracy();//获取精度

                    }else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError","location Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo());
                    }
                }
            }
        };


        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();

        /**
         * 获取一次定位
         */
        //该方法默认为false，true表示只定位一次
        mLocationOption.setOnceLocation(true);
    }

    @OnClick({R.id.select_image, R.id.ni_cheng_username, R.id.ni_cheng_password, R.id.ni_cheng_sex, R.id.ni_cheng_age, R.id.ni_cheng_city, R.id.ni_cheng_jie_shao, R.id.ni_cheng_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.select_image:
                break;
            case R.id.ni_cheng_username://昵称
                break;
            case R.id.ni_cheng_password://密码
                break;
            case R.id.ni_cheng_sex://选择性别
                showSexChooseDialog();
                break;
            case R.id.ni_cheng_age://选择年龄
                showAgeDialog();
                break;
            case R.id.ni_cheng_city:
                showLocal();
                break;
            case R.id.ni_cheng_jie_shao:
                intent = new Intent(NiChengActivity.this,DescribeActivity.class);
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.in_from_left,R.anim.in_from_right);
                break;
            case R.id.ni_cheng_next:

                if (TextUtils.isEmpty(niChengUsername.getText().toString().trim())) {
                    MyToast.makeText(NiChengActivity.this,"昵称不能为空",Toast.LENGTH_SHORT);
                    return;
                }

                if (TextUtils.isEmpty(niChengPassword.getText().toString().trim())) {
                    MyToast.makeText(NiChengActivity.this,"密码不能为空",Toast.LENGTH_SHORT);
                    return;
                }
                if (TextUtils.isEmpty(niChengJieShao.getText().toString().trim())) {
                    MyToast.makeText(NiChengActivity.this,"简介不能为空",Toast.LENGTH_SHORT);
                    return;
                }
                getData(phone,niChengUsername.getText().toString().trim(),niChengSex.getText().toString().trim()
                        , niChengAge.getText().toString().trim(), niChengCity.getText().toString().trim()
                        , niChengJieShao.getText().toString().trim(), Md5Utils.getMD5(niChengPassword.getText().toString().trim()));

                intent = new Intent(NiChengActivity.this,PhotoActivity.class);

                startActivity(intent);
                overridePendingTransition(R.anim.in_from_left,R.anim.in_from_right);
                finish();
                break;
        }
    }


        public void getData(String phone,String nickname,String sex,String age,String area,String introduce,String password){

            Map<String,String> map = new HashMap<String,String>();
            map.put("user.phone",phone);
            map.put("user.nickname",nickname);
            map.put("user.password",password);
            map.put("user.gender",sex);
            map.put("user.area",area);
            map.put("user.age",age);
            map.put("user.introduce",introduce);
            map.put("user.lat", String.valueOf(latitude));
            map.put("user.lng", String.valueOf(accuracy));
        System.out.println("SortUtils.getMapResult(SortUtils.sortString(map)) = " + SortUtils.getMapResult(SortUtils.sortString(map)));
        String sign =  JNICore.getSign(SortUtils.getMapResult(SortUtils.sortString(map))) ;
        map.put("user.sign",sign);
        System.out.println("sign = " + sign);

            RetrofitManager.post("http://qhb.2dyt.com/MyInterface/userAction_add.action", map, new BaseObserver() {
                @Override
                public void onSuccess(String result) {

                    Gson gson = new Gson();
                    RegisterBean registerBean = gson.fromJson(result, RegisterBean.class);

                    if(registerBean.getResult_code() == 200){
                        PreferencesUtils.addConfigInfo(IApplication.getApplication(),"phone",registerBean.getData().getPhone());
                        PreferencesUtils.addConfigInfo(IApplication.getApplication(),"password",registerBean.getData().getPassword());
                        PreferencesUtils.addConfigInfo(IApplication.getApplication(),"yxpassword",registerBean.getData().getYxpassword());
                        PreferencesUtils.addConfigInfo(IApplication.getApplication(),"uid",registerBean.getData().getUserId());
                        PreferencesUtils.addConfigInfo(IApplication.getApplication(),"nickname",registerBean.getData().getNickname());

                        MyToast.makeText(NiChengActivity.this,"注册成功",Toast.LENGTH_SHORT);
                    }
                }
                @Override
                public void onFailed(int code) {
                    MyToast.makeText(NiChengActivity.this,"手机号已存在",Toast.LENGTH_SHORT);
                }
            });

        }




    //选择男女的弹出框
    private String[] sexArry = new String[]{"女", "男"};
    private void showSexChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择性别");
        builder.setItems(sexArry, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                niChengSex.setText(sexArry[which]);
            }
        });
        builder.show();
    }


    AlertDialog.Builder builder;

    private void showAgeDialog() {
        if (builder == null) {
            final String[] ages = getResources().getStringArray(age);
            builder = new AlertDialog.Builder(this);
            builder.setTitle("请选择年龄");
            builder.setItems(ages, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    niChengAge.setText(ages[which]);
                }
            });
        }

        builder.show();

    }


    //选择city
    private void showLocal() {

        CityPickerView cityPickerView = new CityPickerView(this);
        cityPickerView.setOnCityItemClickListener(new CityPickerView.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省份
                String province = citySelected[0];
                //城市
                String city = citySelected[1];
                //区县
                String district = citySelected[2];
                //邮编
                String code = citySelected[3];
                Toast.makeText(NiChengActivity.this,province+"-"+city+"-"+district,Toast.LENGTH_LONG).show();
                niChengCity.setText(province+"-"+city+"-"+district);
            }
        });
        cityPickerView.show();


    }

    /**
     * 判断所有的参数 非空
     * 注册 添加 草稿功能
     */
    private void toData() {

        /*可以实现 监听 edittext 内容变化
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        } ;

        registerUsername.addTextChangedListener(textWatcher);
        RxAdapterView.itemClicks()
        RxTextView.afterTextChangeEvents(new TextView(getActivity())).subscribe(new Consumer<TextViewAfterTextChangeEvent>() {
            @Override
            public void accept(@NonNull TextViewAfterTextChangeEvent textViewAfterTextChangeEvent) throws Exception {
//                textViewAfterTextChangeEvent.view().getText().
            }
        })
        DialogUtils.createLoadingDialog(getActivity()).show();*/



        RxView.clicks(niChengNext).throttleFirst(1, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {

                        System.out.println("o = " + o);
                        /*presenter.vaildInfor(phone, registerUsername.getText().toString().trim(), registerSex.getText().toString().trim()
                                , registerAge.getText().toString().trim(), registerDiquValue.getText().toString().trim()
                                , registerJieshaoValue.getText().toString().trim(), Md5Utils.getMD5(registerPassword.getText().toString().trim()));*/
                    }
                });



    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String des = data.getStringExtra("des");
        niChengJieShao.setText(des);
    }
}
