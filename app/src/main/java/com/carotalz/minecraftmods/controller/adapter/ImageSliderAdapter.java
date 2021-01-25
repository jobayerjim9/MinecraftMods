package com.carotalz.minecraftmods.controller.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.carotalz.minecraftmods.R;
import com.carotalz.minecraftmods.models.Constants;
import com.carotalz.minecraftmods.models.ModModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ImageSliderAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<ModModel.Images> images;
    private LayoutInflater inflater;

    public ImageSliderAdapter(Context context, ArrayList<ModModel.Images> images) {
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View imageLayout = inflater.inflate(R.layout.item_image_slider, container, false);
        assert imageLayout != null;
        final ImageView imageView =  imageLayout
                .findViewById(R.id.image);
        try {
            String fileName = Constants.IMAGE_PATH + images.get(position).getImage();
            InputStream ims = context.getAssets().open(fileName);
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, images.get(position).getImage());
            // set image to ImageView
            imageView.setImageDrawable(d);
        } catch (IOException e) {
            e.printStackTrace();
        }
        container.addView(imageLayout,0);

        return imageLayout;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
