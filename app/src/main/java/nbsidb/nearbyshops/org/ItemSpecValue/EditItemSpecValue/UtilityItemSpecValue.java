package nbsidb.nearbyshops.org.ItemSpecValue.EditItemSpecValue;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import nbsidb.nearbyshops.org.ModelItemSpecification.ItemSpecificationName;
import nbsidb.nearbyshops.org.ModelItemSpecification.ItemSpecificationValue;
import nbsidb.nearbyshops.org.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sumeet on 19/10/16.
 */



public class UtilityItemSpecValue {

    public static final String TAG_ITEM_PREF = "tag_item_spec_value";


    public static void saveItemSpecValue(ItemSpecificationValue itemSpecName, Context context)
    {

        //Creating a shared preference

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = sharedPref.edit();

        if(itemSpecName == null)
        {
            prefsEditor.putString(TAG_ITEM_PREF, "null");

        }
        else
        {
            Gson gson = new Gson();
            String json = gson.toJson(itemSpecName);
            prefsEditor.putString(TAG_ITEM_PREF, json);
        }

        prefsEditor.apply();
    }


    public static ItemSpecificationValue getItemSpecName(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPref.getString(TAG_ITEM_PREF, "null");

        if(json.equals("null"))
        {

            return null;

        }else
        {
            return gson.fromJson(json, ItemSpecificationValue.class);
        }
    }



    // Item for Item ID






}
