package com.chayniki.editorim;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MediaStoreAdapter extends RecyclerView.Adapter<MediaStoreAdapter.ViewHolder> {

    private Cursor mediaStoreCursor;
    private final Activity activity;
    private OnClickThumbListener onClickThumbListener;


    public interface OnClickThumbListener {
        void onClickImage(Uri imageUri);
    }

    public MediaStoreAdapter(Activity activity) {
        this.activity = activity;
        this.onClickThumbListener = (OnClickThumbListener) activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_image_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bitmap bitmap = getBitmapFromMediaStore(position);
        if (bitmap != null) {
            holder.getImageView().setImageBitmap(bitmap);
        }
    }

    @Override
    public int getItemCount() {
        return (mediaStoreCursor == null) ? 0 : mediaStoreCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.mediaStoreImageView);
            imageView.setOnClickListener(this);
        }

        public ImageView getImageView() {
            return imageView;
        }

        @Override
        public void onClick(View v) {
            getOnClickUri(getAdapterPosition());
        }
    }

    private Cursor swapCursor(Cursor cursor) {
        if (mediaStoreCursor == cursor) {
            return null;
        }
        Cursor oldCursor = mediaStoreCursor;
        this.mediaStoreCursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    public void changeCursor(Cursor cursor) {
        Cursor oldCursor = swapCursor(cursor);
        if (oldCursor != null) {
            oldCursor.close();
        }
    }

    private Bitmap getBitmapFromMediaStore(int position) {
        int idIndex = mediaStoreCursor.getColumnIndex(MediaStore.Files.FileColumns._ID);
        int mediaTypeIndex = mediaStoreCursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE);

        mediaStoreCursor.moveToPosition(position);
        switch (mediaStoreCursor.getInt(mediaTypeIndex)) {
            case MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE:
                return MediaStore.Images.Thumbnails.getThumbnail(activity.getContentResolver(), mediaStoreCursor.getLong(idIndex), MediaStore.Images.Thumbnails.MICRO_KIND, null);
            default:
                return null;
        }
    }

    private void getOnClickUri(int position) {
        int mediaTypeIndex = mediaStoreCursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE);
        int dataIndex = mediaStoreCursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);

        mediaStoreCursor.moveToPosition(position);
        String dataString = mediaStoreCursor.getString(dataIndex);

        switch (mediaStoreCursor.getInt(mediaTypeIndex)) {
            case MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE:
                Uri imageUri = Uri.parse("file://" + dataString);
                onClickThumbListener.onClickImage(imageUri);
                break;
            default:
        }
    }
}
