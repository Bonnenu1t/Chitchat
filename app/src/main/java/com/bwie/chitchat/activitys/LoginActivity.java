package com.bwie.chitchat.activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bwie.chitchat.R;
import com.bwie.chitchat.base.IActivity;
import com.bwie.chitchat.bean.RegisterBean;
import com.bwie.chitchat.cipher.Md5Utils;
import com.bwie.chitchat.cipher.aes.JNCryptorUtils;
import com.bwie.chitchat.cipher.rsa.RsaUtils;
import com.bwie.chitchat.core.JNICore;
import com.bwie.chitchat.core.SortUtils;
import com.bwie.chitchat.network.BaseObserver;
import com.bwie.chitchat.network.RetrofitManager;
import com.bwie.chitchat.utils.Constants;
import com.bwie.chitchat.widget.MyToast;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bwie.chitchat.R.id.select_login;

public class LoginActivity extends IActivity {

    @BindView(R.id.select_image)
    ImageView selectImage;
    @BindView(R.id.select_title)
    TextView selectTitle;
    @BindView(select_login)
    Button selectLogin;
    @BindView(R.id.login_phone)
    EditText loginPhone;
    @BindView(R.id.login_pws)
    EditText loginPws;
    @BindView(R.id.login_btn)
    Button loginBtn;

    private SharedPreferences.Editor edit;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        selectTitle.setVisibility(View.VISIBLE);
        selectTitle.setText("登录");
        selectLogin.setVisibility(View.VISIBLE);
        selectLogin.setText("注册");


        sp = getSharedPreferences("login",MODE_PRIVATE);

        edit = sp.edit();

        edit.clear();

        //随机数 rsa 公钥进行加密
//  aes 加密  拿着加密后的
//        String random = RsaUtils.getInstance().createRandom();
//        System.out.println("random = " + random);
//        String rsaKey = RsaUtils.getInstance().createRsaSecret(getApplicationContext(), random);
//        String mobile = JNCryptorUtils.getInstance().encryptData("18511085102", getApplicationContext(), random);
//        System.out.println("mobile = " + mobile);


    }

    @OnClick({R.id.select_image, R.id.login_btn,R.id.select_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.select_image:
                Intent intent = new Intent(LoginActivity.this, ShapeActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_left, R.anim.in_from_right);
                finish();
                break;
            case R.id.login_btn:

                String phone = loginPhone.getText().toString().trim();
                String password = loginPws.getText().toString().trim();
                if (TextUtils.isEmpty(phone) && TextUtils.isEmpty(password)) {
                    MyToast.makeText(LoginActivity.this, "请输入账号与密码", Toast.LENGTH_SHORT);
                } else {
                    getData();
                }

                break;
            case R.id.select_login:
                startActivity(new Intent(this, RegisterActivity.class));
                finish();
                break;
        }
        }

    //注册页面
    private void getData() {

        final String phone=loginPhone.getText().toString().trim();
        final String password=loginPws.getText().toString().trim();
        String randomKey =   RsaUtils.getStringRandom(16);
        String rsaRandomKey =   RsaUtils.getInstance().createRsaSecret(this,randomKey);
        String cipherPhone =   JNCryptorUtils.getInstance().encryptData(phone,this,randomKey);

        Map map = new HashMap<String,String>();
        map.put("user.phone",cipherPhone);
        map.put("user.password", Md5Utils.getMD5(password));
        map.put("user.secretkey",rsaRandomKey);
        String sign= JNICore.getSign(SortUtils.getMapResult(SortUtils.sortString(map)));
        map.put("user.sign",sign);

        RetrofitManager.post(Constants.LOGIN_ACTION, map, new BaseObserver() {
            @Override
            public void onSuccess(String result) {

                Gson gson=new Gson();
                RegisterBean registerBean = gson.fromJson(result, RegisterBean.class);
                int result_code = registerBean.getResult_code();

                if (result_code==303){
                    if (TextUtils.isEmpty(phone)){
                        MyToast.makeText(LoginActivity.this,"账号不能为空", Toast.LENGTH_SHORT);
                        return;
                    }
                    if (TextUtils.isEmpty(password)) {
                        MyToast.makeText(LoginActivity.this,"密码不能为空", Toast.LENGTH_SHORT);
                        return;
                    }
                    MyToast.makeText(LoginActivity.this,"账号或密码错误", Toast.LENGTH_SHORT);
                }
                if (result_code==200){
                    MyToast.makeText(LoginActivity.this,"登录成功", Toast.LENGTH_SHORT);
                    edit.putString("sure","1");

                    edit.commit();
                    Intent intent=new Intent(LoginActivity.this,TabActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailed(int code) {

            }
        });


    }


}
