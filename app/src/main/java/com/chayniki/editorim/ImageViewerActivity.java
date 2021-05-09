package com.chayniki.editorim;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.ImageView;

import java.io.FileDescriptor;
import java.io.IOException;

public class ImageViewerActivity extends AppCompatActivity {

    ImageView imageView;
    private Uri imageUri;
    Bitmap sourceBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        imageView = (ImageView) findViewById(R.id.imageView);

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
    }

    public void onClickEditImage(View view) {
        final Intent intent = new Intent(this, ImageEditorActivity.class);
        intent.putExtra("imageUri", imageUri.toString());
        startActivity(intent);
    }

    public void onClickBack(View view) {
        Intent intent  = new Intent(this, ImageGalleryActivity.class);
        startActivity(intent);
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return bitmap;
    }
}