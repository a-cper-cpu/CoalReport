package com.example.coalreport.releasephoto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.coalreport.R;
import com.example.coalreport.utils.AppConfig;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author a-cper-cpu
 * 发布图片界面
 */


public class ReleasePhotoActivity extends AppCompatActivity {


    private Button btn_quxiao;
    private Button btn_fabu;
    private Button btn_takephoto;
    private EditText et_report;
    private ImageView img_takephoto;
    private Bitmap bitmap;
    private static File file = null;
    private static int REQUEST_CAMERA = 110;
    private static int REQUEST_PICKER = 111;
    final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HHmmss", Locale.CANADA);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_photo);
        et_report = findViewById(R.id.et_report);
        img_takephoto = findViewById(R.id.img_takephoto);
        btn_quxiao = findViewById(R.id.btn_quxiao);
        btn_quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**
         * 拍照
         * Intent捕获图像
         */
        btn_takephoto = findViewById(R.id.btn_takephoto);
        btn_takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                file = new File(getExternalCacheDir(), simpleDateFormat.format(new Date()) + ".png");
                Uri uriForFile = FileProvider.getUriForFile(ReleasePhotoActivity.this, "com.example.coalreport.fileProvider", file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);
                startActivityForResult(intent, REQUEST_CAMERA);
            }
        });
        // 发布，通过后台服务器上传到数据库
        btn_fabu = findViewById(R.id.btn_fabu);
        btn_fabu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // okhttp框架，上传图片

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient okHttpClient = new OkHttpClient();
                        Date dNow = new Date();
                        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
                        RequestBody requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addPart(Headers.of(
                                        "Content-Disposition",
                                        "form-data; name=\"id\""),
                                        RequestBody.create(null, getIntent().getStringExtra("id")))
                                .addPart(Headers.of(
                                        "Content-Disposition",
                                        "form-data; name=\"releaseText\""),
                                        RequestBody.create(null, et_report.getText().toString().trim()))

                                .addPart(Headers.of(
                                        "Content-Disposition",
                                        "form-data; name=\"releasePhoto\"; filename=\"" + file.getName() + "\""), fileBody)
                                .addPart(Headers.of(
                                        "Content-Disposition",
                                        "form-data; name=\"releaseTime\""),
                                        RequestBody.create(null, ft.format(dNow)))
                                .build();
                        String url = AppConfig.BASE_URl + "/insertreleasePhoto";
                        Request request = new Request.Builder()
                                .url(url)
                                .post(requestBody)
                                .build();
                        Call call = okHttpClient.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.e("text", "failure upload!");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Log.i("text", "success upload!");
                                String json = response.body().string();
                                Log.i("success........", "成功" + json);
                            }
                        });
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ReleasePhotoActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                                et_report.setText(null);
                                img_takephoto.setImageBitmap(null);
                            }
                        });
                    }
                }).start();
            }
        });
    }


    /**
     * 回调函数
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                img_takephoto.setImageBitmap(bitmap);
            } else if (requestCode == REQUEST_PICKER) {
            }
        }
    }
}
