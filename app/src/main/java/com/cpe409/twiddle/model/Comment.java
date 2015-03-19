package com.cpe409.twiddle.model;

import com.parse.ParseObject;

/**
 * Created by Calvin on 3/16/2015.
 */
public class Comment {
  private FacebookUser author;
  private String text;

  public Comment() {

  }

  public Comment(FacebookUser author, String text) {
    this.author = author;
    this.text = text;
  }

  public static Comment ParseToFeed(ParseObject parseComment, FacebookUser author) {
    Comment comment = new Comment();
    comment.author = author;
    comment.text = parseComment.getString("text");
    return comment;
  }

  public FacebookUser getAuthor() {
    return author;
  }

  public String getText() {
    return this.text;
  }

}
