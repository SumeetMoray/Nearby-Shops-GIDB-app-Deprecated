package nbsidb.nearbyshops.org.DetachedTabs.Items;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nbsidb.nearbyshops.org.DaggerComponentBuilder;
import nbsidb.nearbyshops.org.ItemsByCategorySimple.EditItem.EditItem;
import nbsidb.nearbyshops.org.ItemsByCategorySimple.EditItem.EditItemFragment;
import nbsidb.nearbyshops.org.ItemsByCategorySimple.EditItem.UtilityItem;
import nbsidb.nearbyshops.org.Model.Item;
import nbsidb.nearbyshops.org.ModelStats.ItemStats;
import nbsidb.nearbyshops.org.R;
import nbsidb.nearbyshops.org.RetrofitRESTContract.ItemService;
import nbsidb.nearbyshops.org.SelectParent.ItemCategoriesParent;
import nbsidb.nearbyshops.org.Utility.UtilityGeneral;
import nbsidb.nearbyshops.org.Utility.UtilityLogin;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 19/12/15.
 */


public class DetachedItemAdapter extends RecyclerView.Adapter<DetachedItemAdapter.ViewHolder>{


    Map<Integer,Item> selectedItems = new HashMap<>();


    @Inject
    ItemService itemCategoryService;

    private List<Item> dataset;

    private Context context;
    private DetachedItemFragment activity;
    private Item requestedChangeParent = null;
    private NotificationReceiver notificationReceiver;


    public DetachedItemAdapter(List<Item> dataset, Context context, DetachedItemFragment activity, DetachedItemAdapter.NotificationReceiver notificationReceiver) {


        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);


        this.notificationReceiver = notificationReceiver;
        this.dataset = dataset;
        this.context = context;
        this.activity = activity;

        if(this.dataset == null)
        {
            this.dataset = new ArrayList<Item>();
        }

    }

    @Override
    public DetachedItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item,parent,false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(DetachedItemAdapter.ViewHolder holder, final int position) {

        holder.categoryName.setText(dataset.get(position).getItemName());

        ItemStats itemStats = dataset.get(position).getItemStats();
        Item item = dataset.get(position);


        if(itemStats!=null)
        {
//            holder.priceRange.setText("Price Range :\nRs." + itemStats.getMin_price() + " - " + itemStats.getMax_price() + " per " + item.getQuantityUnit());
//            holder.priceAverage.setText("Price Average :\nRs." + itemStats.getAvg_price() + " per " + item.getQuantityUnit());
//            holder.shopCount.setText("Available in " + itemStats.getShopCount() + " Shops");
            holder.itemRating.setText(String.format("%.2f",itemStats.getRating_avg()));
            holder.ratingCount.setText("( " + String.valueOf(itemStats.getRatingCount()) + " Ratings )");
        }


        if(selectedItems.containsKey(dataset.get(position).getItemID()))
        {
            holder.itemCategoryListItem.setBackgroundColor(context.getResources().getColor(R.color.gplus_color_2));
        }
        else
        {
            holder.itemCategoryListItem.setBackgroundColor(context.getResources().getColor(R.color.white));
        }




        String imagePath = UtilityGeneral.getServiceURL(context)
                + "/api/v1/Item/Image/three_hundred_" + dataset.get(position).getItemImageURL() + ".jpg";


        Drawable drawable = VectorDrawableCompat
                .create(context.getResources(),
                        R.drawable.ic_nature_people_white_48px, context.getTheme());

        Picasso.with(context).load(imagePath)
                .placeholder(drawable)
                .into(holder.categoryImage);

    }


    @Override
    public int getItemCount() {

        return dataset.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {

        @Bind(R.id.itemName) TextView categoryName;
//        TextView categoryDescription;

        @Bind(R.id.items_list_item)
        CardView itemCategoryListItem;
        @Bind(R.id.itemImage) ImageView categoryImage;

//        @Bind(R.id.price_range) TextView priceRange;
//        @Bind(R.id.price_average) TextView priceAverage;
//        @Bind(R.id.shop_count) TextView shopCount;

        @Bind(R.id.item_rating) TextView itemRating;
        @Bind(R.id.rating_count) TextView ratingCount;


//        private TextView categoryName,categoryDescription;
//        @Bind(R.id.itemCategoryListItem) LinearLayout itemCategoryListItem;
//        @Bind(R.id.categoryImage) ImageView categoryImage;


        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);

//            categoryImage = (ImageView) itemView.findViewById(R.id.categoryImage);
//            categoryName = (TextView) itemView.findViewById(R.id.categoryName);
//            categoryDescription = (TextView) itemView.findViewById(R.id.categoryDescription);

//            itemCategoryListItem = (LinearLayout) itemView.findViewById(R.id.itemCategoryListItem);
        }



        @OnClick(R.id.items_list_item)
        public void listItemClick()
        {

//            showToastMessage("Long Click !");


            if(selectedItems.containsKey(
                    dataset.get(getLayoutPosition())
                            .getItemID()
            )
                    )

            {
                selectedItems.remove(dataset.get(getLayoutPosition()).getItemID());

            }else
            {

                selectedItems.put(dataset.get(getLayoutPosition()).getItemID(),dataset.get(getLayoutPosition()));

                notificationReceiver.notifyItemCategorySelected();
            }


            notifyItemChanged(getLayoutPosition());

//            showToastMessage(String.valueOf(selectedItems.size()));


//            itemCategoryListItem.setBackgroundColor(context.getResources().getColor(R.color.cyan900));


        }




        /*@OnClick(R.id.itemCategoryListItem)
        public void itemCategoryListItemClick()
        {

            if (dataset == null) {

                return;
            }

            if(dataset.size()==0)
            {
                return;
            }



//            notificationReceiver.notifyRequestSubCategory(dataset.get(getLayoutPosition()));

//            selectedItems.clear();

        }


*/
        public void deleteItemCategory()
        {


//            Call<ResponseBody> call = itemCategoryService.deleteItemCategory(dataset.get(getLayoutPosition()).getItemCategoryID());

            Call<ResponseBody> call = itemCategoryService.deleteItem(UtilityLogin.getAuthorizationHeaders(context),
                    dataset.get(getLayoutPosition()).getItemID());


            call.enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                    if(response.code()==200)
                    {
                        notifyDelete();

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


        @OnClick(R.id.more_options)
        void optionsOverflowClick(View v)
        {
            PopupMenu popup = new PopupMenu(context, v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.item_category_item_overflow_detached, popup.getMenu());
            popup.setOnMenuItemClickListener(this);
            popup.show();
        }


        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId())
            {
                case R.id.action_remove:

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle("Confirm Delete Item Category !")
                            .setMessage("Do you want to delete this Item Category ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    deleteItemCategory();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    showToastMessage("Cancelled !");
                                }
                            })
                            .show();


                    break;

                case R.id.action_edit:

