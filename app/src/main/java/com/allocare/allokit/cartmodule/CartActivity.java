package com.allocare.allokit.cartmodule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allocare.allokit.R;
import com.allocare.allokit.kitmodule.SaveSharedPreference;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MyCartListAdapter adpater;
    private ArrayList<CartModel> lists = new ArrayList<CartModel>();


    ProgressDialog dialog ;

    Context mActivity;

    ImageView backImg;

  //  ShimmerFrameLayout shimmer;

    LinearLayout emptylay;

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
        setContentView(R.layout.activity_cart);
        mActivity=this;


        dialog = new ProgressDialog(mActivity);
        dialog.setMessage(getResources().getString(R.string.loading));


        backImg = findViewById(R.id.backImg);
        //shimmer = findViewById(R.id.shimmer_view_container);
        emptylay = findViewById(R.id.emptylay);


        adpater =  new MyCartListAdapter(lists,this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adpater);


        showDialog();
       // getProducts();


        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartActivity.super.onBackPressed();
            }
        });

    }

    public class MyCartListAdapter extends RecyclerView.Adapter {

        public static final int TYPE_MENUS = 0, TYPE_POST = 1, VIEW_TYPE_LOADING = 2 , TYPE_DASHBOARD = 3;

        public static final int cutsomViewcount= 0;
        Context mContext;
        private ArrayList<CartModel> post = new ArrayList<CartModel>();


        public MyCartListAdapter(ArrayList<CartModel> data, Context context) {
            this.post = data;
            this.mContext = context;
            // viewPool = new RecyclerView.RecycledViewPool();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view;
            switch (viewType) {

                case TYPE_POST:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mycart_list, parent, false);
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


                            CartModel tempdata = this.post.get(listPosition - cutsomViewcount);

                            PostTypeViewHolder viewHolder = (PostTypeViewHolder) holder;
                            if (tempdata != null) {
                             //   viewHolder.firstBind(tempdata, listPosition - cutsomViewcount);
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

                                    CartModel tempdata = this.post.get(position - cutsomViewcount);

                                    PostTypeViewHolder viewHolder = (PostTypeViewHolder) holder;

                                    if (tempdata != null) {


                                       // viewHolder.firstBind(tempdata, position - cutsomViewcount);


                                    } else {
                                      //  viewHolder.firstBind(tempdata, position - cutsomViewcount);
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


           /* void firstBind(final CartModel tempdata, int listPosition) {


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



            }*/
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
}
