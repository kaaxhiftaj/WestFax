package com.business.admin.westfax.model;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.business.admin.westfax.R;
import com.business.admin.westfax.fragment.PaymentMethod;
import com.business.admin.westfax.fragment.SelcetPlanFragment;
import com.business.admin.westfax.fragment.SignupFragment;
import com.business.admin.westfax.fragment.YourProfileFragment;

/**
 * Created by SONY on 21-01-2018.
 */

public class MyPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 4;

    public MyPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages.
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for a particular page.
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SignupFragment();
            case 1:
                return new YourProfileFragment();
            case 2:
                return new SelcetPlanFragment();
            case 3:
                return new PaymentMethod();
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "Tab " + position;
    }


}
