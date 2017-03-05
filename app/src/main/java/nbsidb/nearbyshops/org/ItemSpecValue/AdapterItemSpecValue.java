package nbsidb.nearbyshops.org.ItemSpecValue;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
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

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nbsidb.nearbyshops.org.ModelItemSpecification.ItemSpecificationValue;
import nbsidb.nearbyshops.org.R;
import nbsidb.nearbyshops.org.Utility.UtilityGeneral;

/**
 * Created by sumeet on 13/6/16.
 */
class AdapterItemSpecValue extends RecyclerView.Adapter<AdapterItemSpecValue.ViewHolder>{

    private List<ItemSpecificationValue> dataset = null;
    private NotificationsFromAdapter notifyFragment;
    private Context context;

    AdapterItemSpecValue(List<ItemSpecificationValue> dataset, NotificationsFromAdapter notifyFragment, Context context) {
        this.dataset = dataset;
        this.notifyFragment = notifyFragment;
        this.context = context;
    }

    @Override
    public AdapterItemSpecValue.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_item_spec_value,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterItemSpecValue.ViewHolder holder, int position) {

        ItemSpecificationValue itemSpecificationValue = dataset.get(position);

        holder.titleItemSpec.setText(itemSpecificationValue.getTitle());
        holder.description.setText(itemSpecificationValue.getDescription());
        holder.itemCount.setText("Item Count : " + String.valueOf(itemSpecificationValue.getRt_item_count()));

        Drawable drawable = ContextCompat.getDrawable(context,R.drawable.ic_nature_people_white_48px);
        String imagePath = UtilityGeneral.getServiceURL(context) + "/api/v1/ItemSpecificationValue/Image/" + "three_hundred_"+ itemSpecificationValue.getImageFilename() + ".jpg";

        Picasso.with(context)
                .load(imagePath)
                .placeholder(drawable)
                .into(holder.imageItemSpec);

    }



    @Override
    public int getItemCount() {
        return dataset.size();
    }





    class ViewHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {


        @Bind(R.id.title_item_spec) TextView titleItemSpec;
        @Bind(R.id.image_item_spec) ImageView imageItemSpec;
        @Bind(R.id.description) TextView description;
        @Bind(R.id.item_count) TextView itemCount;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }


        @OnClick(R.id.more_options)
        void optionsOverflowClick(View v)
        {
            PopupMenu popup = new PopupMenu(context, v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.item_spec_name_item_overflow, popup.getMenu());
            popup.setOnMenuItemClickListener(this);
            popup.show();
        }



        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId())
            {

                case R.id.action_remove:

//                    showToastMessage("Remove");
                    notifyFragment.removeItemSpecName(dataset.get(getLayoutPosition()),getLayoutPosition());

                    break;

                case R.id.action_edit:

//                    showToastMessage("Edit");
                    notifyFragment.editItemSpecName(dataset.get(getLayoutPosition()),getLayoutPosition());

                    break;


                default:
                    break;

            }

            return false;
        }



    }





    interface NotificationsFromAdapter{

        void addItemImage();
        void editItemSpecName(ItemSpecificationValue itemSpecValue, int position);
        void removeItemSpecName(ItemSpecificationValue itemSpecValue, int position);
    }

}
