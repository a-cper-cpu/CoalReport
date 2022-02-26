package com.example.coalreport.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.coalreport.R;
import com.example.coalreport.adapter.PhotoAdapter;
import com.example.coalreport.adapter.TextAdapter;
import com.example.coalreport.adapter.VideoAdapter;
import com.example.coalreport.entity.PhotoEntity;
import com.example.coalreport.entity.TextEntity;
import com.example.coalreport.entity.VideoEntity;
import com.example.coalreport.utils.AppConfig;
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
 * 首页里文本fragment
 */


public class TextFragment extends Fragment {
    private boolean isCreated = false;
    private String title;
    private RecyclerView recyclerView;
    private RefreshLayout refreshLayout;
    private List<TextEntity> datas = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;

    public TextFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static TextFragment newInstance(String title) {
        TextFragment fragment = new TextFragment();
        fragment.title = title;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_text, container, false);
        recyclerView = v.findViewById(R.id.recyclerView2);
        refreshLayout = v.findViewById(R.id.refreshLayouttext);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getVideoList(true);
            }
        });
        isCreated = true;
        getVideoList(isCreated);
    }

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
                            .url(AppConfig.BASE_URl + "/getReleaseText")
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
                        TextEntity ve = new TextEntity();
                        ve.setVtitle(json_releaseSaying);
                        ve.setAuthor(json_releaseUsername);
                        ve.setDzCount(1);
                        ve.setCommentCount(1);
                        ve.setvCollect(1);
                        ve.setHeadurl(json_releasePhoto);
                        ve.setTextreport(json_releaseText);
                        datas.add(ve);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        TextAdapter textAdapter = new TextAdapter(getActivity(), datas);
        recyclerView.setAdapter(textAdapter);
    }
}
