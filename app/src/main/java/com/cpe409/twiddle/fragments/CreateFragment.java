package com.cpe409.twiddle.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cpe409.twiddle.R;
import com.cpe409.twiddle.activities.AddImageActivity;
import com.cpe409.twiddle.activities.LocationActivity;
import com.cpe409.twiddle.model.Location;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class CreateFragment extends Fragment {

  private final static int SELECT_LOCATION_REQUEST = 1;
  private final static int ADD_IMAGE_REQUEST = 2;
  private View rootView;

  private EditText editTextTitle_;
  private EditText editTextDescription_;
  private TextView textViewLocation;
  private TextView textViewImage;
  private ProgressDialog pDialogMap;
  private byte[] imageData;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    this.rootView = inflater.inflate(R.layout.fragment_create, container, false);

    this.setHasOptionsMenu(true);
    this.initViews();
    this.addEvents();

    return this.rootView;
  }

  @Override
  public void onResume() {
    super.onResume();

    if (pDialogMap != null) {
      pDialogMap.dismiss();
    }
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
    menuInflater.inflate(R.menu.menu_new_activity, menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem menuItem) {
    switch (menuItem.getItemId()) {
      case R.id.action_create:
        this.checkInputAndSave();

      default:
        return super.onOptionsItemSelected(menuItem);
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == SELECT_LOCATION_REQUEST && resultCode == Activity.RESULT_OK) {
        Location location = (Location) data.getSerializableExtra(LocationActivity.EXTRA_LOCATION);
        this.textViewLocation.setText(location.strAddress);
        this.textViewLocation.setTag(location);
    } else if (requestCode == ADD_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
        /* TODO: Make it image in future */
        this.textViewImage.setText("Image Selected!");
        this.imageData = data.getByteArrayExtra("image");

    }
  }

  private void initViews() {
    this.editTextTitle_ = (EditText) this.rootView.findViewById(R.id.editTextTitle);
    this.editTextDescription_ = (EditText) this.rootView.findViewById(R.id.editTextDescription);
    this.textViewLocation = (TextView) this.rootView.findViewById(R.id.textViewAddLocation);
    this.textViewImage = (TextView) this.rootView.findViewById(R.id.textViewAddImage);
  }

  private void addEvents() {
    final Activity context = this.getActivity();

    this.textViewLocation.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        pDialogMap = ProgressDialog.show(getActivity(), "", "Opening map...", true);

        Intent intent = new Intent(context, LocationActivity.class);
        startActivityForResult(intent, SELECT_LOCATION_REQUEST);
      }
    });

      this.textViewImage.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              pDialogMap = ProgressDialog.show(getActivity(), "", "Opening Image Selector...", true);

              Intent intent = new Intent(context, AddImageActivity.class);
              startActivityForResult(intent, ADD_IMAGE_REQUEST);
          }
      });
  }

  private void checkInputAndSave() {
    final String title = this.editTextTitle_.getText().toString();
    final String description = this.editTextDescription_.getText().toString();
    final Location location = (Location) this.textViewLocation.getTag();
    final ParseFile photoFile = this.imageData != null ? new ParseFile("image.png", this.imageData) : null;


    if (!title.isEmpty() && location != null && ParseUser.getCurrentUser() != null) {
      saveObject(title, description, location, photoFile);
    } else {
      Toast.makeText(getActivity(), "Don't leave anything blank!", Toast.LENGTH_SHORT).show();
    }
  }

  private void saveObject(String title, String description, Location location, ParseFile image) {
    ParseObject adventureObject = new ParseObject("Adventure");

    adventureObject.put("adventureTitle", title);
    adventureObject.put("adventureDescription", description);
    adventureObject.put("likes", 0);
    adventureObject.put("locationLatitude", location.latitude);
    adventureObject.put("locationLongitude", location.longitude);
    adventureObject.put("locationAddress", location.strAddress);
    adventureObject.put("image", image);
    adventureObject.put("author", ParseUser.getCurrentUser());


    final ProgressDialog pDialog_;
    pDialog_ = ProgressDialog.show(getActivity(), "", "Creating Adventure...", true);

    adventureObject.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {
        pDialog_.dismiss();
        Toast.makeText(getActivity(), "Adventure saved with success!", Toast.LENGTH_SHORT).show();
      }
    });
  }
}