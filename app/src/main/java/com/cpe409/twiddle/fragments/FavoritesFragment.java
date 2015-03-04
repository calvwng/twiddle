package com.cpe409.twiddle.fragments;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.cpe409.twiddle.R;
import com.cpe409.twiddle.activities.CreateActivity;
import com.cpe409.twiddle.adapters.FeedListAdapter;
import com.cpe409.twiddle.model.CurrentUser;
import com.cpe409.twiddle.model.FacebookUser;
import com.cpe409.twiddle.model.Feed;
import com.cpe409.twiddle.network.FavoriteFeed;
import com.cpe409.twiddle.network.UnfavoriteFeed;
import com.cpe409.twiddle.shared.FeedContextMenuManager;
import com.cpe409.twiddle.shared.LocationHelper;
import com.cpe409.twiddle.shared.UtilHelper;
import com.cpe409.twiddle.views.FeedContextMenu;
import com.melnykov.fab.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Use the {@link com.cpe409.twiddle.fragments.FavoritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoritesFragment extends Fragment implements FeedListAdapter.OnFeedItemClickListener,
    FeedContextMenu.OnFeedContextMenuItemClickListener {

  private Set<String> feedLikes;
  private Set<String> feedFavorites;
  private Activity activity;

  private ListView listView;
  private FeedListAdapter listAdapter;
  private List<Feed> feedList;
  private Context context;
  private Location location;
  private SwipeRefreshLayout refreshLayout;
  private FloatingActionButton floatingActionButton;
  private String searchQuery;

  public static final String SEARCH_QUERY_ARG = "SEARCH_QUERY_TAG";
  private static final float MetersToMiles = 0.000621371f;
  private static final String TAG = FavoritesFragment.class.getSimpleName();

  /**
   * Use this factory method tMaterialNavigationDrawero create a new instance of
   * this fragment using the provided parameters.
   *
   * @return A new instance of fragment FeedFragment.
   */
  public static FavoritesFragment newInstance() {
    Bundle args = new Bundle();
    return newInstance(args);
  }

  public static FavoritesFragment newInstance(Bundle args) {
    FavoritesFragment fragment = new FavoritesFragment();
    fragment.setArguments(args);
    return fragment;
  }

  public FavoritesFragment() {
    // Required empty public constructor
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    this.setHasOptionsMenu(true);
    setupReferences();
    setupListeners();
    checkUserAccess();

    floatingActionButton.setVisibility(View.GONE);

    Bundle args = getArguments();
    searchQuery = args.getString(SEARCH_QUERY_ARG, "");

    feedList = new ArrayList<>();
    feedLikes = new HashSet<>();
    feedFavorites = new HashSet<>();

    listAdapter = new FeedListAdapter(activity.getApplicationContext(), feedList);
    listAdapter.setOnFeedItemClickListener(this);
    listView.setAdapter(listAdapter);
    listView.setOnScrollListener(new AbsListView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(AbsListView view, int scrollState) {

      }

      @Override
      public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        FeedContextMenuManager.getInstance().onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
      }
    });


    refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        queryFeedStories();
      }
    });
    new Handler().postDelayed(new Runnable() {

      @Override
      public void run() {
        refreshLayout.setRefreshing(true);
      }
    }, 1);

    queryFeedStories();
  }

  private void setupReferences() {
    listView = (ListView) activity.findViewById(R.id.feedListView);
    floatingActionButton = (FloatingActionButton) activity.findViewById(R.id.fab);
    refreshLayout = (SwipeRefreshLayout) activity.findViewById(R.id.feed_refresh_layout);
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

  private void checkUserAccess() {
    int visibility = CurrentUser.getInstance().isLoggedIn() ? View.VISIBLE : View.INVISIBLE;
  }

  /**
   * This starts the network queries. Goes in the order:
   * likes -> favorites -> feeds
   * TODO: Launch the likes and favorites asynchornously and then launch feed query when done.
   */
  private void queryFeedStories() {
    Log.d(TAG, "Querying Feed Stories.");
    queryLikes();
  }

  private void queryLikes() {
    if (!UtilHelper.isNetworkOnline(context)) {
      UtilHelper.throwToastError(context, "No network connection.");
      refreshLayout.setRefreshing(false);
      return;
    }

    Log.d(TAG, "Querying Likes");
    ParseQuery<ParseObject> query = ParseQuery.getQuery("Like");
    query.whereEqualTo("user", ParseUser.getCurrentUser());
    query.findInBackground(new FindCallback<ParseObject>() {
      @Override
      public void done(List<ParseObject> parseObjects, ParseException e) {
        if (e != null) {
          UtilHelper.throwToastError(context, e);
          refreshLayout.setRefreshing(false);
          return;
        }

        feedLikes.clear();
        Log.d(TAG, "Found " + parseObjects.size() + " likes");
        for (ParseObject obj : parseObjects) {
          feedLikes.add(obj.getString("adventureId"));
        }

        queryFavorites();
      }
    });
  }

  private void queryFavorites() {
    if (!UtilHelper.isNetworkOnline(context)) {
      UtilHelper.throwToastError(context, "No network connection.");
      refreshLayout.setRefreshing(false);
      return;
    }

    Log.d(TAG, "Querying favorites");
    ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorite");
    query.whereEqualTo("user", ParseUser.getCurrentUser());
    query.findInBackground(new FindCallback<ParseObject>() {
      @Override
      public void done(List<ParseObject> parseObjects, ParseException e) {
        if (e != null) {
          UtilHelper.throwToastError(context, e);
          refreshLayout.setRefreshing(false);
          return;
        }

        feedFavorites.clear();
        Log.d(TAG, "Found " + parseObjects.size() + " favorites");
        for (ParseObject obj : parseObjects) {
          feedFavorites.add(obj.getString("adventureId"));
        }

        queryAdventures();
      }
    });
  }

  private void queryAdventures() {
    if (!UtilHelper.isNetworkOnline(context)) {
      UtilHelper.throwToastError(context, "No network connection.");
      refreshLayout.setRefreshing(false);
      return;
    }

    location = LocationHelper.getInstance().getLocation(context);
    if (location == null) {
      refreshLayout.setRefreshing(false);
      Toast.makeText(context, "Couldn't find location.", Toast.LENGTH_SHORT).show();
      return;
    }

    double lat = location.getLatitude();
    double lon = location.getLongitude();
    double earthRadius = 6371;  // earth radius in km
    double radius = 100; // km
    final double lonMin = lon - Math.toDegrees(radius / earthRadius / Math.cos(Math.toRadians(lat)));
    final double lonMax = lon + Math.toDegrees(radius / earthRadius / Math.cos(Math.toRadians(lat)));
    final double latMax = lat + Math.toDegrees(radius / earthRadius);
    final double latMin = lat - Math.toDegrees(radius / earthRadius);

    ParseQuery<ParseObject> query = new ParseQuery<>("Adventure");
    query.whereContainedIn("objectId", new ArrayList<String>(feedFavorites));
    query.include("author");

    Log.d(TAG, "Querying favorites");
    query.findInBackground(new FindCallback<ParseObject>() {
      @Override
      public void done(List<ParseObject> parseObjects, ParseException e) {
        if (e != null) {
          UtilHelper.throwToastError(context, e);
          refreshLayout.setRefreshing(false);
          return;
        }

        feedList.clear();

        Log.d(TAG, "Found " + parseObjects.size() + " adventures");
        for (ParseObject adventure : parseObjects) {
          String adventureDescriptor = adventure.getString("adventureTitle");
          adventureDescriptor += " " + adventure.getString("adventureDescription");

          if (adventureDescriptor.toLowerCase().contains(searchQuery.toLowerCase())) {
            ParseObject author = adventure.getParseObject("author");
            Feed feed = Feed.ParseToFeed(adventure, FacebookUser.ParseToFacebookUser(author));
            Location feedLocation = new Location("");
            feedLocation.setLatitude(adventure.getDouble("locationLatitude"));
            feedLocation.setLongitude(adventure.getDouble("locationLongitude"));
            feed.setDistance(feedLocation.distanceTo(location) * MetersToMiles);
            feed.setLiked(feedLikes.contains(feed.getObjId()));
            feed.setFavorited(feedFavorites.contains((feed.getObjId())));
            feed.setImage(adventure.getParseFile("image"));
            feedList.add(feed);
          }
        }

        listAdapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
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
  public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
    menuInflater.inflate(R.menu.menu_feed, menu);

    SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
    SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

    SearchableInfo searchableInfo = searchManager.getSearchableInfo(getActivity().getComponentName());
    searchView.setSearchableInfo(searchableInfo);
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

  @Override
  public void onReportClick(Feed feed) {
    FeedContextMenuManager.getInstance().hideContextMenu();
  }

  @Override
  public void onShareClick(Feed feed) {
    FeedContextMenuManager.getInstance().hideContextMenu();
  }

  @Override
  public void onCancelClick(Feed feed) {
    FeedContextMenuManager.getInstance().hideContextMenu();
  }

  @Override
  public void onFavoritesClick(Feed feed) {
    FeedContextMenuManager.getInstance().hideContextMenu();
    if (feed.isFavorited()) {
      UnfavoriteFeed.unfavoriteFeed(feed);
    } else {
      FavoriteFeed.favoriteFeed(feed);
    }
  }

  @Override
  public void onCommentsClick(View v, Feed feed) {
    //TODO
    return;
  }

  @Override
  public void onMoreClick(View v, Feed feed) {
    Log.d(TAG, "More clicked");
    FeedContextMenuManager.getInstance().toggleContextMenuFromView(v, feed, this);
  }
}
