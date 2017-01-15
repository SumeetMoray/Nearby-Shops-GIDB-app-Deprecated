package nbsidb.nearbyshops.org.ItemsByCategorySimple;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import nbsidb.nearbyshops.org.AddFromGlobalSelection.AddFromGlobal;
import nbsidb.nearbyshops.org.DaggerComponentBuilder;
import nbsidb.nearbyshops.org.ItemsByCategorySimple.EditItem.EditItem;
import nbsidb.nearbyshops.org.ItemsByCategorySimple.EditItem.EditItemFragment;
import nbsidb.nearbyshops.org.ItemsByCategorySimple.EditItemCategory.EditItemCategory;
import nbsidb.nearbyshops.org.ItemsByCategorySimple.EditItemCategory.EditItemCategoryFragment;
import nbsidb.nearbyshops.org.ItemsByCategorySimple.Interfaces.NotifyBackPressed;
import nbsidb.nearbyshops.org.ItemsByCategorySimple.Interfaces.NotifyFABClick;
import nbsidb.nearbyshops.org.ItemsByCategorySimple.Interfaces.NotifyHeaderChanged;
import nbsidb.nearbyshops.org.ItemsByCategorySimple.Interfaces.NotifySort;
import nbsidb.nearbyshops.org.ItemsByCategorySimple.Interfaces.ToggleFab;
import nbsidb.nearbyshops.org.ItemsByCategorySimple.Utility.HeaderItemsList;
import nbsidb.nearbyshops.org.ItemsByCategorySimple.Utility.UtilitySortItemsByCategory;
import nbsidb.nearbyshops.org.Model.Item;
import nbsidb.nearbyshops.org.Model.ItemCategory;
import nbsidb.nearbyshops.org.ModelEndpoint.ItemCategoryEndPoint;
import nbsidb.nearbyshops.org.ModelEndpoint.ItemEndPoint;
import nbsidb.nearbyshops.org.R;
import nbsidb.nearbyshops.org.RetrofitRESTContract.ItemCategoryService;
import nbsidb.nearbyshops.org.RetrofitRESTContract.ItemService;
import nbsidb.nearbyshops.org.SelectParent.ItemCategoriesParent;
import nbsidb.nearbyshops.org.Utility.UtilityLogin;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 2/12/16.
 */

public class ItemCategoriesFragmentSimple extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AdapterSimple.NotificationsFromAdapter , NotifyBackPressed, NotifySort,NotifyFABClick {

    boolean isDestroyed = false;
    boolean show = true;

    boolean isFirstChangeParent = true;
    boolean isFirst = true;
//    @State int previous_position = -1;

    int item_count_item_category = 0;

    private int limit_item = 10;
    int offset_item = 0;
    int item_count_item;
    int fetched_items_count = 0;

    @Bind(R.id.swipe_container) SwipeRefreshLayout swipeContainer;
    @Bind(R.id.recycler_view) RecyclerView itemCategoriesList;

    ArrayList<Object> dataset = new ArrayList<>();
    ArrayList<ItemCategory> datasetCategory = new ArrayList<>();
    ArrayList<Item> datasetItems = new ArrayList<>();


    GridLayoutManager layoutManager;

    AdapterSimple listAdapter;

    ItemCategory changeParentRequestedItemCat;
    Item changeParentRequestedItem;

    @Inject
    ItemCategoryService itemCategoryService;


    @Inject
    ItemService itemService;


    ItemCategory currentCategory = null;


    public ItemCategoriesFragmentSimple() {
        super();

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

        currentCategory = new ItemCategory();
        currentCategory.setItemCategoryID(1);
        currentCategory.setCategoryName("");
        currentCategory.setParentCategoryID(-1);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_item_categories_simple, container, false);

        ButterKnife.bind(this,rootView);


        if(savedInstanceState ==null)
        {
            makeRefreshNetworkCall();
        }


