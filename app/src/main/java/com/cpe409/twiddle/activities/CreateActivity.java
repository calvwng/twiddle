package com.cpe409.twiddle.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.cpe409.twiddle.R;
import com.cpe409.twiddle.fragments.CreateFragment;

public class CreateActivity extends ActionBarActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.container, new CreateFragment()).commit();
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
