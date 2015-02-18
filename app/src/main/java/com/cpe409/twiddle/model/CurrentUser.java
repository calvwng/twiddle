package com.cpe409.twiddle.model;

import com.facebook.android.Facebook;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Michael on 2/18/2015.
 */
public class CurrentUser extends FacebookUser{

  private static CurrentUser instance;

  public static CurrentUser getInstance() {
    if (instance == null) {
      instance = new CurrentUser(ParseUser.getCurrentUser());
    }
    return instance;
  }

  private CurrentUser(ParseObject object) {
    super(object);
  }

}
