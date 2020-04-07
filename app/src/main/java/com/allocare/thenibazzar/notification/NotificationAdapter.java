package com.allocare.thenibazzar.notification;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.allocare.thenibazzar.R;
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

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder>
{
        ArrayList<NotificationModel> arrayList;
    Context context;

    public NotificationAdapter(ArrayList<NotificationModel> models,Context context)
    {
        this.arrayList = models;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notificatio_header,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final NotificationModel notificationModel = arrayList.get(position);
        holder.type.setText(notificationModel.getType());
        holder.data1.setText(notificationModel.getData1());
        holder.data2.setText(notificationModel.getData2());

        holder.quantity.setText(notificationModel.getQty() +" x "+ notificationModel.getProductname());

        holder.root_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, NotificationDetailed.class);
                i.putExtra("data1",notificationModel.getData1());
                i.putExtra("id",notificationModel.getId());
                i.putExtra("data2",notificationModel.getData2());
                i.putExtra("date",notificationModel.getDate());
                i.putExtra("comment",notificationModel.getComment());
                i.putExtra("image",notificationModel.getProductimg());
                i.putExtra("type",notificationModel.getType());
                i.putExtra("price",notificationModel.getTotalprice());
                i.putExtra("quantity",notificationModel.getQty());
                i.putExtra("name",notificationModel.getProductname());
                context.startActivity(i);

            }
        });

        if(!notificationModel.getProductimg().equalsIgnoreCase("")) {

            try {
                Glide.with(holder.avatharImg.getContext()).asBitmap() // Bind it with the context of the actual view used
                        .load(notificationModel.getProductimg())
                        //.placeholder(ContextCompat.getDrawable(galleryView.getContext(), R.drawable.who))
                        //.error(ContextCompat.getDrawable(galleryView.getContext(), R.drawable.who))
                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                        .format(DecodeFormat.PREFER_RGB_565)
                        .dontAnimate()// the decode format - this will not use alpha at all
                        .listener(new RequestListener<Bitmap>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
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
                                holder.avatharImg.setImageBitmap(resource);
                            }

                        });
            } catch (Exception e) {
                holder.avatharImg.setImageDrawable(null);

            }
        }else {

        }




    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView type,data1,data2,quantity;
        LinearLayout root_lay;
        ImageView avatharImg;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.type);
            data1 = itemView.findViewById(R.id.data1);
            data2 = itemView.findViewById(R.id.data2);
            root_lay = itemView.findViewById(R.id.root_lay);
            quantity = itemView.findViewById(R.id.quantity);
            avatharImg = itemView.findViewById(R.id.avatharImg);
        }
    }
}
