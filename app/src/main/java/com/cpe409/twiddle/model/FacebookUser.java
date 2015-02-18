package com.cpe409.twiddle.model;

import com.parse.ParseObject;

/**
 * Created by Michael on 2/17/2015.
 */
public class FacebookUser {

  private String userId;
  private String name;

  private final static String FB_GRAPH_URL = "http://graph.facebook.com/";
  private final static String FB_PICTURE_URL = "/picture?type=large";

   public static FacebookUser ParseToFacebookUser(ParseObject user) {
    FacebookUser fbUser = new FacebookUser();
    fbUser.userId = user.getString("fbId");
    fbUser.name = user.getString("fbName");
    return fbUser;
  }

  public String getUserId() {
    return userId;
  }

  public String getName() {
    return name;
  }

  public String getImageURL() {
    return FB_GRAPH_URL + this.userId + FB_PICTURE_URL;
  }
}
