package com.cpe409.twiddle.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.cpe409.twiddle.R;
import com.cpe409.twiddle.activities.CreateActivity;
import com.cpe409.twiddle.adapters.FeedListAdapter;
import com.cpe409.twiddle.model.FacebookUser;
import com.cpe409.twiddle.model.Feed;
import com.cpe409.twiddle.shared.LocationHelper;
import com.cpe409.twiddle.shared.UtilHelper;
import com.melnykov.fab.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedFragment extends Fragment {

  private ListView listView;
  private FeedListAdapter listAdapter;
  private List<Feed> feedList;
  private Activity activity;
  private Context context;
  private FloatingActionButton floatingActionButton;

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @return A new instance of fragment FeedFragment.
   */
  public static FeedFragment newInstance() {
    FeedFragment fragment = new FeedFragment();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  public FeedFragment() {
    // Required empty public constructor
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    setupReferences();
    setupListeners();
    
    feedList = new ArrayList<Feed>();
    listAdapter = new FeedListAdapter(activity.getApplicationContext(), feedList);
    listView.setAdapter(listAdapter);

    Location location = LocationHelper.getInstance().getLocation(context);
    if (location == null) {
      Toast.makeText(context, "Couldn't find location.", Toast.LENGTH_SHORT).show();
      return;
    }

    double lat = location.getLatitude();
    double lon = location.getLongitude();
    double earthRadius = 6371;  // earth radius in km
    double radius = 100; // km
    double longMin = lon - Math.toDegrees(radius / earthRadius / Math.cos(Math.toRadians(lat)));
    double longMax = lon + Math.toDegrees(radius / earthRadius / Math.cos(Math.toRadians(lat)));
    double latMax = lat + Math.toDegrees(radius / earthRadius);
    double latMin = lat - Math.toDegrees(radius / earthRadius);

    queryFeedStories(latMax, latMin, longMax, longMin);
  }

  private void setupReferences() {
    listView = (ListView) activity.findViewById(R.id.feedListView);
    floatingActionButton = (FloatingActionButton) activity.findViewById(R.id.fab);
  }

  private void setupListeners() {
    floatingActionButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent i = new Intent(context, CreateActivity.class);
        startActivity(i);
      }
    });
  }


  private void queryFeedStories(final double latMax, final double latMin, final double lonMax,
                                final double lonMin) {
    ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Adventure");
    query.include("adventureLocation");
    query.include("author");

    query.findInBackground(new FindCallback<ParseObject>() {
      @Override
      public void done(List<ParseObject> parseObjects, ParseException e) {
        if (e != null) {
          UtilHelper.throwToastError(context, e);
          return;
        }

        for (ParseObject adventure : parseObjects) {
          ParseObject location = adventure.getParseObject("adventureLocation");
          double lat = location.getDouble("locationLatitude");
          double lon = location.getDouble("locationLongitude");

          if (lat < latMax && lat > latMin) {
            if (lon < lonMax && lon > lonMin) {
              ParseObject author = adventure.getParseObject("author");
              Feed feed = Feed.ParseToFeed(adventure, FacebookUser.ParseToFacebookUser(author));
              feedList.add(feed);
            }
          }
        }

        listAdapter.notifyDataSetChanged();
      }
    });
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_feed, container, false);
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    this.activity = activity;
    this.context = activity.getApplicationContext();
  }

  @Override
  public void onDetach() {
    super.onDetach();
    activity = null;
    context = null;
  }

}
