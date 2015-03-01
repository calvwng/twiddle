package com.cpe409.twiddle.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.cpe409.twiddle.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;

public class AddImageActivity extends ActionBarActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public final static String IMAGE = "image";
    private byte[] scaledData;

    ImageView pictureImageView;
    View rootView;
    private ParseFile photoFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);
        rootView = this.findViewById(R.id.activity_add_image_layout);
        this.pictureImageView = (ImageView) this.rootView.findViewById(R.id.pictureImageView);
        final Button submitButton = (Button) findViewById(R.id.submit_image_button);
        final Button cancelButton = (Button) findViewById(R.id.cancel_image_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(IMAGE, scaledData);

                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_camera) {
            takePicture();
        } else if (id == R.id.action_gallery) {
            chooseFromGallery();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            this.pictureImageView.setImageBitmap(imageBitmap);
            this.pictureImageView.setVisibility(View.VISIBLE);
            this.pictureImageView.invalidate();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
            scaledData = bos.toByteArray();
        }
    }




    private void takePicture() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void chooseFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                "content://media/internal/images/media"));
        startActivityForResult(galleryIntent, REQUEST_IMAGE_CAPTURE);

    }

}
