package nbsidb.nearbyshops.org.Settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import nbsidb.nearbyshops.org.R;

public class SettingsCustom extends AppCompatActivity {

    public static final String TAG_FRAGMENT = "TAG_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT)==null)
        {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container,new FragmentSettingsCustom(),TAG_FRAGMENT)
                    .commit();
        }
    }



}
