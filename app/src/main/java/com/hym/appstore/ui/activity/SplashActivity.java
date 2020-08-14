package com.hym.appstore.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.hym.appstore.R;
import com.hym.appstore.dagger2.component.AppComponent;

import butterknife.BindView;
import butterknife.OnClick;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.skip_btn)
    Button mSkipBtn;

    Handler mHandler;
    boolean isFirst;
    SharedPreferences sp;
    Intent intent;

    /**
     * handler 跳轉消息
     */
    private static final int MESSAGE_SECOND = 3;


    private int count = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        mHandler.sendEmptyMessage(MESSAGE_SECOND);
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_splash;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void init() {
        sp = getPreferences(MODE_PRIVATE);
        isFirst = sp.getBoolean("isFirst", true);
        intent = new Intent();
        mHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (count == 1) {
                    jumpActivity(isFirst);
                } else {
                    count--;
                    mSkipBtn.setText(count + "s " + getString(R.string.skip));
                    mHandler.sendEmptyMessageDelayed(MESSAGE_SECOND, 1000);
                }


            }
        };

    }


    @Override
    public void initView() {

    }


    @Override
    public void initEvent() {

    }

    @OnClick(R.id.skip_btn)
    public void onViewClicked() {
        mHandler.removeMessages(MESSAGE_SECOND);
        jumpActivity(isFirst);
    }

    /**
     * 跳轉對應的activity
     *
     * @param isFirst 是否第一次打開
     */
    public void jumpActivity(boolean isFirst) {
        if (isFirst) {
            sp.edit().putBoolean("isFirst", false).commit();
            //如果用户是第一次安装应用并进入
            intent.setClass(SplashActivity.this, MainActivity.class);
        } else {

            intent.setClass(SplashActivity.this, MainActivity.class);
        }
        startActivity(intent);
        //可以设置界面之间的切换动画
//                overridePendingTransition();
        finish();
    }




}
