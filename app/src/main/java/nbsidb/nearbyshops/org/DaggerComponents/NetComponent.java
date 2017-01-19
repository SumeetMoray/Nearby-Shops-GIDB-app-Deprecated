package nbsidb.nearbyshops.org.DaggerComponents;



import javax.inject.Singleton;

import dagger.Component;
import nbsidb.nearbyshops.org.AddFromGlobalSelection.FragmentAddFromGlobal;
import nbsidb.nearbyshops.org.DaggerModules.AppModule;
import nbsidb.nearbyshops.org.DaggerModules.NetModule;
import nbsidb.nearbyshops.org.DetachedTabs.ItemCategories.DetachedItemCatAdapter;
import nbsidb.nearbyshops.org.DetachedTabs.ItemCategories.DetachedItemCatFragment;
import nbsidb.nearbyshops.org.DetachedTabs.Items.DetachedItemAdapter;
import nbsidb.nearbyshops.org.DetachedTabs.Items.DetachedItemFragment;
import nbsidb.nearbyshops.org.EditProfileAdmin.EditAdminFragment;
import nbsidb.nearbyshops.org.ItemsByCategorySimple.AdapterSimple;
import nbsidb.nearbyshops.org.ItemsByCategorySimple.EditItem.EditItemFragment;
import nbsidb.nearbyshops.org.ItemsByCategorySimple.EditItemCategory.EditItemCategoryFragment;
import nbsidb.nearbyshops.org.ItemsByCategorySimple.ItemCategoriesFragmentSimple;
import nbsidb.nearbyshops.org.LoginScreen;
import nbsidb.nearbyshops.org.SelectParent.ItemCategoriesParent;
import nbsidb.nearbyshops.org.SelectParent.ItemCategoriesParentAdapter;
import nbsidb.nearbyshops.org.StaffAccounts.EditStaff.EditStaffFragment;
import nbsidb.nearbyshops.org.StaffAccounts.FragmentStaffAccounts;
import nbsidb.nearbyshops.org.StaffHome.EditStaffSelf.EditStaffSelfFragment;
import nbsidb.nearbyshops.org.StaffHome.StaffHome;

/**
 * Created by sumeet on 14/5/16.
 */

@Singleton
@Component(modules={AppModule.class, NetModule.class})
public interface NetComponent {

    void Inject(ItemCategoriesFragmentSimple itemCategoriesFragmentSimple);

    void Inject(ItemCategoriesParentAdapter itemCategoriesParentAdapter);

    void Inject(ItemCategoriesParent itemCategoriesParent);

    void Inject(EditItemFragment editItemFragment);

    void Inject(EditItemCategoryFragment editItemCategoryFragment);

    void Inject(LoginScreen loginScreen);

    void Inject(FragmentStaffAccounts fragmentStaffAccounts);

    void Inject(EditStaffFragment editStaffFragment);

    void Inject(DetachedItemCatAdapter detachedItemCatAdapter);

    void Inject(DetachedItemCatFragment detachedItemCatFragment);

    void Inject(DetachedItemAdapter detachedItemAdapter);

    void Inject(DetachedItemFragment detachedItemFragment);

    void Inject(EditStaffSelfFragment editStaffSelfFragment);

    void Inject(StaffHome staffHome);

    void Inject(FragmentAddFromGlobal fragmentAddFromGlobal);

    void Inject(AdapterSimple adapterSimple);

    void Inject(EditAdminFragment editAdminFragment);


//    void Inject(LoginDialog loginDialog);
}
