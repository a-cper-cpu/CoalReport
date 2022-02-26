package com.example.coalreport.dailysaying;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coalreport.R;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

/**
 * @author a-cper-cpu
 * 每日一句
 */

public class DailySayingActivity extends AppCompatActivity {

    private TextView timeText, dateText, wordText;
    private String mMonth, mDay, mWay, mHours, mMinute;
    //一言API接口
    public static String baseUrl = "https://v1.hitokoto.cn";
    private static String TAG = DailySayingActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_saying);
        init();
    }

    public void init() {
        timeText = (TextView) findViewById(R.id.time_text);
        dateText = (TextView) findViewById(R.id.date_text);
        wordText = (TextView) findViewById(R.id.word_text);

    }


    /**
     * 界面一启动就把系统日期填充
     */
    @Override
    protected void onStart() {
        super.onStart();
        Calendar calendar = Calendar.getInstance();
        mMonth = String.valueOf(calendar.get(Calendar.MONTH) + 1);//0-11
        mDay = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        mWay = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
        mHours = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        if (calendar.get(Calendar.MINUTE) < 10) {
            mMinute = "0" + calendar.get(Calendar.MINUTE);
        } else {
            mMinute = String.valueOf(calendar.get(Calendar.MINUTE));
        }
        if ("1".equals(mWay)) {
            mWay = "天";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        timeText.setText(mHours + ":" + mMinute);
        dateText.setText(mMonth + "月" + mDay + "日" + "  " + "星期" + mWay);
    }

    //点击按钮调用该方法
    public void OnHuanju(View v) throws IOException {
        sendRequestWithHttpClient();
    }


    /**
     * 调用一言API,其中参数c等于i代表诗词，GET请求到JSON后取出诗词然后设置文本
     * 操作步骤：
     * 1.根据接口得到url
     * 2.创建connection对象
     * 3.设置请求方式
     * 4.连接
     * 5.得到响应码，得到响应流
     * 6.响应流转换为字符串
     * 7.根据字符串得到JSON对象
     * 8.从JSON对象中取得hitokoto键的值，即返回的诗词
     * 9.设置在文本框中
     */
    private void sendRequestWithHttpClient() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url1 = "https://v1.hitokoto.cn?c=i";
                    URL url = new URL(url1);
                    //得到connection对象。
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //设置请求方式
                    connection.setRequestMethod("GET");
                    //连接
                    connection.connect();
                    //得到响应码
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        //得到响应流
                        InputStream inputStream = connection.getInputStream();
                        //将响应流转换成字符串
                        String result = is2String(inputStream);//将流转换为字符串。
                        JSONObject jsonObject = new JSONObject(result);
                        String value = jsonObject.optString("hitokoto");
                        wordText.setText(value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //将流转换为字符串
    public static String is2String(InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();
        InputStreamReader inread = new InputStreamReader(in, "UTF-8");
        char[] b = new char[4096];
        for (int n; (n = inread.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}