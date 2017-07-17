package com.bwie.chitchat.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bwie.chitchat.R;
import com.bwie.chitchat.base.IActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DescribeActivity extends IActivity {

    @BindView(R.id.select_image)
    ImageView selectImage;
    @BindView(R.id.select_title)
    TextView selectTitle;
    @BindView(R.id.describle_me)
    EditText describleMe;
    @BindView(R.id.describle_save)
    Button describleSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_describe);
        ButterKnife.bind(this);
        selectTitle.setText("个性签名");

    }

    @OnClick({R.id.select_image, R.id.describle_me, R.id.describle_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.select_image:
                getData();
                break;
            case R.id.describle_me:
                break;
            case R.id.describle_save:
                getData();
                break;
        }
    }

    private void getData() {
        String describle = describleMe.getText().toString().trim();
        Intent mIntent = new Intent();
        mIntent.putExtra("des", describle);
        // 设置结果，并进行传送
        this.setResult(2, mIntent);
        finish();
    }
}
