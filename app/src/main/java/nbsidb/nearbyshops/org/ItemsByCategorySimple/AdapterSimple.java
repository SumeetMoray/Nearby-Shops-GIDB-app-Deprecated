package nbsidb.nearbyshops.org.ItemsByCategorySimple;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
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

import com.squareup.picasso.Picasso;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import nbsidb.nearbyshops.org.ItemsByCategorySimple.EditItem.EditItem;
import nbsidb.nearbyshops.org.ItemsByCategorySimple.EditItem.EditItemFragment;
import nbsidb.nearbyshops.org.ItemsByCategorySimple.EditItem.UtilityItem;
import nbsidb.nearbyshops.org.ItemsByCategorySimple.EditItemCategory.EditItemCategory;
import nbsidb.nearbyshops.org.ItemsByCategorySimple.EditItemCategory.EditItemCategoryFragment;
import nbsidb.nearbyshops.org.ItemsByCategorySimple.EditItemCategory.UtilityItemCategory;
import nbsidb.nearbyshops.org.ItemsByCategorySimple.Utility.HeaderItemsList;
import nbsidb.nearbyshops.org.Model.Item;
import nbsidb.nearbyshops.org.Model.ItemCategory;
import nbsidb.nearbyshops.org.ModelStats.ItemStats;
import nbsidb.nearbyshops.org.R;
import nbsidb.nearbyshops.org.Utility.UtilityGeneral;

/**
 * Created by sumeet on 19/12/15.
 */


public class AdapterSimple extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Map<Integer,ItemCategory> selectedItemCategories = new HashMap<>();
    Map<Integer,Item> selectedItems = new HashMap<>();


    private List<Object> dataset;
    private Context context;
    private NotificationsFromAdapter notificationReceiver;

    public static final int VIEW_TYPE_ITEM_CATEGORY = 1;
    public static final int VIEW_TYPE_ITEM = 2;
    public static final int VIEW_TYPE_HEADER = 3;



    public AdapterSimple(List<Object> dataset, Context context, NotificationsFromAdapter notificationReceiver) {

//        DaggerComponentBuilder.getInstance()
//                .getNetComponent().Inject(this);

        this.notificationReceiver = notificationReceiver;
        this.dataset = dataset;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        if(viewType == VIEW_TYPE_ITEM_CATEGORY)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_category,parent,false);
            return new ViewHolderItemCategory(view);
        }
        else if(viewType == VIEW_TYPE_ITEM)
        {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item,parent,false);
            return new ViewHolderItemSimple(view);
        }
        else if(viewType == VIEW_TYPE_HEADER)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_header_type_simple,parent,false);
            return new ViewHolderHeader(view);
        }

