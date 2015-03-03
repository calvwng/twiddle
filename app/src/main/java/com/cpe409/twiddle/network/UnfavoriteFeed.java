package com.cpe409.twiddle.network;

import com.cpe409.twiddle.model.Feed;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Michael on 3/3/2015.
 */
public class UnfavoriteFeed {

  public static void unfavoriteFeed(Feed feed) {
    ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorite");
    query.whereEqualTo("user", ParseUser.getCurrentUser());
    query.whereEqualTo("adventureId", feed.getObjId());
    query.findInBackground(new FindCallback<ParseObject>() {
      @Override
      public void done(List<ParseObject> parseObjects, ParseException e) {
        for (ParseObject obj : parseObjects) {
          obj.deleteInBackground();
        }
      }
    });

    feed.setFavorited(false);
  }
}
