package com.example.coalreport.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;


import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coalreport.R;
import com.example.coalreport.about.AboutActivity;
import com.example.coalreport.login.LoginActivity;
import com.example.coalreport.login.UpdatePwordActivity;
import com.example.coalreport.releaseprogress.ProgressActivity;
import com.example.coalreport.utils.AppConfig;
import com.example.coalreport.utils.StringUtils;
import com.hb.dialog.myDialog.MyAlertInputDialog;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

import org.xutils.x;


/**
 * @author a-cper-cpu
 * 我的fragment
 * 实现主要的功能有：
 * 1.用户名的修改
 * 2.头像的修改
 * 3.名言的修改
 * 4.菜单的实现
 * 5.举报进展、修改密码、关于界面的跳转
 * 6.退出登录的实现
 */

public class MyFragment extends Fragment {


    //头像
    private ImageView img_header;
    //用户名
    private TextView tv_username;
    //名言
    private TextView tv_saying;
    //关于界面布局
    private RelativeLayout rl_about;
    //退出登录布局
    private RelativeLayout rl_logout;
    //举报进展布局
    private RelativeLayout rl_report;
    //修改密码布局
    private RelativeLayout rl_updatepw;
    //格式化时间
    final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HHmmss", Locale.CHINA);

    /**
     * 记录是处于什么状态：拍照or相册
     */
    //拍照
    private static int REQUEST_CAMERA = 110;
    // 相册
    private static int REQUEST_PICKER = 111;
    private static File file = null;

    public MyFragment() {
        // Required empty public constructor
    }

