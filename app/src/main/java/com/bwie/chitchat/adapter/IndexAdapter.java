package com.bwie.chitchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;
import com.bumptech.glide.Glide;
import com.bwie.chitchat.R;
import com.bwie.chitchat.bean.DataBean;
import com.bwie.chitchat.bean.IndexBean;
import com.bwie.chitchat.utils.AMapUtils;
import com.bwie.chitchat.utils.DeviceUtils;
import com.bwie.chitchat.utils.DisanceUtils;
import com.bwie.chitchat.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by muhanxi on 17/7/8.
 */

public class IndexAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DataBean> list;
    private Context context;
    private int tag = 1; // 1 先行布局 2 瀑布流
    private int itemWidth ;
    private MyItemClickListener listener = null;
    public IndexAdapter(Context context,List<DataBean> list) {
        this.context = context;
        //当前屏幕 的宽度 除以3
        itemWidth = DeviceUtils.getDisplayInfomation(context).x / 3 ;
        this.list = list;
    }

    public void adapterData(List<DataBean> list){
        list.addAll(list);
    }


    public void setData(IndexBean bean) {
        if (list == null) {
            list = new ArrayList<DataBean>();
        }

        list.addAll(bean.getData());
        notifyDataSetChanged();
    }

    /**
     * 2 瀑布流
     * 1 线性布局
     *
     * @param type
     */
    public void dataChange(int type) {
        this.tag = type;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == 0) {
            View view = LayoutInflater.from(context).inflate(R.layout.indexfragment_verticaladapter, parent, false);
            VerticalViewHolder verticalViewHolder = new VerticalViewHolder(view,listener);
            return verticalViewHolder;

        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.indexfragment_pinterest, parent, false);
            PinterestViewHolder pinterestViewHolder = new PinterestViewHolder(view,listener);
            return pinterestViewHolder;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if (holder instanceof VerticalViewHolder) {

            //列表的形式展示
            VerticalViewHolder verticalViewHolder = (VerticalViewHolder) holder;

            verticalViewHolder.indexfragmentNickname.setText(list.get(position).getNickname());


            verticalViewHolder.indexfragmentDes.setText(list.get(position).getArea());

            Glide.with(context).load(list.get(position).getImagePath()).error(R.drawable.example_gril_photo_three).into(verticalViewHolder.indexfragmentFace);
     verticalViewHolder.itemView.setTag(list.get(position).getUserId());
           String lat =  PreferencesUtils.getValueByKey(context, AMapUtils.LAT,"");
            String lng = PreferencesUtils.getValueByKey(context,AMapUtils.LNG,"");

            double olat = list.get(position).getLat();
            double olng = list.get(position).getLng() ;


            if(!TextUtils.isEmpty(lat) && !TextUtils.isEmpty(lng) && olat != 0.0 && olng != 0.0){

                double dlat = Double.valueOf(lat);
                double dlng = Double.valueOf(lng);
                DPoint dPoint = new DPoint(dlat,dlng);
                DPoint oPoint = new DPoint(olat,olng);

                //计算两点之间的距离
               float dis =  CoordinateConverter.calculateLineDistance(dPoint,oPoint);

                verticalViewHolder.indexfragmentAgesex.setText(list.get(position).getAge() + "岁 , " + list.get(position).getGender() + " , " + DisanceUtils.standedDistance(dis));

            } else {

                verticalViewHolder.indexfragmentAgesex.setText(list.get(position).getAge() + "岁 , " + list.get(position).getGender());

            }
        } else {
            PinterestViewHolder staggeredViewHolder = (PinterestViewHolder) holder;

            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) staggeredViewHolder.indexfragmentStagger.getLayoutParams() ;

           float scale =  (float) itemWidth / (float) list.get(position).getPicWidth()  ;
            params.width = itemWidth;
            params.height = (int)( (float)scale * (float)list.get(position).getPicHeight()) ;

//            System.out.println("params.scale = " + scale);
//            System.out.println("params.width = " + params.width + " " + list.get(position).getPicWidth());
//            System.out.println("params.height = " + params.height + "  " + list.get(position).getPicHeight());

            staggeredViewHolder.indexfragmentStagger.setLayoutParams(params);

//            params.width = itemWidth

            staggeredViewHolder.indexfragmentText.setText(list.get(position).getNickname());
            Glide.with(context).load(list.get(position).getImagePath()).error(R.drawable.example_gril_photo_three).into(staggeredViewHolder.indexfragmentStagger);
            staggeredViewHolder.itemView.setTag(list.get(position).getUserId());
        }

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (tag == 1) {
            return 0;
        } else {
            return 1;
        }
    }

    static class VerticalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener listener;
        @BindView(R.id.indexfragment_nickname)
        TextView indexfragmentNickname;
        @BindView(R.id.indexfragment_agesex)
        TextView indexfragmentAgesex;
        @BindView(R.id.indexfragment_des)
        TextView indexfragmentDes;
        @BindView(R.id.indexfragment_face)
        CircleImageView indexfragmentFace;

        public VerticalViewHolder(View itemView,MyItemClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(v, (int) v.getTag());
            }
        }
    }

    static class PinterestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.indexfragment_stagger)
        ImageView indexfragmentStagger;
        @BindView(R.id.indexfragment_text)
        TextView indexfragmentText;

        private MyItemClickListener listener;
        public PinterestViewHolder(View itemView,MyItemClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(v, (int) v.getTag());
            }
        }
    }

    public interface MyItemClickListener{
        void onItemClick(View view,int position);
    }

    public void setOnItemClickListener(MyItemClickListener listener){
        this.listener = listener;
    }


}
