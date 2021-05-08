package com.chayniki.editorim;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.GridLayout;
import android.widget.Toast;

public class ImageGalleryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, MediaStoreAdapter.OnClickThumbListener {

    private final static int READ_EXTERNAL_STORAGE_PERMISSION_RESULT = 0;
    private final static int MEDIA_STORE_LOADER_ID = 0;
    private RecyclerView thumbnailRecyclerView;
    private MediaStoreAdapter mediaStoreAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);

        thumbnailRecyclerView = (RecyclerView) findViewById(R.id.thumbnailRecyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        thumbnailRecyclerView.setLayoutManager(gridLayoutManager);
        mediaStoreAdapter = new MediaStoreAdapter(this);
        thumbnailRecyclerView.setAdapter(mediaStoreAdapter);

        checkReadExternalStoragePermission();
    }

    private void checkReadExternalStoragePermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                getSupportLoaderManager().initLoader(MEDIA_STORE_LOADER_ID, null, this);
            }
            else {
                if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "Нам нужон доступ к медиа, дай!", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION_RESULT);
            }
        }
        else {
            getSupportLoaderManager().initLoader(MEDIA_STORE_LOADER_ID, null, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case READ_EXTERNAL_STORAGE_PERMISSION_RESULT:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(this, "Пасибки :3", Toast.LENGTH_SHORT).show();
                    getSupportLoaderManager().initLoader(MEDIA_STORE_LOADER_ID, null, this);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.DATA,//
                MediaStore.Files.FileColumns.MEDIA_TYPE
        };
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
        return new CursorLoader(this, MediaStore.Files.getContentUri("external"), projection, selection, null, MediaStore.Files.FileColumns.DATE_ADDED + " DESC");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mediaStoreAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mediaStoreAdapter.changeCursor(null);
    }

    @Override
    public void onClickImage(Uri imageUri) {
        //Toast.makeText(this, "адрес картинки: " + imageUri.toString(), Toast.LENGTH_SHORT).show();
        final Intent intent = new Intent(this, ImageViewerActivity.class);
        intent.putExtra("imageUri", imageUri.toString());
        startActivity(intent);
    }
}