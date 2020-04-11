package com.allocare.thenibazzar.kitmodule;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.allocare.thenibazzar.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyOrderActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MyorderListAdapter adpater;
    private ArrayList<MyorderModel> lists = new ArrayList<MyorderModel>();


    ProgressDialog dialog ;

    Context mActivity;

    ImageView backImg;



    @Override
    protected void attachBaseContext(Context newBase) {
        //  String lang_code = "en"; //load it from SharedPref
        Log.e("topLAng","-->"+ SaveSharedPreference.getAppLanguage(newBase));

       // Context context = Utility.changeLang(newBase, SaveSharedPreference.getAppLanguage(newBase));
        super.attachBaseContext(newBase);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        mActivity=this;

        dialog = new ProgressDialog(mActivity);
        dialog.setMessage(getResources().getString(R.string.loading));


        backImg = findViewById(R.id.backImg);

        /*for(int i=0; i<10; i++) {
            MyorderModel mo= new MyorderModel();
            mo.setId(String.valueOf(i));
            lists.add(mo);

        }*/


        recyclerView = findViewById(R.id.recyclerView);

        adpater =  new MyorderListAdapter(lists,this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adpater);


        showDialog();
        getProducts();


        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyOrderActivity.super.onBackPressed();
            }
        });

    }


    public class MyorderListAdapter extends RecyclerView.Adapter {

        public static final int TYPE_MENUS = 0, TYPE_POST = 1, VIEW_TYPE_LOADING = 2 , TYPE_DASHBOARD = 3;

        public static final int cutsomViewcount= 0;
        Context mContext;
        private ArrayList<MyorderModel> post = new ArrayList<MyorderModel>();


        public MyorderListAdapter(ArrayList<MyorderModel> data, Context context) {
            this.post = data;
            this.mContext = context;
            // viewPool = new RecyclerView.RecycledViewPool();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view;
            switch (viewType) {

                case TYPE_POST:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myorder_list, parent, false);
                    //  new PostTypeViewHolder(view).setIsRecyclable(true);
                    //  .innerRecyclerView.setRecycledViewPool(viewPool);
                    return new PostTypeViewHolder(view);

                case VIEW_TYPE_LOADING:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_lay, parent, false);
                    return new LoadingViewHolder(view);




            }
            return null;
        }

        @Override
        public int getItemViewType(int position) {

            /*if (position == 0) {
                return TYPE_MENUS;
            } else if(position == 1) {
                return TYPE_DASHBOARD;
            }else*/
            if(this.post.get(position - cutsomViewcount) != null) {
                // return TYPE_POST;
                // if (!this.post.get(position-1).getArrayData().get(0).getLink().equalsIgnoreCase("")) {
                return TYPE_POST;

                // return this.post.get(position-1) != null ? TYPE_POST : VIEW_TYPE_LOADING;

            } else {
                return VIEW_TYPE_LOADING;
            }


            //  return this.post.get(position-1) == null ? VIEW_TYPE_LOADING : TYPE_POST;


        }

        //inside the adapter class
        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public int getItemCount() {
            return this.post == null ? 0 : this.post.size() + cutsomViewcount;
        }


        @Override
        public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
            super.onViewAttachedToWindow(holder);

           // Descriptor d = descriptors.get(holder.getAdapterPosition());
            /*if (d != null) {
                if (d.getHeight() > 0) {
                    LinearLayout.LayoutParams params = new
                            LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    // Set the height by params
                    ////Log.e("Pos:-->" + holder.getAdapterPosition(), "--->" + d.getHeight());

                    params.height = d.getHeight();
                    // set height of RecyclerView
                    holder.itemView.setMinimumHeight(params.height);

                }
            }*/

        }

        @Override
        public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {

            ////Log.e("detachedPostion","-->"+holder.getAdapterPosition());

            // Descriptor d = descriptors.get(holder.getAdapterPosition());
            /*Descriptor d = new Descriptor();
            d.setWidth(holder.itemView.getWidth());
            d.setHeight(holder.itemView.getHeight());
            descriptors.put(holder.getAdapterPosition(), d);*/


            super.onViewDetachedFromWindow(holder);
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

            try {


                if (holder instanceof PostTypeViewHolder) {

                    try {
                        if (holder.itemView.getTag(listPosition - cutsomViewcount) == null) {


                            MyorderModel tempdata = this.post.get(listPosition - cutsomViewcount);

                            PostTypeViewHolder viewHolder = (PostTypeViewHolder) holder;
                            if (tempdata != null) {
                                viewHolder.firstBind(tempdata, listPosition - cutsomViewcount);
                            }

                        } else {
                            holder.itemView.getTag(listPosition - cutsomViewcount);

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }else if (holder instanceof LoadingViewHolder) {
                    LoadingViewHolder viewHolder = (LoadingViewHolder) holder;
                }

            } catch (Exception e) {

            }

        }


        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List payloads) {
            if (payloads.isEmpty()) {
                super.onBindViewHolder(holder, position, payloads);
            } else {
                Bundle o = (Bundle) payloads.get(0);
                for (String key : o.keySet()) {
                    if (key.equals("articelid")) {

                        if (holder instanceof PostTypeViewHolder) {

                            try {
                                if (holder.itemView.getTag(position - cutsomViewcount) == null) {

                                    MyorderModel tempdata = this.post.get(position - cutsomViewcount);

                                    PostTypeViewHolder viewHolder = (PostTypeViewHolder) holder;

                                    if (tempdata != null) {


                                        viewHolder.firstBind(tempdata, position - cutsomViewcount);


                                    } else {
                                        viewHolder.firstBind(tempdata, position - cutsomViewcount);
                                    }
                                } else {
                                    holder.itemView.getTag(position - cutsomViewcount);

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
            }
        }




        public class LoadingViewHolder extends RecyclerView.ViewHolder {




            public LoadingViewHolder(@NonNull View itemView) {
                super(itemView);



            }
        }




        public class PostTypeViewHolder extends RecyclerView.ViewHolder {


            ImageView avatharImg;

            TextView date, title, quantity,totalprice,status, orderid;
            CardView cancel;


            public PostTypeViewHolder(View itemView) {
                super(itemView);


                quantity = itemView.findViewById(R.id.quantity);
                date = itemView.findViewById(R.id.date);
                title = itemView.findViewById(R.id.title);
                totalprice = itemView.findViewById(R.id.totalprice);
                status = itemView.findViewById(R.id.status);
                avatharImg = itemView.findViewById(R.id.avatharImg);
                orderid = itemView.findViewById(R.id.orderid);
                cancel = itemView.findViewById(R.id.buyLay);



                //articlecontent.setMovementMethod(LinkMovementMethod.getInstance());
                // articleheading.setMovementMethod(LinkMovementMethod.getInstance());

                ViewGroup.LayoutParams param = itemView.getLayoutParams();
                param.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                itemView.setLayoutParams(param);

                itemView.requestLayout();
                //   articlecontent.setVisibility(View.GONE);


            }


            void firstBind(final MyorderModel tempdata, int listPosition) {


                quantity.setText(tempdata.getQuantity() +" x " +tempdata.getProductName());
                date.setText(tempdata.getDate());
                totalprice.setText(tempdata.getTotalprice());
                status.setText(tempdata.getOrderStatus());
                title.setText(tempdata.getProductName());

                orderid.setText(tempdata.getOrderid());

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        showDialog();
                        setProductsCancel(tempdata.getOrderid(),listPosition);

                       // post.notify();
                       // notifyDataSetChanged();
                    }
                });

                if(!tempdata.getImage().equalsIgnoreCase("")) {

                    try {
                        Glide.with(avatharImg.getContext()).asBitmap() // Bind it with the context of the actual view used
                                .load(tempdata.getImage())
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
                                        avatharImg.setImageBitmap(resource);
                                    }

                                });
                    } catch (Exception e) {
                        avatharImg.setImageDrawable(null);

                    }
                }else {

                }



            }
        }







    }

    public void getProducts() {
        try{
            String language = SaveSharedPreference.getAppLanguage(this);
            //Log.e("Language_Checking","---->"+language);

            String type = "english";
            if(language.equals("ta")) {
                type = "tamil";
            }else if(language.equals("en")) {
                type = "english";

            }





            JSONObject map =new JSONObject();
            map.put("registerid",SaveSharedPreference.getPrefUserRegisterId(mActivity));
            map.put("language",type);
            map.put("offset","1");
            map.put("limit","40");
          //  map.put("userid","2");



            Log.e("Params","--->"+map);

            //String url = SaveSharedPreference.getBaseURL(mActivity)+Utility.APINAME_PRODUCT+"/"+type+"/"+SaveSharedPreference.getHostFor(mActivity)+"/1/5";
            String url = SaveSharedPreference.getBaseURL(mActivity)+Utility.APINAME_ORDERS;


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, map, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Log.e("Respoonce","-->"+response);

                   cancelDialog();
                    try {
                        if(response.getString(Utility.API_RESPONCE_SUCCESS).equalsIgnoreCase("true"))
                        {

                            lists.clear();
                            JSONArray datas = response.getJSONArray("data");
                            if(datas.length()>0)
                            {
                                for (int i=0;i< datas.length();i++)
                                {
                                    JSONObject obj= datas.getJSONObject(i);
                                    MyorderModel gm = new MyorderModel();

                                    gm.setId(Utility.NullCheckJson(obj,Utility.ID));
                                    gm.setProductName(Utility.NullCheckJson(obj,"productname"));
                                    gm.setDate(Utility.NullCheckJson(obj,"orderdate"));
                                    gm.setPrice(Utility.NullCheckJson(obj,"proprice"));
                                    gm.setImage(Utility.NullCheckJson(obj,"productimg"));
                                    gm.setTotalprice(Utility.NullCheckJson(obj,"totalprice"));
                                    gm.setQuantity(Utility.NullCheckJson(obj,"Quantity"));
                                    gm.setPaytype(Utility.NullCheckJson(obj,"paymenttype"));
                                    gm.setOrderStatus(Utility.NullCheckJson(obj,"orderstatus"));
                                    gm.setOrderid(Utility.NullCheckJson(obj,"orderid"));

                                    lists.add(gm);

                                }
                            }


                            adpater.notifyDataSetChanged();

                        }

                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    cancelDialog();

                    error.printStackTrace();
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> headers = new HashMap<>();
                    headers.put("token", SaveSharedPreference.getPrefUserToken(mActivity));
                    return headers;
                }
            };

            request.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(mActivity).addToRequestQueue(request);

            /*{
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<>();
                headers.put("token", SaveSharedPreference.getPrefUserToken(mActivity));
                return headers;
            }
            }*/


            /*String language = SaveSharedPreference.getAppLanguage(this);
            //Log.e("Language_Checking","---->"+language);

            String type = "english";
            if(language.equals("ta")) {
                type = "tamil";
            }else if(language.equals("en")) {
                type = "english";

            }*/

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void showDialog() {
        if(dialog!=null) {
            if(!dialog.isShowing())
                dialog.show();
        }
    }

    public void cancelDialog()
    {
        if(dialog!=null) {
            if(dialog.isShowing())
                dialog.dismiss();
        }
    }


    public void setProductsCancel(String orderid,int postion) {
        try{
            String language = SaveSharedPreference.getAppLanguage(this);
            //Log.e("Language_Checking","---->"+language);

            String type = "english";
            if(language.equals("ta")) {
                type = "tamil";
            }else if(language.equals("en")) {
                type = "english";

            }





            JSONObject map =new JSONObject();
            map.put("orderid",orderid);
            //map.put("language",type);
            //map.put("offset","1");
            //map.put("limit","40");
            //  map.put("userid","2");



            Log.e("Params","--->"+map);

            //String url = SaveSharedPreference.getBaseURL(mActivity)+Utility.APINAME_PRODUCT+"/"+type+"/"+SaveSharedPreference.getHostFor(mActivity)+"/1/5";
            String url = SaveSharedPreference.getBaseURL(mActivity)+Utility.APINAME_ORDERCANCEL;


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, map, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Log.e("Respoonce","-->"+response);

                    cancelDialog();
                    try {
                        if(response.getString(Utility.API_RESPONCE_SUCCESS).equalsIgnoreCase("true"))
                        {

                            lists.remove(postion);


                            adpater.notifyDataSetChanged();
                            Toast.makeText(mActivity, getResources().getString(R.string.order_cancel), Toast.LENGTH_SHORT).show();

                        }

                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    cancelDialog();

                    error.printStackTrace();
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> headers = new HashMap<>();
                    headers.put("token", SaveSharedPreference.getPrefUserToken(mActivity));
                    return headers;
                }
            };

            request.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(mActivity).addToRequestQueue(request);

            /*{
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<>();
                headers.put("token", SaveSharedPreference.getPrefUserToken(mActivity));
                return headers;
            }
            }*/


            /*String language = SaveSharedPreference.getAppLanguage(this);
            //Log.e("Language_Checking","---->"+language);

            String type = "english";
            if(language.equals("ta")) {
                type = "tamil";
            }else if(language.equals("en")) {
                type = "english";

            }*/

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }



}
