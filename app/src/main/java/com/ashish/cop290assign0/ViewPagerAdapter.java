package com.ashish.cop290assign0;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
    FragmentManager fragmentManager;
    Fragment[] fragments;

    public ViewPagerAdapter(FragmentManager fm,int size){
        fragmentManager = fm;
        fragments = new Fragment[size];
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        assert(0 <= position && position < fragments.length);
        FragmentTransaction trans = fragmentManager.beginTransaction();
        trans.remove(fragments[position]);
        trans.commit();
        fragments[position] = null;
    }

    @Override
    public Fragment instantiateItem(ViewGroup container, int position){
        Fragment fragment = getItem(position);
        FragmentTransaction trans = fragmentManager.beginTransaction();
        trans.add(container.getId(),fragment,"fragment:"+position);
        trans.commit();
        return fragment;
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object fragment) {
        return ((Fragment) fragment).getView() == view;
    }

    public Fragment getItem(int position){
        int pos = position%getCount();
        if(fragments[pos] == null){
            fragments[pos] = PagerFragment.newInstance(pos);
        }
        return fragments[pos];
    }
}