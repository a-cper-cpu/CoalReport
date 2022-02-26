package com.example.coalreport.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dueeeke.videocontroller.component.PrepareView;

import com.example.coalreport.entity.PhotoEntity;
import com.squareup.picasso.Picasso;


import com.example.coalreport.R;
import com.example.coalreport.entity.VideoEntity;

import com.example.coalreport.listener.OnItemChildClickListener;
import com.example.coalreport.listener.OnItemClickListener;
import com.example.coalreport.view.CircleTransform;


import org.xutils.x;

import java.util.List;


/**
 * @author a-cper-cpu
 * 图片 适配器
 */

public class PhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context mContext;

    //数据
    private List<PhotoEntity> mDatas;

    private OnItemChildClickListener mOnItemChildClickListener;

    private OnItemClickListener mOnItemClickListener;

    public PhotoAdapter(Context context, List<PhotoEntity> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }


    /**
     * @author 黄卓
     * 布局效果
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_photo_layout, parent, false);
        //容纳View视图
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    /**
     * @author 黄卓
     * 绑定数据
     */

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        //把获取到的PhotoEntity实体集，点击哪个就填充哪个实体
        PhotoEntity photoEntity = mDatas.get(position);
        vh.tvTitle.setText(photoEntity.getVtitle());
        vh.tvAuthor.setText(photoEntity.getAuthor());
        vh.tvCollect.setText(String.valueOf(photoEntity.getvCollect()));
        vh.tvComment.setText(String.valueOf(photoEntity.getCommentCount()));
        vh.tvDz.setText(String.valueOf(photoEntity.getDzCount()));
        vh.textreport.setText(photoEntity.getText_report());
        //Picasso框架 加载图片，更新UI
        Picasso.with(mContext)
                .load(photoEntity.getHeadurl())
                //设置原型头像
                .transform(new CircleTransform())
                .into(vh.tvHeader);
        Picasso.with(mContext)
                .load(photoEntity.getReporturl())
                .into(vh.imgreport);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvAuthor;
        private TextView tvDz;
        private TextView tvComment;
        private TextView tvCollect;
        private ImageView tvHeader;
        public ImageView imgreport;
        public TextView textreport;
        private ImageView img_comment;
        private ImageView img_dz;
        private ImageView img_collect;
        private boolean flag_collect = true;
        private boolean flag_dianzan = true;

        public ViewHolder(@NonNull View view) {
            super(view);
            tvTitle = view.findViewById(R.id.title);
            tvAuthor = view.findViewById(R.id.author);
            tvDz = view.findViewById(R.id.dz);
            tvComment = view.findViewById(R.id.comment);
            tvCollect = view.findViewById(R.id.collect);
            tvHeader = view.findViewById(R.id.img_header);
            imgreport = view.findViewById(R.id.iv_report);
            textreport = view.findViewById(R.id.text_report);
            img_dz = view.findViewById(R.id.img_like);
            img_collect = view.findViewById(R.id.img_collect);
            img_comment = view.findViewById(R.id.img_comment);
            img_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ProgressDialog pd = ProgressDialog.show(v.getContext(), "评论功能正在完善中", "感谢您的使用！");
                    new Thread() {
                        public void run() {
                            SystemClock.sleep(2000);
                            pd.dismiss();
                        }

                        ;
                    }.start();
                }
            });
            img_dz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flag_dianzan) {
                        int dianzanNum = Integer.valueOf(tvDz.getText().toString());
                        img_dz.setImageResource(R.mipmap.dianzan_select);
                        tvDz.setText(String.valueOf(++dianzanNum));
                        flag_dianzan = false;
                    } else {
                        int dianzanNum = Integer.valueOf(tvDz.getText().toString());
                        img_dz.setImageResource(R.mipmap.dianzan);
                        tvDz.setText(String.valueOf(--dianzanNum));
                        flag_dianzan = true;
                    }
                }
            });
            img_collect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flag_collect) {
                        int collectNum = Integer.valueOf(tvCollect.getText().toString());
                        img_collect.setImageResource(R.mipmap.collect_select);
                        tvCollect.setText(String.valueOf(++collectNum));
                        flag_collect = false;
                    } else {
                        int collectNum = Integer.valueOf(tvCollect.getText().toString());
                        img_collect.setImageResource(R.mipmap.collect);
                        tvCollect.setText(String.valueOf(--collectNum));
                        flag_collect = true;
                    }
                }
            });
        }
    }
}
