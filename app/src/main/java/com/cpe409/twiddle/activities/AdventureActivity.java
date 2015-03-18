package com.cpe409.twiddle.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cpe409.twiddle.R;
import com.cpe409.twiddle.adapters.CommentsListAdapter;
import com.cpe409.twiddle.model.Comment;
import com.cpe409.twiddle.model.CurrentUser;
import com.cpe409.twiddle.model.FacebookUser;
import com.cpe409.twiddle.views.ExpandableTextView;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdventureActivity extends ActionBarActivity {


  public static final String TITLE = "adventure_title";
  public static final String OBJ_ID = "object_id";
  public static final String DESCRIPTION = "description";
  public static final String IMAGE_URL = "image_url";
  public static final String IMAGE_DATA = "image_data";
  public static final String IS_LIKED = "is_liked";
  public static final String LIKE_COUNT = "like_count";

  private String title;
  private String objId;
  private String description;
  private String imgUrl;
  private byte[] imgData;
  private boolean isLiked;
  private int likeCount;

  private ObservableListView listView;
  private CommentsListAdapter commentsAdapter;
  private ArrayList<Comment> commentList;
  private ImageButton likeButton;

  private TextView socialCounters;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_adventure);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    View header = getLayoutInflater().inflate(R.layout.view_activity_descr, null);


    title = getIntent().getExtras().getString(TITLE);
    objId = getIntent().getExtras().getString(OBJ_ID);
    description = getIntent().getExtras().getString(DESCRIPTION);
    isLiked = getIntent().getExtras().getBoolean(IS_LIKED);
    likeCount = getIntent().getExtras().getInt(LIKE_COUNT);
    imgData = getIntent().getExtras().getByteArray(IMAGE_DATA);
    imgUrl = getIntent().getExtras().getString(IMAGE_URL);

    ExpandableTextView descriptView = (ExpandableTextView) header.findViewById(R.id.adventure_description);
    socialCounters = (TextView) header.findViewById(R.id.text_social_counters);
    ImageView adventureImage = (ImageView) header.findViewById(R.id.adventure_image);
    listView = (ObservableListView) findViewById(R.id.list);

    listView.addHeaderView(header);
    commentList = new ArrayList<Comment>();
    commentsAdapter = new CommentsListAdapter(this, commentList);
    listView.setAdapter(commentsAdapter);
    queryData();

    setTitle(title);
    descriptView.setText(description);
    socialCounters.setText(likeCount + " likes, " + 0 + " comments");


    if (imgData != null) {
      Bitmap bitmap = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
      adventureImage.setImageBitmap(bitmap);
    } else if (imgUrl != null) {
      Picasso.with(this).load(imgUrl).into(adventureImage);
    } else {
      adventureImage.setImageResource(R.drawable.bg_nav_drawer);
    }

    likeButton = (ImageButton) findViewById(R.id.btn_like);
    likeButton.setImageResource(isLiked ? R.drawable.ic_heart_red : R.drawable.ic_heart_outline_white);

    // TODO: Update the FeedListAdapter's feedList's like data upon changing it here
    likeButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!CurrentUser.getInstance().isLoggedIn()) {
          Toast.makeText(getApplicationContext(), "Please log in before liking.", Toast.LENGTH_SHORT).show();
          return;
        }

        // Like/unlike the feed
        if (isLiked) {
          unlikeFeed();
          isLiked = false;
          likeCount--;
          likeButton.setImageResource(R.drawable.ic_heart_outline_white);
        } // Unlike the feed
        else {
          likeFeed();
          isLiked = true;
          likeCount++;
          likeButton.setImageResource(R.drawable.ic_heart_red);
        }
        socialCounters.setText(likeCount + " likes, " + 0 + " comments");
      }
    });
  }

  private void queryData() {
    commentList.clear();
    ParseQuery commentQuery = new ParseQuery("Comment");
    commentQuery.whereEqualTo("adventureId", objId);
    commentQuery.include("author"); // Include/fetch "author" pointer data with each result
    commentQuery.findInBackground(new FindCallback<ParseObject>() {
      @Override
      public void done(List<ParseObject> parseObjects, ParseException e) {
        for (ParseObject obj : parseObjects) {
          ParseObject parseUser = obj.getParseObject("author");
          Comment comment = Comment.ParseToFeed(obj, FacebookUser.ParseToFacebookUser(parseUser));
          commentList.add(comment);
        }
        commentsAdapter.notifyDataSetChanged();
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_adventure, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_settings:
        return true;
      case android.R.id.home:
        onBackPressed();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void likeFeed() {
    ParseObject like = new ParseObject("Like");
    like.put("user", ParseUser.getCurrentUser());
    like.put("adventureId", objId);
    like.saveInBackground();

    ParseQuery<ParseObject> query = ParseQuery.getQuery("Adventure");
    query.getInBackground(objId, new GetCallback<ParseObject>() {
      @Override
      public void done(ParseObject parseObject, ParseException e) {
        parseObject.put("likes", parseObject.getInt("likes") + 1);
        parseObject.saveInBackground();
      }
    });
  }

  private void unlikeFeed() {
    ParseQuery<ParseObject> query = ParseQuery.getQuery("Like");
    query.whereEqualTo("user", ParseUser.getCurrentUser());
    query.whereEqualTo("adventureId", objId);
    query.findInBackground(new FindCallback<ParseObject>() {
      @Override
      public void done(List<ParseObject> parseObjects, ParseException e) {
        for (ParseObject obj : parseObjects) {
          obj.deleteInBackground();
        }
      }
    });

    ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Adventure");
    query2.getInBackground(objId, new GetCallback<ParseObject>() {
      @Override
      public void done(ParseObject parseObject, ParseException e) {
        parseObject.put("likes", parseObject.getInt("likes") - 1);
        parseObject.saveInBackground();
      }
    });
  }
}
