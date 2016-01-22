package com.ashish.cop290assign0;

import android.nfc.Tag;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.ashish.cop290assign0.data.FormData;

public class ViewPagerAdapter extends PagerAdapter {
    FragmentManager fragmentManager;
    Fragment[] fragments;
    FormData formData;
    FragmentTransaction mTransaction;

    private static final String TAG = ViewPagerAdapter.class.getSimpleName();

    public ViewPagerAdapter(FragmentManager fm){
        Log.i(TAG,String.format("constructor called. fragmentManager:%s",fm));
        fragmentManager = fm;
    }
    public void setVals(int size, FormData formData){
        Log.i(TAG,String.format("setVals called. size:%d; formData:%s",size,formData));
        fragments = new Fragment[size];
        this.formData = formData;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.i(TAG,String.format("destroyItem called. position:%d, object:%s, container:%s",position,object,container));
        assert(0 <= position && position < fragments.length);   //ask ashish why??
        mTransaction = fragmentManager.beginTransaction();
        mTransaction.detach(fragments[position]);
        mTransaction.remove(fragments[position]);
        mTransaction.commit();
        fragments[position] = null;
    }

    @Override
    public Fragment instantiateItem(ViewGroup container, int position){
        Log.i(TAG,String.format("instantiateItem called. position:%d, container:%s",position,container));
        Fragment fragment = getItem(position);
        mTransaction = fragmentManager.beginTransaction();
        mTransaction.add(container.getId(), fragment, "fragment:" + position);
        mTransaction.commit();
        return fragment;
    }

    @Override
    public int getCount() {
        //Log.i(TAG,"getCount called.");
        return fragments.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object fragment) {
        //Log.i(TAG,String.format("isViewFromObject called. fragment:%s; view:%s",fragment,view));
        return ((Fragment) fragment).getView() == view;
    }

    public Fragment getItem(int position){
        Log.i(TAG,String.format("getItem called. position:%d",position));
        int pos = position%getCount();
        if(fragments[pos] == null){
            if(pos == 0)
                fragments[pos] = TeamNameFragment.newInstance(formData.getIsFilled(0),formData.getTeamName());
            else
                fragments[pos] = MemberFragment.newInstance(pos,formData.getIsFilled(pos),formData.getMember(pos));
            //fragments[pos] = MemberFragment.newInstance(pos, formData.getIsFilled(pos), formData.getTeamName(), formData.getMember(pos));
        }
        return fragments[pos];
    }
    @Override
    public void finishUpdate(ViewGroup container) {
        //Log.i(TAG,String.format("finishUpdate called. container:%s",container));
        if (mTransaction != null) {
            mTransaction = null;
            fragmentManager.executePendingTransactions();
        }
    }
}