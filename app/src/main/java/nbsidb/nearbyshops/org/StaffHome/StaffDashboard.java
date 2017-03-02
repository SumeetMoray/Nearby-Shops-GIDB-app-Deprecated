package nbsidb.nearbyshops.org.StaffHome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nbsidb.nearbyshops.org.DetachedTabs.DetachedTabs;
import nbsidb.nearbyshops.org.ItemsByCategorySimple.ItemCategoriesSimple;
import nbsidb.nearbyshops.org.ModelRoles.Staff;
import nbsidb.nearbyshops.org.R;
import nbsidb.nearbyshops.org.Utility.UtilityLogin;

public class StaffDashboard extends AppCompatActivity {

    @Bind(R.id.border_top) TextView borderTop;
    @Bind(R.id.header_items) TextView headerItems;

    @Bind(R.id.items_database) ImageView itemsDatabase;
    @Bind(R.id.text_items_database) TextView textItemsDatabase;

    @Bind(R.id.detached_items) ImageView detachedItems;
    @Bind(R.id.text_detached_items)TextView textDetached;


//    @Bind(R.id.border_approvals) TextView borderApprovals;
//    @Bind(R.id.header_approvals) TextView headerApprovals;

//    @Bind(R.id.shop_admin_approvals) ImageView shopAdminApprovals;
//    @Bind(R.id.text_shop_admin_approvals) TextView textShopAdminApprovals;
//
//    @Bind(R.id.shop_approvals) ImageView shopApprovals;
//    @Bind(R.id.text_shop_approvals) TextView textShopApprovals;
//
//    @Bind(R.id.end_user_approvals) ImageView endUserApprovals;
//    @Bind(R.id.text_end_user_approvals) TextView textEndUserApprovals;

    @Bind(R.id.border_bottom) TextView borderBottom;

    Staff staff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_dashboard);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        staff = UtilityLogin.getStaff(this);
        setupDashboard();
    }



    void setupDashboard()
    {
        if(!staff.getEnabled())
        {
            return;
        }

//        if(staff.isCreateUpdateItemCategory()||staff.isCreateUpdateItems())
//        {
            borderTop.setVisibility(View.VISIBLE);
            headerItems.setVisibility(View.VISIBLE);

            itemsDatabase.setVisibility(View.VISIBLE);
            textItemsDatabase.setVisibility(View.VISIBLE);

            detachedItems.setVisibility(View.VISIBLE);
            textDetached.setVisibility(View.VISIBLE);
//        }


    }



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



//    @OnClick(R.id.end_user_approvals)
    void itemCategoriesClick(View view)
    {
//        Intent intent = new Intent(this, ItemCategoriesTabs.class);
//        startActivity(intent);
    }




//    @OnClick(R.id.shop_approvals)
    void optionAdminClick(View view)
    {
//        Intent intent = new Intent(this, ShopApprovals.class);
//        startActivity(intent);
    }



//    @OnClick(R.id.shop_admin_approvals)
    void distributorAccountClick(View view)
    {
//        Intent intent = new Intent(this, ShopAdminApprovals.class);
//        startActivity(intent);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
