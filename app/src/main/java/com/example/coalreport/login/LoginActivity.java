package com.example.coalreport.login;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coalreport.MainActivity;
import com.example.coalreport.R;
import com.example.coalreport.register.RegisterActivity;
import com.example.coalreport.utils.AppConfig;
import com.example.coalreport.utils.StringUtils;
import com.example.coalreport.welcome.InitAdvActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author a-cper-cpu
 * 登录界面
 */

public class LoginActivity extends AppCompatActivity {


    private EditText etAccount;
    private EditText etPwd;
    private Button btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etAccount = findViewById(R.id.et_account);
        etPwd = findViewById(R.id.et_pwd);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String account = etAccount.getText().toString().trim();
                        String pwd = etPwd.getText().toString().trim();
                        if (StringUtils.isEmpty(account)) {
                            Toast.makeText(LoginActivity.this, "请输入账号", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (StringUtils.isEmpty(pwd)) {
                            Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            OkHttpClient okHttpClient = new OkHttpClient();
                            //创造http请求
                            Request request = new Request.Builder().get()
                                    .url(AppConfig.BASE_URl + "/user?id=" + account)
                                    // .post(RequestBody.create(MediaType.parse("application/json"),json)).build();
                                    .build();
                            //执行发送的指令
                            Response response = okHttpClient.newCall(request).execute();
                            final String responseData = response.body().string();
                            JSONObject jsonObject = new JSONObject(responseData);
                            final String json_account = jsonObject.getString("id");
                            final String json_pwd = jsonObject.getString("passWord");
                            if (account.equals(json_account)) {
                                if (pwd.equals(json_pwd)) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    finish();
                                    Intent it = new Intent(LoginActivity.this, MainActivity.class);
                                    it.putExtra("id", json_account);
                                    startActivity(it);
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(LoginActivity.this, "登录失败，请确保密码正确！", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this, "登录失败，请确保账号正确！", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, "未查询到该账户，请先注册", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }
}
