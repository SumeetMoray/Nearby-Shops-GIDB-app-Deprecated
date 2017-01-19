package nbsidb.nearbyshops.org.Settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import nbsidb.nearbyshops.org.R;

/**
 * Created by sumeet on 15/1/17.
 */

public class FragmentSettingsCustomPref extends PreferenceFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        addPreferencesFromResource(R.xml.pref_custom);
    }
}
