package com.chayniki.editorim;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.DiscretePathEffect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ColorFiltersFragment extends Fragment {

    ImageEditorActivity imageEditorActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        imageEditorActivity.editBitmap = null;

        return inflater.inflate(R.layout.fragment_color_filters, container, false);
    }
}