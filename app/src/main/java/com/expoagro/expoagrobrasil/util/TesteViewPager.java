package com.expoagro.expoagrobrasil.util;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.expoagro.expoagrobrasil.R;
import com.github.chrisbanes.photoview.PhotoView;

/**
 * Created by Samir on 19/07/2017.
 */

public class TesteViewPager extends PagerAdapter {



    private String[] images= {
            "https://firebasestorage.googleapis.com/v0/b/expoagro-brasil.appspot.com/o/teste%2FDILMA%20(1).png?alt=media&token=1bf1f081-eadd-4184-90e7-15f38056a7d7",
            "https://firebasestorage.googleapis.com/v0/b/expoagro-brasil.appspot.com/o/teste%2Fdilma-rousseff5-1.jpg?alt=media&token=290569ee-fec4-4bf7-9134-b647db098e07",
            "https://firebasestorage.googleapis.com/v0/b/expoagro-brasil.appspot.com/o/teste%2FDILMA.png?alt=media&token=7ac946f9-8905-4426-8c4d-ed2486a49786",
            "https://firebasestorage.googleapis.com/v0/b/expoagro-brasil.appspot.com/o/teste%2Fdilmaruim-600x337.jpg?alt=media&token=7dc16c6e-4307-4e64-a1e8-0432e2ecf2c9",
            "https://firebasestorage.googleapis.com/v0/b/expoagro-brasil.appspot.com/o/temer.jpg?alt=media&token=6a70ac67-7e0f-45ff-8c53-f25044d1843b",
            "https://firebasestorage.googleapis.com/v0/b/expoagro-brasil.appspot.com/o/temer.jpg?alt=media&token=e2a8536d-2d26-4a94-8438-c4f1fd8477c3",
            "https://firebasestorage.googleapis.com/v0/b/expoagro-brasil.appspot.com/o/temer.jpg?alt=media&token=429c58c2-5d22-45ee-946b-a7b55addff32"
    };


    private Context context;
    private LayoutInflater layoutInflater;

    public TesteViewPager(Context context){
        this.context = context;
        //this.images = images;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = layoutInflater.inflate(R.layout.teste_viewpager, container, false);
//        ImageView imageView = (ImageView) item_view.findViewById(R.id.imageView6);
        PhotoView photoView = (PhotoView) item_view.findViewById(R.id.photo_view);
        container.addView(item_view);
        Glide.with(context).load(images[position]).centerCrop().into(photoView);


        return item_view;
    }

    @Override
    public void destroyItem(ViewGroup viewGroup, int position, Object o){
        viewGroup.removeView((LinearLayout) o);
    }
}
