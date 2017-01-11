package nbsidb.nearbyshops.org.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.google.gson.Gson;

import nbsidb.nearbyshops.org.ModelRoles.Admin;
import nbsidb.nearbyshops.org.ModelRoles.Staff;
import nbsidb.nearbyshops.org.MyApplication;
import nbsidb.nearbyshops.org.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sumeet on 25/9/16.
 */

public class UtilityLogin {


    public static final Integer ROLE_ADMIN = 1;
    public static final Integer ROLE_STAFF = 2;




    public static String getAuthorizationHeaders(Context context)
    {
        return UtilityLogin
                .baseEncoding(UtilityLogin.getUsername(context), UtilityLogin.getPassword(context));
    }

    public static String baseEncoding(String username,String password)
    {
        String credentials = username + ":" + password;
        // create Base64 encodet string
        String basic =
                "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        return basic;
    }





    public static void saveCredentials(Context context, String username, String password)
    {
        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username", username);
        editor.putString("password",password);
        editor.apply();
    }




    public static String getUsername(Context context)
    {
        context = MyApplication.getAppContext();

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        String service_url = sharedPref.getString("username", "");
        return service_url;
    }

    public static String getPassword(Context context)
    {
        context = MyApplication.getAppContext();

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        String service_url = sharedPref.getString("password", "");
        return service_url;
    }







    public static void setRoleID(Context context, int role_id)
    {
        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt("role", role_id);

        editor.apply();
    }


    public static int getRoleID(Context context) {

        context = MyApplication.getAppContext();
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        int role_id = sharedPref.getInt("role", -1);
        return role_id;
    }





    public static void saveAdmin(Admin admin, Context context)
    {

        if(context == null)
        {
            return;
        }

        //Creating a shared preference

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(admin);
        prefsEditor.putString("admin", json);
        prefsEditor.commit();
    }


    public static Admin getAdmin(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPref.getString("admin", "null");

        if(json.equals("null"))
        {

            return null;

        }else
        {
            Admin admin = gson.fromJson(json, Admin.class);
            return admin;
        }

    }




    public static void saveStaff(Staff staff, Context context)
    {

        //Creating a shared preference

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(staff);
        prefsEditor.putString("staff", json);
        prefsEditor.commit();
    }


    public static Staff getStaff(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPref.getString("staff", "null");

        if(json.equals("null"))
        {

            return null;

        }else
        {
            return gson.fromJson(json, Staff.class);
        }

    }




}
