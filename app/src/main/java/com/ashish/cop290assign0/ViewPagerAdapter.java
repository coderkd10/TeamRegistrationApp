package com.ashish.cop290assign0;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class ViewPagerAdapter extends PagerAdapter {
    FragmentManager fragmentManager;
    Fragment[] fragments;
    String[] names,entryCodes,images;
    String teamName;
    private int[] visibility;
    FragmentTransaction mTransaction;
    public ViewPagerAdapter(FragmentManager fm){
        fragmentManager = fm;
    }
    public void setVals(int size,String t,String[] n,String[] e,String[] i,int[] v){
        fragments = new Fragment[size];
        teamName = t;
        names = n;
        entryCodes = e;
        images = i;
        visibility = v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        assert(0 <= position && position < fragments.length);
        mTransaction = fragmentManager.beginTransaction();
        mTransaction.detach(fragments[position]);
        mTransaction.remove(fragments[position]);
        mTransaction.commit();
        fragments[position] = null;
    }

    @Override
    public Fragment instantiateItem(ViewGroup container, int position){
        Fragment fragment = getItem(position);
        mTransaction = fragmentManager.beginTransaction();
        mTransaction.add(container.getId(), fragment, "fragment:" + position);
        mTransaction.commit();
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
            fragments[pos] = PagerFragment.newInstance(pos,visibility[pos],names[pos],entryCodes[pos],images[pos],teamName);
        }
        return fragments[pos];
    }
    @Override
    public void finishUpdate(ViewGroup container) {
        if (mTransaction != null) {
            mTransaction = null;
            fragmentManager.executePendingTransactions();
        }
    }
}