//        else
//        {
//            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_guide,parent,false);
//            return new ViewHolderItemSimple(view);
//        }

        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if(holder instanceof ViewHolderItemCategory)
        {
            bindItemCategory((ViewHolderItemCategory) holder,position);
        }
        else if(holder instanceof ViewHolderItemSimple)
        {
            bindItem((ViewHolderItemSimple) holder,position);
        }
        else if(holder instanceof ViewHolderHeader)
        {
            if(dataset.get(position) instanceof HeaderItemsList)
            {
                HeaderItemsList header = (HeaderItemsList) dataset.get(position);

                ((ViewHolderHeader) holder).header.setText(header.getHeading());
            }

        }

    }


    @Override
    public int getItemViewType(int position) {

        super.getItemViewType(position);

        if(dataset.get(position) instanceof ItemCategory)
        {
            return VIEW_TYPE_ITEM_CATEGORY;
        }
        else if (dataset.get(position) instanceof Item)
        {
            return VIEW_TYPE_ITEM;
        }
        else if(dataset.get(position) instanceof HeaderItemsList)
        {
            return VIEW_TYPE_HEADER;
        }


        return -1;
    }

    @Override
    public int getItemCount() {

        return dataset.size();
    }





    class ViewHolderHeader extends RecyclerView.ViewHolder{


        @Bind(R.id.header)
        TextView header;

        public ViewHolderHeader(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }// ViewHolder Class declaration ends



    void bindItemCategory(ViewHolderItemCategory holder,int position)
    {

        if(dataset.get(position) instanceof ItemCategory)
        {
            ItemCategory itemCategory = (ItemCategory) dataset.get(position);

            holder.categoryName.setText(String.valueOf(itemCategory.getCategoryName()));


            if(selectedItemCategories.containsKey(itemCategory.getItemCategoryID()))
            {
                //context.getResources().getColor(R.color.gplus_color_2)
                holder.itemCategoryListItem.setBackgroundColor(ContextCompat.getColor(context,R.color.gplus_color_2));
            }
            else
            {
                //context.getResources().getColor(R.color.white)
                holder.itemCategoryListItem.setBackgroundColor(ContextCompat.getColor(context,R.color.white));
            }



            String imagePath = UtilityGeneral.getServiceURL(context) + "/api/v1/ItemCategory/Image/five_hundred_"
                    + itemCategory.getImagePath() + ".jpg";

            Drawable placeholder = VectorDrawableCompat
                    .create(context.getResources(),
                            R.drawable.ic_nature_people_white_48px, context.getTheme());

            Picasso.with(context).load(imagePath)
                    .placeholder(placeholder)
                    .into(holder.categoryImage);

        }
    }



    class ViewHolderItemCategory extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {


        @Bind(R.id.name) TextView categoryName;
        @Bind(R.id.itemCategoryListItem) ConstraintLayout itemCategoryListItem;
        @Bind(R.id.categoryImage) ImageView categoryImage;
        @Bind(R.id.cardview) CardView cardView;


        public ViewHolderItemCategory(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }


        @OnClick(R.id.itemCategoryListItem)
        public void itemCategoryListItemClick()
        {
            notificationReceiver.notifyRequestSubCategory(
                    (ItemCategory) dataset.get(getLayoutPosition()));

            selectedItemCategories.clear();
            selectedItems.clear();
        }



        @OnLongClick(R.id.itemCategoryListItem)
        boolean listItemLongClick()
        {

//            showToastMessage("Long Click !");

            ItemCategory itemCategory = (ItemCategory) dataset.get(getLayoutPosition());


            if(selectedItemCategories.containsKey(
                    itemCategory.getItemCategoryID()
            ))
            {
                selectedItemCategories.remove(itemCategory.getItemCategoryID());

            }else
            {

                selectedItemCategories.put(itemCategory.getItemCategoryID(),itemCategory);

                notificationReceiver.notifyItemCategorySelected();
            }


            notifyItemChanged(getLayoutPosition());

            return true;
        }



        @OnClick(R.id.more_options)
        void optionsOverflowClick(View v)
        {
            PopupMenu popup = new PopupMenu(context, v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.item_category_item_overflow, popup.getMenu());
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

//                                    deleteItemCategory();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


//                                    showToastMessage("Cancelled !");
                                }
                            })
                            .show();
                    break;



                case R.id.action_edit:


                    if(dataset.get(getLayoutPosition()) instanceof ItemCategory)
                    {
                        UtilityItemCategory.saveItemCategory((ItemCategory) dataset.get(getLayoutPosition()),context);

                        Intent intent = new Intent(context,EditItemCategory.class);
                        intent.putExtra(EditItemCategoryFragment.EDIT_MODE_INTENT_KEY,EditItemCategoryFragment.MODE_UPDATE);
                        context.startActivity(intent);
                    }

                    break;



                case R.id.action_detach:

