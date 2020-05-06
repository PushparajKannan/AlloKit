package com.allocare.allokit.address;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.allocare.allokit.R;
import com.allocare.allokit.kitmodule.GPS;
import com.allocare.allokit.kitmodule.IGPSActivity;
import com.allocare.allokit.kitmodule.LocationValueModel;
import com.allocare.allokit.kitmodule.OrderSumActivity;
import com.allocare.allokit.kitmodule.SaveSharedPreference;
import com.allocare.allokit.kitmodule.Utility;
import com.allocare.allokit.kitmodule.VolleySingleton;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class AddressActivity extends AppCompatActivity implements IGPSActivity {


    CardView currentLocation;
    TextInputEditText streetnumber, city,phonenumberText,idPinceode, state;

    Context mActivity;
    AlertDialog alert;

    private static final int PERMISSION_REQUEST_CODE_MAIN = 300;

    private GPS gps;

    String adrdress="",pincoeds="";

    TextView save;
    ImageView backImg;

    Dialog searchDialog;

    ArrayList<String> cities = new ArrayList<String>();

    ListView citiesList;

    BlueToothShowAdapter adapterCities;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        mActivity = this;

        try {
            cities.addAll(OrderSumActivity.cities);
        }catch (Exception e) {
            e.printStackTrace();
        }

        alert = new AlertDialog.Builder(mActivity)
                .setTitle("Conform Your Location")  // GPS not found
                .setMessage("Are you sure to proceed") // Want to enable?
                .setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {


                        FinishComplete(adrdress,pincoeds);

                    }
                })
                .setNegativeButton(getResources().getString(R.string.No), null).create();

        currentLocation = findViewById(R.id.currentLocation);
        streetnumber = findViewById(R.id.streetnumber);
        city = findViewById(R.id.city);
        phonenumberText = findViewById(R.id.phonenumberText);
        idPinceode = findViewById(R.id.idPinceode);
        state = findViewById(R.id.state);
        save = findViewById(R.id.save);
        backImg = findViewById(R.id.backImg);

        idPinceode.setText(getResources().getString(R.string.select));

        searchDialog = new Dialog(this);

        //sign_district = findViewById(R.id.sign_district);
        // sign_district.setText("Select State");


        searchDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        searchDialog.setContentView(R.layout.search_dialog);
        searchDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        citiesList = searchDialog.findViewById(R.id.stateList);

        adapterCities = new BlueToothShowAdapter(this);
        adapterCities.setBluetoothList(cities);

        citiesList.setAdapter(adapterCities);

        idPinceode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchDialog.show();
            }
        });

        citiesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

               /* ArrayList<CitiesModel> tempCenter = new ArrayList<CitiesModel>();


                if(cities.get(position)!=null) {
                    if (helpdata.get(cities.get(position)) != null) {
                        tempCenter.addAll(helpdata.get(cities.get(position)));
                    }

                    selectedState.setText(cities.get(position));


                }

                adapter.updateTestListAdapter(tempCenter);*/


               // selPostion = String.valueOf(position);

                idPinceode.setText(cities.get(position));

                if (searchDialog.isShowing()) {
                    searchDialog.dismiss();
                }

            }
        });


        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(LocationValueModel.getmLatitude() != 0.0 && LocationValueModel.getmLongitude() != 0.0)
                {

                    adrdress = Utility.getAddress(mActivity,LocationValueModel.getmLatitude(),LocationValueModel.getmLongitude());

                    pincoeds = Utility.getBinCode(mActivity,LocationValueModel.getmLatitude(),LocationValueModel.getmLongitude());



                    alert.setMessage(adrdress);

                    alertShow();

                }else {
                    Toast.makeText(mActivity, "Wait Location fetching", Toast.LENGTH_SHORT).show();

                }






            }
        });


        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressActivity.super.onBackPressed();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(streetnumber.getText().toString().trim()) &&
                        !TextUtils.isEmpty(city.getText().toString().trim()) &&
                        !TextUtils.isEmpty(state.getText().toString().trim()) &&
                        !TextUtils.isEmpty(idPinceode.getText().toString().trim()))
                {

                    if(Utility.isValidPin(idPinceode.getText().toString().trim())) {

                        String steert="" , citys="", states="",idpincodes="";

                        steert = streetnumber.getText().toString().trim();
                        citys = city.getText().toString().trim();
                        states = state.getText().toString().trim();
                        idpincodes = idPinceode.getText().toString().trim();



                        adrdress = steert +" "+ citys+" "+states+" "+idpincodes;

                        pincoeds = idpincodes;

                        FinishComplete(adrdress,pincoeds);





                       // finish();
                    }else {

                        Toast.makeText(mActivity, getResources().getString(R.string.invalid_pin), Toast.LENGTH_SHORT).show();

                    }



                }else if(TextUtils.isEmpty(streetnumber.getText().toString().trim()))
                {
                    Toast.makeText(mActivity, getResources().getString(R.string.enter_stret), Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(city.getText().toString().trim()))
                {
                    Toast.makeText(mActivity, getResources().getString(R.string.enter_cityname), Toast.LENGTH_SHORT).show();

                }else if(TextUtils.isEmpty(state.getText().toString().trim()))
                {
                    Toast.makeText(mActivity, getResources().getString(R.string.enter_cityname), Toast.LENGTH_SHORT).show();

                }else if(TextUtils.isEmpty(idPinceode.getText().toString().trim()))
                {
                    Toast.makeText(mActivity, getResources().getString(R.string.enter_pincode), Toast.LENGTH_SHORT).show();

                }

            }
        });



        getPINCode();

    }

    private void getAddress()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        },5000);
    }


    private void FinishComplete(String text,String pincode)
    {
        Intent intent=new Intent();
        intent.putExtra("addres",text);
        intent.putExtra("pincode",pincode);
       // setResult(Activity.RESULT_OK,intent);

        setResult(RESULT_OK, intent);

        Toast.makeText(mActivity, getResources().getString(R.string.new_address_saved), Toast.LENGTH_SHORT).show();

        finish();
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


    @Override
    public void locationChanged(double longitude, double latitude) {
        Log.e("Location","Long-->"+longitude + "Lat-->"+ latitude);
        if(latitude != 0.0) {
            LocationValueModel.setmLatitude(latitude);
        }

        if(longitude != 0.0) {
            LocationValueModel.setmLongitude(longitude);
        }
    }

    @Override
    public void displayGPSSettingsDialog() {

    }

    public class BlueToothShowAdapter extends BaseAdapter
    {

        private Context context;

        ArrayList<String> bluetoothList;




        public BlueToothShowAdapter(Context context) {
            this.context = context;
        }


        public void setBluetoothList(ArrayList<String> bluetoothList) {
            this.bluetoothList = bluetoothList;
            notifyDataSetChanged();
        }

        public int getCount() {
            return bluetoothList == null ? 0 : bluetoothList.size();
        }

        @Override
        public Object getItem(int arg0) {
            return bluetoothList != null ? bluetoothList.get(arg0) : null;
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder =null;
            if (convertView==null){
                convertView = LayoutInflater.from(context).inflate(R.layout.layout_sate, null);
                viewHolder=new ViewHolder();
                viewHolder.name = (TextView) convertView.findViewById(R.id.name);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            setDateToView(position,viewHolder);
            // //setItemClickEvent(position,viewHolder);
            return convertView;
        }

        private void setDateToView(int position, ViewHolder viewHolder) {

            if (bluetoothList.get(position) != null) {
                viewHolder.name.setText(bluetoothList.get(position));

            }








            //if(bluetoothList.get(position))

        }


        class  ViewHolder {
            public TextView name=null;
            public TextView text_mac=null;
            public ImageView xinhao=null;
        }



    }

    public void getPINCode() {
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
           // map.put("hostfor",SaveSharedPreference.getHostFor(mActivity));
           // map.put("language",type);
           // map.put("offset","1");
           // map.put("limit","5");

            String url = Utility.BASE_POSTCODE;


            Log.e("Productparams","-->"+map);
            Log.e("ProducturL","-->"+url);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, map, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Log.e("Respoonce","-->"+response);

                    //cancelDialog();
                    try {
                        if(response.getString(Utility.API_RESPONCE_SUCCESS).equalsIgnoreCase("true"))
                        {

                            JSONArray datas = response.getJSONArray("data");
                            if(datas.length()>0)
                            {
                                cities.clear();

                                for (int i=0;i<datas.length();i++)
                                {
                                    JSONObject obj= datas.getJSONObject(i);

                                    cities.add(Utility.NullCheckJson(obj,"postcode"));

                                  //  GroceryKitModule gm = new GroceryKitModule();

                                    //gm.setId(Utility.NullCheckJson(obj,Utility.ID));
                                    //gm.setName(Utility.NullCheckJson(obj,Utility.NAME));
                                   // gm.setDetails(Utility.NullCheckJson(obj,Utility.APINAME_DETAILS));
                                   // gm.setPrice(Utility.NullCheckJson(obj,Utility.PRICE));
                                   // gm.setImage(Utility.NullCheckJson(obj,Utility.IMAGE));

                                   // data.add(gm);

                                }
                            }

                            adapterCities.notifyDataSetChanged();

                          //  setDetails();

                        }

                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                   // cancelDialog();

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


}
