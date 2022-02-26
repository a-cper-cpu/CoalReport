package com.example.coalreport.releasetext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.coalreport.R;
import com.example.coalreport.fragment.ReleaseFragment;
import com.example.coalreport.register.RegisterActivity;
import com.example.coalreport.utils.AppConfig;
import com.example.coalreport.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author a-cper-cpu
 * 发布文字举报界面
 */

public class ReleaseTextActivity extends AppCompatActivity {

    private Button btn_quxiao;
    private Button btn_fabu;
    private EditText et_report;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_text);

        btn_quxiao = findViewById(R.id.btn_quxiao);

        btn_quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        et_report = findViewById(R.id.et_report);
        btn_fabu = findViewById(R.id.btn_fabu);
        btn_fabu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String report = et_report.getText().toString().trim();
                if (StringUtils.isEmpty(report)) {
                    Toast.makeText(ReleaseTextActivity.this, "举报内容为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Date dNow = new Date();
                            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            FormBody.Builder params = new FormBody.Builder();
                            params.add("id", getIntent().getStringExtra("id"));
                            params.add("releaseText", report);
                            params.add("releaseTime", ft.format(dNow));
                            OkHttpClient okHttpClient = new OkHttpClient();
                            //创造http请求
                            Request request = new Request.Builder()
                                    .url(AppConfig.BASE_URl + "/releaseText")
                                    // .post(RequestBody.create(MediaType.parse("application/json"),json)).build();
                                    .post(params.build())
                                    .build();
                            //执行发送的指令
                            Response response = okHttpClient.newCall(request).execute();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ReleaseTextActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                                    et_report.setText(null);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ReleaseTextActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }
}
