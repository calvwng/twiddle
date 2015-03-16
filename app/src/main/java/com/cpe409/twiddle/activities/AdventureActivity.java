package com.cpe409.twiddle.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.cpe409.twiddle.R;
import com.cpe409.twiddle.views.ExpandableTextView;
import com.squareup.picasso.Picasso;

public class AdventureActivity extends ActionBarActivity {


  public static final String TITLE = "adventure_title";
  public static final String DESCRIPTION = "description";
  public static final String IMAGE_URL = "image_url";
  public static final String IMAGE_DATA = "image_data";
  private String title;
  private String description;
  private String imgUrl;
  private byte[] imgData;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_adventure);

    title = getIntent().getExtras().getString(TITLE);
    setTitle(title);

    description = getIntent().getExtras().getString(DESCRIPTION);
    ExpandableTextView descriptView = (ExpandableTextView)findViewById(R.id.adventure_description);
    descriptView.setText(description);

    ImageView adventureImage = (ImageView)findViewById(R.id.adventure_image);
    imgData = getIntent().getExtras().getByteArray(IMAGE_DATA);
    imgUrl = getIntent().getExtras().getString(IMAGE_URL);

    if (imgData != null) {
      Bitmap bitmap = BitmapFactory.decodeByteArray(imgData , 0, imgData.length);
      adventureImage.setImageBitmap(bitmap);
    }
    else if (imgUrl != null) {
      Picasso.with(this).load(imgUrl).into(adventureImage);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_adventure, menu);
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
}
