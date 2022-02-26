package com.example.coalreport.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.coalreport.R;
import com.example.coalreport.utils.AppConfig;
import com.example.coalreport.utils.StringUtils;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author a-cper-cpu
 * 修改密码界面
 */


public class UpdatePwordActivity extends AppCompatActivity {

    private EditText et_password;
    private Button btn_updatepw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pword);
        et_password = findViewById(R.id.et_updatepwd);
        btn_updatepw = findViewById(R.id.btn_updatepword);
        btn_updatepw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(et_password.getText().toString().trim())) {
                    Toast.makeText(UpdatePwordActivity.this, "密码为空，请重新输入！", Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FormBody.Builder params = new FormBody.Builder();
                            params.add("id", getIntent().getStringExtra("id"));
                            params.add("passWord", et_password.getText().toString().trim());
                            OkHttpClient okHttpClient = new OkHttpClient();
                            //创造http请求
                            Request request = new Request.Builder()
                                    .url(AppConfig.BASE_URl + "/updatePassword")
                                    // .post(RequestBody.create(MediaType.parse("application/json"),json)).build();
                                    .post(params.build())
                                    .build();
                            //执行发送的指令
                            Response response = okHttpClient.newCall(request).execute();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(UpdatePwordActivity.this, "重置密码成功！", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
}
