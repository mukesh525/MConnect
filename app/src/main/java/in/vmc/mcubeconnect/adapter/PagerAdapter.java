package in.vmc.mcubeconnect.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import in.vmc.mcubeconnect.fragment.FragmentAll;
import in.vmc.mcubeconnect.fragment.FragmentLike;
import in.vmc.mcubeconnect.fragment.FragmentOffer;
import in.vmc.mcubeconnect.fragment.FragmentVisit;

/**
 * Created by mukesh on 18/11/15.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    private String titles[] = {"All", "Visit", "Like", "Offers",};

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment myFragment = null;
        switch (position) {
            case 0:
                myFragment = FragmentAll.newInstance("", "'");
                break;
            case 1:
                myFragment = FragmentVisit.newInstance("", "'");
                break;
            case 2:
                myFragment = FragmentLike.newInstance("", "'");
                break;
            case 3:
                myFragment = FragmentOffer.newInstance("", "'");
                break;

        }
        return myFragment;

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}