package com.hym.appstore.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.hym.appstore.R;
import com.hym.appstore.bean.AppInfoBean;
import com.hym.appstore.common.Constant;
import com.hym.appstore.common.imageloader.ImageLoader;
import com.hym.appstore.common.utils.DensityUtil;
import com.hym.appstore.dagger2.component.AppComponent;
import com.hym.appstore.ui.fragment.AppDetailFragment;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.ionicons_typeface_library.Ionicons;

import butterknife.BindView;

public class AppDetailsActivity extends BaseActivity {


    @BindView(R.id.app_details_fl)
    FrameLayout frameLayout;
    @BindView(R.id.img_icon)
    ImageView imgIcon;
    @BindView(R.id.view_temp)
    View viewTemp;
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.view_coordinator)
    CoordinatorLayout viewCoordinator;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;


    private AppInfoBean mAppInfoBean;
    private boolean isAnim = true;


    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_app_details;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void init() {
        toolbar.setNavigationIcon(
                new IconicsDrawable(this)
                        .icon(Ionicons.Icon.ion_ios_arrow_back)
                        .sizeDp(16)
                        .color(getResources().getColor(R.color.theme_black)
                        )
        );
        mAppInfoBean = (AppInfoBean) getIntent().getSerializableExtra("appInfo");
        isAnim = (boolean) getIntent().getSerializableExtra("isAnim");
        if (mAppInfoBean != null) {
            mToolbarLayout.setTitle(mAppInfoBean.getDisplayName());
            ImageLoader.load(Constant.BASE_IMG_URL + mAppInfoBean.getIcon(), imgIcon);
        }



        if (isAnim) {
            View view = mMyApplication.getView();
            Bitmap bitmap = getViewImageCache(view);
            if (bitmap != null) {
                viewTemp.setBackground(new BitmapDrawable(bitmap));
            }

            int[] location = new int[2];
            view.getLocationOnScreen(location);
            int left = location[0];
            int top = location[1];

            ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(viewTemp.getLayoutParams());
            marginLayoutParams.topMargin = top;
            marginLayoutParams.leftMargin = left;
            marginLayoutParams.width = view.getWidth();
            marginLayoutParams.height = view.getHeight();


            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(marginLayoutParams);
            viewTemp.setLayoutParams(params);
            open();
        }else {
//            viewTemp.setBackgroundColor(getResources().getColor(R.color.theme_while));
            initFragment();
            viewTemp.setVisibility(View.GONE);
            if (viewCoordinator != null) {
                viewCoordinator.setVisibility(View.VISIBLE);
            }
        }

    }


    @Override
    public void initView() {

    }

    @Override
    public void initEvent() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private Bitmap getViewImageCache(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();

        Bitmap bitmap = view.getDrawingCache();
        if (bitmap == null) {
            return null;
        }

        bitmap = Bitmap.createBitmap(bitmap);

        view.destroyDrawingCache();

        return bitmap;
    }


    private void open() {
        int h = DensityUtil.getScreenH(this);
        ObjectAnimator animator = ObjectAnimator.ofFloat(viewTemp, "scaleY", 1f, (float) h);
        animator.setStartDelay(500);
        animator.setDuration(300);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (viewTemp != null) {
                    viewTemp.setVisibility(View.GONE);
                }
                if (viewCoordinator != null) {
                    viewCoordinator.setVisibility(View.VISIBLE);
                }
                initFragment();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                if (viewTemp != null){
                    viewTemp.setBackgroundColor(getResources().getColor(R.color.theme_while));
                }
            }
        });
        animator.start();
    }

    private void initFragment() {
        AppDetailFragment fragment = new AppDetailFragment(mAppInfoBean.getId());
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.app_details_fl, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }



}
