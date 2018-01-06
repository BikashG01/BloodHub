package com.bloodhubnp.bikas.project.Activites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bloodhubnp.bikas.project.Extras.Notification;
import com.bloodhubnp.bikas.project.R;
import com.bloodhubnp.bikas.project.Adapters.ViewPagerAdapter;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String Name = "nameKey";
    public static final String Phone = "phoneKey";
    private CircleImageView donar_image;
    private TextView donar_name, donar_blood;
    private ViewPager viewPager;
    private DrawerLayout drawer;
    private TabLayout tabLayout;
    private String[] pageTitle = {"SEARCH DONOR", "INVITE"};
    private String phone_number;
    private String name, name_s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerview = navigationView.getHeaderView(0);
        donar_image = (CircleImageView) headerview.findViewById(R.id.DonorImage);
        donar_blood = (TextView) headerview.findViewById(R.id.details);
        donar_name = (TextView) headerview.findViewById(R.id.demo_name);

        Bundle bundle = getIntent().getExtras();
        String image_url = bundle.getString("user_image");
        if (donar_image.getDrawable() == null) {
            Picasso.with(this)
                    .load(image_url)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .placeholder(R.drawable.ic_user)
                    .error(R.drawable.ic_user)
                    .into(donar_image);

        }

        donar_name.setText(bundle.getString("user_name"));
        donar_blood.setText(bundle.getString("user_bloodgroup"));
        phone_number = bundle.getString("phone_number");
        name = bundle.getString("user_name");
        Log.e("NAME", name);
        Log.e("PHONE_NUMBER", phone_number);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        setSupportActionBar(toolbar);
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(Name, name);
        edit.putString(Phone, phone_number);
        edit.commit();
        Log.e("SHaredPerference Name", Name);
        Log.e("SharedPerference PHONE", Phone);

        //create default navigation drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //setting Tab layout (number of Tabs = number of ViewPager pages)
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        for (int i = 0; i < 2; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(pageTitle[i]));
        }

        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        //set viewpager adapter
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        //change Tab selection when swipe ViewPager
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //change ViewPager page when tab selected
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);

        }

    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.Notification) {
            Intent intent = new Intent(MainActivity.this, Notification.class);
            startActivity(intent);
        } else if (id == R.id.friend_circle) {
            Intent intent = new Intent(MainActivity.this, Friendlist.class);
            Bundle bundle = new Bundle();
            bundle.putString("name", name);
            bundle.putString("number", phone_number);
            Log.e("HAMRO NAME", name);
            Log.e("HAMRO NUMBER", phone_number);
            intent.putExtras(bundle);

            startActivity(intent);

        } else if (id == R.id.profile) {

        } else if (id == R.id.log_out) {
            SharedPreferences sharedPreferences = getSharedPreferences(Login.SHAREDPREP, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();

            Intent intent = new Intent(MainActivity.this, Login.class);
            finish();
            startActivity(intent);

        } else if (id == R.id.help) {
            Intent intent = new Intent(MainActivity.this, Help.class);
            startActivity(intent);

        }
        return false;
    }
}
