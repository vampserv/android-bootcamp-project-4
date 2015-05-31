package com.codepath.apps.twitterclient.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateUtils;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by edwardyang on 5/19/15.
 */
public class TweetUtilities {

    // an error handler for twitter error responses to display a toast with one or more error messages returned from twitter
    public static void displayTwitterError(Context context, String action, JSONObject error) {
        if (error == null) {
            Toast.makeText(context, "Error " + action + ", please try again later", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            JSONArray errorArray = error.getJSONArray("errors");
            String errors = "";
            for (int i = 0; i < errorArray.length(); i++) {
                if (i != 0) errors += ", ";
                errors += errorArray.getJSONObject(i).getString("message");
            }
            Toast.makeText(context, "Error " + action + ": " + errors, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            Toast.makeText(context, "Error " + action + ", please try again later", Toast.LENGTH_SHORT).show();
        }
    }

    public static String getRelativeTimeAgo(String rawJsonDate) {

        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    // check network connectivity
    public static Boolean isNetworkAvailable(Context context) {
//        try {
//            Process p1 = java.lang.Runtime.getRuntime().exec("ping -n 1 www.google.com");
//            int returnVal = p1.waitFor();
//            boolean reachable = (returnVal==0);
//            return reachable;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static String ArrayListToString(ArrayList<String> array, String prefix, String delimiter) {
        String res = "";
        for (String s : array) {
            res += prefix + s + delimiter;
        }
        return res;
    }

}
