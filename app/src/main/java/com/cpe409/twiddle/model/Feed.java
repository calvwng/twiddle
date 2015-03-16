package com.cpe409.twiddle.model;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.text.DecimalFormat;

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
  private boolean favorited;
  private String distance;
  private byte[] imageData;

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
    feed.favorited = false;
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

  public boolean isFavorited() {
    return favorited;
  }

  public void setFavorited(boolean favorited) {
    this.favorited = favorited;
  }

  public void setImage(final ParseFile file) {
    if (file == null) {
      return;
    }

    try {
      this.imgUrl = file.getUrl();
      this.imageData = file.getData();
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public byte[] getImageData() {
      return this.imageData;
  }

  public String getDistance() {
    return this.distance;
  }

  public void setDistance(float distance) {
    DecimalFormat df = new DecimalFormat("#.#");
    this.distance = df.format(distance);
  }
}
