package com.cpe409.twiddle.activities;

import com.cpe409.twiddle.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class LoginActivity extends ActionBarActivity {

  private ViewPager viewPager_;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (isExistingUser()) {
      startMainActivity();
    }

    setContentView(R.layout.activity_login);
    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .add(R.id.container, new PlaceholderFragment())
          .commit();
    }
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




  /**
   * A placeholder fragment containing a simple view.
   */
  public static class PlaceholderFragment extends Fragment {

    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View rootView = inflater.inflate(R.layout.fragment_login, container, false);
      return rootView;
    }
  }
}
