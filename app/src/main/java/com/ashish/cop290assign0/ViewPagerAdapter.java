package com.ashish.cop290assign0;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

class ViewPagerAdapter extends FragmentPagerAdapter {
    protected static List<Bitmap> imageIds = new ArrayList<>();
    protected static List<String> names = new ArrayList<>();
    protected static List<String> entryCodes = new ArrayList<>();

    private int mCount = 0;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
//    public void setVals(int c,List<Bitmap> i,List<String> n,List<String> e){
//        this.mCount = c;
//        this.imageIds = i;
//        this.names = n;
//        this.entryCodes = e;
//    }

    public void setVals(int c){
        this.mCount = c;
    }
//    @Override
//    public Fragment getItem(int position) {
//        int pos = position%names.size();
//        return PagerFragment.newInstance(pos,imageIds.get(pos),names.get(pos),entryCodes.get(pos));
//    }
    @Override
    public Fragment getItem(int position) {
        //int pos = position%names.size();
        return PagerFragment.newInstance();
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return ViewPagerAdapter.names.get(position % names.size());
    }

    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }
}