package com.bwie.chitchat.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bwie.chitchat.R;
import com.bwie.chitchat.base.IActivity;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.bwie.chitchat.R.id.zhu_ce_phone;

public class ZhuCeActivity extends IActivity {

    @BindView(R.id.select_image)
    ImageView selectImage;
    @BindView(R.id.select_login)
    Button selectLogin;
    @BindView(zhu_ce_phone)
    EditText zhuCePhone;
    @BindView(R.id.zhu_ce_pws)
    EditText zhuCePws;
    @BindView(R.id.zhu_ce_btn)
    Button zhuCeBtn;//注册点击按钮
    @BindView(R.id.zhu_ce_numble)
    Button zhuCeNumble;//获取验证码
    @BindView(R.id.zhu_ce_delete)
    Button zhuCeDelete;

    private EventHandler eventHandler;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhu_ce);
        ButterKnife.bind(this);

        zhuCeBtn.setClickable(false);


    }

    @OnClick({R.id.select_image, zhu_ce_phone, R.id.zhu_ce_pws, R.id.zhu_ce_btn, R.id.zhu_ce_numble})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.select_image:
                startActivity(new Intent(ZhuCeActivity.this, ShapeActivity.class));
                overridePendingTransition(R.anim.in_from_left,R.anim.in_from_right);
                finish();
                break;
            case zhu_ce_phone:

                break;
            case R.id.zhu_ce_numble://获取验证码
                getData();

                break;
            case R.id.zhu_ce_pws:
                break;
            case R.id.zhu_ce_btn://注册点击按钮
                getBtn();
                break;
            case R.id.zhu_ce_delete://删除所输入电话号

                break;
        }
    }

    private void getBtn() {
        String code = zhuCePws.getText().toString().trim();
        String phone = zhuCePhone.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();

            //zhuCeBtn.setBackgroundColor(Color.parseColor("radio_sure02"));
            return;
        }else {
            Intent intent=new Intent(ZhuCeActivity.this,NiChengActivity.class);
             intent.putExtra("phone",phone);
            startActivity(intent);
        }

        /*handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    Toast.makeText(ZhuCeActivity.this, "回调完成", Toast.LENGTH_SHORT).show();
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        Toast.makeText(ZhuCeActivity.this, "提交验证码成功", Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(ZhuCeActivity.this,NiChengActivity.class);
                        // intent.putExtra("user",edit_text.getText().toString());
                        startActivity(intent);

                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                        Toast.makeText(ZhuCeActivity.this, "获取验证码成功", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //回调失败
                    ((Throwable) data).printStackTrace();
                }
            }

        };*/


    }


    //获取验证码
    private void getData() {

        final String phone = zhuCePhone.getText().toString().trim();
        SMSSDK.getSupportedCountries();//获取短信目前支持的国家列表
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return;
        }




        if(!TextUtils.isEmpty(phone)){
            if (!TextUtils.isEmpty(phone)) {
                zhuCeDelete.setVisibility(View.VISIBLE);
                zhuCeDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        zhuCePhone.setText("");
                        zhuCeDelete.setVisibility(View.GONE);
                        return;
                    }
                });
            } else {
                zhuCeDelete.setVisibility(View.GONE);
                return;
            }

            //正则表达式
            if (checkTel(phone)) {
                SMSSDK.getVerificationCode("86", phone);
                //获取验证码
                //countDownTimer.start();

                //倒计时显示
                showTimer();

                //设置注册按钮可点击和变色
                zhuCeBtn.setClickable(true);
                zhuCeBtn.setPressed(true);

                //String code = zhuCePws.getText().toString().trim();

                /*//输入验证码
                if (TextUtils.isEmpty(code)) {

                    return;
                }else {
                    zhuCeBtn.setClickable(true);
                    zhuCeBtn.setPressed(true);
                }*/


            }else{
                Toast.makeText(ZhuCeActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
            }
        }



        // 创建EventHandler对象
        eventHandler = new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                System.out.println("======duanxin======" + data.toString());
                /*Message msg = Message.obtain();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);*/
            }
        };

        // 注册监听器
        SMSSDK.registerEventHandler(eventHandler);
    }

    /**
     * 正则匹配手机号码
     * @param tel
     * @return
     */
    public boolean checkTel(String tel){
        Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
        Matcher matcher = p.matcher(tel);
        return matcher.matches();
    }




    private Disposable disposable;
    //显示倒计时
    public void showTimer() {

        zhuCeNumble.setClickable(false);

        Observable.interval(0,1, TimeUnit.SECONDS)
                .take(30)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(@NonNull Long aLong) throws Exception {
                        return 29 - aLong;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                        disposable = d ;
//                        d.dispose();

                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {

                        System.out.println("aLong = " + aLong);
                        zhuCeNumble.setText(aLong+" S ");

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        zhuCeNumble.setClickable(true);
                        zhuCeNumble.setText(getText(R.string.huoQu));

                    }
                });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);

        if (disposable !=null) {
            disposable.dispose();
        }
    }


}
