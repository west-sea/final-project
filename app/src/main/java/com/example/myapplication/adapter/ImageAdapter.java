package com.example.myapplication.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ImageAdapter extends ArrayAdapter<Uri> {

    private ArrayList<Uri> imageList;
    public ImageAdapter(Context context, ArrayList<Uri> imageList) {
        super(context, 0, imageList);
        this.imageList = imageList;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(getContext());
            imageView.setLayoutParams(new LinearLayout.LayoutParams(420, 420)); // 이미지 크기 조절
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        Uri imageUri = getItem(position);
        Glide.with(getContext()).load(imageUri).into(imageView);

        return imageView;
    }

    public void renameUri(int position, Uri newUri) {
        imageList.set(position, newUri);
        notifyDataSetChanged();
    }


}