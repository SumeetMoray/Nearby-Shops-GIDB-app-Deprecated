package nbsidb.nearbyshops.org.ItemSpecName.EditItemSpecName;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import nbsidb.nearbyshops.org.R;


public class EditItemSpecName extends AppCompatActivity {

    public static final String TAG_FRAGMENT_EDIT = "fragment_edit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item_spec_name);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_EDIT)==null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container,new EditItemSpecNameFragment(),TAG_FRAGMENT_EDIT)
                    .commit();
        }
    }


}
