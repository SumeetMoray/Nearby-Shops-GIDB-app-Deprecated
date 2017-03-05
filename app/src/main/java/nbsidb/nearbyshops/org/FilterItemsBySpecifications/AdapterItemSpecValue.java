package nbsidb.nearbyshops.org.FilterItemsBySpecifications;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import nbsidb.nearbyshops.org.ModelItemSpecification.ItemSpecificationName;
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

    private Fragment fragment;

    AdapterItemSpecValue(List<ItemSpecificationValue> dataset,
                         NotificationsFromAdapter notifyFragment, Context context,
                         Fragment fragment)
    {
        this.dataset = dataset;
        this.notifyFragment = notifyFragment;
        this.context = context;

        this.fragment = fragment;
    }

    @Override
    public AdapterItemSpecValue.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_filter_values,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterItemSpecValue.ViewHolder holder, int position) {

        ItemSpecificationValue itemSpecificationValue = dataset.get(position);

        holder.results.setText("(" + String.valueOf(itemSpecificationValue.getRt_item_count()) + " results)");
        holder.title.setText(itemSpecificationValue.getTitle());


        if(fragment instanceof FilterItemsFragment)
        {
            if(((FilterItemsFragment) fragment).checkboxesList.contains(itemSpecificationValue.getId()))
            {
                holder.checkBoxValues.setChecked(true);
            }
            else
            {
                holder.checkBoxValues.setChecked(false);
            }

        }

    }



    @Override
    public int getItemCount() {
        return dataset.size();
    }





    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


//        @Bind(R.id.title_item_spec) TextView titleItemSpec;
//        @Bind(R.id.image_item_spec) ImageView imageItemSpec;
//        @Bind(R.id.description) TextView description;
//        @Bind(R.id.item_count) TextView itemCount;

        @Bind(R.id.results) TextView results;
        @Bind(R.id.checkbox_values) CheckBox checkBoxValues;
        @Bind(R.id.title_values) TextView title;



        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            notifyFragment.listItemClick(dataset.get(getLayoutPosition()),getLayoutPosition());
            checkBoxValues.setChecked(!checkBoxValues.isChecked());
            checkChanged();
        }


        @OnClick(R.id.checkbox_values)
        void checkChanged()
        {
            if(checkBoxValues.isChecked())
            {
                notifyFragment.insertItemSpecItem(dataset.get(getLayoutPosition()).getId());
            }
            else if(!checkBoxValues.isChecked())
            {
                notifyFragment.deleteItemSpecItem(dataset.get(getLayoutPosition()).getId());
            }
        }



    }





    interface NotificationsFromAdapter{

        void listItemClick(ItemSpecificationValue itemSpecValue, int position);
        void deleteItemSpecItem(int itemSpecValueID);
        void insertItemSpecItem(int itemSpecValueID);

    }

}
