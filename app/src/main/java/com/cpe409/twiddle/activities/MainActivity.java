package com.cpe409.twiddle.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.cpe409.twiddle.R;
import com.cpe409.twiddle.fragments.FavoritesFragment;
import com.cpe409.twiddle.fragments.FeedFragment;
import com.cpe409.twiddle.fragments.UserProfileFragment;
import com.cpe409.twiddle.model.CurrentUser;
import com.facebook.AppEventsLogger;
import com.facebook.Session;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialAccount;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;
import it.neokree.materialnavigationdrawer.elements.listeners.MaterialSectionListener;

/**
 * The wiki for MaterialNavigationDrawer is https://github.com/neokree/MaterialNavigationDrawer/wiki
 */
public class MainActivity extends MaterialNavigationDrawer {

  private MaterialAccount account;
  private Target userPhoto = new Target() {

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
      account.setPhoto(bitmap.copy(Bitmap.Config.ARGB_8888, true));
      // do not call notifyAccountDataChanged();, because the library get your drawable/bitmap
      // and call the notifyAccountDataChanged when the scaled and resized drawable will be available.
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
  };

  @Override
  public void init(Bundle savedInstanceState) {
    initNavDrawer();
  }

  public void callFacebookLogout(Context context) {
    CurrentUser.getInstance().logout();
    Session session = Session.getActiveSession();
    if (session != null) {
      if (!session.isClosed()) {
        session.closeAndClearTokenInformation();
      }
    } else {
      session = new Session(context);
      Session.setActiveSession(session);
      session.closeAndClearTokenInformation();
    }
  }

  /**
   * Sets up the MaterialNavigationDrawer
   */
  public void initNavDrawer() {
    this.disableLearningPattern(); // Hides drawer on start

    if (CurrentUser.getInstance().isLoggedIn()) {
      account = new MaterialAccount(this.getResources(), "",
          CurrentUser.getInstance().getName(),
          R.drawable.ic_action_account_circle, R.drawable.bg_nav_drawer);
    } else {
      account = new MaterialAccount(this.getResources(), "",
          "Guest",
          R.drawable.ic_action_account_circle, R.drawable.bg_nav_drawer);
    }

    MaterialSection browseSection = this.newSection("Adventures", FeedFragment.newInstance()); // TODO: set drawable & fragment
    MaterialSection favoritesSection = this.newSection("Favorites", new FavoritesFragment().newInstance()); // TODO: set drawable & fragment
    MaterialSection profileSection = this.newSection("My Profile",
        new UserProfileFragment().newInstance(CurrentUser.getInstance().getUserId())); // TODO: set drawable & fragment
//    MaterialSection settingsSection = this.newSection("Settings", new Fragment()); // TODO: set drawable & fragment
    MaterialSection logoutSection = this.newSection("Logout", R.drawable.ic_action_exit_to_app, new Fragment()); // TODO: set drawable & fragment
    logoutSection.setOnClickListener(new MaterialSectionListener() {

      @Override
      public void onClick(MaterialSection section) {
        ParseUser.logOut();
        callFacebookLogout(getApplicationContext());
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
      }
    });

    this.addBottomSection(logoutSection);
    this.addSection(browseSection);

    if (CurrentUser.getInstance().isLoggedIn()) {
      this.addSection(favoritesSection);
      this.addSection(profileSection);
    }

//    this.addSection(settingsSection);
    this.addAccount(account);

    if (CurrentUser.getInstance().isLoggedIn()) {
      Picasso.with(this).load(CurrentUser.getInstance().getImageURL()).into(userPhoto);
    }
  }

  @Override
  protected void onDestroy() {
    Picasso.with(this).cancelRequest(userPhoto);
    super.onDestroy();
  }

  @Override
  protected void onPause() {
    super.onPause();
    AppEventsLogger.deactivateApp(this);
  }
}
