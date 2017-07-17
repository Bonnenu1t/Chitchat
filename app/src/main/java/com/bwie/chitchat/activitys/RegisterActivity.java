package com.bwie.chitchat.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bwie.chitchat.R;
import com.bwie.chitchat.base.IActivity;
import com.example.horizontalselectedviewlibrary.HorizontalselectedView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends IActivity {

    @BindView(R.id.radio_man)
    RadioButton radioMan;
    @BindView(R.id.radio_women)
    RadioButton radioWomen;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.radio_sure)
    Button radioSure;
    @BindView(R.id.select_image)
    ImageView selectImage;
    @BindView(R.id.select_login)
    Button selectLogin;
    @BindView(R.id.select_title)
    TextView selectTitle;
    List<String> strings = new ArrayList<String>();
    @BindView(R.id.iv_left)
    TextView ivLeft;
    @BindView(R.id.hd_main)
    HorizontalselectedView hsMain;
    @BindView(R.id.iv_right)
    TextView ivRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        selectLogin.setVisibility(View.VISIBLE);
        selectTitle.setVisibility(View.VISIBLE);
        selectTitle.setText("");
        //selectLogin.setClickable(true);
        initMan();
        initWomen();
        initdata();

    }

    private void initdata() {

        for (int i = 15; i < 50; i++) {
            strings.add(i+"岁");
        }
        hsMain.setData(strings);
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println(radioMan.isChecked() || radioWomen.isChecked());
        if (radioMan.isChecked()||radioWomen.isChecked()){
            radioSure.setPressed(true);
        }
    }

    //选择男的时候的点击事件
    private void initMan() {

        radioMan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                radioSure.setPressed(true);
                if (isChecked) {
                    radioSure.setClickable(true);
                    radioSure.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(RegisterActivity.this, "点击了", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, ZhuCeActivity.class));
                            overridePendingTransition(R.anim.in_from_left,R.anim.in_from_right);
                        }
                    });
                }
            }
        });
    }

    //选择女的时候的点击事件
    private void initWomen() {

        radioWomen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                radioSure.setPressed(true);
                if (isChecked) {
                    radioSure.setClickable(true);
                    radioSure.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(RegisterActivity.this, ZhuCeActivity.class));
                            overridePendingTransition(R.anim.in_from_left,R.anim.in_from_right);
                        }
                    });
                }
            }
        });
    }


    @OnClick({R.id.select_image, R.id.select_login,R.id.iv_left,R.id.iv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.select_image:
                startActivity(new Intent(RegisterActivity.this, ShapeActivity.class));
                overridePendingTransition(R.anim.in_from_left,R.anim.in_from_right);
                break;
            case R.id.select_login:
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.iv_left:
                hsMain.setAnLeftOffset();
                break;
            case R.id.iv_right:
                hsMain.setAnRightOffset();
                break;
        }
    }

}
