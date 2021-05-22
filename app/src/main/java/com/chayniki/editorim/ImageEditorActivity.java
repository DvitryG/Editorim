package com.chayniki.editorim;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageEditorActivity extends AppCompatActivity {

    private Fragment[] fragments;
    ImageView imageView;
    public Bitmap sourceBitmap;
    ViewPager2 fragmentViewPager;
    ToolListAdapter toolListAdapter;
    private String[] toolList;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_editor);

        imageView = (ImageView) findViewById(R.id.editImageView);

        Intent intent = getIntent();
        if (intent != null) {
            imageUri = Uri.parse(intent.getStringExtra("imageUri"));
            try {
                sourceBitmap = getBitmapFromUri(imageUri);
                imageView.setImageBitmap(sourceBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        setPanels();

        fragmentViewPager = (ViewPager2) findViewById(R.id.fragmentViewPager);
        toolListAdapter = new ToolListAdapter(toolList);
        fragmentViewPager.setAdapter(toolListAdapter);

        fragmentViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                ChangePanel(position);
                super.onPageSelected(position);
            }
        });
    }

    private void setPanels() {
        toolList = new String[]{
                "Цветокоррекция", "Поворот", "Масштабирование"
        };

        fragments = new Fragment[3];
        fragments[0] = new ColorFiltersFragment();
        fragments[1] = new RotateFragment();
        fragments[2] = new ResizeFragment();
    }

    public void ChangePanel(int position) {
        Fragment fragment = null;

        if (fragments[position] != null) {
            fragment = fragments[position];
        } else return;

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.settingsPanelFragment, fragment);
        fragmentTransaction.commit();
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return bitmap;
    }

    public void updateImage() {
        Bitmap bitmap = sourceBitmap;

        bitmap = ColorFiltersFragment.setFilter(bitmap);
        bitmap = RotateFragment.rotateImage(bitmap);
        bitmap = ResizeFragment.setResize(bitmap);

        imageView.setImageBitmap(bitmap);
    }

    public void onClickBack(View view) {
        Intent intent = new Intent(this, ImageViewerActivity.class);
        intent.putExtra("imageUri", imageUri.toString());
        startActivity(intent);
    }

    public void onClickSaveImage(View view) {
        Bitmap finalBitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        saveImage(finalBitmap);
        Intent intent = new Intent(this, ImageGalleryActivity.class);
        startActivity(intent);
    }

    private void saveImage(Bitmap finalBitmap) {

        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy.MM.dd'-'hh:mm:ss");
        String image_name = formatForDateNow.format(dateNow);

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root);
        myDir.mkdirs();
        String fName = "Image-" + image_name+ ".jpg";
        File file = new File(myDir, fName);
        if (file.exists()) file.delete();
        Log.i("LOAD", root + fName);
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}