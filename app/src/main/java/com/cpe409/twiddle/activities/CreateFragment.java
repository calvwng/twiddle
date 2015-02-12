package com.cpe409.twiddle.activities;

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
import com.cpe409.twiddle.model.Location;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class CreateFragment extends Fragment {

    private final static int SELECT_LOCATION_REQUEST = 1;
    private View rootView;

    private EditText editTextTitle_;
    private EditText editTextDescription_;
    private TextView textViewLocation;
    private ProgressDialog pDialogMap;

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
                return true;
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
        }
    }

    private void initViews() {
        this.editTextTitle_ = (EditText) this.rootView.findViewById(R.id.editTextTitle);
        this.editTextDescription_ = (EditText) this.rootView.findViewById(R.id.editTextDescription);

        this.textViewLocation = (TextView) this.rootView.findViewById(R.id.textViewAddLocation);
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
    }

    private void checkInputAndSave() {
        String title = this.editTextTitle_.getText().toString();
        String description = this.editTextDescription_.getText().toString();
        Location location = (Location) this.textViewLocation.getTag();

        if (!title.isEmpty() && location != null) {
            saveObject(title, description, location);
        } else {
            Toast.makeText(getActivity(), "Don't left anything blank!", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveObject(String title, String description, Location location) {
        ParseObject activityObject = new ParseObject("Adventure");
        ParseObject locationObject = new ParseObject("Location");

        locationObject.put("locationLatitude", location.latitude);
        locationObject.put("locationLongitude", location.longitude);
        locationObject.put("locationAddress", location.strAddress);

        activityObject.put("adventureTitle", title);
        activityObject.put("adventureDescription", description);
        activityObject.put("adventureLocation", locationObject);

        final ProgressDialog pDialog_;
        pDialog_ = ProgressDialog.show(getActivity(), "", "Creating Adventure...", true);

        activityObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                pDialog_.dismiss();
                Toast.makeText(getActivity(), "Adventure saved with success!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}