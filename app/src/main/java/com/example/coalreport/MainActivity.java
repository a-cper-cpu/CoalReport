package com.example.coalreport;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.coalreport.about.AboutActivity;
import com.example.coalreport.adapter.MyPagerAdapter;
import com.example.coalreport.dailysaying.DailySayingActivity;
import com.example.coalreport.entity.TabEntity;
import com.example.coalreport.fragment.HomeFragment;
import com.example.coalreport.fragment.MyFragment;
import com.example.coalreport.fragment.ReleaseFragment;
import com.example.coalreport.login.LoginActivity;
import com.example.coalreport.utils.BaseActivity;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private String[] mTitles = {"首页", "发布", "我的"};

    //未选中图标
    private int[] mIconUnselectIds = {
            R.mipmap.home_unselect,
            R.mipmap.send_unselect,
            R.mipmap.my_unselect

    };


    //选中图标
    private int[] mIconSelectIds = {
            R.mipmap.home_selected,
            R.mipmap.send_selected,
            R.mipmap.my_selected
    };

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    //Tab选项(标题、选中图标、未选中图标)
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private ViewPager viewPager;
    private CommonTabLayout commonTabLayout;

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    //初始化
    @Override
    protected void initView() {
        viewPager = findViewById(R.id.viewpager);
        commonTabLayout = findViewById(R.id.commonTabLayout);
    }

    /**
     * @param menu
     * @return
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    //给菜单设置点击事件
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dailysaying:
                Intent intent = new Intent(MainActivity.this, DailySayingActivity.class);
                startActivity(intent);
                break;
            case R.id.about:
                Intent intent1 = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent1);
                break;
            case R.id.logout:
                Toast.makeText(MainActivity.this, "退出成功", Toast.LENGTH_SHORT).show();
                this.onBackPressed();
                Intent it = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(it);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initData() {
        //实例化fragment
        mFragments.add(HomeFragment.newInstance());
        mFragments.add(ReleaseFragment.newInstance());
        mFragments.add(MyFragment.newInstance());
        //初始化Tab选项
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        commonTabLayout.setTabData(mTabEntities);
        commonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        viewPager.setOffscreenPageLimit(mFragments.size());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                commonTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mTitles, mFragments));
    }

    //回调
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getSupportFragmentManager().getFragments() != null && getSupportFragmentManager().getFragments().size() > 0) {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            for (Fragment mFragment : fragments) {
                mFragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}
