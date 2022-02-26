package com.example.coalreport.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coalreport.R;
import com.example.coalreport.register.RegisterActivity;

/**
 * @author a-cper-cpu
 * 选择界面
 */


public class StartActivity extends AppCompatActivity {

    private Button btnLogin;
    private Button btnRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(in);
            }
        });
        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inr = new Intent(StartActivity.this, RegisterActivity.class);
                startActivity(inr);
            }
        });
    }
}
