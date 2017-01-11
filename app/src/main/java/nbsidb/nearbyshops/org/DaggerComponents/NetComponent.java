package nbsidb.nearbyshops.org.DaggerComponents;



import javax.inject.Singleton;

import dagger.Component;
import nbsidb.nearbyshops.org.DaggerModules.AppModule;
import nbsidb.nearbyshops.org.DaggerModules.NetModule;
import nbsidb.nearbyshops.org.ItemsByCategorySimple.EditItem.EditItemFragment;
import nbsidb.nearbyshops.org.ItemsByCategorySimple.EditItemCategory.EditItemCategoryFragment;
import nbsidb.nearbyshops.org.ItemsByCategorySimple.ItemCategoriesFragmentSimple;
import nbsidb.nearbyshops.org.LoginScreen;
import nbsidb.nearbyshops.org.SelectParent.ItemCategoriesParent;
import nbsidb.nearbyshops.org.SelectParent.ItemCategoriesParentAdapter;

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


//    void Inject(LoginDialog loginDialog);
}
