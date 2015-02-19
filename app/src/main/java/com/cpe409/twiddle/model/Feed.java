package com.cpe409.twiddle.model;

import com.parse.ParseObject;

/**
 * Created by Michael on 2/17/2015.
 */
public class Feed {
  private String objId;
  private String title;
  private String imgUrl;
  private String description;
  private int likesCount;
  private FacebookUser author;
  private boolean liked;

  public Feed() {

  }

  public Feed(String objId, String title, String imgUrl, int likesCount) {
    this.objId = objId;
    this.title = title;
    this.imgUrl = imgUrl;
    this.likesCount = likesCount;
  }


  public static Feed ParseToFeed(ParseObject adventure, FacebookUser author) {
    Feed feed = new Feed();
    feed.title = adventure.getString("adventureTitle");
    feed.objId = adventure.getObjectId();
    feed.description = adventure.getString("adventureDescription");
    feed.likesCount = adventure.getInt("likes");
    feed.author = author;
    feed.liked = false;
    return feed;
  }

  public FacebookUser getAuthor() {
    return author;
  }

  public String getObjId() {
    return objId;
  }

  public String getTitle() {
    return title;
  }

  public String getImgUrl() {
    return imgUrl;
  }

  public int getLikesCount() {
    return likesCount;
  }

  public boolean getIsLiked() {
    return liked;
  }

  public void setLikesCount(int count) {
    this.likesCount = count;
  }

  public void setLiked(Boolean liked) {
    this.liked = liked;
  }
}