        setupRecyclerView();
        setupSwipeContainer();
        notifyItemHeaderChanged();
        return rootView;
    }



    void setupSwipeContainer()
    {

        if(swipeContainer!=null) {

            swipeContainer.setOnRefreshListener(this);
            swipeContainer.setColorSchemeResources(
                    android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }

    }



    void setupRecyclerView()
    {

        listAdapter = new AdapterSimple(dataset,getActivity(),this);
        itemCategoriesList.setAdapter(listAdapter);

        layoutManager = new GridLayoutManager(getActivity(),6, LinearLayoutManager.VERTICAL,false);
        itemCategoriesList.setLayoutManager(layoutManager);



        // Code for Staggered Grid Layout
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {


            @Override
            public int getSpanSize(int position) {

                if(dataset.get(position) instanceof ItemCategory)
                {

                    final DisplayMetrics metrics = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

                    int spanCount = (int) (metrics.widthPixels/(180 * metrics.density));

                    if(spanCount==0){
                        spanCount = 1;
                    }

                    return (6/spanCount);

                }
                else if(dataset.get(position) instanceof Item)
                {

                    return 6;
                }
                else if(dataset.get(position) instanceof HeaderItemsList)
                {
                    return 6;
                }

                return 3;
            }
        });


        final DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

//        layoutManager.setSpanCount(metrics.widthPixels/350);


//        int spanCount = (int) (metrics.widthPixels/(150 * metrics.density));
//
//        if(spanCount==0){
//            spanCount = 1;
//        }

//        layoutManager.setSpanCount(spanCount);


        /*final DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int spanCount = (int) (metrics.widthPixels/(180 * metrics.density));

        if(spanCount==0){
            spanCount = 1;
        }

        return (3/spanCount);*/


        itemCategoriesList.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                /*if(layoutManager.findLastVisibleItemPosition()==dataset.size()-1)
                {
                    // trigger fetch next page

                    if((offset_item + limit_item)<=item_count_item)
                    {
                        offset_item = offset_item + limit_item;

                        makeRequestItem(false,false);
                    }

                }
*/


                if(layoutManager.findLastVisibleItemPosition()==dataset.size()-1)
                {

                    // trigger fetch next page

//                    if(layoutManager.findLastVisibleItemPosition()== previous_position)
//                    {
//                        return;
//                    }


                    // trigger fetch next page

                    if((offset_item+limit_item)<=item_count_item)
                    {
                        offset_item = offset_item + limit_item;

                        makeRequestItem(false,false);
                    }

//                    previous_position = layoutManager.findLastVisibleItemPosition();

                }
            }




            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                if(dy > 20)
                {

                    boolean previous = show;

                    show = false ;

                    if(show!=previous)
                    {
                        // changed
                        Log.d("scrolllog","show");

                        if(getActivity() instanceof ToggleFab)
                        {
                            ((ToggleFab)getActivity()).hideFab();
                        }
                    }

                }else if(dy < -20)
                {

                    boolean previous = show;

                    show = true;

                    if(show!=previous)
                    {
                        Log.d("scrolllog","hide");

                        if(getActivity() instanceof ToggleFab)
                        {
                            ((ToggleFab)getActivity()).showFab();
                        }
                    }
                }


            }


        });

    }






    @Override
    public void onRefresh() {

        System.out.println("OnRefresh:ItemsByCat Simple : Fired !");
        makeRequestItemCategory();
        makeRequestItem(true,true);
    }


    void makeRefreshNetworkCall()
    {
        swipeContainer.post(new Runnable() {
            @Override
            public void run() {

                swipeContainer.setRefreshing(true);
                onRefresh();
            }
        });

    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isDestroyed = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        isDestroyed=false;
    }

    private void showToastMessage(String message)
    {
        if(getActivity()!=null)
        {
            Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
        }
    }






    void makeRequestItemCategory()
    {


        Call<ItemCategoryEndPoint> endPointCall = itemCategoryService.getItemCategoriesQuerySimple(
                currentCategory.getItemCategoryID(),null,ItemCategory.CATEGORY_ORDER,null,null
        );

        //"id"

//        Call<ItemCategoryEndPoint> endPointCall = itemCategoryService.getItemCategoriesEndPoint(
//                null,
//                currentCategory.getItemCategoryID(),
//                null,
//                null,
//                null,
//                null,null,null,
//                "ITEM_CATEGORY_NAME",null,null,false);


        endPointCall.enqueue(new Callback<ItemCategoryEndPoint>() {
            @Override
            public void onResponse(Call<ItemCategoryEndPoint> call, Response<ItemCategoryEndPoint> response) {

                if(isDestroyed)
                {
                    return;
                }

                if(response.body()!=null)
                {

                    ItemCategoryEndPoint endPoint = response.body();
                    item_count_item_category = endPoint.getItemCount();

                    datasetCategory.clear();
                    datasetCategory.addAll(endPoint.getResults());
                }


                if(isFirst)
                {
                    isFirst = false;
                }
                else
                {
                    // is last
                    refreshAdapter();
                    isFirst = true;// reset the flag
                }


//                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ItemCategoryEndPoint> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }

                showToastMessage("Network request failed. Please check your connection !");


                if(isFirst)
                {
                    isFirst = false;
                }
                else
                {
                    // is last
                    refreshAdapter();
                    isFirst = true;// reset the flag
                }



//                if(swipeContainer!=null)
//                {
//                    swipeContainer.setRefreshing(false);
//                }

            }
        });
    }



    void refreshAdapter()
    {
        dataset.clear();

        HeaderItemsList headerItemCategory = new HeaderItemsList();
        headerItemCategory.setHeading(currentCategory.getCategoryName() + " Subcategories");
        dataset.add(headerItemCategory);

        dataset.addAll(datasetCategory);

        HeaderItemsList headerItem = new HeaderItemsList();
        headerItem.setHeading(currentCategory.getCategoryName() + " Items");
        dataset.add(headerItem);

        dataset.addAll(datasetItems);

        listAdapter.notifyDataSetChanged();

        swipeContainer.setRefreshing(false);
    }




    void makeRequestItem(final boolean clearDataset, boolean resetOffset)
    {

        if(resetOffset)
        {
            offset_item = 0;
        }


        String current_sort = "";

        current_sort = UtilitySortItemsByCategory.getSort(getContext()) + " " + UtilitySortItemsByCategory.getAscending(getContext());

//        Call<ItemEndPoint> endPointCall = itemService.getItemsEndpoint(currentCategory.getItemCategoryID(),
//                null,
//                null,
//                null,
//                null,null, null, null,
//                current_sort, limit_item,offset_item,null);


        Call<ItemEndPoint> endPointCall = itemService.getItemsOuterJoin(currentCategory.getItemCategoryID(),
                null, limit_item,offset_item, null);


        endPointCall.enqueue(new Callback<ItemEndPoint>() {
            @Override
            public void onResponse(Call<ItemEndPoint> call, Response<ItemEndPoint> response) {


                if(isDestroyed)
                {
                    return;
                }


                if(clearDataset)
                {

                    if(response.body()!=null)
                    {

                        datasetItems.clear();
                        datasetItems.addAll(response.body().getResults());
                        item_count_item = response.body().getItemCount();
                        fetched_items_count = datasetItems.size();

//                        if(response.body().getItemCount()!=null)
//                        {
//
//                        }
                    }


                    if(isFirst)
                    {
                        isFirst = false;
                    }
                    else
                    {
                        // is last
                        refreshAdapter();
                        isFirst = true;// reset the flag
                    }

                }
                else
                {
                    if(response.body()!=null)
                    {

                        dataset.addAll(response.body().getResults());
                        fetched_items_count = fetched_items_count + response.body().getResults().size();
                        item_count_item = response.body().getItemCount();
                        listAdapter.notifyDataSetChanged();
                    }

                    swipeContainer.setRefreshing(false);
                }


                notifyItemHeaderChanged();


            }

            @Override
            public void onFailure(Call<ItemEndPoint> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }


                if(clearDataset)
                {

                    if(isFirst)
                    {
                        isFirst = false;
                    }
                    else
                    {
                        // is last
                        refreshAdapter();
                        isFirst = true;// reset the flag
                    }
                }
                else
                {
                    swipeContainer.setRefreshing(false);
                }


                showToastMessage("Items: Network request failed. Please check your connection !");

            }
        });

    }





    @Override
    public void notifyItemCategorySelected() {
        if(getActivity() instanceof ToggleFab)
        {
            ((ToggleFab)getActivity()).showFab();
            show=true;
        }

    }

    @Override
    public void notifyItemSelected() {

        if(getActivity() instanceof ToggleFab)
        {
            ((ToggleFab)getActivity()).showFab();
            show=true;
        }
    }

    @Override
    public void detachItemCat(final ItemCategory itemCategory) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Confirm Detach Item Categories !")
                .setMessage("Are you sure you want to detach selected category ? ")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        itemCategory.setParentCategoryID(-1);
                        makeRequestUpdateItemCat(itemCategory);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        showToastMessage("Cancelled !");

                    }
                })
                .show();
    }



    @Override
    public void notifyDeleteItemCat(ItemCategory itemCategory, final int position) {

        Call<ResponseBody> call = itemCategoryService.deleteItemCategory(
                UtilityLogin.getAuthorizationHeaders(getActivity()),
                itemCategory.getItemCategoryID()
        );

        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if(response.code()==200)
                {

                    showToastMessage("Removed !");
                    dataset.remove(position);
                    listAdapter.notifyItemRemoved(position);

                }else if(response.code()==304)
                {
                    showToastMessage("Delete failed !");

                }else
                {
                    showToastMessage("Server Error !");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Network request failed ! Please check your connection!");
            }
        });

    }



    @Override
    public void notifyDeleteItem(Item item, final int position) {

        Call<ResponseBody> call = itemService.deleteItem(
                UtilityLogin.getAuthorizationHeaders(getActivity()),
                item.getItemID()
        );


        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if(response.code()==200)
                {
                    dataset.remove(position);
                    listAdapter.notifyItemRemoved(position);
                    showToastMessage("Removed !");

                }else if(response.code()==304)
                {
                    showToastMessage("Delete failed !");

                }else
                {
                    showToastMessage("Server Error !");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Network request failed ! Please check your connection!");
            }
        });

    }



    @Override
    public void changeParentItemCat(ItemCategory itemCategory) {


        Intent intentParent = new Intent(getActivity(), ItemCategoriesParent.class);

//        requestedChangeParent = dataset.get(getLayoutPosition());

        changeParentRequestedItemCat = itemCategory;


        // add the selected item category in the exclude list so that it does not get showed up as an option.
        // This is required to prevent an item category to assign itself or its children as its parent.
        // This should not happen because it would be erratic.

        ItemCategoriesParent.clearExcludeList(); // it is a safe to clear the list before adding any items in it.
        ItemCategoriesParent.excludeList
                .put(itemCategory.getItemCategoryID(),itemCategory);

        startActivityForResult(intentParent,1,null);

    }

    @Override
    public void detachItem(final Item item) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Confirm Detach Item Categories !")
                .setMessage("Are you sure you want to detach selected category ? ")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        item.setItemCategoryID(-1);
