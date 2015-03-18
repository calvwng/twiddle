package com.cpe409.twiddle.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;

import com.cpe409.twiddle.R;
import com.cpe409.twiddle.activities.AdventureActivity;
import com.cpe409.twiddle.adapters.CommentsListAdapter;
import com.cpe409.twiddle.model.Comment;
import com.cpe409.twiddle.model.FacebookUser;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Calvin on 3/16/2015.
 */
public class CommentListFragment extends ListFragment {

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    final ArrayList<Comment> commentList = new ArrayList<Comment>();
    ParseQuery commentQuery = new ParseQuery("Comment");
    commentQuery.whereEqualTo("adventureId", getActivity().getIntent().getExtras().getString(AdventureActivity.OBJ_ID));
    commentQuery.include("author"); // Include/fetch "author" pointer data with each result

//
    try {
      List<ParseObject> parseComments = commentQuery.find();
      for (ParseObject obj : parseComments) {
        ParseObject parseUser = (ParseObject)obj.get("author");
        Comment comment = Comment.ParseToFeed(obj, FacebookUser.ParseToFacebookUser(parseUser));
        commentList.add(comment);
      }
    }
    catch (Exception e) {
      System.out.println(e);
    }

    CommentsListAdapter commentsAdapter = new CommentsListAdapter(getActivity(), commentList);
    setListAdapter(commentsAdapter);

    //    commentQuery.findInBackground(new FindCallback<ParseObject>() {
//      @Override
//      public void done(List<ParseObject> parseObjects, ParseException e) {
//        for (ParseObject obj : parseObjects) {
//          ParseObject parseUser = obj.getParseObject("author");
//          Comment comment = Comment.ParseToFeed(obj, FacebookUser.ParseToFacebookUser(parseUser));
//          commentList.add(comment);
//        }
//
//        CommentsListAdapter commentsAdapter = new CommentsListAdapter(getActivity(), commentList);
//        setListAdapter(commentsAdapter);
//      }
//    });
  }
}
