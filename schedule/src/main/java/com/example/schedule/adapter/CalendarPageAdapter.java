package com.example.schedule.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.schedule.calendar.Constant;
import com.example.schedule.fragment.CalendarFragment;

public class CalendarPageAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = "CalendarPageAdapter";
    public CalendarPageAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return CalendarFragment.newInstance(position+1);
    }

    @Override
    public int getCount() {
        return 12;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return new String(Constant.xuhaoArray[position+1]+"月");
    }
}
