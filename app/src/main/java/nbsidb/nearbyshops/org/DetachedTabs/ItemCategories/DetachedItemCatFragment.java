package nbsidb.nearbyshops.org.DetachedTabs.ItemCategories;

import android.app.Activity;
import android.content.Intent;
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
import nbsidb.nearbyshops.org.DetachedTabs.Interfaces.NotifyAssignParent;
import nbsidb.nearbyshops.org.DetachedTabs.Interfaces.NotifyScroll;
import nbsidb.nearbyshops.org.Interfaces.NotifyTitleChanged;
import nbsidb.nearbyshops.org.Model.ItemCategory;
import nbsidb.nearbyshops.org.ModelEndpoint.ItemCategoryEndPoint;
import nbsidb.nearbyshops.org.R;
import nbsidb.nearbyshops.org.RetrofitRESTContract.ItemCategoryService;
import nbsidb.nearbyshops.org.SelectParent.ItemCategoriesParent;
import nbsidb.nearbyshops.org.Utility.UtilityLogin;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetachedItemCatFragment extends Fragment implements DetachedItemCatAdapter.NotifyFromAdapter,
        SwipeRefreshLayout.OnRefreshListener,
        NotifyAssignParent
{

    ItemCategory changeParentRequested;


    ArrayList<ItemCategory> dataset = new ArrayList<>();
    RecyclerView itemCategoriesList;
    GridLayoutManager layoutManager;
    DetachedItemCatAdapter listAdapter;

    boolean show = false;
    @Inject
    ItemCategoryService itemCategoryService;

//    @Bind(R.id.appbar) AppBarLayout appBar;

    private int limit = 30;
    int offset = 0;
    int item_count = 0;


    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;


    ItemCategory currentCategory = null;


    public DetachedItemCatFragment() {
        super();

        // Inject the dependencies using Dependency Injection
        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

        currentCategory = new ItemCategory();
        currentCategory.setItemCategoryID(1);
        currentCategory.setParentCategoryID(-1);
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        setRetainInstance(true);


        View rootView = inflater.inflate(R.layout.fragment_detached_item_cat, container, false);

        ButterKnife.bind(this,rootView);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        itemCategoriesList = (RecyclerView)rootView.findViewById(R.id.recyclerViewItemCategories);

        setupRecyclerView();
        setupSwipeContainer();


        if(savedInstanceState==null)
        {
            // make request to the network only for the first time and not the second time or when the context is changed.

            // reset the offset before making request
            offset = 0;
            dataset.clear();
            makeRequestRetrofit(true);
        }


        return  rootView;
    }




    void setupRecyclerView()
    {


        listAdapter = new DetachedItemCatAdapter(dataset,getActivity(),this);

        itemCategoriesList.setAdapter(listAdapter);

        layoutManager = new GridLayoutManager(getActivity(),2, LinearLayoutManager.VERTICAL,false);
        itemCategoriesList.setLayoutManager(layoutManager);


//        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//
//                return (position % 3 == 0 ? 2 : 1);
//            }
//        });


        final DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);


//        layoutManager.setSpanCount(metrics.widthPixels/350);


        int spanCount = (int) (metrics.widthPixels/(150 * metrics.density));

        if(spanCount==0){
            spanCount = 1;
        }
//
        layoutManager.setSpanCount(spanCount);



        itemCategoriesList.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(layoutManager.findLastVisibleItemPosition()==dataset.size()-1)
                {
                    // trigger fetch next page

                    if((offset+limit)<=item_count)
                    {
                        offset = offset + limit;
                        makeRequestRetrofit(true);
                    }

                }
            }


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                if(getActivity() instanceof NotifyScroll)
                {
                    ((NotifyScroll) getActivity()).scrolled(dx,dy);
                }