//                        makeRequestUpdate(itemCategory);
                        makeUpdateRequestItem(item);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showToastMessage("Cancelled !");
                    }
                })
                .show();
    }


    @Override
    public void changeParentItem(Item item) {

        Intent intentParent = new Intent(getActivity(), ItemCategoriesParent.class);

        changeParentRequestedItem = item;

//        requestedChangeParent = dataset.get(getLayoutPosition());

        // add the selected item category in the exclude list so that it does not get showed up as an option.
        // This is required to prevent an item category to assign itself or its children as its parent.
        // This should not happen because it would be erratic.

        ItemCategoriesParent.clearExcludeList(); // it is a safe to clear the list before adding any items in it.

//                    ItemCategoriesParent.excludeList
//                            .put(requestedChangeParent.getItemID(),requestedChangeParent);

        startActivityForResult(intentParent,3,null);

    }


    @Override
    public void notifyRequestSubCategory(ItemCategory itemCategory) {

        ItemCategory temp = currentCategory;
        currentCategory = itemCategory;
        currentCategory.setParentCategory(temp);
        makeRefreshNetworkCall();
    }

    @Override
    public boolean backPressed() {

        int currentCategoryID = 1; // the ID of root category is always supposed to be 1

        if(currentCategory!=null) {


            if (currentCategory.getParentCategory() != null) {

                currentCategory = currentCategory.getParentCategory();
                currentCategoryID = currentCategory.getItemCategoryID();

            } else {
                currentCategoryID = currentCategory.getParentCategoryID();
            }


            if (currentCategoryID != -1) {

                makeRefreshNetworkCall();
            }
        }

        return currentCategoryID == -1;
    }


    void notifyItemHeaderChanged()
    {
        if(getActivity() instanceof NotifyHeaderChanged)
        {
            ((NotifyHeaderChanged) getActivity()).notifyItemHeaderChanged(String.valueOf(fetched_items_count) + " out of " + String.valueOf(item_count_item) + " " + currentCategory.getCategoryName() + " Items");
        }
    }


    @Override
    public void notifySortChanged() {
        makeRefreshNetworkCall();
    }


    @Override
    public void detachSelectedClick() {

//        showToastMessage("Detach");
        detachedSelectedDialog();
    }


    void detachedSelectedDialog()
    {

        if(listAdapter.selectedItems.size()==0&& listAdapter.selectedItemCategories.size()==0)
        {
            showToastMessage("No item selected. Please make a selection !");

            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Confirm Detach Item Categories !")
                .setMessage("Are you sure you want to remove / detach parent for the selected Categories ? ")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        detachSelected();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showToastMessage("Cancelled !");
                    }
                })
                .show();
    }

    void detachSelected()
    {

        /*if(listAdapter.selectedItems.size()==0)
        {
            showToastMessage("No item selected. Please make a selection !");

            return;
        }*/

        List<Item> tempList = new ArrayList<>();

        for(Map.Entry<Integer,Item> entry : listAdapter.selectedItems.entrySet())
        {
            entry.getValue().setItemCategoryID(-1);
            tempList.add(entry.getValue());
        }

//        makeRequestUpdateBulk(tempList);
        makeUpdateRequestItemsBulk(tempList);



        List<ItemCategory> tempListItemCat = new ArrayList<>();

        for(Map.Entry<Integer,ItemCategory> entry : listAdapter.selectedItemCategories.entrySet())
        {
            entry.getValue().setParentCategoryID(-1);
            tempListItemCat.add(entry.getValue());
        }

        makeRequestUpdateItemCatBulk(tempListItemCat);

    }



    @Override
    public void changeParentForSelected() {
//        showToastMessage("Change Parent");
        changeParentBulk();
    }

    @Override
    public void addItem() {
//        showToastMessage("add item");

        Intent intent = new Intent(getActivity(),EditItem.class);
        intent.putExtra(EditItemFragment.EDIT_MODE_INTENT_KEY,EditItemFragment.MODE_ADD);
        intent.putExtra(EditItemFragment.ITEM_CATEGORY_INTENT_KEY,currentCategory);
        startActivity(intent);
    }

    @Override
    public void addfromGlobal() {
        Intent intent  = new Intent(getActivity(), AddFromGlobal.class);
        intent.putExtra(AddFromGlobal.INTENT_KEY_ITEM_CAT_PARENT,currentCategory);
        startActivity(intent);
    }


    @Override
    public void addItemCategory() {
//        showToastMessage("add item cat !");

        Intent intent = new Intent(getActivity(),EditItemCategory.class);
        intent.putExtra(EditItemCategoryFragment.ITEM_CATEGORY_INTENT_KEY,currentCategory);
        intent.putExtra(EditItemCategoryFragment.EDIT_MODE_INTENT_KEY, EditItemCategoryFragment.MODE_ADD);
        startActivity(intent);
    }




    void changeParentBulk()
    {

        if(listAdapter.selectedItemCategories.size()==0 && listAdapter.selectedItems.size()==0)
        {
            showToastMessage("No item selected. Please make a selection !");

            return;
        }

        // make an exclude list. Put selected items to an exclude list. This is done to preven a category to make itself or its
        // children its parent. This is logically incorrect and should not happen.

        ItemCategoriesParent.clearExcludeList();
        ItemCategoriesParent.excludeList.putAll(listAdapter.selectedItemCategories);

        Intent intentParent = new Intent(getActivity(), ItemCategoriesParent.class);
        startActivityForResult(intentParent,2,null);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);




        if(requestCode == 3)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                ItemCategory parentCategory = data.getParcelableExtra("result");

                if(parentCategory!=null)
                {

                    if(parentCategory.getAbstractNode())
                    {
                        showToastMessage(parentCategory.getCategoryName()
                                + " is an abstract category you cannot add item to an abstract category");
                        return;
                    }

//                    listAdapter.getRequestedChangeParent().setParentCategoryID(parentCategory.getItemCategoryID());

//                    listAdapter.getRequestedChangeParent().setItemCategoryID(parentCategory.getItemCategoryID());
//                    makeUpdateRequest(listAdapter.getRequestedChangeParent());


                    changeParentRequestedItem.setItemCategoryID(parentCategory.getItemCategoryID());
                    makeUpdateRequestItem(changeParentRequestedItem);


                }
            }
        }




        if(requestCode == 1)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                ItemCategory parentCategory = data.getParcelableExtra("result");

                if(parentCategory!=null)
                {

                    changeParentRequestedItemCat.setParentCategoryID(parentCategory.getItemCategoryID());
                    makeRequestUpdateItemCat(changeParentRequestedItemCat);

                }
            }
        }


        if(requestCode == 2)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                ItemCategory parentCategory = data.getParcelableExtra("result");

                if(parentCategory!=null)
                {

                    // update Item Categories
                    List<ItemCategory> tempList = new ArrayList<>();

                    for(Map.Entry<Integer,ItemCategory> entry : listAdapter.selectedItemCategories.entrySet())
                    {
                        entry.getValue().setParentCategoryID(parentCategory.getItemCategoryID());
                        tempList.add(entry.getValue());
                    }

                    makeRequestUpdateItemCatBulk(tempList);




                    // update Items
                    if(parentCategory.getAbstractNode())
                    {
                        showToastMessage(parentCategory.getCategoryName()
                                + " is an abstract category you cannot add item to an abstract category");

                        return;
                    }

                    List<Item> tempListItems = new ArrayList<>();

                    for(Map.Entry<Integer,Item> entry : listAdapter.selectedItems.entrySet())
                    {
                        entry.getValue().setItemCategoryID(parentCategory.getItemCategoryID());
                        tempListItems.add(entry.getValue());
                    }

                    makeUpdateRequestItemsBulk(tempListItems);

                }

            }
        }


    }// on activity result ends




    void makeRequestUpdateItemCat(ItemCategory itemCategory)
    {
        Call<ResponseBody> call = itemCategoryService.changeParent(UtilityLogin.getAuthorizationHeaders(getContext()),
                itemCategory,itemCategory.getItemCategoryID());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if(isDestroyed)
                {
                    return;
                }

                if(response.code() == 200)
                {
                    showToastMessage("Successful !");

                    makeRefreshNetworkCall();

                }else
                {
                    showToastMessage("Failed Code : " + String.valueOf(response.code()));
                }

//                listAdapter.setRequestedChangeParent(null);

                changeParentRequestedItemCat=null;
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }

                showToastMessage("Network request failed. Please check your connection !");

