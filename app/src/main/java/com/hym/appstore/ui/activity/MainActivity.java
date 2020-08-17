package com.hym.appstore.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.hym.appstore.R;
import com.hym.appstore.bean.FragmentInfo;
import com.hym.appstore.bean.User;
import com.hym.appstore.common.Constant;
import com.hym.appstore.common.font.Cniao5Font;
import com.hym.appstore.common.imageloader.GlideCircleTransform;
import com.hym.appstore.common.rx.RxBus;
import com.hym.appstore.common.utils.ACache;
import com.hym.appstore.dagger2.component.AppComponent;
import com.hym.appstore.dagger2.component.DaggerMainComponent;
import com.hym.appstore.dagger2.module.MainModule;
import com.hym.appstore.presenter.MainPresenter;
import com.hym.appstore.presenter.contract.MainContract;
import com.hym.appstore.ui.adapter.MyViewPagerAdapter;
import com.hym.appstore.ui.fragment.GameFragment;
import com.hym.appstore.ui.fragment.HomeFragment;
import com.hym.appstore.ui.fragment.RankingFragment;
import com.hym.appstore.ui.fragment.SortFragment;
import com.hym.appstore.ui.widget.BadgeActionProvider;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.ionicons_typeface_library.Ionicons;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.MainView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    /* @BindView(R.id.appBarLayout)
     AppBarLayout appBarLayout;*/
    @BindView(R.id.main_tab_layout)
    TabLayout mainTabLayout;
    @BindView(R.id.main_viewpager)
    ViewPager mainViewpager;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    private View headerView;
    private ImageView mUserHeadView;
    private TextView mTextUserName;
    private TextView mTextUserPhone;

    private long lastClickTime = 0;

    private BadgeActionProvider badgeActionProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_main;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMainComponent.builder().appComponent(appComponent)
                .mainModule(new MainModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void init() {

//        RxBus.get().register(this);

        RxBus.getDefault().toObservable(User.class).subscribe(new Consumer<User>() {
            @Override
            public void accept(User user) {
                navigationView.getMenu().setGroupVisible(R.id.group2,true);
                initUserHeadView(user);
            }
        });

        mPresenter.requestPermission();

        mPresenter.getAppUpdateInfo();

    }

    private List<FragmentInfo> initFragments(){
        List<FragmentInfo>  mFragments=new ArrayList<>(4);
        mFragments.add(new FragmentInfo(getString(R.string.home_tab_recommend), HomeFragment.class));
        mFragments.add(new FragmentInfo(getString(R.string.home_tab_ranking), RankingFragment.class));
        mFragments.add(new FragmentInfo(getString(R.string.home_tab_game), GameFragment.class));
        mFragments.add(new FragmentInfo(getString(R.string.home_tab_sort), SortFragment.class));
        return mFragments;
    }

    private void initDrawerLayout() {
        headerView = navigationView.getHeaderView(0);
        mUserHeadView = (ImageView) headerView.findViewById(R.id.icon_image);
//        mUserHeadView.setImageDrawable(new IconicsDrawable(this, Cniao5Font.Icon.cniao_head).colorRes(R.color.theme_while));
        mUserHeadView.setImageDrawable(getResources().getDrawable(R.drawable.vector_drawable_no_log));
        mTextUserName = (TextView) headerView.findViewById(R.id.username);
        mTextUserPhone = (TextView) headerView.findViewById(R.id.mail);
        navigationView.getMenu().findItem(R.id.menu_app_update).setIcon(new IconicsDrawable(this, Ionicons.Icon.ion_ios_loop));
//        navigationView.getMenu().findItem(R.id.menu_download_manager).setIcon(new IconicsDrawable(this, Cniao5Font.Icon.cniao_download));
        navigationView.getMenu().findItem(R.id.menu_download_manager).setIcon(new IconicsDrawable(this, Ionicons.Icon.ion_ios_cloud_download_outline));
        navigationView.getMenu().findItem(R.id.menu_app_uninstall).setIcon(new IconicsDrawable(this, Ionicons.Icon.ion_ios_trash_outline));
        navigationView.getMenu().findItem(R.id.menu_setting).setIcon(new IconicsDrawable(this, Ionicons.Icon.ion_ios_gear_outline));
        navigationView.getMenu().findItem(R.id.menu_logout).setIcon(new IconicsDrawable(this, Ionicons.Icon.ion_log_out));
//        navigationView.getMenu().findItem(R.id.menu_logout).setIcon(new IconicsDrawable(this, Cniao5Font.Icon.cniao_shutdown));


//        navigationView.setCheckedItem(R.id.nav_call);//设置默认选择
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                CharSequence title = item.getTitle();
                Toast.makeText(MainActivity.this, title, Toast.LENGTH_SHORT).show();
                switch (item.getItemId()) {
                    case R.id.menu_logout:
                        logout();
                        break;
                    case R.id.menu_download_manager:
                        startActivity(AppManagerActivity.class);
                        break;

                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(LoginActivity.class);
            }
        });

    }

    @Override
    public void initView() {

    }

    private void initTabLayout() {
        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(),initFragments());
        mainViewpager.setAdapter(myViewPagerAdapter);
        mainTabLayout.setupWithViewPager(mainViewpager);
    }

    private void initToolbar(){
        // NavigationView 可以将滑动菜单页面的实现变得非常简单
        ActionBar supportActionBar = getSupportActionBar();
        //Toolbar 的最左边加入一个导航按钮；引得用户滑动
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setHomeAsUpIndicator(new IconicsDrawable(this, Ionicons.Icon.ion_android_menu).color(getResources().getColor(R.color.TextColor)).actionBar());
        }
        toolbar.setOverflowIcon(new IconicsDrawable(this, Ionicons.Icon.ion_android_more_vertical).color(getResources().getColor(R.color.TextColor)).actionBar());
    }


    @Override
    public void initEvent() {

    }

