package com.cubic.fundo.travello.image_slider;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cubic.fundo.travello.R;



public class SliderPagerAdapter extends PagerAdapter {
    private Context mContext;
    private String[] mImages;
    private LayoutInflater mLayoutInflater;
    private Integer[] img ={R.drawable.demo_image,R.drawable.demo_image_1,R.drawable.division_dhaka};
    public SliderPagerAdapter(Context mContext, String[] mImages){
        this.mContext = mContext;
        this.mImages = mImages;
    }
    @Override
    public int getCount() {
        return mImages.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mLayoutInflater.inflate(R.layout.slider_layout,null);
        ImageView imageView = view.findViewById(R.id.imagePreview);

        Glide.with(mContext)
                .load(mImages[position])
                .apply(new RequestOptions().placeholder(R.drawable.blank_image)
                        .error(R.drawable.blank_image))
                .into(imageView);




        ViewPager viewPager = (ViewPager) container;

        viewPager.addView(view,0);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);

    }
}
