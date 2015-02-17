package com.cpe409.twiddle.activities;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.cpe409.twiddle.R;
import com.facebook.AppEventsLogger;
import android.content.Intent;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;
import it.neokree.materialnavigationdrawer.util.MaterialDrawerLayout;

/**
 * The wiki for MaterialNavigationDrawer is https://github.com/neokree/MaterialNavigationDrawer/wiki
 */
public class MainActivity extends MaterialNavigationDrawer {

  @Override
  public void init(Bundle savedInstanceState) {
    initNavDrawer();
  }

  /**
   * Sets up the MaterialNavigationDrawer
   */
  public void initNavDrawer() {
    this.disableLearningPattern(); // Hides drawer on start

    this.setUsername("Test User");

    MaterialSection browseSection = this.newSection("Browse Adventures", new Fragment()); // TODO: set drawable & fragment
    MaterialSection favoritesSection = this.newSection("Favorites", new Fragment()); // TODO: set drawable & fragment
    MaterialSection settingsSection = this.newSection("Settings", new Fragment()); // TODO: set drawable & fragment
    MaterialSection logoutSection = this.newSection("Logout", new Fragment()); // TODO: set drawable & fragment
    this.addSection(browseSection);
    this.addSection(favoritesSection);
    this.addSection(settingsSection);
    this.addSection(logoutSection);
  }

  @Override
  protected void onPause() {
    super.onPause();
    AppEventsLogger.deactivateApp(this);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  /**
   * A placeholder fragment containing a simple view.
   */
  public static class PlaceholderFragment extends Fragment {

    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View rootView = inflater.inflate(R.layout.fragment_main, container, false);
      return rootView;
    }
  }
}
