package com.cpe409.twiddle.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.cpe409.twiddle.R;
import com.cpe409.twiddle.fragments.FeedFragment;
import com.cpe409.twiddle.model.AdventureLocation;
import com.cpe409.twiddle.model.Feed;

public class FilteredFeedActivity extends ActionBarActivity {

  private AdventureLocation peekLocation;
  private boolean active;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_filtered_feed);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    active = false;
    peekLocation = (AdventureLocation) getIntent().getSerializableExtra(FeedFragment.LOCATION_ARG);
    handleIntent(getIntent());
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem menuItem) {
    switch (menuItem.getItemId()) {
      case android.R.id.home:
        onBackPressed();
    }

    return super.onOptionsItemSelected(menuItem);
  }

  @Override
  protected void onNewIntent(Intent intent) {
    if (active) {
      // Only handles the Intent again if the Activity was already active
      handleIntent(intent);
    }
  }

  @Override
  public void startActivity(Intent intent) {
    super.startActivity(intent);

    if (intent.getAction().equals(Intent.ACTION_SEARCH)) {
      intent.putExtra(FeedFragment.LOCATION_ARG, peekLocation);
    }

    active = true;
  }

  private void handleIntent(Intent intent) {
    Bundle args = new Bundle();

    if (intent.getAction().equals(Intent.ACTION_SEARCH)) {
      String searchQuery = intent.getStringExtra(SearchManager.QUERY);
      args.putString(FeedFragment.SEARCH_QUERY_ARG, searchQuery);
      args.putSerializable(FeedFragment.LOCATION_ARG, peekLocation);
    } else if (intent.getAction().equals(FeedFragment.ACTION_PEEK)) {
      peekLocation = (AdventureLocation) intent.getSerializableExtra(LocationActivity.EXTRA_LOCATION);
      args.putSerializable(FeedFragment.LOCATION_ARG, peekLocation);
    }

    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.container, FeedFragment.newInstance(args)).commit();
  }
}