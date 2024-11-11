package com.iisc.consultation;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.iisc.R;
import com.google.android.material.tabs.TabLayout;

public class Admin_Payments extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_payment);
        tabLayout=findViewById(R.id.tabLayout);
        viewPager=findViewById(R.id.viewPager);
        Toast.makeText(Admin_Payments.this, "Swipe to mark the payment!", Toast.LENGTH_LONG).show();
        getTabs();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void getTabs()
    {
        final FragmentPagerAdapter fragmentPagerAdapter=new FragmentPagerAdapter(getSupportFragmentManager());

        new Handler().post(new Runnable() {
            @Override
            public void run() {

                fragmentPagerAdapter.addFragment(Admin_Payments_Previous.getInstance(),"Completed");
                fragmentPagerAdapter.addFragment(Admin_Payments_Current.getInstance(),"Upcoming");

                viewPager.setAdapter(fragmentPagerAdapter);
                tabLayout.setupWithViewPager(viewPager);
            }
        });
    }
}
