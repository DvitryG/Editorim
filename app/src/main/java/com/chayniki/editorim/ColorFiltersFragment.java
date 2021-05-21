package com.chayniki.editorim;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.DiscretePathEffect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;


public class ColorFiltersFragment extends Fragment {

    Button negative, sepia;
    static int selectedFilter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_color_filters, container, false);

        sepia = (Button) view.findViewById(R.id.sepiaFilterButton);
        negative = (Button) view.findViewById(R.id.negativeFilterButton);

        sepia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedFilter != 1) selectedFilter = 1;
                else selectedFilter = 0;
                ((ImageEditorActivity) getActivity()).updateImage();
            }
        });

        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedFilter != 2) selectedFilter = 2;
                else selectedFilter = 0;
                ((ImageEditorActivity) getActivity()).updateImage();
            }
        });

        return view;
    }

    public static Bitmap setFilter(Bitmap bitmap) {
        switch (selectedFilter) {
            case 1:
                return setSepia(bitmap);
            case 2:
                return setNegative(bitmap);
            default:
                return bitmap;
        }
    }

    private static Bitmap setNegative(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap returnBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        int colorArray[] = new int[width * height];
        int r, g, b;
        bitmap.getPixels(colorArray, 0, width, 0, 0, width, height);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                r = 255 - Color.red(colorArray[y * width + x]);
                g = 255 - Color.green(colorArray[y * width + x]);
                b = 255 - Color.blue(colorArray[y * width + x]);

                colorArray[y * width + x] = Color.rgb(r, g, b);
                returnBitmap.setPixel(x, y, colorArray[y * width + x]);
            }
        }

        return returnBitmap;
    }

    private static Bitmap setSepia(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int pixColor = 0;
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < height; i++) {
            for (int k = 0; k < width; k++) {
                pixColor = pixels[width * i + k];
                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);
                newR = (int) (0.393 * pixR + 0.769 * pixG + 0.189 * pixB);
                newG = (int) (0.349 * pixR + 0.686 * pixG + 0.168 * pixB);
                newB = (int) (0.272 * pixR + 0.534 * pixG + 0.131 * pixB);
                int newColor = Color.argb(255, newR > 255 ? 255 : newR, newG > 255 ? 255 : newG, newB > 255 ? 255 : newB);
                pixels[width * i + k] = newColor;
            }
        }

        Bitmap returnBitmap = Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);
        return returnBitmap;
    }
}