package nbsidb.nearbyshops.org.SelectParent;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import nbsidb.nearbyshops.org.DaggerComponentBuilder;
import nbsidb.nearbyshops.org.ItemsByCategorySimple.EditItemCategory.EditItemCategory;
import nbsidb.nearbyshops.org.ItemsByCategorySimple.EditItemCategory.EditItemCategoryFragment;
import nbsidb.nearbyshops.org.ItemsByCategorySimple.EditItemCategory.UtilityItemCategory;
import nbsidb.nearbyshops.org.Model.ItemCategory;
import nbsidb.nearbyshops.org.R;
import nbsidb.nearbyshops.org.RetrofitRESTContract.ItemCategoryService;
import nbsidb.nearbyshops.org.Utility.UtilityGeneral;
import nbsidb.nearbyshops.org.Utility.UtilityLogin;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 19/12/15.
 */


public class ItemCategoriesParentAdapter extends RecyclerView.Adapter<ItemCategoriesParentAdapter.ViewHolder>{



    @Inject
    ItemCategoryService itemCategoryService;

    private List<ItemCategory> dataset;
    private Context context;
    private ItemCategoriesParent itemCategoriesParent;
    private Integer selectedPosition = null;
    private NotificationReceiver notificationReceiver;


    final String IMAGE_ENDPOINT_URL = "/api/Images";

    public ItemCategoriesParentAdapter(List<ItemCategory> dataset, Context context, ItemCategoriesParent activity
                            ,NotificationReceiver notificationReceiver) {


        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

        this.notificationReceiver = notificationReceiver;
        this.dataset = dataset;
        this.context = context;
        this.itemCategoriesParent = activity;

        if(this.dataset == null)
        {
            this.dataset = new ArrayList<ItemCategory>();
        }

    }

    @Override
    public ItemCategoriesParentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_category_parent,parent,false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ItemCategoriesParentAdapter.ViewHolder holder, final int position) {

        holder.categoryName.setText(dataset.get(position).getCategoryName());
//        holder.categoryDescription.setText(dataset.get(position).getCategoryDescription());

        if(selectedPosition!=null)
        {
            if(selectedPosition==position){

                holder.itemCategoryListItem.setBackgroundColor(context.getResources().getColor(R.color.gplus_color_2));

                notificationReceiver.notifyItemSelected();

            }

//            holder.itemCategoryListItem.animate().rotation(90);
        }else
        {
            holder.itemCategoryListItem.setBackgroundColor(context.getResources().getColor(R.color.white));

        }




//        String imagePath = UtilityGeneral.getImageEndpointURL(context)
//                + dataset.get(position).getImagePath();




        String imagePath = UtilityGeneral.getServiceURL(context) + "/api/v1/ItemCategory/Image/five_hundred_"
                + dataset.get(position).getImagePath() + ".jpg";


        Drawable placeholder = VectorDrawableCompat
                .create(context.getResources(),
                        R.drawable.ic_nature_people_white_48px, context.getTheme());

        Drawable compat = ContextCompat.getDrawable(context,R.drawable.ic_nature_people_white_48px);

        Picasso.with(context)
                .load(imagePath)
                .placeholder(compat)
                .into(holder.categoryImage);


        Log.d("applog",imagePath);

    }


    @Override
    public int getItemCount() {

        return dataset.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {



//        TextView categoryDescription;

        @Bind(R.id.categoryName) TextView categoryName;
        @Bind(R.id.itemCategoryListItem) LinearLayout itemCategoryListItem;
        @Bind(R.id.categoryImage) ImageView categoryImage;


        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);

//            categoryImage = (ImageView) itemView.findViewById(R.id.categoryImage);
//            categoryName = (TextView) itemView.findViewById(R.id.categoryName);
//            categoryDescription = (TextView) itemView.findViewById(R.id.categoryDescription);

//            itemCategoryListItem = (LinearLayout) itemView.findViewById(R.id.itemCategoryListItem);
        }



        @OnLongClick(R.id.itemCategoryListItem)
        public boolean listItemLongClick()
        {

            int previousPosition = -1;



            if(selectedPosition!=null)
            {
                previousPosition = selectedPosition;
            }


//            showToastMessage("Long Click !");

            selectedPosition = getLayoutPosition();


//
            if(previousPosition!=selectedPosition)
            {

                notifyItemChanged(previousPosition);
                notifyItemChanged(selectedPosition);

                // item Selected


            }
            else
            {
                selectedPosition = null;


                if(previousPosition!=-1)
                {
                    notifyItemChanged(previousPosition);
                }

            }

//            notifyDataSetChanged();



//            itemCategoryListItem.setBackgroundColor(context.getResources().getColor(R.color.cyan900));

            return true;
        }



        @OnClick(R.id.itemCategoryListItem)
        public void itemCategoryListItemClick()
        {

            if (dataset == null) {

                return;
            }

            if(dataset.size()==0)
            {
                return;
            }



            selectedPosition = null;
            itemCategoriesParent.notifyRequestSubCategory(dataset.get(getLayoutPosition()));


//            if (dataset.get(getLayoutPosition()).getIsLeafNode()) {

//                Intent intent = new Intent(context, Items.class);
//
//                intent.putExtra(Items.ITEM_CATEGORY_INTENT_KEY,dataset.get(getLayoutPosition()));
//
//                context.startActivity(intent);
//            }
//            else
//            {
//
//            }


        }



        public void deleteItemCategory()
        {


            Call<ResponseBody> call = itemCategoryService
                    .deleteItemCategory(UtilityLogin.getAuthorizationHeaders(context),
                            dataset.get(getLayoutPosition()).getItemCategoryID());

            call.enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                    if(response.code()==200)
                    {
//                        notifyDelete();
                        notificationReceiver.notifyItemDeleted();

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
            inflater.inflate(R.menu.item_category_item_overflow_parent_selection, popup.getMenu());
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


                    UtilityItemCategory.saveItemCategory(dataset.get(getLayoutPosition()),context);

                    Intent intent = new Intent(context,EditItemCategory.class);
                    intent.putExtra(EditItemCategoryFragment.EDIT_MODE_INTENT_KEY, EditItemCategoryFragment.MODE_UPDATE);
                    context.startActivity(intent);


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

//        itemCategoriesParent.notifyDelete();

    }


    public interface requestSubCategory
    {
        // method for notifying the list object to request sub category
        public void notifyRequestSubCategory(ItemCategory itemCategory);
    }




    public void clearSelection()
    {

        selectedPosition = null;
    }



    public ItemCategory getSelection()
    {
        if(selectedPosition!=null)
        {
            return dataset.get(selectedPosition);
        }
        else
        {
            return null;
        }
    }


    interface NotificationReceiver{

        void notifyItemSelected();
        void notifyItemDeleted();
    }

}