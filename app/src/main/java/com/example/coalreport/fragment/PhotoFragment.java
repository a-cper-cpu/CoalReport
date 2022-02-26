package com.example.coalreport.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coalreport.R;
import com.example.coalreport.adapter.PhotoAdapter;
import com.example.coalreport.entity.PhotoEntity;
import com.example.coalreport.utils.AppConfig;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
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
 * 首页里图片fragment
 */

public class PhotoFragment extends Fragment {

    private boolean isCreated = false;
    private String title;
    private RecyclerView recyclerView;
    private RefreshLayout refreshLayout;
    private List<PhotoEntity> datas = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;

    public PhotoFragment() {
        // Required empty public constructor
    }


    //实例化fragment
    public static PhotoFragment newInstance(String title) {
        PhotoFragment fragment = new PhotoFragment();
        fragment.title = title;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //初始化
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo, container, false);
        //类似listview，来一项一项展示数据
        recyclerView = v.findViewById(R.id.recyclerView1);
        //布局
        refreshLayout = v.findViewById(R.id.refreshLayoutphoto);
        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //当下拉刷新时，刷新数据并更新UI
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getVideoList(true);
            }
        });
        isCreated = true;
        getVideoList(isCreated);
    }

    /**
     * @param isRefresh 核心功能
     *                  用来从后台服务器获取数据，并更新UI
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
                            .url(AppConfig.BASE_URl + "/getReleasePhoto")
                            // .post(RequestBody.create(MediaType.parse("application/json"),json)).build();
                            .build();
                    //执行发送的指令
                    Response response = okHttpClient.newCall(request).execute();
                    final String responseData = response.body().string();
                    JSONArray jsonArray = new JSONArray(responseData);
                    datas = new ArrayList<>();
                    // 获取UI中各组件的数据，并填充更新UI
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        final String json_account = jsonObject.getString("id");
                        final String json_releaseText = jsonObject.getString("releaseText");
                        final String json_rpeleasePhoto = jsonObject.getString("releasePhoto");
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
                        PhotoEntity ve = new PhotoEntity();
                        ve.setVtitle(json_releaseSaying);
                        ve.setAuthor(json_releaseUsername);
                        ve.setDzCount(1);
                        ve.setCommentCount(1);
                        ve.setvCollect(1);
                        ve.setHeadurl(json_releasePhoto);
                        ve.setReporturl(json_rpeleasePhoto);
                        ve.setText_report(json_releaseText);
                        datas.add(ve);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        PhotoAdapter photoAdapter = new PhotoAdapter(getActivity(), datas);
        recyclerView.setAdapter(photoAdapter);
    }
}
