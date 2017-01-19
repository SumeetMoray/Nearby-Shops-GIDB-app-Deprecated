package nbsidb.nearbyshops.org.EditProfileAdmin.NotRequired;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;


import nbsidb.nearbyshops.org.ModelRoles.Admin;
import nbsidb.nearbyshops.org.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sumeet on 25/9/16.
 */

public class UtilityAdmin {

    private static final String TAG_PREF_ADMIN = "admin_profile";

    public static void saveAdmin(Admin admin, Context context)
    {

        //Creating a shared preference
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(admin);
        prefsEditor.putString(TAG_PREF_ADMIN, json);
        prefsEditor.apply();
    }


    public static Admin getAdmin(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPref.getString(TAG_PREF_ADMIN, "null");

        if(json.equals("null"))
        {

            return null;

        }else
        {
            return gson.fromJson(json, Admin.class);
        }
    }


}
