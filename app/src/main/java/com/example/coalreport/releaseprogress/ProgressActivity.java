package com.example.coalreport.releaseprogress;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.coalreport.R;


/**
 * @author a-cper-cpu
 * 举报进展，三个按钮，分别跳转到文字举报进展、图片举报进展、视频举报进展
 */

public class ProgressActivity extends AppCompatActivity {

    private Button btn_text;
    private Button btn_video;
    private Button btn_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        btn_video = findViewById(R.id.btn_progressvideo);
        btn_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it3 = new Intent(ProgressActivity.this, VideoProgressActivity.class);
                it3.putExtra("id", getIntent().getStringExtra("id"));
                startActivity(it3);
            }
        });

        btn_text = findViewById(R.id.btn_progresstext);
        btn_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it3 = new Intent(ProgressActivity.this, TextProgressActivity.class);
                it3.putExtra("id", getIntent().getStringExtra("id"));
                startActivity(it3);
            }
        });

        btn_photo = findViewById(R.id.btn_progressphoto);
        btn_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it3 = new Intent(ProgressActivity.this, PhotoProgressActivity.class);
                it3.putExtra("id", getIntent().getStringExtra("id"));
                startActivity(it3);
            }
        });
    }
}
