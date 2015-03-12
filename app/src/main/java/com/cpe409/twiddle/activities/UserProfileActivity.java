package com.cpe409.twiddle.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.cpe409.twiddle.R;
import com.cpe409.twiddle.fragments.UserProfileFragment;

/**
 * Created by Michael on 3/4/2015.
 */
public class UserProfileActivity extends ActionBarActivity {

  private String userId;
  public static final String USER_ID = "userId";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_container);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    userId = getIntent().getExtras().getString(USER_ID);
    Bundle args = new Bundle();
    args.putString(USER_ID, userId);

    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.container, UserProfileFragment.newInstance(args)).commit();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem menuItem) {
    switch (menuItem.getItemId()) {
      case android.R.id.home:
        onBackPressed();
    }

    return super.onOptionsItemSelected(menuItem);
  }
}
