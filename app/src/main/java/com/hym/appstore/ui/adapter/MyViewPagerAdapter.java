package com.hym.appstore.ui.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.hym.appstore.bean.FragmentInfo;

import java.util.ArrayList;
import java.util.List;

public class MyViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<FragmentInfo> fragmentInfos = new ArrayList<>();

    public MyViewPagerAdapter(@NonNull FragmentManager fm,List<FragmentInfo> fragmentInfo) {
        super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragmentInfos = fragmentInfo;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        try {
            return (Fragment)fragmentInfos.get(position).getFragment().newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public int getCount() {
        return fragmentInfos.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentInfos.get(position).getTitle();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
    }
}
