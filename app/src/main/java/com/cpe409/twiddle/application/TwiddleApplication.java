package com.cpe409.twiddle.application;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

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
