package nbsidb.nearbyshops.org.DetachedTabs.ItemCategories;

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


public class DetachedItemCatAdapter extends RecyclerView.Adapter<DetachedItemCatAdapter.ViewHolder>{


    Map<Integer,ItemCategory> selectedItems = new HashMap<>();


    @Inject
    ItemCategoryService itemCategoryService;

    private List<ItemCategory> dataset;
    private Context context;
//    private DetachedItemCatFragment activity;
//    private ItemCategory requestedChangeParent = null;
    private NotifyFromAdapter notificationReceiver;


    final String IMAGE_ENDPOINT_URL = "/api/Images";

    public DetachedItemCatAdapter(List<ItemCategory> dataset, Context context, NotifyFromAdapter notificationReceiver) {


        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);


        this.notificationReceiver = notificationReceiver;
        this.dataset = dataset;
        this.context = context;
//        this.activity = activity;

        if(this.dataset == null)
        {
            this.dataset = new ArrayList<ItemCategory>();
        }

    }

    @Override
    public DetachedItemCatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_category,parent,false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(DetachedItemCatAdapter.ViewHolder holder, final int position) {

        //String.valueOf(dataset.get(position).getItemCategoryID()) + ". "

        holder.categoryName.setText(dataset.get(position).getCategoryName());
//        holder.categoryDescription.setText(dataset.get(position).getDescriptionShort());

        if(selectedItems.containsKey(dataset.get(position).getItemCategoryID()))
        {
            // context.getResources().getColor(R.color.gplus_color_2)
            holder.itemCategoryListItem.setBackgroundColor(ContextCompat.getColor(context, R.color.gplus_color_2));
        }
        else
        {
            holder.itemCategoryListItem.setBackgroundColor(ContextCompat.getColor(context,R.color.white));
        }



//        String imagePath = UtilityGeneral.getImageEndpointURL(context)
//                + dataset.get(position).getImagePath();

//        if(dataset.get(position).getImagePath()!=null && !dataset.get(position).getImagePath().equals(""))
//        {

//        }


        String imagePath = UtilityGeneral.getServiceURL(context) + "/api/v1/ItemCategory/Image/five_hundred_"
                + dataset.get(position).getImagePath() + ".jpg";

        Drawable placeholder = VectorDrawableCompat
                .create(context.getResources(),
                        R.drawable.ic_nature_people_white_48px, context.getTheme());

        Picasso.with(context)
                .load(imagePath)
                .placeholder(placeholder)
                .into(holder.categoryImage);

    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);


    }

    @Override
    public int getItemCount() {

        return dataset.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {

        //        TextView categoryDescription;

        @Bind(R.id.name) TextView categoryName;
        @Bind(R.id.itemCategoryListItem)
        ConstraintLayout itemCategoryListItem;
        @Bind(R.id.categoryImage) ImageView categoryImage;
        @Bind(R.id.cardview)
        CardView cardView;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }




        @OnClick(R.id.itemCategoryListItem)
        public void listItemClick()
        {

//            showToastMessage("Long Click !");


            if(selectedItems.containsKey(
                    dataset.get(getLayoutPosition())
                            .getItemCategoryID()
            )
                    )

            {
                selectedItems.remove(dataset.get(getLayoutPosition()).getItemCategoryID());

            }else
            {

                selectedItems.put(dataset.get(getLayoutPosition()).getItemCategoryID(),dataset.get(getLayoutPosition()));

                notificationReceiver.notifyItemCategorySelected();
            }


            notifyItemChanged(getLayoutPosition());

        }




//        public void itemCategoryListItemClick()
//        {
//
//            if (dataset == null) {
//
//                return;
//            }
//
//            if(dataset.size()==0)
//            {
//                return;
//            }
//
//
////            itemCategoryListItem.animate()
////                    .y(100)
////                    .x(100);
//
//
////            notificationReceiver.notifyRequestSubCategory(dataset.get(getLayoutPosition()));
//            selectedItems.clear();
//
//
//
//
//            if (dataset.get(getLayoutPosition()).getIsLeafNode()) {
//
////                Intent intent = new Intent(context, Items.class);
////
////                intent.putExtra(Items.ITEM_CATEGORY_INTENT_KEY,dataset.get(getLayoutPosition()));
////
////                context.startActivity(intent);
//
//            }
//            else
//            {
//
//            }
//
//        }


        public void deleteItemCategory()
        {


            Call<ResponseBody> call = itemCategoryService.deleteItemCategory(UtilityLogin.getAuthorizationHeaders(context),
                    dataset.get(getLayoutPosition()).getItemCategoryID());

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

                    UtilityItemCategory.saveItemCategory(dataset.get(getLayoutPosition()),context);

                    Intent intent = new Intent(context,EditItemCategory.class);
                    intent.putExtra(EditItemCategoryFragment.EDIT_MODE_INTENT_KEY, EditItemCategoryFragment.MODE_UPDATE);
                    context.startActivity(intent);


                    break;


                case R.id.action_change_parent:


                    notificationReceiver.changeParentSingle(dataset.get(getLayoutPosition()));


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
        notificationReceiver.notifyDeleted();
    }



    interface NotifyFromAdapter
    {
        // method for notifying the list object to request sub category
//        public void notifyRequestSubCategory(ItemCategory itemCategory);
        void notifyItemCategorySelected();
        void notifyDeleted();
        void changeParentSingle(ItemCategory itemCategory);
    }


//    public void setRequestedChangeParent(ItemCategory requestedChangeParent) {
//        this.requestedChangeParent = requestedChangeParent;
//    }

//    public ItemCategory getRequestedChangeParent() {
//        return requestedChangeParent;
//    }


}