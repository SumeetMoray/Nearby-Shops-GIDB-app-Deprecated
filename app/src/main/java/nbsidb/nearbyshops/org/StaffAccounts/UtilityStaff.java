package nbsidb.nearbyshops.org.StaffAccounts;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;


import nbsidb.nearbyshops.org.ModelRoles.Staff;
import nbsidb.nearbyshops.org.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sumeet on 25/9/16.
 */

public class UtilityStaff {

    private static final String TAG_PREF_STAFF = "staff";

    public static void saveStaff(Staff staff, Context context)
    {

        //Creating a shared preference

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(staff);
        prefsEditor.putString(TAG_PREF_STAFF, json);
        prefsEditor.apply();
    }


    public static Staff getStaff(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPref.getString(TAG_PREF_STAFF, "null");

        if(json.equals("null"))
        {

            return null;

        }else
        {
            return gson.fromJson(json, Staff.class);
        }
    }


}
