package nbsidb.nearbyshops.org.DetachedTabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import nbsidb.nearbyshops.org.DetachedTabs.ItemCategories.DetachedItemCatFragment;
import nbsidb.nearbyshops.org.DetachedTabs.Items.DetachedItemFragment;


/**
 * Created by sumeet on 27/6/16.
 */

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class PagerAdapter extends FragmentPagerAdapter {

//    DetachedTabs activity;

    public PagerAdapter(FragmentManager fm, DetachedTabs activity) {
        super(fm);

//        this.activity = activity;
    }


//    DetachedItemCatFragment detachedItemCatFragment;
//
//    DetachedItemFragment detachedItemFragment;

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a FragmentShopAdmins (defined as a static inner class below).



        if(position == 0)
        {
//            detachedItemCatFragment = ;

//            activity.setNotificationReceiver(detachedItemCatFragment);

            return new DetachedItemCatFragment();
        }
        else if (position == 1)
        {

//            detachedItemFragment = new DetachedItemFragment();

            return new DetachedItemFragment();
        }


        return null;
    }


    @Override
    public int getCount() {
        // Show 3 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {

            case 0:
                return titleDetachedItemCategories;
            case 1:
                return titleDetachedItems;
            case 2:
                return titleDetachedItemCategories;
            case 3:
                return titleDetachedItems;
        }
        return null;
    }



    private String titleCategories = "Categories(0)";
    private String titleItems = "Items (0/0)";
    private String titleDetachedItemCategories = "Categories(0/0)";
    private String titleDetachedItems = "Items(0/0)";


    public void setTitle(String title, int tabPosition)
    {
        if(tabPosition == 0){


            titleDetachedItemCategories = title;

        }
        else if (tabPosition == 1)
        {
            titleDetachedItems = title;


        }else if(tabPosition == 2)
        {
            titleCategories = title;


        }else if(tabPosition == 3)
        {
            titleItems = title;

        }


        notifyDataSetChanged();
    }




}