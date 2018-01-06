package com.bloodhubnp.bikas.project.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bloodhubnp.bikas.project.Activites.Search_Blood;
import com.bloodhubnp.bikas.project.Activites.Invite_Friends;

/**
 * Created by bikas on 7/8/2017.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter{
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position ==0) {
            return new Search_Blood();
        } else {
            return new Invite_Friends();
        }
    }






    @Override
    public int getCount() {
        return 2;
    }
}
