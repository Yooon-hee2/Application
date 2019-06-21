package b1g4.com.yourseat;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class SavedSharedPreference {
    private static String TAG = "SavedSharedPreference";

    static final String PREF_START_ADDRESS = "start_address";
    static final String PREF_END_ADDRESS = "end_address";
    static final String PREF_ADDRESS_LIST = "address_list";

    //즐겨찾기를 위해 추가
    static final String STAR_LIST_ADDRESS = "starlist_address";

    static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setStarListAddress(Context context, ArrayList<String> starAddressList) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();

        editor.remove(STAR_LIST_ADDRESS);
        editor.apply();

        JSONArray jsonArray =  new JSONArray();
        for (int i = 0; i < starAddressList.size(); i++)
            jsonArray.put(starAddressList.get(i));

        if (!starAddressList.isEmpty())
            editor.putString(STAR_LIST_ADDRESS, jsonArray.toString());
        else
            editor.putString(STAR_LIST_ADDRESS, null);
        editor.apply();
    }

    public static ArrayList<String> getStarListAddress(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        String json = sharedPreferences.getString(STAR_LIST_ADDRESS, null);

        ArrayList<String> stringArrayList = new ArrayList<String>();
        if (json != null) {
            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    String address = jsonArray.optString(i);
                    stringArrayList.add(address);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return stringArrayList;
    }

    public static void setAddressList(Context context, ArrayList<String> addressList) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();

        editor.remove(PREF_ADDRESS_LIST);
        editor.apply();

        JSONArray jsonArray =  new JSONArray();
        for (int i = 0; i < addressList.size(); i++)
            jsonArray.put(addressList.get(i));

        if (!addressList.isEmpty())
            editor.putString(PREF_ADDRESS_LIST, jsonArray.toString());
        else
            editor.putString(PREF_ADDRESS_LIST, null);
        editor.apply();
        Log.d(TAG, jsonArray.toString());
        Log.d(TAG, String.valueOf(jsonArray.length()));

    }

    public static ArrayList<String> getAddressList(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        String json = sharedPreferences.getString(PREF_ADDRESS_LIST, null);

        ArrayList<String> addressList = new ArrayList<String>();
        if (json != null) {
            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    String address = jsonArray.optString(i);
                    addressList.add(address);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "Route : " + addressList.toString());
        return addressList;
    }

    public static void deleteAll(Context context) {
        Log.i(TAG, "Delete All");
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.clear();
        editor.commit();
    }
}
