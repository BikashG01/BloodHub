package com.bloodhubnp.bikas.project.Extras;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.bloodhubnp.bikas.project.Adapters.Tab_holder_Adapter;
import com.bloodhubnp.bikas.project.R;

/**
 * Created by bikas on 8/4/2017.
 */

public class Tab_holder extends AppCompatActivity {
    private Toolbar toolbar2;
    private TabLayout tabLayout2;
    private ViewPager viewPager2;
    private String[] pageTitle = {"ALL","FRIEND","BLOOD BANK"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_holder);
        toolbar2=(Toolbar)findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);
        viewPager2=(ViewPager)findViewById(R.id.pager);
        tabLayout2=(TabLayout)findViewById(R.id.tabLayout2);
        tabLayout2.setupWithViewPager(viewPager2);
        Tab_holder_Adapter pagerAdapter = new Tab_holder_Adapter(getSupportFragmentManager());
        viewPager2.setAdapter(pagerAdapter);

        //change Tab selection when swipe ViewPager
        viewPager2.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout2));

        //change ViewPager page when tab selected
        tabLayout2.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }




}
