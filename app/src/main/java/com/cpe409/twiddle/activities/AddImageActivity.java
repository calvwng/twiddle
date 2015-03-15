package com.cpe409.twiddle.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.cpe409.twiddle.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddImageActivity extends ActionBarActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_SELECT = 2;
    public final static String IMAGE = "image";

    View rootView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);
        rootView = this.findViewById(R.id.activity_add_image_layout);
        final GridView gridview = (GridView) findViewById(R.id.imageGridView);
        gridview.setAdapter(new ImageAdapter(this));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setPicture((byte[]) gridview.getAdapter().getItem(position));
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

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
            setPicture(bos.toByteArray());
        } else if (requestCode == REQUEST_IMAGE_SELECT && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap imageBitmap = BitmapFactory.decodeStream(imageStream);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(imageBitmap, 500,500, true);


            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            scaledBitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
            setPicture(bos.toByteArray());
        }
    }

    private void takePicture() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void setPicture(byte[] image) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(IMAGE, image);

        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void chooseFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUEST_IMAGE_SELECT);

    }




    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        @Override
        public int getCount() {
            return mThumbIds.length;
        }
        @Override
        public Object getItem(int position) {
            Bitmap pictureBitMap = BitmapFactory.decodeResource(mContext.getResources(),
                    mThumbIds[position]);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            pictureBitMap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] pictureByteArray = stream.toByteArray();

            return pictureByteArray;
        }
        @Override
        public long getItemId(int position) {
            return 0;
        }

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null){
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            }
            else{
                imageView = (ImageView) convertView;
            }
            imageView.setImageResource(mThumbIds[position]);
            return imageView;
        }

        private Integer[] mThumbIds = {
                R.drawable.gridview_beach, R.drawable.gridview_coffee,
                R.drawable.gridview_flowers, R.drawable.gridview_food,
                R.drawable.gridview_forest, R.drawable.gridview_museum,
                R.drawable.gridview_outdoors, R.drawable.gridview_parachute,
                R.drawable.gridview_snowboarding, R.drawable.gridview_icecream,
                R.drawable.gridview_library, R.drawable.gridview_marshmallows
        };
    }



}