//                    showToastMessage("Detach");

                    if(dataset.get(getLayoutPosition()) instanceof ItemCategory)
                    {
                        notificationReceiver.detachItemCat((ItemCategory) dataset.get(getLayoutPosition()));
                    }


                    break;

                case R.id.action_change_parent:

                    if(dataset.get(getLayoutPosition()) instanceof ItemCategory)
                    {
                        notificationReceiver.changeParentItemCat((ItemCategory) dataset.get(getLayoutPosition()));
                    }

                    break;

                default:

                    break;

            }



            return false;

        }
    }// ViewHolder Class declaration ends




    void bindItem(ViewHolderItemSimple holder,int position)
    {

        Item item = (Item) dataset.get(position);

        holder.categoryName.setText(item.getItemName());

        ItemStats itemStats = item.getItemStats();

//        holder.priceRange.setText("Price Range :\nRs." + itemStats.getMin_price() + " - " + itemStats.getMax_price() + " per " + item.getQuantityUnit());
//        holder.priceAverage.setText("Price Average :\nRs." + itemStats.getAvg_price() + " per " + item.getQuantityUnit());
//        holder.shopCount.setText("Available in " + itemStats.getShopCount() + " Shops");

        holder.itemDescription.setText(item.getItemDescription());
        holder.item_unit.setText("Item Unit : " + item.getQuantityUnit());


        if(itemStats!=null)
        {
            holder.itemRating.setText(String.format("%.2f",itemStats.getRating_avg()));
            holder.ratingCount.setText("( " + String.valueOf(itemStats.getRatingCount()) + " Ratings )");
        }

        if(selectedItems.containsKey(item.getItemID()))
        {
            //context.getResources().getColor(R.color.gplus_color_2)
            holder.itemCategoryListItem.setBackgroundColor(ContextCompat.getColor(context,R.color.gplus_color_2));
        }
        else
        {
            //context.getResources().getColor(R.color.white)
            holder.itemCategoryListItem.setBackgroundColor(ContextCompat.getColor(context,R.color.white));
        }



        String imagePath = UtilityGeneral.getServiceURL(context)
                + "/api/v1/Item/Image/three_hundred_" + item.getItemImageURL() + ".jpg";


        Drawable drawable = VectorDrawableCompat
                .create(context.getResources(),
                        R.drawable.ic_nature_people_white_48px, context.getTheme());

        Picasso.with(context).load(imagePath)
                .placeholder(drawable)
                .into(holder.categoryImage);

    }



    class ViewHolderItemSimple extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {


        @Bind(R.id.itemName) TextView categoryName;
//        TextView categoryDescription;

        @Bind(R.id.items_list_item) CardView itemCategoryListItem;
        @Bind(R.id.itemImage) ImageView categoryImage;
//        @Bind(R.id.price_range) TextView priceRange;
//        @Bind(R.id.price_average) TextView priceAverage;
//        @Bind(R.id.shop_count) TextView shopCount;
        @Bind(R.id.description_short) TextView itemDescription;
        @Bind(R.id.quantity_unit) TextView item_unit;
        @Bind(R.id.item_rating) TextView itemRating;
        @Bind(R.id.rating_count) TextView ratingCount;



        public ViewHolderItemSimple(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }



            @OnClick(R.id.items_list_item)
            public void listItemClick()
            {
                Item item = (Item) dataset.get(getLayoutPosition());

                if(selectedItems.containsKey(
                        item.getItemID()
                ))
                {
                    selectedItems.remove(item.getItemID());
                }
                else
                {

                    selectedItems.put(item.getItemID(),item);
                    notificationReceiver.notifyItemSelected();
                }

                notifyItemChanged(getLayoutPosition());
            }


        @OnClick(R.id.more_options)
        void optionsOverflowClick(View v)
        {
            PopupMenu popup = new PopupMenu(context, v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.item_category_item_overflow, popup.getMenu());
            popup.setOnMenuItemClickListener(this);
            popup.show();
        }


        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId()) {
                case R.id.action_remove:

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle("Confirm Delete Item Category !")
                            .setMessage("Do you want to delete this Item Category ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

//                                    deleteItemCategory();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


//                                    showToastMessage("Cancelled !");
                                }
                            })
                            .show();


                    break;

                case R.id.action_edit:

                    if (dataset.get(getLayoutPosition()) instanceof Item) {
                        UtilityItem.saveItem((Item) dataset.get(getLayoutPosition()), context);

                        Intent intentEdit = new Intent(context, EditItem.class);
                        intentEdit.putExtra(EditItemFragment.EDIT_MODE_INTENT_KEY, EditItemFragment.MODE_UPDATE);
                        context.startActivity(intentEdit);
                    }
                    break;

                case R.id.action_detach:

                    if (dataset.get(getLayoutPosition()) instanceof Item) {
                        notificationReceiver.detachItem((Item) dataset.get(getLayoutPosition()));
                    }
                    break;


                case R.id.action_change_parent:

                    if (dataset.get(getLayoutPosition()) instanceof Item)
                    {
                        notificationReceiver.changeParentItem((Item) dataset.get(getLayoutPosition()));
                    }
                    break;

                default:
                    break;

            }

            return false;
        }



    }// ViewHolder Class declaration ends




    interface NotificationsFromAdapter
    {
        // method for notifying the list object to request sub category
        void notifyRequestSubCategory(ItemCategory itemCategory);
        void notifyItemCategorySelected();
        void notifyItemSelected();

        void detachItemCat(ItemCategory itemCategory);
        void notifyDeleteItemCat();
        void changeParentItemCat(ItemCategory itemCategory);



        void detachItem(Item item);
        void changeParentItem(Item item);
    }



//    private void showToastMessage(String message)
//    {
//        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
//    }

}