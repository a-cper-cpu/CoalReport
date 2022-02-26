package com.example.coalreport.fragment;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dueeeke.videocontroller.StandardVideoController;
import com.dueeeke.videocontroller.component.CompleteView;
import com.dueeeke.videocontroller.component.ErrorView;
import com.dueeeke.videocontroller.component.GestureView;
import com.dueeeke.videocontroller.component.TitleView;
import com.dueeeke.videocontroller.component.VodControlView;
import com.dueeeke.videoplayer.player.VideoView;


import com.dueeeke.videoplayer.player.VideoViewManager;
import com.example.coalreport.R;
import com.example.coalreport.adapter.VideoAdapter;
import com.example.coalreport.entity.VideoEntity;
import com.example.coalreport.listener.OnItemChildClickListener;
import com.example.coalreport.utils.AppConfig;

import com.example.coalreport.utils.Tag;
import com.example.coalreport.utils.Utils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * @author a-cper-cpu
 * 首页里视频fragment
 */


public class VideoFragment extends Fragment implements OnItemChildClickListener {

    private boolean isCreated = false;

    private String title;

    private int pageNum = 1;

    private RecyclerView recyclerView;

    private RefreshLayout refreshLayout;

    protected VideoView mVideoView;
    protected StandardVideoController mController;
    protected ErrorView mErrorView;
    protected CompleteView mCompleteView;
    protected TitleView mTitleView;
    private List<VideoEntity> datas = new ArrayList<>();
    private VideoEntity list = new VideoEntity();

    private LinearLayoutManager linearLayoutManager;

    /**
     * 当前播放的位置
     */
    protected int mCurPos = -1;
    /**
     * 上次播放的位置，用于页面切回来之后恢复播放
     */
    protected int mLastPos = mCurPos;


    public VideoFragment() {
        // Required empty public constructor
    }

