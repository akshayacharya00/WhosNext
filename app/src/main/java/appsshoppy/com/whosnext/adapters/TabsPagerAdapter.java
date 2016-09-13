package appsshoppy.com.whosnext.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import appsshoppy.com.whosnext.Fragments.FragmentInspirations;
import appsshoppy.com.whosnext.Fragments.FragmentMapDetails;
import appsshoppy.com.whosnext.Fragments.FragmentServices;


public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0)
            return new FragmentServices();
        else if(position == 1)
            return new FragmentMapDetails();
        else if(position == 2)
            return new FragmentInspirations();
        return PageFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        if(position == 0)
            return "Services";
        else if(position == 1)
            return  "Map & Details";
        else if(position == 2)
            return  "Inspirations";
        else if(position == 3)
            return  "Reviews";
        return "TAB " + (position + 1);
    }
}