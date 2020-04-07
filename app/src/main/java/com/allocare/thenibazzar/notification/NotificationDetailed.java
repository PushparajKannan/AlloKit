package com.allocare.thenibazzar.notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.allocare.thenibazzar.R;
import com.allocare.thenibazzar.kitmodule.SaveSharedPreference;
import com.allocare.thenibazzar.kitmodule.Utility;
import com.allocare.thenibazzar.kitmodule.VolleySingleton;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NotificationDetailed extends AppCompatActivity {

    TextView data1, data2, comment, date;

    String id = "";

    // ImageView webtoall;

    Context context;

    ImageView close;

    String image="";


    TextView productId, productTitle, productPrice;

    ImageView imageView;

    @Override
    protected void attachBaseContext(Context newBase) {
        //  String lang_code = "en"; //load it from SharedPref
        Log.e("topLAng","-->"+ SaveSharedPreference.getAppLanguage(newBase));

        // Context context = Utility.changeLang(newBase, SaveSharedPreference.getAppLanguage(newBase));
        super.attachBaseContext(newBase);
    }
    @Override
    protected void onResume() {
        super.onResume();
        // SlangBuddy.getBuiltinUI().hide();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notification_detailed);

        context = this;


        data1 = findViewById(R.id.data1);

        data2 = findViewById(R.id.data2);
        // webtoall = findViewById(R.id.webtoall);
       date = findViewById(R.id.date);
        close = findViewById(R.id.backImg);
        productId = findViewById(R.id.productId);
        productTitle = findViewById(R.id.productTitle);
        productPrice = findViewById(R.id.productPrice);
        imageView = findViewById(R.id.image);

        comment = findViewById(R.id.comment);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

       /* notification_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });*/


        Intent i = getIntent();

        if (i != null) {
            id = i.getStringExtra("id");
            data1.setText(i.getStringExtra("data1"));
            data2.setText(i.getStringExtra("data2"));
            date.setText(i.getStringExtra("date"));
            comment.setText(i.getStringExtra("comment"));
            image = i.getStringExtra("image");
            productId.setText(i.getStringExtra("type"));
            productPrice.setText("Rs : "+i.getStringExtra("price"));
            productTitle.setText(i.getStringExtra("quantity") +" x "+i.getStringExtra("name"));
            //name.setText(i.getStringExtra("name"));
            changeStatus();
        }



        if(!image.equalsIgnoreCase("")) {

            try {
                Glide.with(imageView.getContext()).asBitmap() // Bind it with the context of the actual view used
                        .load(image)
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
                                imageView.setImageBitmap(resource);
                            }

                        });
            } catch (Exception e) {
                imageView.setImageDrawable(null);

            }
        }else {

        }

       /* webtoall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.openWebtoall(context);
            }
        });*/
    }

    private void changeStatus() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.e("status_Params", "----->" + jsonObject);
        String url = SaveSharedPreference.getBaseURL(context)+ Utility.APINAME_DETAILS;


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("Notification_DETAILS", "----->" + response);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("Notification_ERROR", "----->" + error.getMessage());
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<>();
                headers.put("token", SaveSharedPreference.getPrefUserToken(NotificationDetailed.this));
                return headers;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);

    }
}
