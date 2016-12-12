package com.example.astroboy.myneusd.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by AstroBoy on 2016/12/1.
 */

public class InternetConnectable {

    public static boolean is_internet(Context context){
        if (context != null){
            ConnectivityManager mconnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mconnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null){
                return  mNetworkInfo.isAvailable();
            }
        }
        return  false;
    }

}
