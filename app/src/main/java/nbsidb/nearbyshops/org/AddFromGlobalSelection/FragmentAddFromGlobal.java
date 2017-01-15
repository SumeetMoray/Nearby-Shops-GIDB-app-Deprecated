package nbsidb.nearbyshops.org.AddFromGlobalSelection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import nbsidb.nearbyshops.org.DaggerComponentBuilder;
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
import nbsidb.nearbyshops.org.RetrofitRESTContractGIDB.ItemCategoryServiceGIDB;
import nbsidb.nearbyshops.org.RetrofitRESTContractGIDB.ItemServiceGIDB;
import nbsidb.nearbyshops.org.Utility.UtilityGeneral;
import nbsidb.nearbyshops.org.Utility.UtilityLogin;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 2/12/16.
 */

public class FragmentAddFromGlobal extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AdapterAddFromGlobal.NotificationsFromAdapter , NotifyBackPressed, NotifySort,NotifyFABClickAddFromGlobal{

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

    AdapterAddFromGlobal listAdapter;

    ItemCategory changeParentRequestedItemCat;
    Item changeParentRequestedItem;


    @Inject ItemCategoryServiceGIDB itemCategoryService;
    @Inject ItemServiceGIDB itemServiceGIDB;
    @Inject ItemService itemService;
    @Inject ItemCategoryService itemCatService;

    ItemCategory currentCategory = null;


    public FragmentAddFromGlobal() {

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
        View rootView = inflater.inflate(R.layout.fragment_add_from_global, container, false);

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

        listAdapter = new AdapterAddFromGlobal(dataset,getActivity(),this);
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


        Call<ItemEndPoint> endPointCall = itemServiceGIDB.getItemsOuterJoin(currentCategory.getItemCategoryID(),
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
    public void notifyDeleteItemCat() {

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




    void clearSelectedItemCat()
    {
        // clear the selected items
        listAdapter.selectedItemCategories.clear();
    }


    void clearSelectedItems()
    {
        // clear the selected items
        listAdapter.selectedItems.clear();
    }




    @Override
    public void copySelected() {

        copyItems();
        copyItemCat();
    }

    void copyItemCat()
    {

        ItemCategory parent = getActivity().getIntent().getParcelableExtra(AddFromGlobal.INTENT_KEY_ITEM_CAT_PARENT);

        List<ItemCategory> tempList = new ArrayList<>();

        for(Map.Entry<Integer,ItemCategory> entry : listAdapter.selectedItemCategories.entrySet())
        {
            if(parent!=null)
            {
                entry.getValue().setParentCategoryID(parent.getItemCategoryID());
            }
            entry.getValue().setRt_gidb_service_url(UtilityGeneral.getServiceURL_GIDB(getActivity()));
            tempList.add(entry.getValue());
        }


        Call<ResponseBody> call = itemCatService.addItemCatFromGlobal(UtilityLogin.getAuthorizationHeaders(getActivity()), tempList);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==201)
                {
                    showToastMessage("Item Categories Copied !");
                    clearSelectedItems();
                    clearSelectedItemCat();
                }
                else if(response.code()==206)
                {
                    showToastMessage("Few Item Categories Copied !");
                }
                else if(response.code()==304)
                {
                    showToastMessage("No item categories updated !");
                }
                else
                {
                    showToastMessage("Unknown server error Code : " + String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Failed !");
            }
        });
    }



    void copyItems()
    {

        ItemCategory parent = getActivity().getIntent().getParcelableExtra(AddFromGlobal.INTENT_KEY_ITEM_CAT_PARENT);

        List<Item> tempList = new ArrayList<>();

        for(Map.Entry<Integer,Item> entry : listAdapter.selectedItems.entrySet())
        {
            if(parent!=null)
            {
                entry.getValue().setItemCategoryID(parent.getItemCategoryID());
            }
            entry.getValue().setRt_gidb_service_url(UtilityGeneral.getServiceURL_GIDB(getActivity()));
            tempList.add(entry.getValue());
        }


        Call<ResponseBody> call = itemService.addItemFromGlobal(UtilityLogin.getAuthorizationHeaders(getActivity()),tempList);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==201)
                {
                    showToastMessage("Items Copied !");
                    clearSelectedItems();
                    clearSelectedItemCat();
                }
                else if(response.code()==206)
                {
                    showToastMessage("Few Items Copied !");
                }
                else if(response.code()==304)
                {
                    showToastMessage("No item updated !");
                }
                else
                {
                    showToastMessage("Unknown server error Code : " + String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Failed !");
            }
        });
    }

}
