package com.iisc.consultation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.Handler;

import com.iisc.R;
import com.google.android.material.tabs.TabLayout;

public class Appointments extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        getTabs();


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void getTabs() {
        final FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager());

        new Handler().post(new Runnable() {
            @Override
            public void run() {

                fragmentPagerAdapter.addFragment(PreviousFragment.getInstance(), "Previous");
                fragmentPagerAdapter.addFragment(CurrentFragment.getInstance(), "Current");

                viewPager.setAdapter(fragmentPagerAdapter);
                tabLayout.setupWithViewPager(viewPager);
            }
        });
    }


}