/*
                if(dy > 20)
                {

                    boolean previous = show;

                    show = false ;

                    if(show!=previous)
                    {
                        // changed
                        Log.d("scrolllog","show");

//                        notificationReceiverFragment.showAppBar();
                    }

                }else if(dy < -20)
                {

                    boolean previous = show;

                    show = true;



                    if(show!=previous)
                    {
                        // changed
//                        options.setVisibility(View.VISIBLE);
//                        options.animate().translationX(0);
                        Log.d("scrolllog","hide");

//                        options.animate().translationY(0);


//                        notificationReceiverFragment.hideAppBar();
                    }
                }
*/


            }

        });

    }


    void setupSwipeContainer()
    {

        if(swipeContainer!=null) {

            swipeContainer.setOnRefreshListener(this);
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }

    }





    public void makeRequestRetrofit(Boolean parentIsNull)
    {

//        Call<ItemCategoryEndPoint> endPointCall = itemCategoryService.getItemCategories(
//                null,null,
//                parentIsNull,null,null,null,null,null,"id",limit,offset,false);

        Call<ItemCategoryEndPoint> endPointCall = itemCategoryService.getItemCategoriesQuerySimple(
                null,parentIsNull,"id",limit,offset
        );


        Log.d("applog","DetachedTabs: Network call made !");



        endPointCall.enqueue(new Callback<ItemCategoryEndPoint>() {
            @Override
            public void onResponse(Call<ItemCategoryEndPoint> call, Response<ItemCategoryEndPoint> response) {

                if(response.body()!=null)
                {
                    ItemCategoryEndPoint endPoint = response.body();

                    item_count = endPoint.getItemCount();

                    dataset.addAll(endPoint.getResults());

                    Log.d("applog",String.valueOf(item_count) + " : " + endPoint.getResults().size());

                    listAdapter.notifyDataSetChanged();

                    notifyTitleChanged();


                }else
                {
                    Log.d("applog","body null" + " : " + response.errorBody().toString());

                }

                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<ItemCategoryEndPoint> call, Throwable t) {


                showToastMessage("Network request failed. Please check your connection !");


                if(swipeContainer!=null)
                {
                    swipeContainer.setRefreshing(false);
                }
            }
        });

    }



    private void showToastMessage(String message)
    {
        if(getActivity()!=null)
        {
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                ItemCategory parentCategory = data.getParcelableExtra("result");

                if(parentCategory!=null)
                {

                    changeParentRequested.setParentCategoryID(parentCategory.getItemCategoryID());
                    makeUpdateRequest(changeParentRequested);

//                    listAdapter.getRequestedChangeParent().setParentCategoryID(parentCategory.getItemCategoryID());

//                    makeUpdateRequest(listAdapter.getRequestedChangeParent());
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

                    List<ItemCategory> tempList = new ArrayList<>();

                    for(Map.Entry<Integer,ItemCategory> entry : listAdapter.selectedItems.entrySet())
                    {
                        entry.getValue().setParentCategoryID(parentCategory.getItemCategoryID());
                        tempList.add(entry.getValue());
                    }

                    makeRequestBulk(tempList);
                }

            }
        }
    }



    void makeUpdateRequest(ItemCategory itemCategory)
    {
        Call<ResponseBody> call = itemCategoryService.changeParent(UtilityLogin.getAuthorizationHeaders(getActivity()),
                itemCategory,itemCategory.getItemCategoryID());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code() == 200)
                {
                    showToastMessage("Change Parent Successful !");

                    dataset.clear();
                    offset = 0 ; // reset the offset
                    makeRequestRetrofit(true);

                }else
                {
                    showToastMessage("Change Parent Failed !");
                }

//                listAdapter.setRequestedChangeParent(null);
                changeParentRequested= null;
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Network request failed. Please check your connection !");

//                listAdapter.setRequestedChangeParent(null);

                changeParentRequested= null;
            }
        });
    }




    void changeParentBulk()
    {

        if(listAdapter.selectedItems.size()==0)
        {
            showToastMessage("No item selected. Please make a selection !");

            return;
        }

        // make an exclude list. Put selected items to an exclude list. This is done to preven a category to make itself or its
        // children its parent. This is logically incorrect and should not happen.

        ItemCategoriesParent.clearExcludeList();
        ItemCategoriesParent.excludeList.putAll(listAdapter.selectedItems);

        Intent intentParent = new Intent(getActivity(), ItemCategoriesParent.class);
        startActivityForResult(intentParent,2,null);
    }


    void makeRequestBulk(final List<ItemCategory> list)
    {
        Call<ResponseBody> call = itemCategoryService.changeParentBulk(UtilityLogin.getAuthorizationHeaders(getActivity()),
                list);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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


                dataset.clear();
                offset = 0 ; // reset the offset
                makeRequestRetrofit(true);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                showToastMessage("Network Request failed. Check your internet / network connection !");

            }
        });

    }


    void clearSelectedItems()
    {
        // clear the selected items
        listAdapter.selectedItems.clear();
    }






    @Override
    public void notifyItemCategorySelected() {

//        exitFullscreen();
    }

    @Override
    public void notifyDeleted() {
        onRefresh();
    }





    @Override
    public void changeParentSingle(ItemCategory itemCategory) {

        //                    showToastMessage("Change parent !");

        Intent intentParent = new Intent(getActivity(), ItemCategoriesParent.class);


        changeParentRequested = itemCategory;
//        requestedChangeParent = dataset.get(getLayoutPosition());

        // add the selected item category in the exclude list so that it does not get showed up as an option.
        // This is required to prevent an item category to assign itself or its children as its parent.
        // This should not happen because it would be erratic.

        ItemCategoriesParent.clearExcludeList(); // it is a safe to clear the list before adding any items in it.
        ItemCategoriesParent.excludeList
                .put(itemCategory.getItemCategoryID(),itemCategory);

        startActivityForResult(intentParent,1,null);
    }




    @Override
    public void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }




    @Override
    public void onRefresh() {

        // reset the offset and make a network call
        offset = 0;
        dataset.clear();
        makeRequestRetrofit(true);
    }



    void notifyTitleChanged()
    {
        if(getActivity() instanceof NotifyTitleChanged)
        {
            ((NotifyTitleChanged) getActivity()).NotifyTitleChanged("Subcategories (" + String.valueOf(dataset.size()) + "/" + String.valueOf(item_count )+ ")",0);
        }
    }





    @Override
    public void assignParentClick() {
        changeParentBulk();
    }
}