package com.bwie.chitchat.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bwie.chitchat.R;

public class VideoActivity extends Activity {

    public static void startTelActivity(int type, String uid, Context context) {

        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("uid", uid);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
    }
}
