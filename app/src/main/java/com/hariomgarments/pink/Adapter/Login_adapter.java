package com.hariomgarments.pink.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.hariomgarments.pink.Fragments.Login_tab;
import com.hariomgarments.pink.Fragments.Registration_tab;

public class Login_adapter extends FragmentPagerAdapter {


    public Login_adapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Login_tab();
            case 1:
                return new Registration_tab();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}