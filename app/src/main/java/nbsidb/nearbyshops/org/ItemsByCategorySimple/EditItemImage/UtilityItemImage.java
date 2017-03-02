package nbsidb.nearbyshops.org.ItemsByCategorySimple.EditItemImage;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import nbsidb.nearbyshops.org.Model.Item;
import nbsidb.nearbyshops.org.Model.ItemImage;
import nbsidb.nearbyshops.org.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sumeet on 19/10/16.
 */



public class UtilityItemImage {

    public static final String TAG_ITEM_PREF = "tag_item_image";


    public static void saveItemImage(ItemImage itemImage, Context context)
    {

        //Creating a shared preference

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = sharedPref.edit();

        if(itemImage == null)
        {
            prefsEditor.putString(TAG_ITEM_PREF, "null");

        }
        else
        {
            Gson gson = new Gson();
            String json = gson.toJson(itemImage);
            prefsEditor.putString(TAG_ITEM_PREF, json);
        }

        prefsEditor.apply();
    }


    public static ItemImage getItemImage(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPref.getString(TAG_ITEM_PREF, "null");

        if(json.equals("null"))
        {

            return null;

        }else
        {
            return gson.fromJson(json, ItemImage.class);
        }
    }



    // Item for Item ID






}
