package com.chayniki.editorim;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Objects;

public class RotateFragment extends Fragment {
    private TextView textView;
    ImageButton rotateButton;
    static int rotationAngle;
    int num = 0;
    int tempAngle = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rotate, container, false);

        rotateButton = (ImageButton) view.findViewById(R.id.rotateButton);
        textView = (TextView) view.findViewById(R.id.progressText);
        SeekBar seekBar = (SeekBar) view.findViewById(R.id.rotateBar);
        rotationAngle = 0;
        textView.setText("0");

        rotateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotationAngle += 90;
                if (rotationAngle >= 360 || rotationAngle <= -360)
                    rotationAngle = Math.abs(rotationAngle) - 360;
                ((ImageEditorActivity) Objects.requireNonNull(getActivity())).updateImage();

            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                num = progress - 45;
                textView.setText("" + num + "°");
                rotationAngle = rotationAngle + num - tempAngle;
                if (rotationAngle >= 360 || rotationAngle <= -360)
                    rotationAngle = Math.abs(rotationAngle) - 360;
                ((ImageEditorActivity) Objects.requireNonNull(getActivity())).updateImage();
                tempAngle = num;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return view;
    }

    public static Bitmap rotateImage(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        double angle = Math.toRadians(rotationAngle);
        double rCos = Math.cos(angle);
        double rSin = Math.sin(angle);
        double alpha = -Math.tan(angle / 2);

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

                int newX = (int) Math.round(x + alpha * y);
                int newY = (int) Math.round(y + rSin * newX);
                newX = (int) Math.round(newX + alpha * newY);

                newX = newCenterX - newX;
                newY = newCenterY - newY;

                if ((newX >= 0 && newX < newWight) && (newY >= 0 && newY < newHeight)) {

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