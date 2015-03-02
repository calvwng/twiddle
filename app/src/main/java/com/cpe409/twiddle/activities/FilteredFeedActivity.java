package com.cpe409.twiddle.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.cpe409.twiddle.R;
import com.cpe409.twiddle.fragments.FeedFragment;

public class FilteredFeedActivity extends ActionBarActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_filtered_feed);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
    handleIntent(intent);
  }

  private void handleIntent(Intent intent) {
    if (intent.getAction().equals(Intent.ACTION_SEARCH)) {
      String searchQuery = intent.getStringExtra(SearchManager.QUERY);

      Bundle args = new Bundle();
      args.putString(FeedFragment.SEARCH_QUERY_ARG, searchQuery);

      FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
      transaction.replace(R.id.container, FeedFragment.newInstance(args)).commit();
    }
  }
}