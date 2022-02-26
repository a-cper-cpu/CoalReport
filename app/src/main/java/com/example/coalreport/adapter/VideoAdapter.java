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
 * 视频适配器
 */


public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context mContext;
    private List<VideoEntity> mDatas;
    private OnItemChildClickListener mOnItemChildClickListener;
    private OnItemClickListener mOnItemClickListener;

    public VideoAdapter(Context context, List<VideoEntity> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }


    /**
     * 布局效果
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_video_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }


    /**
     * 绑定数据
     */

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        VideoEntity videoEntity = mDatas.get(position);
        vh.tvTitle.setText(videoEntity.getVtitle());
        vh.tvAuthor.setText(videoEntity.getAuthor());
        vh.tvCollect.setText(String.valueOf(videoEntity.getvCollect()));
        vh.tvComment.setText(String.valueOf(videoEntity.getCommentCount()));
        vh.tvDz.setText(String.valueOf(videoEntity.getDzCount()));
        vh.textreport.setText(videoEntity.getText_report());
        //Picasso框架  加载图片并更新UI
        Picasso.with(mContext)
                .load(videoEntity.getHeadurl())
                .transform(new CircleTransform())
                .into(vh.tvHeader);
        vh.mPosition = position;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvTitle;
        private TextView tvAuthor;
        private TextView tvDz;
        private TextView tvComment;
        private TextView tvCollect;
        private ImageView img_dz;
        private ImageView img_collect;
        private ImageView tvHeader;
        public ImageView mThumb;
        public PrepareView mPrepareView;
        public FrameLayout mPlayerContainer;
        public int mPosition;
        public TextView textreport;
        private boolean flag_collect = true;
        private boolean flag_dianzan = true;
        private ImageView img_comment;

        public ViewHolder(@NonNull View view) {
            super(view);
            tvTitle = view.findViewById(R.id.title);
            tvAuthor = view.findViewById(R.id.author);
            tvDz = view.findViewById(R.id.dz);
            tvComment = view.findViewById(R.id.comment);
            tvCollect = view.findViewById(R.id.collect);
            tvHeader = view.findViewById(R.id.img_header);
            textreport = view.findViewById(R.id.text_report);
            img_dz = view.findViewById(R.id.img_like);
            img_collect = view.findViewById(R.id.img_collect);
            mPlayerContainer = view.findViewById(R.id.player_container);
            mPrepareView = view.findViewById(R.id.prepare_view);
            mThumb = mPrepareView.findViewById(R.id.thumb);
            if (mOnItemChildClickListener != null) {
                mPlayerContainer.setOnClickListener(this);
            }
            if (mOnItemClickListener != null) {
                view.setOnClickListener(this);
            }
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
            view.setTag(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.player_container) {
                if (mOnItemChildClickListener != null) {
                    mOnItemChildClickListener.onItemChildClick(mPosition);
                }
            } else {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(mPosition);
                }
            }
        }
    }

    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        mOnItemChildClickListener = onItemChildClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
