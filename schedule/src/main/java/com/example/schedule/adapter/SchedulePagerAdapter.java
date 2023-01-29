package com.example.schedule.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.schedule.calendar.Constant;
import com.example.schedule.fragment.ScheduleFragment;

public class SchedulePagerAdapter extends FragmentStatePagerAdapter {
    public SchedulePagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return ScheduleFragment.newInstance(position+1);
    }

    @Override
    public int getCount() {
        return 52;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return new String("第"+ Constant.xuhaoArray[position+1]+"周");
    }
}
