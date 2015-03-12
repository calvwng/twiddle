package com.cpe409.twiddle.shared;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.widget.Toast;

/**
 * Created by Michael on 2/17/2015.
 */
public class UtilHelper {

  public static void throwToastError(Context context, Exception e) {
    Toast.makeText(context, "Error: " + e.toString(), Toast.LENGTH_LONG).show();
  }

  public static void throwToastError(Context context, String message) {
    Toast.makeText(context, "Error: " + message, Toast.LENGTH_LONG).show();
  }


  public static int dpToPx(int dp) {
    DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
    float px = dp * (metrics.densityDpi / 160f);
    return Math.round(px);
  }

  public static boolean isNetworkOnline(Context context) {
    boolean status = false;
    try {
      ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo netInfo = cm.getNetworkInfo(0);
      if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
        status = true;
      } else {
        netInfo = cm.getNetworkInfo(1);
        if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED)
          status = true;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return status;
  }
}
