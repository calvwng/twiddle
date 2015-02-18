package com.cpe409.twiddle.shared;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Michael on 2/17/2015.
 */
public class UtilHelper {

  public static void throwToastError(Context context, Exception e) {
    Toast.makeText(context, "Error: " + e.toString(), Toast.LENGTH_LONG).show();
  }
}
