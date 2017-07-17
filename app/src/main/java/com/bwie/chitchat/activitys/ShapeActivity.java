package com.bwie.chitchat.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;

import com.bwie.chitchat.R;
import com.bwie.chitchat.base.IActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShapeActivity extends IActivity{
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.register)
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shape);

        ButterKnife.bind(this);

        Animation animation = new AlphaAnimation(0.1f, 1.0f);
        animation.setDuration(3000);
        login.startAnimation(animation);
        register.startAnimation(animation);
    }

    @OnClick({R.id.login, R.id.register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login:
                startActivity(new Intent(ShapeActivity.this,LoginActivity.class));
                overridePendingTransition(R.anim.in_from_left,R.anim.in_from_right);
                finish();
                break;
            case R.id.register:
                startActivity(new Intent(ShapeActivity.this,RegisterActivity.class));
                overridePendingTransition(R.anim.in_from_left,R.anim.in_from_right);
                finish();
                break;
        }
    }
}
