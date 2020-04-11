package com.allocare.thenibazzar.kitmodule;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.allocare.thenibazzar.R;
import com.allocare.thenibazzar.address.AddressActivity;
import com.allocare.thenibazzar.notification.NotificationActivity;
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
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Grocerykit extends AppCompatActivity implements View.OnClickListener ,IGPSActivity {

    ImageView optionMenu, increase, decrease, image;

    CardView checkOutLay;
    TextView totalprice, quantityText, productTitle, productPrice,languageText;

    TextInputEditText addressText;

    RadioGroup paymentType;
    RadioButton cash, digital;

    Context mActivity;

    private GPS gps;
    private static final int PERMISSION_REQUEST_CODE_MAIN = 300;


    ArrayList<GroceryKitModule> data = new ArrayList<GroceryKitModule>();

    ProgressDialog dialog ;

    public int quantity=1;
    public int pricetotal = 1000;

    AlertDialog alert;

   public String productid="1";

   ImageView badage;

   TextView change;
    String selectedpaymentType;
    final int UPI_PAYMENT=0;


   /* @Override
    protected void attachBaseContext(Context newBase) {
      //  String lang_code = "en"; //load it from SharedPref
        Log.e("topLAng","-->"+ SaveSharedPreference.getAppLanguage(newBase));

       // Utility.changeLang(newBase, SaveSharedPreference.getAppLanguage(newBase));
        super.attachBaseContext(Utility.setLocale(newBase, SaveSharedPreference.getAppLanguage(newBase)));
    }*/

    @Override
    protected void attachBaseContext(Context newBase) {

        Log.e("topLAng","-->"+ SaveSharedPreference.getAppLanguage(newBase));

       // Context context = Utility.changeLang(newBase, SaveSharedPreference.getAppLanguage(newBase));
        super.attachBaseContext(newBase);
    }

   RelativeLayout notification;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocerykit);
        mActivity=this;

        dialog = new ProgressDialog(mActivity);
        dialog.setMessage(getResources().getString(R.string.loading));

        alert = new AlertDialog.Builder(mActivity)
                .setTitle("Conform Order")  // GPS not found
                .setMessage("Are you sure to proceed") // Want to enable?
                .setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton(getResources().getString(R.string.No), null).create();


        optionMenu = findViewById(R.id.optionMenu);
        decrease = findViewById(R.id.decrease);
        increase = findViewById(R.id.increase);
        image = findViewById(R.id.image);
        badage = findViewById(R.id.badage);
        change = findViewById(R.id.change);


        totalprice = findViewById(R.id.totalprice);
        quantityText = findViewById(R.id.quantityText);
        productTitle = findViewById(R.id.productTitle);
        productPrice = findViewById(R.id.productPrice);
        languageText = findViewById(R.id.languageText);

        addressText = findViewById(R.id.addressText);
        paymentType = findViewById(R.id.paymentType);

        cash = findViewById(R.id.cash);
        digital = findViewById(R.id.digital);

        checkOutLay = findViewById(R.id.checkOutLay);

        notification = findViewById(R.id.notification);


        increase.setOnClickListener(this);
        decrease.setOnClickListener(this);
        optionMenu.setOnClickListener(this);
        checkOutLay.setOnClickListener(this);
        languageText.setOnClickListener(this);
        notification.setOnClickListener(this);
        change.setOnClickListener(this);

        badage.setVisibility(View.GONE);

        if(!SaveSharedPreference.getUserArea(mActivity).equalsIgnoreCase(""))
        {
            addressText.setText(SaveSharedPreference.getUserArea(mActivity));
        }else {

          //  Toast.makeText(mActivity, getResources().getString(R.string.pleas_fil_adress), Toast.LENGTH_SHORT).show();
        }

        showDialog();

        getProducts();


        getNotifications();


    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.optionMenu:
                showOptionMenu();
                break;
            case R.id.decrease:
                decrementValue();
                break;
            case R.id.increase:

                incrementValue();
                break;
            case R.id.checkOutLay:

               // if(!SaveSharedPreference.getVerifystatus(mActivity).equalsIgnoreCase("No")) {

                if(!SaveSharedPreference.getUserArea(mActivity).equalsIgnoreCase(""))
                {
                    if(!TextUtils.isEmpty(addressText.getText().toString().trim()))
                    {


                        int radioButtonID = paymentType.getCheckedRadioButtonId();
                        View radioButton = paymentType.findViewById(radioButtonID);
                        int idx = paymentType.indexOfChild(radioButton);
                        RadioButton r = (RadioButton) paymentType.getChildAt(idx);
                        selectedpaymentType= r.getText().toString();

                        if(selectedpaymentType.equalsIgnoreCase("Cash"))
                        {
                            showDialog();
                            OrderProducts(addressText.getText().toString().trim(),"1");
                        }else if(selectedpaymentType.equalsIgnoreCase("Digital"))
                        {
                            int total = quantity * pricetotal;
                           // Log.e("Total Amount","------->"+total);
                            payUsingPay("1","iamsivaram27@oksbi","Sivaram","Sivaram");
                        }

                    }else {
                        Toast.makeText(mActivity, getResources().getString(R.string.enter_address), Toast.LENGTH_SHORT).show();
                    }
                }else {

                    Toast.makeText(mActivity, getResources().getString(R.string.pleas_fil_adress), Toast.LENGTH_SHORT).show();
                }

                /*}else {

                    Intent  i =new Intent(mActivity,SignOTPUpActivity.class);

                    i.putExtra("type","verify");

                    startActivity(i);
                   // overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

                    Toast.makeText(mActivity, getResources().getString(R.string.verify_your_otp), Toast.LENGTH_SHORT).show();

                    finish();
                }*/



                break;

            case R.id.languageText:
                languageSelect();
                break;

            case R.id.notification:

                Intent i = new Intent(mActivity, NotificationActivity.class);
                startActivity(i);
                //overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

                break;
            case R.id.change:

                Intent s =new Intent(mActivity, AddressActivity.class);
                startActivityForResult(s,23);


                break;
        }
    }

    private void languageSelect()
    {
        String language = SaveSharedPreference.getAppLanguage(this);
        //Log.e("Language_Checking","---->"+language);

       // String type = "english";
        if(language.equals("en")) {
           // type = "tamil";
            Utility.changeLang(mActivity, "ta");
            SaveSharedPreference.setAppLanguge(mActivity,"ta");

        }else {
            Utility.changeLang(mActivity, "en");
            SaveSharedPreference.setAppLanguge(mActivity,"en");


        }

        Log.e("selectedLanguage","-->"+ SaveSharedPreference.getAppLanguage(mActivity));

        updateView(SaveSharedPreference.getAppLanguage(this));

    }

    private void incrementValue() {

        quantity++;

        quantityText.setText(String.valueOf(quantity));

        int total = quantity * pricetotal;
        totalprice.setText(String.valueOf("Rs : "+ total));
    }

    private void decrementValue()
    {
        if(quantity>1) {
            quantity--;

            quantityText.setText(String.valueOf(quantity));

            int total = quantity * pricetotal;
            totalprice.setText(String.valueOf("Rs : "+ total));

        }

    }

    @Override
    protected void onResume() {


        super.onResume();

        if(checkPermissionMain()) {
            if(gps!=null) {
                if (!gps.isRunning()) gps.resumeGPS();
            }
        }

        getLocationWithPermission();


    }

    private void getLocationWithPermission() {

        if(checkPermissionMain()) {
            gps = new GPS(this);
        }else {
            requestPermissionMain();
        }

    }

    @Override
    protected void onStop() {
        gps.stopGPS();
        super.onStop();
    }

    public boolean checkPermissionMain() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        //int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

        return result == PackageManager.PERMISSION_GRANTED; //&& result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissionMain() {

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE_MAIN);

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {


            case PERMISSION_REQUEST_CODE_MAIN:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    // boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted) {

                        gps = new GPS(this);


                    }
                        /*////Here to do the task
                        lm = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
                        boolean gps_enabled = false;
                        try {
                            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                        } catch (Exception ex) {
                        }
                        if (gps_enabled) {
                            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, locationListener);
                            //if(latitude.equals("") && longitude.equals(""))
                            //{
                            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if(location!=null)
                            {
                                if(location.getLatitude() != 0.0) {
                                    LocationValueModel.setmLatitude(location.getLatitude());
                                }

                                if(location.getLongitude()!= 0.0) {
                                    LocationValueModel.setmLongitude(location.getLongitude());
                                }

                                // longitude = String.valueOf(location.getLongitude());
                                // latitude = String.valueOf(location.getLatitude());
                            }else
                            {
                                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, locationListener);
                            }
                            //  }
                            // doAction();
                        } else {
                            new AlertDialog.Builder(mActivity)
                                    .setTitle(getResources().getString(R.string.LocationisNotEnable))  // GPS not found
                                    .setMessage(getResources().getString(R.string.Wanttoenable)) // Want to enable?
                                    .setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                        }
                                    })
                                    .setNegativeButton(getResources().getString(R.string.No), null)
                                    .show();
                        }
                    }*/
                }
                break;


        }
    }


    public void showOptionMenu()
    {
        //Creating the instance of PopupMenu
        PopupMenu popup= new PopupMenu(Grocerykit.this, optionMenu);
        popup.getMenuInflater().inflate(R.menu.option_menu,popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item){
                //Intent intent = new Intent(Grocerykit.this,MyOrderActivity.class);
                Intent intent = new Intent(Grocerykit.this,OrderSumActivity.class);
                startActivity(intent);
                //overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

                return true;
            }
        });
        popup.show();//showing popup menu
    }

    public void OrderProducts(String address,String id) {
        try{
            String language = SaveSharedPreference.getAppLanguage(mActivity);
            //Log.e("Language_Checking","---->"+language);

            String type = "english";
            if(language.equals("ta")) {
                type = "tamil";
            }else if(language.equals("en")) {
                type = "english";

            }

           /* {
                "productid":"1",
                    "registerid":"4",
                    "userid":"2",
                    "qty":"1",
                    "paytype":"Digital",
                    "location":"testing",
                    "address":"testing"
            }*/



            String link = "http://www.google.com/maps/place/" + String.valueOf(LocationValueModel.getmLatitude()) + "," + String.valueOf(LocationValueModel.getmLongitude());

            JSONObject map =new JSONObject();
            map.put("productid",id);
            map.put("registerid",SaveSharedPreference.getPrefUserRegisterId(mActivity));
            map.put("userid",SaveSharedPreference.getHostFor(mActivity));
            map.put("qty",quantity);
            map.put("paytype",selectedpaymentType);
            map.put("location",link);
            map.put("address",address);


            Log.e("Params","-->"+map);
            //String url = SaveSharedPreference.getBaseURL(mActivity)+Utility.APINAME_PRODUCT+"/"+type+"/"+SaveSharedPreference.getHostFor(mActivity)+"/1/5";
            String url = SaveSharedPreference.getBaseURL(mActivity)+Utility.APINAME_PLACEORDER;


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, map, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Log.e("Respoonce","-->"+response);

                    cancelDialog();
                    try {
                        if(response.getString(Utility.API_RESPONCE_SUCCESS).equalsIgnoreCase("true"))
                        {

                            Toast.makeText(mActivity, getResources().getString(R.string.your_order_sucessfull), Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Grocerykit.this,MyOrderActivity.class);
                            startActivity(intent);
                           // overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

                            finish();


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

                    Log.d("TAG", "Error: " + error
                            + "\nStatus Code " + error.networkResponse.statusCode
                            + "\nResponse Data " + error.networkResponse.data
                            + "\nCause " + error.getCause()
                            + "\nmessage" + error.getMessage());
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("token", SaveSharedPreference.getPrefUserToken(mActivity));
                    Log.e("Header",""+headers);

                    return headers;

                }
                @Override
                public String getBodyContentType() {
                    return "application/json";
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
            map.put("hostfor",SaveSharedPreference.getHostFor(mActivity));
            map.put("language",type);
            map.put("offset","1");
            map.put("limit","5");

             String url = SaveSharedPreference.getBaseURL(mActivity)+Utility.APINAME_PRODUCT;


             Log.e("Productparams","-->"+map);
             Log.e("ProducturL","-->"+url);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, map, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Log.e("Respoonce","-->"+response);

                    cancelDialog();
                    try {
                        if(response.getString(Utility.API_RESPONCE_SUCCESS).equalsIgnoreCase("true"))
                        {

                            JSONArray datas = response.getJSONArray("data");
                            if(datas.length()>0)
                            {
                                for (int i=0;i<1;i++)
                                {
                                    JSONObject obj= datas.getJSONObject(i);
                                    GroceryKitModule gm = new GroceryKitModule();

                                    gm.setId(Utility.NullCheckJson(obj,Utility.ID));
                                    gm.setName(Utility.NullCheckJson(obj,Utility.NAME));
                                    gm.setDetails(Utility.NullCheckJson(obj,Utility.APINAME_DETAILS));
                                    gm.setPrice(Utility.NullCheckJson(obj,Utility.PRICE));
                                    gm.setImage(Utility.NullCheckJson(obj,Utility.IMAGE));

                                    data.add(gm);

                                }
                            }


                            setDetails();

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

                    Log.e("error",error.toString());

                    error.printStackTrace();
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> headers = new HashMap<>();
                    headers.put("token", SaveSharedPreference.getPrefUserToken(mActivity));
                    Log.e("Header",""+headers);

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

    public void updateView(String language) {
        //Log.e("Button Clicked","----->UpdateView");
        //Log.e("Update View"," ");
        //LocaleHelper.setLocale(this, language);
        //Tovuti.from(MainActivity.this).stop();

        setLocale(this,language);

        Intent i = getIntent();
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(getIntent());
        // Tovuti.from(MainActivity.this).stop();
        overridePendingTransition(0, 0);
        overridePendingTransition(0, 0);
        finish();
        //call_police.setText(resources.getString(R.string.Call_Namakkal_Police));
    }

    private void setDetails()
    {
        GroceryKitModule tempdat = data.get(0);
        if(tempdat!=null)
        {
            pricetotal = Integer.parseInt(tempdat.getPrice());
            productid = tempdat.getId();
            productPrice.setText("Rs : "+tempdat.getPrice());
            productTitle.setText(tempdat.getName());

            quantityText.setText(String.valueOf(quantity));
            totalprice.setText(String.valueOf("Rs : "+(pricetotal * quantity)));

            if(!tempdat.getImage().equalsIgnoreCase("")) {

                try {
                    Glide.with(image.getContext()).asBitmap() // Bind it with the context of the actual view used
                            .load(tempdat.getImage())
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
                                    image.setImageBitmap(resource);
                                }

                            });
                } catch (Exception e) {
                    image.setImageDrawable(null);

                }
            }else {

            }

        }
    }

    @Override
    public void locationChanged(double longitude, double latitude) {
        Log.e("Location","Long-->"+longitude + "Lat-->"+ latitude);
        if(latitude != 0.0) {
            LocationValueModel.setmLatitude(latitude);
        }

        if(longitude != 0.0) {
            LocationValueModel.setmLongitude(longitude);
        }

        addressText = findViewById(R.id.addressText);


        /*if(addressText!=null) {
            if (TextUtils.isEmpty(addressText.getText().toString().trim())) {
                addressText.setText(Utility.getAddress(mActivity, LocationValueModel.getmLatitude(), LocationValueModel.getmLongitude()));

            }
        }*/

    }

    @Override
    public void displayGPSSettingsDialog() {

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

    public void alertShow()
    {
        if(alert!=null) {
            if(!alert.isShowing()) {
                alert.show();
            }
        }
    }

    public void alertDismiss() {
        if(alert!=null) {
            if(alert.isShowing()) {
                alert.dismiss();
            }
        }
    }


    private void getNotifications()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("registerid",SaveSharedPreference.getPrefUserRegisterId(mActivity));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = SaveSharedPreference.getBaseURL(mActivity)+Utility.APINAME_COUNT;

        Log.e("Params Count","---->"+jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Notification_RESPONSE","----->"+response);
                        try {
                            if(response.getString("success").equalsIgnoreCase("true"))
                            {
                              //  notification_count.setText(response.getString("count"));
                                badage.setVisibility(View.VISIBLE);
                            }else
                            {
                               // notification_count.setText("0");
                                badage.setVisibility(View.GONE);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Notification_ERROR","----->"+error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<>();
                headers.put("token", SaveSharedPreference.getPrefUserToken(mActivity));
                return headers;
            }
        };


        VolleySingleton.getInstance(mActivity).addToRequestQueue(jsonObjectRequest);

    }

        public static void setLocale(Activity context,String langCode) {
            Locale locale;
            //Log.e("Lan",session.getLanguage());
            locale = new Locale(langCode);
            Configuration config = new Configuration(context.getResources().getConfiguration());

            Locale.setDefault(locale);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                config.setLocale(locale);
            }else {
                context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());

            }

            context.getBaseContext().getResources().updateConfiguration(config,
                    context.getBaseContext().getResources().getDisplayMetrics());
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK)
        {
            switch (requestCode)
            {
                case 23:

                    if(data!=null)
                    {
                        addressText.setText(data.getStringExtra("addres"));
                    }



                    break;
                case UPI_PAYMENT:
                    if (data!=null){
                        String text = data.getStringExtra("response");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(text);
                        upiPaymentDataOperation(dataList);
                    } else {
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                    break;
            }

        }

    }


    //Sivaram
    private void payUsingPay(String amount_str, String upi_str, String name_str, String note_str) {
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa",upi_str)
                .appendQueryParameter("pn",name_str)
                .appendQueryParameter("tn",note_str)
                .appendQueryParameter("am",amount_str)
                .appendQueryParameter("cu","INR")
                .build();
        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);
        Intent chooser = Intent.createChooser(upiPayIntent,"Pay with");

        if (null != chooser.resolveActivity(getPackageManager())){
            startActivityForResult(chooser,UPI_PAYMENT);
        }else {
            Toast.makeText(this,"No UPI app found,please install one to continue",Toast.LENGTH_SHORT).show();
        }
    }


    private void upiPaymentDataOperation(ArrayList<String> data) {

            String str = data.get(0);
            String paymentCancel = "";
            if (str == null) str = "discard";
            String status = "";
            String approvalRefNo ="";
            String response[] = str.split("&");
            for (int i=0;i<response.length;i++){
                String equalStr[] = response[i].split("=");
                if (equalStr.length>=2){
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())){
                        status = equalStr[1].toLowerCase();
                    }else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase())|| equalStr[0].toLowerCase().equals("tnxRef".toLowerCase())){
                        approvalRefNo = equalStr[1];
                    }
                } else {
                    paymentCancel = "Payment cancelled by User.";
                }
            }
            if (status.equals("success")){
                Toast.makeText(Grocerykit.this,"Transaction  Successful.",Toast.LENGTH_SHORT).show();
                showDialog();
                OrderProducts(addressText.getText().toString().trim(),"1");
            }else if ("Payment cancelled by User.".equals(paymentCancel)){
                Toast.makeText(Grocerykit.this,"Payment cancelled by User.",Toast.LENGTH_SHORT).show();
                finish();
            }else {
                Toast.makeText(Grocerykit.this,"Transaction failed.Please try again",Toast.LENGTH_SHORT).show();
                finish();
            }

    }
}


   /* public class GorceryListAdpter extends RecyclerView.Adapter<GorceryListAdpter.MyviewHolder> {

        List<GroceryKitModule> statusList = Collections.emptyList();
        //  private List<Word> mWords = Collections.emptyList(); // Cached copy of words


        Context mContext;


        public GorceryListAdpter(Context mContext, List<GroceryKitModule> statusList) {
            this.mContext = mContext;
            this.statusList = statusList;
        }


        @NonNull
        @Override
        public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.grocery_list_items, parent, false);
            return new MyviewHolder(itemview);
        }

        @Override
        public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {

            ////Log.e("StatusLay", "postion-->" + position);

            GroceryKitModule data = statusList.get(position);

            if (data != null) {

                holder.bind(data);
                //    holder.bind(data);
                //  holder.title.setText(data.getName());
            }

                *//*if(position==0)
                {
                    int sizeInDP = 16;

                    int marginInDp = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, sizeInDP, getResources()
                                    .getDisplayMetrics());

                    ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
                    p.setMargins(marginInDp, 0, 0, 0);
                    holder.itemView.setLayoutParams(p);
                    holder.itemView.requestLayout();
                }else if(position==statusList.size())
                {
                    int sizeInDP = 16;

                    int marginInDp = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, sizeInDP, getResources()
                                    .getDisplayMetrics());

                    ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
                    p.setMargins(0, 0, sizeInDP, 0);
                    holder.itemView.setLayoutParams(p);
                    holder.itemView.requestLayout();
                }*//*


        }

        @Override
        public int getItemCount() {
            return statusList.size();
        }


        public class MyviewHolder extends RecyclerView.ViewHolder {


            TextView title,area;
            ImageView icon;

            @SuppressLint("SetTextI18n")
            public MyviewHolder(@NonNull View itemView) {
                super(itemView);

                title = itemView.findViewById(R.id.description);
                area = itemView.findViewById(R.id.title);
                // icon = itemView.findViewById(R.id.icon);


            }

            void bind(DonateModel data)
            {
                String are = data.getDonateDistrict() +", " + data.getDonateArea();

                title.setText(data.getDonateDetails());
                area.setText(are);
            }

            *//*void bind(final MenuListAdpter data) {
                title.setText(data.ge());

                try {
                    icon.setImageResource(data.getImage());
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent  i = null;

                        if(data.getName().equalsIgnoreCase(getResources().getString(R.string.help_line)))
                        {
                            // i = new Intent(mActivity, QuatCapActivity.class);

                            if(!SaveSharedPreference.getQuaId(mActivity).equalsIgnoreCase("")) {
                                i = new Intent(mActivity, QuatCapActivity.class);
                                // i.putExtra("id",quaid.getString("quaid",""));
                            }else {
                                i = new Intent(mActivity, QuarantineActivity.class);
                            }

                        }else if(data.getName().equalsIgnoreCase(getResources().getString(R.string.test_center)))
                        {
                            i = new Intent(mActivity,AwarenessActivity.class);


                        }else if(data.getName().equalsIgnoreCase(getResources().getString(R.string.self_screen))){
                            //i = new Intent(mActivity,HelpLineActivity.class);
                            i = new Intent(mActivity,ScreenMirrorActivity.class);

                        }else if(data.getName().equalsIgnoreCase(getResources().getString(R.string.FakeBot))){
                            i = new Intent(mActivity,FakeDetector.class);
                        }else if(data.getName().equalsIgnoreCase("Request")) {
                            i =new Intent(mActivity, RequestActivity.class);

                        }else if(data.getName().equalsIgnoreCase("Donate")) {
                            i =new Intent(mActivity, DonateActivity.class);

                        }else if(data.getName().equalsIgnoreCase("Valentier")) {
                            i =new Intent(mActivity, ValentierActivity.class);

                        }



                        startActivity(i);

                    }
                });

            }*//*
        }
    }*/