    //实例化VideoFragment
    public static VideoFragment newInstance(String title) {
        VideoFragment fragment = new VideoFragment();
        fragment.title = title;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_video, container, false);
        initVideoView();
        recyclerView = v.findViewById(R.id.recyclerView);
        refreshLayout = v.findViewById(R.id.refreshLayout0);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //下拉刷新，更新UI
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getVideoList(true);
            }
        });
        isCreated = true;
        getVideoList(isCreated);
    }

    //初始化视频界面
    protected void initVideoView() {
        mVideoView = new VideoView(getActivity());
        mVideoView.setOnStateChangeListener(new com.dueeeke.videoplayer.player.VideoView.SimpleOnStateChangeListener() {
            @Override
            public void onPlayStateChanged(int playState) {
                //监听VideoViewManager释放，重置状态
                if (playState == com.dueeeke.videoplayer.player.VideoView.STATE_IDLE) {
                    Utils.removeViewFormParent(mVideoView);
                    mLastPos = mCurPos;
                    mCurPos = -1;
                }
            }
        });
        mController = new StandardVideoController(getActivity());
        mErrorView = new ErrorView(getActivity());
        mController.addControlComponent(mErrorView);
        mCompleteView = new CompleteView(getActivity());
        mController.addControlComponent(mCompleteView);
        mTitleView = new TitleView(getActivity());
        mController.addControlComponent(mTitleView);
        mController.addControlComponent(new VodControlView(getActivity()));
        mController.addControlComponent(new GestureView(getActivity()));
        mController.setEnableOrientation(true);
        mVideoView.setVideoController(mController);
    }


    /**
     * @param isRefresh 通过后台服务器获取数据，并更新UI
     * @author 黄卓
     */
    private void getVideoList(final boolean isRefresh) {
        if (isRefresh) {
            refreshLayout.finishRefresh(true);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    //创造http请求
                    Request request = new Request.Builder().get()
                            .url(AppConfig.BASE_URl + "/getReleaseVideo")
                            // .post(RequestBody.create(MediaType.parse("application/json"),json)).build();
                            .build();
                    //执行发送的指令
                    Response response = okHttpClient.newCall(request).execute();
                    final String responseData = response.body().string();
                    JSONArray jsonArray = new JSONArray(responseData);
                    datas = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        final String json_account = jsonObject.getString("id");
                        final String json_releaseText = jsonObject.getString("releaseText");
                        final String json_releaseVideo = jsonObject.getString("releaseVideo");
                        final String json_releaseTime = jsonObject.getString("releaseTime");
                        OkHttpClient okHttpClient1 = new OkHttpClient();
                        //创造http请求
                        Request request1 = new Request.Builder().get()
                                .url(AppConfig.BASE_URl + "/getUserName?id=" + json_account)
                                // .post(RequestBody.create(MediaType.parse("application/json"),json)).build();
                                .build();
                        //执行发送的指令
                        Response response1 = okHttpClient1.newCall(request1).execute();
                        final String responseData1 = response1.body().string();
                        JSONObject jsonObject1 = new JSONObject(responseData1);
                        final String json_releaseUsername = jsonObject1.getString("userName");
                        final String json_releaseSaying = jsonObject1.getString("saying");
                        final String json_releasePhoto = jsonObject1.getString("photo");
                        VideoEntity ve = new VideoEntity();
                        ve.setVtitle(json_releaseSaying);
                        ve.setAuthor(json_releaseUsername);
                        ve.setDzCount(1);
                        ve.setCommentCount(1);
                        ve.setvCollect(1);
                        ve.setHeadurl(json_releasePhoto);
                        ve.setVideourl(json_releaseVideo);
                        ve.setText_report(json_releaseText);
                        datas.add(ve);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        VideoAdapter videoAdapter = new VideoAdapter(getActivity(), datas);
        videoAdapter.setOnItemChildClickListener(this);
        videoAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(videoAdapter);
        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {
            }

            /**
             * 列表项滑出可见窗口之外的时候会调用
             * @param view
             */
            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                FrameLayout playerContainer = view.findViewById(R.id.player_container);
                View v = playerContainer.getChildAt(0);
                if (v != null && v == mVideoView && !mVideoView.isFullScreen()) {
                    releaseVideoView();
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        pause();
    }

    /**
     * 由于onPause必须调用super。故增加此方法，
     * 子类将会重写此方法，改变onPause的逻辑
     */
    protected void pause() {
        releaseVideoView();
    }


    @Override
    public void onResume() {
        super.onResume();
        resume();
    }

    /**
     * 由于onResume必须调用super。故增加此方法，
     * 子类将会重写此方法，改变onResume的逻辑
     */
    protected void resume() {
        if (mLastPos == -1)
            return;
        //恢复上次播放的位置
        startPlay(mLastPos);
    }


    //释放
    private void releaseVideoView() {
        mVideoView.release();
        if (mVideoView.isFullScreen()) {
            mVideoView.stopFullScreen();
        }
        if (getActivity().getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        mCurPos = -1;
    }


    /**
     * PrepareView被点击
     */

    @Override
    public void onItemChildClick(int position) {
        startPlay(position);
    }

    /**
     * 开始播放
     *
     * @param position 列表位置
     */
    protected void startPlay(int position) {
        if (mCurPos == position) return;
        if (mCurPos != -1) {
            releaseVideoView();
        }
        //获取点击的实体
        VideoEntity videoEntity = datas.get(position);
        //边播边存
//        mVideoView.setUrl(proxyUrl);
        //获取视频的URL地址
        mVideoView.setUrl(videoEntity.getVideourl());
        //设置标题
        mTitleView.setTitle(videoEntity.getVtitle());
        View itemView = linearLayoutManager.findViewByPosition(position);
        if (itemView == null) return;
        VideoAdapter.ViewHolder viewHolder = (VideoAdapter.ViewHolder) itemView.getTag();
        //把列表中预置的PrepareView添加到控制器中，注意isPrivate此处只能为true。
        mController.addControlComponent(viewHolder.mPrepareView, true);
        Utils.removeViewFormParent(mVideoView);
        viewHolder.mPlayerContainer.addView(mVideoView, 0);
        //播放之前将VideoView添加到VideoViewManager以便在别的页面也能操作它
        getVideoViewManager().add(mVideoView, Tag.LIST);
        mVideoView.start();
        mCurPos = position;
    }

    protected VideoViewManager getVideoViewManager() {
        return VideoViewManager.instance();
    }
}
