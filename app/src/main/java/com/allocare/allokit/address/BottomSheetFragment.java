package com.allocare.allokit.address;


import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.allocare.allokit.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    public static String images = "";
    public ImageView imageView;

    public RelativeLayout progress;

    public static BottomSheetFragment newInstance(String image) {
        BottomSheetFragment fragment = new BottomSheetFragment();
        Bundle args = new Bundle();
        args.putString("image", image);
        fragment.setArguments(args);
        images  = image;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getArguments()!=null) {
            images =  getArguments().getString("image");

         if(imageView!=null) {

             if (!images.equalsIgnoreCase("")) {

                 try {
                     Glide.with(imageView.getContext()).asBitmap() // Bind it with the context of the actual view used
                             .load(images)
                             //.placeholder(ContextCompat.getDrawable(galleryView.getContext(), R.drawable.who))
                             //.error(ContextCompat.getDrawable(galleryView.getContext(), R.drawable.who))
                             .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                             .format(DecodeFormat.PREFER_RGB_565)
                             .dontAnimate()// the decode format - this will not use alpha at all
                             .listener(new RequestListener<Bitmap>() {
                                 @Override
                                 public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                     progress.setVisibility(View.GONE);
                                     imageView.setVisibility(View.VISIBLE);
                                     return false;
                                 }

                                 @Override
                                 public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                     progress.setVisibility(View.GONE);
                                     imageView.setVisibility(View.VISIBLE);
                                     return false;
                                 }
                             })
                             .centerCrop() // scale type
                             //.placeholder(R.drawable.default_product_400_land) // temporary holder displayed while the image loads
                             //.animate(R.anim.fade_in) // need to manually set the animation as bitmap cannot use cross fade
                             .thumbnail(0.2f) // make use of the thumbnail which can display a down-sized version of the image
                             .into(new SimpleTarget<Bitmap>() {
                                 @Override
                                 public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                     imageView.setImageBitmap(resource);
                                 }

                             });
                 } catch (Exception e) {
                     imageView.setImageDrawable(getResources().getDrawable(R.drawable.itemlists));

                 }
             } else {
                 imageView.setImageDrawable(getResources().getDrawable(R.drawable.itemlists));

             }
         }

        }


    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.fragment_bottomsheet, null);
        dialog.setContentView(contentView);
        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        imageView = contentView.findViewById(R.id.imageView);
        progress = contentView.findViewById(R.id.progressLay);

        if (!images.equalsIgnoreCase("")) {

            try {
                Glide.with(imageView.getContext()).asBitmap() // Bind it with the context of the actual view used
                        .load(images)
                        //.placeholder(ContextCompat.getDrawable(galleryView.getContext(), R.drawable.who))
                        //.error(ContextCompat.getDrawable(galleryView.getContext(), R.drawable.who))
                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                        .format(DecodeFormat.PREFER_RGB_565)
                        .dontAnimate()// the decode format - this will not use alpha at all
                        .listener(new RequestListener<Bitmap>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                progress.setVisibility(View.GONE);
                                imageView.setVisibility(View.VISIBLE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                progress.setVisibility(View.GONE);
                                imageView.setVisibility(View.VISIBLE);
                                return false;
                            }
                        })
                        .centerCrop() // scale type
                        //.placeholder(R.drawable.default_product_400_land) // temporary holder displayed while the image loads
                        //.animate(R.anim.fade_in) // need to manually set the animation as bitmap cannot use cross fade
                        .thumbnail(0.2f) // make use of the thumbnail which can display a down-sized version of the image
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                imageView.setImageBitmap(resource);
                            }

                        });
            } catch (Exception e) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.itemlists));

            }
        } else {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.itemlists));

        }


    }

}