//                listAdapter.setRequestedChangeParent(null);
                changeParentRequestedItemCat=null;

            }
        });
    }





    void makeRequestUpdateItemCatBulk(final List<ItemCategory> list)
    {
        Call<ResponseBody> call = itemCategoryService.changeParentBulk(UtilityLogin.getAuthorizationHeaders(getActivity()),
                list);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if(isDestroyed)
                {
                    return;
                }

                if(response.code() == 200)
                {
                    showToastMessage("Update Successful !");

                    clearSelectedItemCat();

                }else if (response.code() == 206)
                {
                    showToastMessage("Partially Updated. Check for data changes !");

                    clearSelectedItemCat();

                }else if(response.code() == 304)
                {

                    showToastMessage("No item updated !");

                }else
                {
                    showToastMessage("Unknown server error or response !");
                }


                /*dataset.clear();
                offset = 0 ; // reset the offset
                makeRequestRetrofit(false);*/



                if(isFirstChangeParent)
                {
                    isFirstChangeParent = false;
                }
                else
                {
                    // is last
                    makeRefreshNetworkCall();
                    isFirstChangeParent = true;// reset the flag
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }

                showToastMessage("Network Request failed. Check your internet / network connection !");

                if(isFirstChangeParent)
                {
                    isFirstChangeParent = false;
                }
                else
                {
                    // is last

                    makeRefreshNetworkCall();
                    isFirstChangeParent = true;// reset the flag
                }

            }
        });

    }


    void clearSelectedItemCat()
    {
        // clear the selected items
        listAdapter.selectedItemCategories.clear();
    }





    void makeUpdateRequestItem(Item item)
    {

//        Call<ResponseBody> call2 = itemCategoryService.updateItemCategory(itemCategory,itemCategory.getItemCategoryID());

        Call<ResponseBody> call = itemService.changeParent(UtilityLogin.getAuthorizationHeaders(getContext()),
                item,item.getItemID());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code() == 200)
                {
                    showToastMessage("Change Parent Successful !");

                    onRefresh();

                }else
                {
                    showToastMessage("Change Parent Failed !");
                }

