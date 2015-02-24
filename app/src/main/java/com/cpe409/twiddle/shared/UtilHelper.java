package com.cpe409.twiddle.shared;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.widget.Toast;

/**
 * Created by Michael on 2/17/2015.
 */
public class UtilHelper {

  public static void throwToastError(Context context, Exception e) {
    Toast.makeText(context, "Error: " + e.toString(), Toast.LENGTH_LONG).show();
  }

  public static int dpToPx(int dp) {
    DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
    float px = dp * (metrics.densityDpi / 160f);
    return Math.round(px);
  }
}
