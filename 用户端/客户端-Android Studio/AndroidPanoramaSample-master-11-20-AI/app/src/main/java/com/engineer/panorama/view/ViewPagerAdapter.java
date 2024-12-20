package com.engineer.panorama.view;

import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;


public class ViewPagerAdapter extends PagerAdapter {
    private ArrayList<View> viewLists;

    public ViewPagerAdapter(FragmentManager supportFragmentManager) {
    }

    public ViewPagerAdapter(ArrayList<View> viewLists) {
        super();
        this.viewLists = viewLists;
    }

    @Override
    public int getCount() {
        return viewLists.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewLists.get(position));
        return viewLists.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewLists.get(position));
    }
}