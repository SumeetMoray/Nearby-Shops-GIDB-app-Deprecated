package nbsidb.nearbyshops.org.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import nbsidb.nearbyshops.org.MyApplication;
import nbsidb.nearbyshops.org.R;


/**
 * Created by sumeet on 5/5/16.
 */
public class UtilityGeneral {


//
//    public static void saveDistributorID(int distributorID)
//    {
//        Context context = MyApplication.getAppContext();
//        // Get a handle to shared preference
//        SharedPreferences sharedPref;
//        sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), context.MODE_PRIVATE);
//
//        // write to the shared preference
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putInt(context.getString(R.string.preference_distributor_id_key),distributorID);
//        editor.commit();
//
//    }
//
//    public static int getDistributorID(Context context) {
//        // Get a handle to shared preference
//        SharedPreferences sharedPref;
//        sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), context.MODE_PRIVATE);
//
//        // read from shared preference
//        int distributorID = sharedPref.getInt(context.getString(R.string.preference_distributor_id_key), 0);
//        return distributorID;
//    }



    public static void saveServiceURL(String service_url,Context context)
    {

//        Context context = MyApplication.getAppContext();
        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                context.MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(
                context.getString(R.string.preference_service_url_key),
                service_url);

        editor.commit();
    }

    public static String getServiceURL(Context context) {

//        context = MyApplication.getAppContext();

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), Context.MODE_PRIVATE);
        String service_url = sharedPref.getString(context.getString(R.string.preference_service_url_key), "http://nbsidb.nearbyshops.org");

        //http://192.168.1.33:5000
        //service_url = "http://localareademo-env.ap-southeast-1.elasticbeanstalk.com";

        return service_url;
    }


//    public static boolean isNetworkAvailable(Context context) {
//        ConnectivityManager connectivityManager
//                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//    }
//


    public static void saveServiceURL_GIDB(String service_url,Context context)
    {
//        Context context = MyApplication.getAppContext();
        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), Context.MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(context.getString(R.string.preference_service_url_gidb_key), service_url);
        editor.apply();
    }

    public static String getServiceURL_GIDB(Context context) {

//        context = MyApplication.getAppContext();

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), Context.MODE_PRIVATE);
        String service_url = sharedPref.getString(context.getString(R.string.preference_service_url_gidb_key), "http://192.168.1.33:5000");

        //service_url = "http://localareademo-env.ap-southeast-1.elasticbeanstalk.com";

        return service_url;
    }



}
