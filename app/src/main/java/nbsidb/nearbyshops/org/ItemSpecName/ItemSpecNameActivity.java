package nbsidb.nearbyshops.org.ItemSpecName;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import nbsidb.nearbyshops.org.ItemSpecName.EditItemSpecName.EditItemSpecName;
import nbsidb.nearbyshops.org.ItemSpecName.EditItemSpecName.EditItemSpecNameFragment;
import nbsidb.nearbyshops.org.R;

public class ItemSpecNameActivity extends AppCompatActivity {

    public static final String TAG_FRAGMENT = "fragment";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_specification_name);
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
                    .add(R.id.fragment_container,new ItemSpecNameFragment(),TAG_FRAGMENT)
                    .commit();
        }




    }


    @OnClick(R.id.fab)
    void fabClick()
    {
        Intent intent = new Intent(this, EditItemSpecName.class);
        intent.putExtra(EditItemSpecNameFragment.EDIT_MODE_INTENT_KEY,EditItemSpecNameFragment.MODE_ADD);
        startActivity(intent);
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
