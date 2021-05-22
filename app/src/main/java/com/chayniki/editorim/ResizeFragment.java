package com.chayniki.editorim;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Objects;

public class ResizeFragment extends Fragment {

    static int zoomValue = 1;
    static boolean upOrDown = false;
    SeekBar zoomSeekBar;
    TextView resizeT;
    double num = 0;
    double numMinus = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_resize, container, false);

        zoomSeekBar = (SeekBar) view.findViewById(R.id.zoomSeekBar);
        resizeT = (TextView) view.findViewById(R.id.resizeText);
        resizeT.setText("" + 1 + "x");

        zoomSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                num = progress - 5;

                if (num < 0) --num;
                else ++num;

                zoomValue = (int) Math.abs(num);

                if (num < 0) {
                    upOrDown = false;
                    String numMinus = String.format("%.2f", -1 / num);
                    resizeT.setText("" + numMinus + "x");
                }
                if (num > 0) {
                    upOrDown = true;
                    resizeT.setText("" + num + "x");
                }

                ((ImageEditorActivity) Objects.requireNonNull(getActivity())).updateImage();
            }
        });

        return view;
    }

    public static Bitmap setResize(Bitmap bitmap) {
        if (zoomValue == 1) {
            return bitmap;
        } else if (upOrDown) {
            return zoomUp(bitmap);
        } else {
            return zoomDown(bitmap);
        }
    }

    private static Bitmap zoomUp(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap returnBitmap = Bitmap.createBitmap(width * zoomValue, height * zoomValue, Bitmap.Config.RGB_565);

        int[] colorArray = new int[width * height];
        int r, g, b;
        bitmap.getPixels(colorArray, 0, width, 0, 0, width, height);

        for (int x = 0; x < returnBitmap.getWidth(); ++x) {
            for (int y = 0; y < returnBitmap.getHeight(); ++y) {
                int parentX = (x - (x % zoomValue)) / zoomValue;
                int parentY = (y - (y % zoomValue)) / zoomValue;

                r = Color.red(colorArray[parentY * width + parentX]);
                g = Color.green(colorArray[parentY * width + parentX]);
                b = Color.blue(colorArray[parentY * width + parentX]);

                colorArray[parentY * width + parentX] = Color.rgb(r, g, b);
                returnBitmap.setPixel(x, y, colorArray[parentY * width + parentX]);
            }
        }

        return returnBitmap;
    }

    private static Bitmap zoomDown(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int pWidth = width / zoomValue + ((width % zoomValue == 0) ? 0 : 1);
        int pHeight = height / zoomValue + ((width % zoomValue == 0) ? 0 : 1);

        Bitmap returnBitmap = Bitmap.createBitmap(pWidth, pHeight, Bitmap.Config.RGB_565);

        int[] colorArray = new int[width * height];
        int r, g, b;
        bitmap.getPixels(colorArray, 0, width, 0, 0, width, height);

        for (int x = 0; x < returnBitmap.getWidth(); ++x) {
            for (int y = 0; y < returnBitmap.getHeight(); ++y) {
                r = 0;
                g = 0;
                b = 0;
                int count = 0;

                for (int i = 0; i < zoomValue; ++i) {
                    for (int j = 0; j < zoomValue; ++j) {
                        int parentX = x * zoomValue + i;
                        int parentY = y * zoomValue + j;

                        if (parentX < width && parentY < height) {
                            r += Color.red(colorArray[parentY * width + parentX]);
                            g += Color.green(colorArray[parentY * width + parentX]);
                            b += Color.blue(colorArray[parentY * width + parentX]);
                            ++count;
                        }
                    }
                }
                if (count != 0) {
                    r /= count;
                    g /= count;
                    b /= count;
                }

                returnBitmap.setPixel(x, y, Color.rgb(r, g, b));
            }
        }

        return returnBitmap;
    }
}