/*    @Subscribe
    public void getUser(User user){
        navigationView.getMenu().setGroupVisible(R.id.group2,true);
        initUserHeadView(user);
    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.toolbar, menu);//加载toolbar.xml 菜单文件
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        menu.getItem(2).setIcon(new IconicsDrawable(this, Ionicons.Icon.ion_ios_trash_outline).color(getResources().getColor(R.color.TextColor)).actionBar());
//        menu.findItem(R.id.delete).setIcon(new IconicsDrawable(this, Ionicons.Icon.ion_ios_trash_outline).color(getResources().getColor(R.color.TextColor)).actionBar());
//        MenuItem menuItem = menu.findItem(R.id.delete).setIcon(new IconicsDrawable(this, Ionicons.Icon.ion_ios_cloud_download_outline).color(getResources().getColor(R.color.TextColor)).actionBar());
        MenuItem downloadMenuItem = toolbar.getMenu().findItem(R.id.delete);
        badgeActionProvider = (BadgeActionProvider) MenuItemCompat.getActionProvider(downloadMenuItem);

        menu.findItem(R.id.search).setIcon(new IconicsDrawable(this, Ionicons.Icon.ion_ios_search).color(getResources().getColor(R.color.TextColor)).actionBar());

        return true;
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        badgeActionProvider.setIcon(DrawableCompat.wrap(new IconicsDrawable(this, Ionicons.Icon.ion_ios_cloud_download_outline).color(ContextCompat.getColor(this,R.color.TextColor))));
        badgeActionProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AppManagerActivity.class);
                if (badgeActionProvider.getBadgeNum() > 0) {
                    intent.putExtra(Constant.POSITION,2);
                }
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home://Toolbar 的最左边加入一个导航按钮；引得用户滑动
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.search:
                startActivity(SearchActivity.class);
                break;
/*            case R.id.setting:
                Toast.makeText(this, "you clicked 11", Toast.LENGTH_SHORT).show();
                break;*/
            case R.id.setting2:
                Toast.makeText(this, "you clicked 22", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    private void logout() {
        headerView.setEnabled(true);
        ACache aCache = ACache.get(this);
        aCache.put(Constant.TOKEN, "");
        aCache.put(Constant.USER, "");
        mUserHeadView.setImageDrawable(new IconicsDrawable(this, Cniao5Font.Icon.cniao_head).colorRes(R.color.theme_while));
        mTextUserName.setText(R.string.no_login);
        mTextUserPhone.setText(R.string.phone_num);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
        Toast.makeText(MainActivity.this, "退出登錄", Toast.LENGTH_LONG).show();
        navigationView.getMenu().setGroupVisible(R.id.group2,false);
        mUserHeadView.setImageDrawable(getResources().getDrawable(R.drawable.vector_drawable_no_log));
    }

    private void initUser() {
        Object objUser = ACache.get(this).getAsObject(Constant.USER);
        if (objUser == null) {
            headerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            });
            navigationView.getMenu().setGroupVisible(R.id.group2,false);
        } else {
            navigationView.getMenu().setGroupVisible(R.id.group2,true);
            User user = (User) objUser;
            initUserHeadView(user);
        }
    }

    private void initUserHeadView(User user) {
        headerView.setEnabled(false);
        Glide.with(this).load("http:" + user.getLogo_url()).transform(new GlideCircleTransform())
                .into(mUserHeadView);
        mTextUserName.setText(user.getUsername());
        mTextUserPhone.setText(String.valueOf(user.getId()));
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();//一定要将改行删掉
        if (lastClickTime <= 0) {
            Toast.makeText(this, "再点击一下，退出应用", Toast.LENGTH_SHORT).show();
            lastClickTime = System.currentTimeMillis();
        } else {
            if ((System.currentTimeMillis() - lastClickTime) > 3000) {  //这里3000，表示两次点击的间隔时间
                Toast.makeText(this, "再点击一下，退出应用", Toast.LENGTH_SHORT).show();
                lastClickTime = System.currentTimeMillis();
            } else {
                System.exit(0);
            }

        }

    }


    @Override
    public void requestPermissionSuccess() {
        initToolbar();
        initDrawerLayout();
        initTabLayout();
        initUser();
    }

    @Override
    public void requestPermissionFail() {
        Toast.makeText(MainActivity.this,"授权失败....",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void changeAppNeedUpdateCount(int count) {
        if(count>0){
            badgeActionProvider.setText(count+"");
        }
        else{
            badgeActionProvider.hideBadge();
        }
    }


}
