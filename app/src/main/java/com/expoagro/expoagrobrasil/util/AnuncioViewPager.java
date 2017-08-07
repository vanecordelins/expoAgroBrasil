package com.expoagro.expoagrobrasil.util;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.expoagro.expoagrobrasil.R;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

/**
 * Created by Samir on 24/07/2017.
 */

public class AnuncioViewPager extends PagerAdapter {

    private ArrayList<String> images;


    private Context context;

    public AnuncioViewPager(Context context, ArrayList<String> images){
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = layoutInflater.inflate(R.layout.teste_viewpager, container, false);
//        ImageView imageView = (ImageView) item_view.findViewById(R.id.imageView6);
        PhotoView photoView = (PhotoView) item_view.findViewById(R.id.photo_view);
        container.addView(item_view);
        Glide.with(context).load(images.get(position)).into(photoView);

        return item_view;
    }

    @Override
    public void destroyItem(ViewGroup viewGroup, int position, Object o){
        viewGroup.removeView((LinearLayout) o);
    }
}
