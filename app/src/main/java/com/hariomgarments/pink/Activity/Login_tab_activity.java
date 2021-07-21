package com.hariomgarments.pink.Activity;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.hariomgarments.pink.Adapter.Login_adapter;
import com.hariomgarments.pink.Fragments.Login_tab;
import com.hariomgarments.pink.Fragments.Registration_tab;
import com.hariomgarments.pink.R;

public class Login_tab_activity  extends AppCompatActivity {

    Login_adapter myFragmentPagerAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_tab);
        getSupportActionBar().hide();

        viewPager = (ViewPager) findViewById(R.id.View_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        setPagerAdapter();
        setTabLayout();
    }

    private void setPagerAdapter(){
        myFragmentPagerAdapter = new Login_adapter(getSupportFragmentManager());
        viewPager.setAdapter(myFragmentPagerAdapter);
    }

    private void setTabLayout() {
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("Login");
        tabLayout.getTabAt(1).setText("Sign up");
    }
}