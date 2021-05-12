package com.chayniki.editorim;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.widget.ImageView;

import java.io.FileDescriptor;
import java.io.IOException;

public class ImageEditorActivity extends AppCompatActivity {

    private Fragment[] fragments;
    ImageView imageView;
    private Bitmap sourceBitmap;
    public Bitmap editBitmap;
    ViewPager2 fragmentViewPager;
    ToolListAdapter toolListAdapter;
    private String[] toolList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_editor);

        imageView = (ImageView) findViewById(R.id.editImageView);

        Intent intent = getIntent();
        if (intent != null) {
            Uri imageUri = Uri.parse(intent.getStringExtra("imageUri"));
            try {
                sourceBitmap = getBitmapFromUri(imageUri);
                imageView.setImageBitmap(sourceBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        editBitmap = sourceBitmap;

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
        toolList = new String[] {
                "Цветокор", "2", "3", "4", "5"
        };
        fragments = new Fragment[5];
        fragments[0] = new ColorFiltersFragment();
    }

    public void ChangePanel (int position) {
        Fragment fragment = null;

        if (fragments[position] != null) {
            fragment = fragments[position];
        }
        else return;

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
}