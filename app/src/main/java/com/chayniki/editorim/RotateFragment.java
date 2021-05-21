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

public class RotateFragment extends Fragment {

    ImageButton rotateButton;
    static int rotationAngle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rotate, container, false);

        rotateButton = (ImageButton) view.findViewById(R.id.rotateButton);
        rotationAngle = 0;
        rotateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotationAngle += 9;
                if (rotationAngle >= 360 || rotationAngle <= -360) rotationAngle = Math.abs(rotationAngle) - 360;
                ((ImageEditorActivity) getActivity()).updateImage();
            }
        });

        return view;
    }

    public static Bitmap rotateImage(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        //Log.d("test", "width: " + width + ", height: " + height);

        double angle = Math.toRadians(rotationAngle);
        double rCos = Math.cos(angle);
        double rSin = Math.sin(angle);
        double alpha = -Math.tan(angle/2);

        int newWight = (int) Math.round(Math.abs(width * rCos) + Math.abs(height * rSin));
        int newHeight = (int) Math.round(Math.abs(height * rCos) + Math.abs(width * rSin));

        Bitmap returnBitmap = Bitmap.createBitmap(newWight, newHeight, Bitmap.Config.RGB_565);

        int[] colorArray = new int[width * height];
        int r, g, b;
        bitmap.getPixels(colorArray, 0, width, 0, 0, width, height);

        int centerY = Math.round(((height + 1) / 2) - 1);
        int centerX = Math.round(((width + 1) / 2) - 1);

        int newCenterY = Math.round(((newHeight + 1) / 2) - 1);
        int newCenterX = Math.round(((newWight + 1) / 2) - 1);

        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                int x = width - 1 - i - centerX;
                int y = height - 1 - j - centerY;

                /*int newX = (int) Math.round(x * rCos + y * rSin);
                int newY = (int) Math.round(-x * rSin + y * rCos);*/
                int newX = (int) (x + alpha * y);
                int newY = (int) (y + rSin * newX);
                newX += (int) (alpha * newY);

                newX = newCenterX - newX;
                newY = newCenterY - newY;

                if ((newX >= 0 && newX < newWight) && (newY >= 0 && newY < newHeight)) {
                    //Log.d("test", "X:" + newX + ", Y: " + newY);
                    //Log.d("test", "size: " + colorArray.length + ", index: " + (j * width + i) + "; j: " + j + ", width: " + width + ", i: " + i);
                    r = Color.red(colorArray[j * width + i]);
                    g = Color.green(colorArray[j * width + i]);
                    b = Color.blue(colorArray[j * width + i]);

                    colorArray[j * width + i] = Color.rgb(r, g, b);
                    returnBitmap.setPixel(newX, newY, colorArray[j * width + i]);
                }
            }
        }

        return returnBitmap;
    }

}