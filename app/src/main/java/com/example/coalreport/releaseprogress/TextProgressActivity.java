package com.example.coalreport.releaseprogress;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import com.example.coalreport.R;

import static com.example.coalreport.utils.AppConfig.BASE_PROGRESS_URl;


/**
 * @author a-cper-cpu
 * 文字举报进展界面，webView加载网页
 * 后台服务器提供URL，app端加载显示
 */

public class TextProgressActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_progress);
        webView = findViewById(R.id.webView_text);
        webView.loadUrl(BASE_PROGRESS_URl + "/coalreport/textreport/" + getIntent().getStringExtra("id"));
    }
}
