package nbsidb.nearbyshops.org.ItemSpecName;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import nbsidb.nearbyshops.org.DaggerComponentBuilder;
import nbsidb.nearbyshops.org.ItemSpecName.EditItemSpecName.EditItemSpecName;
import nbsidb.nearbyshops.org.ItemSpecName.EditItemSpecName.EditItemSpecNameFragment;
import nbsidb.nearbyshops.org.ItemSpecName.EditItemSpecName.UtilityItemSpecName;
import nbsidb.nearbyshops.org.ModelItemSpecification.EndPoints.ItemSpecNameEndPoint;
import nbsidb.nearbyshops.org.ModelItemSpecification.ItemSpecificationName;
import nbsidb.nearbyshops.org.R;
import nbsidb.nearbyshops.org.RetrofitRESTContract.ItemSpecNameService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 3/3/17.
 */

public class ItemSpecNameFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AdapterItemSpecName.NotificationsFromAdapter {


    @Inject
    ItemSpecNameService itemSpecNameService;

    @Bind(R.id.recycler_view) RecyclerView recyclerView;
    AdapterItemSpecName adapter;

    public List<ItemSpecificationName> dataset = new ArrayList<>();

    GridLayoutManager layoutManager;
    @Bind(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;

    boolean isDestroyed;


    private int limit = 10;
    int offset = 0;
    int item_count = 0;


    public ItemSpecNameFragment() {
        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.content_item_specification_name, container, false);
        ButterKnife.bind(rootView);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);

        setupRecyclerView();
        setupSwipeContainer();



        if(savedInstanceState==null)
        {
            makeRefreshNetworkCall();
        }





        return rootView;
    }



    void setupRecyclerView()
    {

        adapter = new AdapterItemSpecName(dataset,this,getActivity());

        recyclerView.setAdapter(adapter);

        layoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(layoutManager);

//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL_LIST));
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

//        layoutManager.setSpanCount(metrics.widthPixels/400);


        int spanCount = (int) (metrics.widthPixels/(230 * metrics.density));

        if(spanCount==0){
            spanCount = 1;
        }

        layoutManager.setSpanCount(spanCount);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(layoutManager.findLastVisibleItemPosition()==dataset.size()-1)
                {

                    if(offset + limit > layoutManager.findLastVisibleItemPosition())
                    {
                        return;
                    }

                    // trigger fetch next page

                    if((offset+limit)<=item_count)
                    {
                        offset = offset + limit;
                        makeNetworkCall(false,false,false);
                    }

                }
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



    boolean getRowCount = true;


    void makeNetworkCall(final boolean clearDataset, boolean resetOffset, final boolean resetGetRowCount)
    {

        if(resetGetRowCount)
        {
            getRowCount = true;
        }

        if(resetOffset)
        {
            offset = 0;
        }


        Call<ItemSpecNameEndPoint> call = itemSpecNameService.getItemSpecName(
          null,null,limit,offset,getRowCount
        );


        call.enqueue(new Callback<ItemSpecNameEndPoint>() {
            @Override
            public void onResponse(Call<ItemSpecNameEndPoint> call, Response<ItemSpecNameEndPoint> response) {

                if(isDestroyed)
                {
                    return;
                }


                if(clearDataset)
                {
                    dataset.clear();
                }


                if(response.code()==200)
                {
                    dataset.addAll(response.body().getResults());

                    if(getRowCount)
                    {
                        item_count = response.body().getItemCount();
                    }

                    getRowCount=false;
                    adapter.notifyDataSetChanged();
                }
                else
                {
                    showToastMessage("Failed Code : " + String.valueOf(response.code()));
                }


                swipeContainer.setRefreshing(false);
            }



            @Override
            public void onFailure(Call<ItemSpecNameEndPoint> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }

                showToastMessage("Failed !");

                swipeContainer.setRefreshing(false);
            }
        });
    }


    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onResume() {
        super.onResume();
        isDestroyed=false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroyed=true;
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
    public void onRefresh() {

        boolean resetGetRowCount = false;
        if(item_count == 0)
        {
            resetGetRowCount=true;
        }

        makeNetworkCall(true,true,resetGetRowCount);
    }


    @Override
    public void addItemImage() {

    }

    @Override
    public void editItemSpecName(ItemSpecificationName itemSpecName, int position) {

//        showToastMessage("Edit Click");

        Intent intent = new Intent(getActivity(), EditItemSpecName.class);
        intent.putExtra(EditItemSpecNameFragment.EDIT_MODE_INTENT_KEY,EditItemSpecNameFragment.MODE_UPDATE);
        UtilityItemSpecName.saveItemSpecName(itemSpecName,getActivity());
        startActivity(intent);
    }

    @Override
    public void removeItemSpecName(ItemSpecificationName itemSpecName, int position) {

        showToastMessage("Remove Click");
    }
}