//                listAdapter.setRequestedChangeParent(null);
                changeParentRequestedItem=null;
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Network request failed. Please check your connection !");

//                listAdapter.setRequestedChangeParent(null);
                changeParentRequestedItem=null;

            }
        });

    }




    void makeUpdateRequestItemsBulk(final List<Item> list)
    {
//        Call<ResponseBody> call = itemService.updateItemCategoryBulk(list);

        Call<ResponseBody> call = itemService.changeParentBulk(UtilityLogin.getAuthorizationHeaders(getContext()),
                list);
//        Call<ResponseBody> call = null;
//
//
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(isDestroyed)
                {
                    return;
                }


                if(response.code() == 200)
                {
                    showToastMessage("Update Successful !");

                    clearSelectedItems();

                }else if (response.code() == 206)
                {
                    showToastMessage("Partially Updated. Check data changes !");

                    clearSelectedItems();

                }else if(response.code() == 304)
                {

                    showToastMessage("No item updated !");

                }else
                {
                    showToastMessage("Unknown server error or response !");
                }


//                makeRequestRetrofit();



                if(isFirstChangeParent)
                {
                    isFirstChangeParent = false;
                }
                else
                {
                    // is last
                    makeRefreshNetworkCall();
                    isFirstChangeParent = true;// reset the flag
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }


                showToastMessage("Network Request failed. Check your internet / network connection !");

                if(isFirstChangeParent)
                {
                    isFirstChangeParent = false;
                }
                else
                {
                    // is last

                    makeRefreshNetworkCall();
                    isFirstChangeParent = true;// reset the flag
                }


            }
        });

    }


    void clearSelectedItems()
    {
        // clear the selected items
        listAdapter.selectedItems.clear();
    }
}
