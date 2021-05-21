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

import java.util.Objects;


public class ColorFiltersFragment extends Fragment {

    Button negative, sepia, blackWhite, red;
    static int selectedFilter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_color_filters, container, false);

        sepia = (Button) view.findViewById(R.id.sepiaFilterButton);
        negative = (Button) view.findViewById(R.id.negativeFilterButton);
        blackWhite = (Button) view.findViewById(R.id.blackWhiteFilterButton);
        red = (Button) view.findViewById(R.id.redFilterButton);

        sepia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedFilter != 1) selectedFilter = 1;
                else selectedFilter = 0;
                ((ImageEditorActivity) Objects.requireNonNull(getActivity())).updateImage();
            }
        });

        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedFilter != 2) selectedFilter = 2;
                else selectedFilter = 0;
                ((ImageEditorActivity) Objects.requireNonNull(getActivity())).updateImage();
            }
        });

        blackWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedFilter != 3) selectedFilter = 3;
                else selectedFilter = 0;
                ((ImageEditorActivity) Objects.requireNonNull(getActivity())).updateImage();
            }
        });

        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedFilter != 4) selectedFilter = 4;
                else selectedFilter = 0;
                ((ImageEditorActivity) Objects.requireNonNull(getActivity())).updateImage();
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
            case 3:
                return setBlackWhite(bitmap);
            case 4:
                return setRed(bitmap);
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

        Bitmap returnBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        int[] colorArray = new int[width * height];
        int r, g, b;
        int newR, newG, newB;
        bitmap.getPixels(colorArray, 0, width, 0, 0, width, height);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                r = Color.red(colorArray[y * width + x]);
                g = Color.green(colorArray[y * width + x]);
                b = Color.blue(colorArray[y * width + x]);

                newR = (int) (0.393 * r + 0.769 * g + 0.189 * b);
                newG = (int) (0.349 * r + 0.686 * g + 0.168 * b);
                newB = (int) (0.272 * r + 0.534 * g + 0.131 * b);

                colorArray[y * width + x] = Color.rgb(Math.min(newR, 255), Math.min(newG, 255), Math.min(newB, 255));
                returnBitmap.setPixel(x, y, colorArray[y * width + x]);
            }
        }

        return returnBitmap;
    }

    private static Bitmap setBlackWhite(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap returnBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        int colorArray[] = new int[width * height];
        int r, g, b;
        bitmap.getPixels(colorArray, 0, width, 0, 0, width, height);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                r = Color.red(colorArray[y * width + x]);
                g = Color.green(colorArray[y * width + x]);
                b = Color.blue(colorArray[y * width + x]);

                int bw = (r + g + b) / 3;
                r = bw; g = bw; b = bw;

                colorArray[y * width + x] = Color.rgb(r, g, b);
                returnBitmap.setPixel(x, y, colorArray[y * width + x]);
            }
        }

        return returnBitmap;
    }

    private static Bitmap setRed(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap returnBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        int colorArray[] = new int[width * height];
        int r;
        bitmap.getPixels(colorArray, 0, width, 0, 0, width, height);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                r = Color.red(colorArray[y * width + x]);

                colorArray[y * width + x] = Color.rgb(r, 0, 0);
                returnBitmap.setPixel(x, y, colorArray[y * width + x]);
            }
        }

        return returnBitmap;
    }
}