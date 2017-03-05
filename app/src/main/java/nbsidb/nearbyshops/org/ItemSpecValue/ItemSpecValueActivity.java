package nbsidb.nearbyshops.org.ItemSpecValue;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.ButterKnife;
import butterknife.OnClick;
import nbsidb.nearbyshops.org.ItemSpecName.EditItemSpecName.EditItemSpecName;
import nbsidb.nearbyshops.org.ItemSpecName.EditItemSpecName.EditItemSpecNameFragment;
import nbsidb.nearbyshops.org.ItemSpecValue.EditItemSpecValue.EditItemSpecValue;
import nbsidb.nearbyshops.org.ItemSpecValue.EditItemSpecValue.EditItemSpecValueFragment;
import nbsidb.nearbyshops.org.ItemSpecValue.EditItemSpecValue.UtilityItemSpecValue;
import nbsidb.nearbyshops.org.R;

public class ItemSpecValueActivity extends AppCompatActivity {

    public static final String TAG_FRAGMENT = "fragment";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_specification_value);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });





        if(getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT)==null)
        {
            // add fragment to the activity
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container,new ItemSpecValueFragment(),TAG_FRAGMENT)
                    .commit();
        }




    }


    @OnClick(R.id.fab)
    void fabClick()
    {
//        Intent intent = new Intent(this, EditItemSpecName.class);
//        intent.putExtra(EditItemSpecNameFragment.EDIT_MODE_INTENT_KEY,EditItemSpecNameFragment.MODE_ADD);
//        startActivity(intent);

        Intent intent = new Intent(this, EditItemSpecValue.class);
        intent.putExtra(EditItemSpecValueFragment.EDIT_MODE_INTENT_KEY, EditItemSpecValueFragment.MODE_ADD);

        int itemSpecNameID = getIntent().getIntExtra(ItemSpecValueFragment.ITEM_SPEC_NAME_INTENT_KEY,0);
        intent.putExtra(EditItemSpecValueFragment.ITEM_SPEC_NAME_INTENT_KEY,itemSpecNameID);
        startActivity(intent);
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
