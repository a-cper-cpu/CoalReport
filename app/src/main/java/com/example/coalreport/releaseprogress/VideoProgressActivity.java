package com.example.coalreport.releaseprogress;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import com.example.coalreport.R;

import static com.example.coalreport.utils.AppConfig.BASE_PROGRESS_URl;


/**
 * @author a-cper-cpu
 * 视频举报进展界面，webView加载网页
 * 后台服务器提供URL，app端加载显示
 */

public class VideoProgressActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_progress);
        webView = findViewById(R.id.webView_video);
        webView.loadUrl(BASE_PROGRESS_URl + "/coalreport/videoreport/" + getIntent().getStringExtra("id"));
    }
}
