package com.cpe409.twiddle.application;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Base64;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Michael on 2/5/2015.
 */
public class TwiddleApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    Parse.enableLocalDatastore(this);
    Parse.initialize(this, "rydmOwn5WkEFwjyQtjvgCXru0T26dUmGS1qFwmlH", "I9yJ6oOQ6sEecHGBBCC2AnPJWNOaBxESOrFzHk59");
  }
}
