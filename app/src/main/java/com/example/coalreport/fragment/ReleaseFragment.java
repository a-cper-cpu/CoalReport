package com.example.coalreport.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.coalreport.MainActivity;
import com.example.coalreport.R;
import com.example.coalreport.login.LoginActivity;
import com.example.coalreport.releasephoto.ReleasePhotoActivity;
import com.example.coalreport.releasetext.ReleaseTextActivity;
import com.example.coalreport.releasevideo.ReleaseVideoActivity;

import java.io.File;
import java.util.Date;

/**
 * @author a-cper-cpu
 * 发布fragment
 * 1.文字举报
 * 2.图片举报
 * 3.视频举报
 */


public class ReleaseFragment extends Fragment {
    private Button btn_report;

    public ReleaseFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ReleaseFragment newInstance() {
        ReleaseFragment fragment = new ReleaseFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_release, container, false);
        btn_report = myView.findViewById(R.id.btn_report);
        btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickDialog(getActivity());
            }
        });
        return myView;
    }


    /**
     * 显示获取照片不同方式对话框
     */
    public static void showImagePickDialog(final Activity activity) {
        String title = "选择举报方式";
        String[] items = new String[]{"文字举报", "图片举报", "视频举报"};
        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switch (which) {
                            case 0:
                                Intent it = new Intent(activity, ReleaseTextActivity.class);
                                //intent传参，把id传过去
                                it.putExtra("id", activity.getIntent().getStringExtra("id"));
                                activity.startActivity(it);
                                break;
                            case 1:
                                Intent it1 = new Intent(activity, ReleasePhotoActivity.class);
                                it1.putExtra("id", activity.getIntent().getStringExtra("id"));
                                activity.startActivity(it1);
                                break;
                            case 2:
                                Intent it2 = new Intent(activity, ReleaseVideoActivity.class);
                                it2.putExtra("id", activity.getIntent().getStringExtra("id"));
                                activity.startActivity(it2);
                                break;
                            default:
                                break;
                        }
                    }
                })
                .show();
    }
}
