package com.bwie.chitchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bwie.chitchat.R;
import com.bwie.chitchat.bean.UserBean;

import java.util.List;

public class BlackAdapter extends RecyclerView.Adapter<BlackAdapter.Myviewholder>{
    private Context context;
    List<UserBean> others;

    public BlackAdapter(Context context,  List<UserBean> others) {
        this.context = context;
        this.others = others;
    }

    @Override
    public Myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vh= LayoutInflater.from(context).inflate(R.layout.item_recler,parent,false);
        Myviewholder root = new Myviewholder(vh);

        return root;
    }

    @Override
    public void onBindViewHolder(Myviewholder holder, int position) {

        holder.text1.setText(others.get(position).getName());
        holder.text2.setText(others.get(position).getZiLiao());
    }

    @Override
    public int getItemCount() {
        return others.size();
    }

    class Myviewholder extends RecyclerView.ViewHolder{
        TextView text1;
        TextView text2;

        public Myviewholder(View itemView) {
            super(itemView);
            text1 = (TextView) itemView.findViewById(R.id.textview_name);
            text2 = (TextView) itemView.findViewById(R.id.text2);

        }
    }
}