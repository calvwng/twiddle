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
    return new FacebookUser(user);
  }

  public static String fbIdtoPhotoUrl(String fbId) {
    return FB_GRAPH_URL + fbId + FB_PICTURE_URL;
  }

  public FacebookUser(ParseObject user) {
    this.userId = user.getString("fbId");
    this.name = user.getString("fbName");
  }

  public FacebookUser() {}


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
