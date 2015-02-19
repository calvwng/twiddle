package com.cpe409.twiddle.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.cpe409.twiddle.R;
import com.cpe409.twiddle.adapters.LoginPagerAdapter;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.viewpagerindicator.CirclePageIndicator;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginActivity extends ActionBarActivity {

  private ProgressDialog pDialog_;
  private ViewPager viewPager_;
  private LoginButton loginButton_;
  private static final String TAG = LoginActivity.TAG;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (isExistingUser()) {
      startMainActivity();
    }

    setContentView(R.layout.activity_login);
    initializeReferences();

    CirclePageIndicator login_indicator = (CirclePageIndicator) findViewById(R.id.login_indicator);
    viewPager_.setPageTransformer(false, new FadePageTransfomer());

    PagerAdapter adapter = new LoginPagerAdapter(LoginActivity.this);
    viewPager_.setAdapter(adapter);

    login_indicator.setSnap(true);
    login_indicator.setViewPager(viewPager_);
  }



  private boolean isExistingUser() {
    ParseUser user = ParseUser.getCurrentUser();
    return user != null && ParseFacebookUtils.isLinked(user);
  }


  private void startMainActivity() {
    startActivity(new Intent(LoginActivity.this, MainActivity.class));
    finish();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
  }

  private void initializeReferences() {
    viewPager_ = (ViewPager) findViewById(R.id.login_viewpager);
    loginButton_ = (LoginButton) findViewById(R.id.login);
    loginButton_.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        pDialog_ = ProgressDialog.show(LoginActivity.this, "", "Logging in...", true);
        ParseFacebookUtils.logIn(
            Arrays.asList(ParseFacebookUtils.Permissions.User.EMAIL, ParseFacebookUtils.Permissions.User.ABOUT_ME, "user_friends"),
            LoginActivity.this, new LogInCallback() {
              @Override
              public void done(ParseUser user, ParseException err) {
                pDialog_.dismiss();
                if (user == null) {
                  Log.d("Exception", err.toString());
                  Log.d("Facebook Login", "Uh oh. The user cancelled the Facebook login.");
//                } else if (getSharedPreferences("BOOT_PREF", MODE_PRIVATE).getBoolean("firstboot",
//                    true)) {
//                  getSharedPreferences("BOOT_PREF", MODE_PRIVATE).edit()
//                      .putBoolean("firstboot", false).commit();
//                  updateParseUserInfoInBackground();
//                  startTutorialActivity();
                } else {
                  Log.d("Facebook Login", "User logged in through Facebook!");
                  updateParseUserInfoInBackground();
                  startMainActivity();
                }
              }
            });
      }
    });
  }

  private void updateParseUserInfoInBackground() {
    Request.newMeRequest(ParseFacebookUtils.getSession(), new Request.GraphUserCallback() {
      @Override
      public void onCompleted(GraphUser user, Response response) {
        if (user != null) {
          ParseUser.getCurrentUser().put("fbId", user.getId());
          ParseUser.getCurrentUser().put("fbName", user.getName());
          ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {

            @Override
            public void done(ParseException arg0) {
              if (arg0 != null) {
                Log.e(TAG, "Error saving fbID and fbName in parse " + arg0.toString());
              }
            }
          });
          ParseInstallation installation = ParseInstallation.getCurrentInstallation();
          installation.put("fbId", user.getId());
          installation.saveInBackground(new SaveCallback() {

            @Override
            public void done(ParseException arg0) {
              if (arg0 != null) {
                Log.e(TAG, "Error saving fbID in installation table. " + arg0.toString());
              }
            }
          });
        }
      }
    }).executeAsync();
  }

  public void callFacebookLogout(Context context) {
    Session session = Session.getActiveSession();
    if (session != null) {

      if (!session.isClosed()) {
        session.closeAndClearTokenInformation();
        // clear your preferences if saved
      }
    } else {
      session = new Session(context);
      Session.setActiveSession(session);
      session.closeAndClearTokenInformation();
      // clear your preferences if saved
    }
  }

  public class FadePageTransfomer implements ViewPager.PageTransformer {
    public void transformPage(View view, float position) {
      view.setAlpha(1 - Math.abs(position));
    }
  }
}
