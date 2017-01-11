package nbsidb.nearbyshops.org.DetachedTabs;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nbsidb.nearbyshops.org.DetachedTabs.Interfaces.NotifyAssignParent;
import nbsidb.nearbyshops.org.DetachedTabs.Interfaces.NotifyScroll;
import nbsidb.nearbyshops.org.Interfaces.NotifyTitleChanged;
import nbsidb.nearbyshops.org.R;

public class DetachedTabs extends AppCompatActivity implements NotifyTitleChanged, NotifyScroll {


    @Bind(R.id.options) RelativeLayout options;


    private PagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;


    @Bind(R.id.tablayout)
    TabLayout tabLayout;

    @Bind(R.id.tablayoutPager)
    TabLayout tabLayoutPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_categories_tabs_detached);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new PagerAdapter(getSupportFragmentManager(),this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayoutPager.setupWithViewPager(mViewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item_categories_tabs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void NotifyTitleChanged(String title, int tabPosition) {

        mSectionsPagerAdapter.setTitle(title,tabPosition);
    }






    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }




    @OnClick(R.id.changeParentBulk)
    void assignParentButtonClick()
    {

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(makeFragmentName(mViewPager.getId(),mViewPager.getCurrentItem()));

        if(fragment instanceof NotifyAssignParent)
        {
            ((NotifyAssignParent) fragment).assignParentClick();
        }



//        if(mViewPager.getCurrentItem()==0)
//        {

//            if(assignParentItemCategory!=null)
//            {
//                assignParentItemCategory.assignParentClick();
//            }

//        }

    }



    private static String makeFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
    }




    @Override
    public void scrolled(int dx, int dy) {


        if((options.getTranslationY()+dy) >= 0 && (options.getTranslationY() + dy) < options.getHeight())
        {
            options.setTranslationY(options.getTranslationY() + dy);

        }
        else if((options.getTranslationY()+dy)<0)
        {
            options.setTranslationY(0);
        }
        else if((options.getTranslationY()+dy)>options.getHeight())
        {
            options.setTranslationY(options.getHeight());
        }
    }


}
