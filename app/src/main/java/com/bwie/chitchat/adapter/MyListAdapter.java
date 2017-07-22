package com.bwie.chitchat.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bwie.chitchat.R;
import com.bwie.chitchat.bean.DataBean;

import java.util.List;


public class MyListAdapter extends BaseAdapter {

    private Context context;
    private List<DataBean> data1;
    private ImageView indexfragment_face_id;
    private TextView indexfragment_nickname;
    private TextView indexfragment_agesex;
    private TextView indexfragment_des;


    public MyListAdapter(Context context, List<DataBean> data1){
        this.context=context;
        this.data1=data1;

    }

    @Override
    public int getCount() {
        return data1 == null ? 0 : data1.size();
    }

    @Override
    public Object getItem(int position) {
        return data1.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView==null){

            convertView=View.inflate(context, R.layout.second_item,null);
            indexfragment_face_id = (ImageView) convertView.findViewById(R.id.indexfragment_face_id);
            indexfragment_nickname = (TextView) convertView.findViewById(R.id.indexfragment_nickname_id);
             indexfragment_agesex =   (TextView) convertView.findViewById(R.id.indexfragment_agesex_id);
            indexfragment_des = (TextView) convertView.findViewById(R.id.indexfragment_des_id);

            Glide.with(context).load(data1.get(position).getImagePath()).into(indexfragment_face_id);
            indexfragment_nickname.setText(data1.get(position).getNickname());
            indexfragment_agesex.setText(data1.get(position).getAge());
            indexfragment_des.setText(data1.get(position).getIntroduce());

        }
        return convertView;
    }
}
