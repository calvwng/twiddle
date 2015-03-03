package com.cpe409.twiddle.model;

import com.facebook.android.Facebook;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Michael on 2/18/2015.
 */
public class CurrentUser extends FacebookUser {

  private static CurrentUser instance;
  private boolean loggedIn;

  public static CurrentUser getInstance() {
    if (instance == null) {
      if (ParseUser.getCurrentUser() == null) {
        instance = new CurrentUser();
      } else {
        instance = new CurrentUser(ParseUser.getCurrentUser());
      }
    }
    return instance;
  }

  private CurrentUser() {
    super();
    loggedIn = false;
  }

  private CurrentUser(ParseObject object) {
    super(object);
    loggedIn = true;
  }

  public boolean isLoggedIn() {
    return loggedIn;
  }

  public void logout() {
    this.instance = null;
  }

}
