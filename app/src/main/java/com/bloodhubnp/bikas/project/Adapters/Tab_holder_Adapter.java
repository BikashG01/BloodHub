package com.bloodhubnp.bikas.project.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bloodhubnp.bikas.project.Activites.All;
import com.bloodhubnp.bikas.project.Activites.BloodBank;
import com.bloodhubnp.bikas.project.Activites.Friend;

/**
 * Created by bikas on 8/4/2017.
 */



public class Tab_holder_Adapter extends FragmentStatePagerAdapter {
    private String[] tab_titles= new String[]{"ALL","FRIEND","BLOOD BANK"};
    public Tab_holder_Adapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tab_titles[position];
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0: {
                return new All();

            }
            case 1: {
                return new Friend();

            }
            case 2: {
                return new BloodBank();

            }
            default:{
                return null;
            }
        }
    }




    @Override
    public int getCount() {
        return tab_titles.length;
    }
}
