package com.application.vpp.Adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.application.vpp.HttpsTrustManager;
import com.application.vpp.Interfaces.SliderImagesPojo;
import com.application.vpp.R;
import com.application.vpp.activity.SliderImages;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends PagerAdapter {

    Integer[] IMAGES = {R.drawable.tab1, R.drawable.tab2, R.drawable.tab3, R.drawable.tab8,R.drawable.tab9,R.drawable.tab5,R.drawable.tab6,R.drawable.tab7};
    Context context;
    LayoutInflater layoutInflater;
    int navigate = 0;
    ArrayList<SliderImagesPojo>sliderImagesPojoArrayList;

    public ImageAdapter(Context context, int navigate, ArrayList<SliderImagesPojo>arrayList){
        this.context = context;
        this.navigate = navigate;
        sliderImagesPojoArrayList=arrayList;
    }
    @Override
    public int getCount() {
        if(navigate==0){
            return sliderImagesPojoArrayList.size();
        }else {
            return sliderImagesPojoArrayList.size()-1;
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (LinearLayout)o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view_item = layoutInflater.inflate(R.layout.slidingimages_layout,container,false);
        ImageView image = (ImageView)view_item.findViewById(R.id.image);

        Log.e("zzzzzzzz ", sliderImagesPojoArrayList.get(position).getLink());
        Log.e("SIZEEEE", position+"");

//        Picasso.Builder builder = new Picasso.Builder(context);
//        builder.listener(new Picasso.Listener() {
//            @Override
//            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
//                exception.printStackTrace();
//            });

//
//        String imageUrl_="";
//        if (sliderImagesPojoArrayList.get(position).getSliderLink().contains("https")){
//            imageUrl_=sliderImagesPojoArrayList.get(position).getSliderLink().replace("https", "http");
//        }
//        Picasso.with(context).load().into(image);

        HttpsTrustManager.allowAllSSL();

        Picasso.Builder builder = new Picasso.Builder(context);
        builder.listener(new Picasso.Listener()
        {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
            {
                exception.printStackTrace();
                Log.e( "onImageLoadFailed: ",exception.getMessage() );
            }
        });
        builder.build().load(sliderImagesPojoArrayList.get(position).getLink()).memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE).into(image);
//        FancyButton btnFinish = (FancyButton)view_item.findViewById(R.id.btnFinish1);

        container.addView(view_item);

//        if(position==5){
//            btnFinish.setVisibility(View.VISIBLE);
//        }else {
//            btnFinish.setVisibility(View.GONE);
//        }

//        btnFinish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                context.startActivity(new Intent(context,LoginScreen.class));
//
//
//            }
//        });

        return view_item;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }

}
