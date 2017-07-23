package com.expoagro.expoagrobrasil.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.expoagro.expoagrobrasil.R;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

/**
 * Created by Samir on 22/07/2017.
 */

public class ProdutoViewPager extends PagerAdapter {

    private Context context;
    private List<Bitmap> images;

    public ProdutoViewPager(Context context, List<Bitmap> images){
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
        View item_view = layoutInflater.inflate(R.layout.produto_viewpager, container, false);
//        ImageView imageView = (ImageView) item_view.findViewById(R.id.imageView6);
        PhotoView photoView = (PhotoView) item_view.findViewById(R.id.photo_view2);
        container.addView(item_view);
//        Glide.with(context).load(images.get(position)).centerCrop().into(photoView);
        photoView.setImageBitmap(images.get(position));


        return item_view;
    }

    @Override
    public void destroyItem(ViewGroup viewGroup, int position, Object o){
        viewGroup.removeView((LinearLayout) o);
    }
}