    // 实例化myfragment
    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 1.实例化组件
     * 2.实现创建界面，然后把数据库数据填充到组件
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_my, container, false);
        tv_username = myView.findViewById(R.id.tv_username);
        tv_saying = myView.findViewById(R.id.tv_saying);
        img_header = myView.findViewById(R.id.img_header);

        /**
         * okHttp框架，http请求，操作数据库
         * 操作步骤：
         * 1.创建okhttpclient
         * 2.创造http请求
         * 3.执行发送的指令
         * 4.把response体转换为String
         * 5.创建JSON对象
         * 6.取出JSON对象的每一项
         * 7.更新UI组件
         */
        //okHttp框架，http请求，操作数据库
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    //创造http请求
                    Request request = new Request.Builder().get()
                            .url(AppConfig.BASE_URl + "/getUserName?id=" + getActivity().getIntent().getStringExtra("id"))
                            // .post(RequestBody.create(MediaType.parse("application/json"),json)).build();
                            .build();
                    //执行发送的指令
                    Response response = okHttpClient.newCall(request).execute();
                    //把response体转换为String
                    final String responseData = response.body().string();
                    //创建JSON对象
                    JSONObject jsonObject = new JSONObject(responseData);
                    //取出JSON中用户名、名言、头像
                    final String json_userName = jsonObject.getString("userName");
                    final String json_saying = jsonObject.getString("saying");
                    final String json_photo = jsonObject.getString("photo");
                    //用于UI更新操作，当用户登录成功时，自动填充数据
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!StringUtils.isEmpty(json_userName)) {
                                //if (if(json_userName)) {
                                tv_username.setText(json_userName);
                            }
                            if (!StringUtils.isEmpty(json_saying)) {
                                tv_saying.setText(json_saying);
                            }
                            if (!StringUtils.isEmpty(json_photo)) {
                                //xutils框架，用于头像
                                x.image().bind(img_header, json_photo);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        /**
         * 设置用户名
         * okhttp框架，修改用户名
         * 操作步骤类似前者
         */
        tv_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出框
                final MyAlertInputDialog myAlertInputDialog = new MyAlertInputDialog(getContext()).builder()
                        .setTitle("设置用户名")
                        .setEditText("");
                myAlertInputDialog.setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //okHttp框架请求
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (StringUtils.isEmpty(myAlertInputDialog.getResult())) {
                                    Toast.makeText(getContext(), "用户名为空，请重新设置！", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                try {
                                    FormBody.Builder params = new FormBody.Builder();
                                    //获取intent传过来的id
                                    params.add("id", getActivity().getIntent().getStringExtra("id"));
                                    //获取弹出框的输入值
                                    params.add("userName", myAlertInputDialog.getResult());
                                    OkHttpClient okHttpClient = new OkHttpClient();
                                    //创造http请求
                                    Request request = new Request.Builder()
                                            .url(AppConfig.BASE_URl + "/updateUserName")
                                            // .post(RequestBody.create(MediaType.parse("application/json"),json)).build();
                                            .post(params.build())
                                            .build();
                                    //执行发送的指令
                                    Response response = okHttpClient.newCall(request).execute();
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getContext(), "设置用户名成功！", Toast.LENGTH_SHORT).show();
                                            tv_username.setText(myAlertInputDialog.getResult());
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        myAlertInputDialog.dismiss();
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //showMsg("取消");
                        myAlertInputDialog.dismiss();
                    }
                });
                myAlertInputDialog.show();
            }
        });


        /**
         * 设置名言
         * okhttp框架，修改名言
         * 操作步骤类似前者
         */

        tv_saying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MyAlertInputDialog myAlertInputDialog1 = new MyAlertInputDialog(getContext()).builder()
                        .setTitle("设置名言")
                        .setEditText("");
                myAlertInputDialog1.setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (StringUtils.isEmpty(myAlertInputDialog1.getResult())) {
                                    Toast.makeText(getContext(), "名言为空，请重新设置！", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                try {
                                    FormBody.Builder params = new FormBody.Builder();
                                    params.add("id", getActivity().getIntent().getStringExtra("id"));
                                    params.add("saying", myAlertInputDialog1.getResult());
                                    OkHttpClient okHttpClient = new OkHttpClient();
                                    //创造http请求
                                    Request request = new Request.Builder()
                                            .url(AppConfig.BASE_URl + "/updateUserSaying")
                                            // .post(RequestBody.create(MediaType.parse("application/json"),json)).build();
                                            .post(params.build())
                                            .build();
                                    //执行发送的指令
                                    Response response = okHttpClient.newCall(request).execute();
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getContext(), "设置名言成功！", Toast.LENGTH_SHORT).show();
                                            tv_saying.setText(myAlertInputDialog1.getResult());
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        myAlertInputDialog1.dismiss();
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myAlertInputDialog1.dismiss();
                    }
                });
                myAlertInputDialog1.show();
            }
        });


        /**
         * 设置头像
         * okhttp框架
         */

        img_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickDialog(getActivity());
            }
        });
        tv_username = myView.findViewById(R.id.tv_username);
        tv_saying = myView.findViewById(R.id.tv_saying);

        /**
         * 退出登录
         */
        rl_logout = myView.findViewById(R.id.rl_logout);
        rl_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "退出成功", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
                Intent it = new Intent(getContext(), LoginActivity.class);
                startActivity(it);
            }
        });


        rl_about = myView.findViewById(R.id.rl_about);
        rl_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), AboutActivity.class);
                startActivity(it);
            }
        });

        rl_report = myView.findViewById(R.id.rl_report);
        rl_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it3 = new Intent(getContext(), ProgressActivity.class);
                it3.putExtra("id", getActivity().getIntent().getStringExtra("id"));
                startActivity(it3);
            }
        });

        rl_updatepw = myView.findViewById(R.id.rl_updatepw);
        rl_updatepw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it5 = new Intent(getContext(), UpdatePwordActivity.class);
                it5.putExtra("id", getActivity().getIntent().getStringExtra("id"));
                startActivity(it5);
            }
        });
        return myView;
    }


    /**
     * 显示获取照片不同方式对话框
     */
    public static void showImagePickDialog(final Activity activity) {
        String title = "选择获取图片方式";
        String[] items = new String[]{"拍照"};
        //弹出框，用来选择
        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switch (which) {
                            case 0:
                                /**
                                 * 核心步骤
                                 *
                                 * 1、定义FileProvider
                                 *
                                 * 2、定义可用的文件路径
                                 *
                                 * 3、为定义的FileProvider添加文件路径
                                 *
                                 * 4、为特定文件生成ContentURI
                                 *
                                 * 5、授予ContentURI授予临时权限
                                 *
                                 * 6、使用Intent传递ContentURI
                                 */
                                //意图捕获图像
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                file = new File(activity.getExternalCacheDir(), simpleDateFormat.format(new Date()) + ".png");
                                //FileProvider content://的模式替换掉 file://
                                Uri uriForFile = FileProvider.getUriForFile(activity, "com.example.coalreport.fileProvider", file);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);
                                activity.startActivityForResult(intent, REQUEST_CAMERA);
                                break;
                            default:
                                break;
                        }
                    }
                })
                .show();
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
                final Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                img_header.setImageBitmap(bitmap);
                 // 上传图片
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient okHttpClient = new OkHttpClient();
                        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
                        RequestBody requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addPart(Headers.of(
                                        "Content-Disposition",
                                        "form-data; name=\"id\""),
                                        RequestBody.create(null, getActivity().getIntent().getStringExtra("id")))
                                .addPart(Headers.of(
                                        "Content-Disposition",
                                        "form-data; name=\"photo\"; filename=\"" + file.getName() + "\""), fileBody)
                                .build();
                        String url = AppConfig.BASE_URl + "/updateUserPhoto";
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
                    }
                }).start();
                //相册选择  有bug因时间原因未实现
            } else if (requestCode == REQUEST_PICKER) {
                try {
                    handleImageBeforeKitKat(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection来获取真实图片路径
        Cursor cursor = getContext().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            img_header.setImageBitmap(bitmap);
        } else {
            Toast.makeText(getContext(), "fail to get image", Toast.LENGTH_SHORT).show();
        }
    }


    public Bitmap getBitmap(String url) {
        Bitmap bm = null;
        try {
            URL iconUrl = new URL(url);
            URLConnection conn = iconUrl.openConnection();
            HttpURLConnection http = (HttpURLConnection) conn;
            int length = http.getContentLength();
            conn.connect();
            // 获得图像的字符流
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is, length);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();// 关闭流
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }
}