//                    Intent intent = new Intent(context,EditItemCategoryOld.class);
//                    intent.putExtra(EditItemCategoryOld.ITEM_CATEGORY_INTENT_KEY,dataset.get(getLayoutPosition()));
//                    context.startActivity(intent);

//                    Intent intentEdit = new Intent(context,EditItemOld.class);
//                    intentEdit.putExtra(EditItemOld.ITEM_INTENT_KEY,dataset.get(getLayoutPosition()));
//                    context.startActivity(intentEdit);



                    UtilityItem.saveItem(dataset.get(getLayoutPosition()),context);

                    Intent intentEdit = new Intent(context,EditItem.class);
                    intentEdit.putExtra(EditItemFragment.EDIT_MODE_INTENT_KEY,EditItemFragment.MODE_UPDATE);
                    context.startActivity(intentEdit);

                    break;


                case R.id.action_change_parent:


//                    showToastMessage("Change parent !");

                    Intent intentParent = new Intent(context, ItemCategoriesParent.class);

                    requestedChangeParent = dataset.get(getLayoutPosition());

                    // add the selected item category in the exclude list so that it does not get showed up as an option.
                    // This is required to prevent an item category to assign itself or its children as its parent.
                    // This should not happen because it would be erratic.

                    ItemCategoriesParent.clearExcludeList(); // it is a safe to clear the list before adding any items in it.

//                    ItemCategoriesParent.excludeList
//                            .put(requestedChangeParent.getItemID(),requestedChangeParent);


                    activity.startActivityForResult(intentParent,1,null);


                    break;




                default:

                    break;

            }

            return false;
        }



    }// ViewHolder Class declaration ends




    void showToastMessage(String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }


    public void notifyDelete()
    {
        activity.notifyDelete();

    }


    public interface NotificationReceiver
    {
        // method for notifying the list object to request sub category
//        public void notifyRequestSubCategory(ItemCategory itemCategory);

        void notifyItemCategorySelected();
        void changeParentRequested();

    }



    public Item getRequestedChangeParent() {
        return requestedChangeParent;
    }


    public void setRequestedChangeParent(Item requestedChangeParent) {
        this.requestedChangeParent = requestedChangeParent;
    }
}