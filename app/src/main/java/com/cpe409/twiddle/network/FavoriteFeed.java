package com.cpe409.twiddle.network;

import com.cpe409.twiddle.model.Feed;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Michael on 3/3/2015.
 */
public class FavoriteFeed {

  public static void favoriteFeed(Feed feed) {
    ParseObject fav = new ParseObject("Favorite");
    fav.put("user", ParseUser.getCurrentUser());
    fav.put("adventureId", feed.getObjId());
    fav.saveInBackground();

    feed.setFavorited(true);
  }
}
