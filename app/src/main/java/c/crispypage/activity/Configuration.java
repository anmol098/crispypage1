package c.crispypage.activity;

/**
 * Created by Anmol Pratap Singh on 07-12-2017.
 */
import android.support.annotation.NonNull;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Configuration {


    public static final String TAG = "TAG";

    public static final String APP_SCRIPT_WEB_APP_URL = "https://script.google.com/macros/s/AKfycbxAQ-JRJ_C4o8BdDCNlD42qv0nsTMyaXFAgrWjvM4xnuxQIcAkR/exec";
    public static final String ADD_USER_URL = APP_SCRIPT_WEB_APP_URL;
    private static Response response;
    public static final String KEY_TIME = "uTime";
    public static final String KEY_NAME = "uName";
    public static final String KEY_EMAIL = "uEmail";
    public static final String KEY_COPIES = "uCopies";
    public static final String KEY_CONTACT = "uContact";
    public static final String KEY_ADDRESS = "uAddress";
    public static final String KEY_SUGGEST = "uSuggest";
    public static final String KEY_URL = "uLink";
    public static final String KEY_ID = "uId";
    public static final String KEY_BIND = "uBind";
    public static final String KEY_ORIENTATION = "uOrientation";
    public static final String KEY_AREA = "uArea";
    public static final String KEY_CHOICE = "uChoice";
    public static final String KEY_DELIVERY="uDelivery";
    public  static final String KEY_ACTION = "action";

    public static JSONArray readData(String email) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(APP_SCRIPT_WEB_APP_URL+"?action=read&uEmail="+email)
                    .build();
            response = client.newCall(request).execute();
            // Log.e(TAG,"response from gs"+response.body().string());
            return new JSONArray(response.body().string());


        } catch (@NonNull IOException | JSONException e) {
            Log.e(TAG, "recieving null " + e.getLocalizedMessage());
        }
        return null;
    }
}
