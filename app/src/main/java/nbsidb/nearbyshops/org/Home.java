package nbsidb.nearbyshops.org;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import nbsidb.nearbyshops.org.DetachedTabs.DetachedTabs;
import nbsidb.nearbyshops.org.ItemsByCategorySimple.ItemCategoriesSimple;
import nbsidb.nearbyshops.org.StaffAccounts.StaffAccounts;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

//    DrawerLayout drawer;
//    NavigationView navigationView;

//    @Bind(R.id.option_saved_configuration)
//    RelativeLayout optionSavedConfiguration;

//    @Inject
//    ServiceConfigurationService configurationService;



    public Home() {

//        DaggerComponentBuilder.getInstance().getNetComponent().Inject(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.app_bar_home);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();

//        navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
    }



//    void setupFab()
//    {
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//    }



//    @OnClick(R.id.option_service_stats)
//    void optionServiceStatsClick()
//    {
//        startActivity(new Intent(this,MapsActivity.class));
//    }


    @OnClick(R.id.detached_items)
    void optionDetachedClick()
    {
        startActivity(new Intent(this, DetachedTabs.class));
    }


    @OnClick(R.id.items_database)
    void optionItemCatApprovals()
    {
        startActivity(new Intent(this, ItemCategoriesSimple.class));
    }





    @Override
    public void onBackPressed() {

        super.onBackPressed();

//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }


//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    @OnClick(R.id.staff_accounts)
    void staffAccounts()
    {
        startActivity(new Intent(this, StaffAccounts.class));
    }




//    @OnClick(R.id.service_configuration)
//    void savedConfigurationClick(View view)
//    {
//
//    }


    void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }




//    @OnClick(R.id.settings)
//    void settingsClick(View view)
//    {
//        Intent intent = new Intent(this, SettingsActivity.class);
//        startActivity(intent);
//    }



//    @OnClick(R.id.end_user_approvals)
//    void itemCategoriesClick(View view)
//    {
//        Intent intent = new Intent(this, ItemCategoriesTabs.class);
//        startActivity(intent);
//    }




//    @OnClick(R.id.shop_approvals)
//    void optionAdminClick(View view)
//    {
//        Intent intent = new Intent(this, ShopApprovals.class);
//        startActivity(intent);
//    }


//    @OnClick(R.id.staff_accounts)
//    void optionStaffClick(View view)
//    {
//        Intent intent = new Intent(this, StaffAccounts.class);
//        startActivity(intent);
//    }



//    @OnClick(R.id.shop_admin_approvals)
//    void distributorAccountClick(View view)
//    {
//        Intent intent = new Intent(this, ShopAdminApprovals.class);
//        startActivity(intent);
//    }
//

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
