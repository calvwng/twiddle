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

public class NewActivityFragment extends Fragment {

    private final static int SELECT_LOCATION_REQUEST = 1;
    private View rootView;

    private EditText editTextTitle_;
    private EditText editTextDescription_;
    private TextView textViewLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_new_activity, container, false);

        this.setHasOptionsMenu(true);
        this.initViews();
        this.addEvents();

        return this.rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_new_activity, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_create:
                this.saveObject();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_LOCATION_REQUEST && resultCode == Activity.RESULT_OK) {
            Location location = (Location) data.getSerializableExtra(LocationActivity.EXTRA_LOCATION);
            this.textViewLocation.setText(location.latitude+" "+location.longitude);
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
                Intent intent = new Intent(context, LocationActivity.class);
                startActivityForResult(intent, SELECT_LOCATION_REQUEST);
            }
        });
    }

    private void saveObject() {
        ParseObject activityObject = new ParseObject("ActivityObject");
        activityObject.put("activityTitle", this.editTextTitle_.getText().toString());
        activityObject.put("activityDescription", this.editTextDescription_.getText().toString());

        final ProgressDialog pDialog_;
        pDialog_ = ProgressDialog.show(getActivity(), "", "Creating Activity...", true);

        activityObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                pDialog_.dismiss();
                Toast.makeText(getActivity(), "Activity saved with success!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}