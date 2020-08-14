package com.hym.appstore.ui.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.hym.appstore.ui.fragment.SortAppFragment;

import java.util.ArrayList;
import java.util.List;

public class SortAppViewPagerAdapter extends FragmentStatePagerAdapter {

    private int mSortID;

    private List<String> titles = new ArrayList<>(3);

    public SortAppViewPagerAdapter(@NonNull FragmentManager fm, int sortID) {
        super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mSortID = sortID;
        titles.add("精品");
        titles.add("排行");
        titles.add("新品");
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return SortAppFragment.newInstance(mSortID,position);
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
    }
}
