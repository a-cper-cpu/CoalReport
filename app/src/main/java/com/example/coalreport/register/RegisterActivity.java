package com.example.coalreport.register;

import android.os.Bundle;
import android.provider.SyncStateContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coalreport.R;
import com.example.coalreport.login.LoginActivity;
import com.example.coalreport.utils.AppConfig;

import com.example.coalreport.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * @author a-cper-cpu
 * 注册界面
 */


public class RegisterActivity extends AppCompatActivity {


    private Button btn_register;
    private EditText et_account;
    private EditText et_pwd;
    private String account;
    private String pwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btn_register = findViewById(R.id.btn_register);
        et_account = findViewById(R.id.et_account);
        et_pwd = findViewById(R.id.et_pwd);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account = et_account.getText().toString().trim();
                pwd = et_pwd.getText().toString().trim();
                if (StringUtils.isEmpty(account)) {
                    Toast.makeText(RegisterActivity.this, "请输入账号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtils.isEmpty(pwd)) {
                    Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FormBody.Builder params = new FormBody.Builder();
                            params.add("id", account);
                            params.add("passWord", pwd);
                            OkHttpClient okHttpClient = new OkHttpClient();
                            //创造http请求
                            Request request = new Request.Builder()
                                    .url(AppConfig.BASE_URl + "/register")
                                    // .post(RequestBody.create(MediaType.parse("application/json"),json)).build();
                                    .post(params.build())
                                    .build();
                            //执行发送的指令
                            Response response = okHttpClient.newCall(request).execute();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegisterActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }
}
