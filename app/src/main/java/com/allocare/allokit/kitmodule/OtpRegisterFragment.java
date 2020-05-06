package com.allocare.allokit.kitmodule;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.allocare.allokit.GsingupActivity;
import com.allocare.allokit.R;
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


public class OtpRegisterFragment extends Fragment implements View.OnClickListener {

    TextView title;

    TextInputEditText usernameText, phonenumberText, addressText,emailText;

    CardView singInBtn;

    SignOTPUpActivity mActivity;

   public CustomViewPager viewPager;
    ProgressDialog dialog ;

    TextInputEditText streetnumber, city,idPinceode,state;

    Dialog searchDialog;

    ArrayList<String> cities = new ArrayList<String>();

    ListView citiesList;

    BlueToothShowAdapter adapterCities;



   /* @Override
    public void onAttach(@NonNull Context context) {

        //   language = PreferenceManager.getDefaultSharedPreferences(context).getString("language", "en");

        super.onAttach(LocaleHelper.OnAttach(context, SaveSharedPreference.getAppLanguage(context)));
    }*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.otpregister_frame, container, false);

        mActivity  = ((SignOTPUpActivity) getActivity());
        title = mActivity.findViewById(R.id.title);
        singInBtn = view.findViewById(R.id.singInBtn);
        viewPager = mActivity.findViewById(R.id.viewPager);
        usernameText = view.findViewById(R.id.usernameText);
        phonenumberText = view.findViewById(R.id.phonenumberText);
        emailText = view.findViewById(R.id.emailText);
        addressText = view.findViewById(R.id.addressText);
        idPinceode = view.findViewById(R.id.idPinceode);
        city = view.findViewById(R.id.city);
        streetnumber = view.findViewById(R.id.streetnumber);
        state = view.findViewById(R.id.state);

        return view;
    }




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dialog = new ProgressDialog(mActivity);
        dialog.setMessage("Loading...");

        singInBtn.setOnClickListener(this);
       // addressText.setText(Utility.getAddress(mActivity,LocationValueModel.getmLatitude(), LocationValueModel.getmLongitude()));

        usernameText.setText(SaveSharedPreference.getUserName(mActivity));
        // addressText.setText(SaveSharedPreference.getPrefUserAddress(mActivity));
        emailText.setText(SaveSharedPreference.getPrefUserEmail(mActivity));

        searchDialog = new Dialog(mActivity);

        //sign_district = findViewById(R.id.sign_district);
        // sign_district.setText("Select State");


        searchDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        searchDialog.setContentView(R.layout.search_dialog);
        searchDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        citiesList = searchDialog.findViewById(R.id.stateList);

        adapterCities = new BlueToothShowAdapter(mActivity);
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


        addressText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (addressText.getRight() - addressText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here

                        if(TextUtils.isEmpty(addressText.getText().toString().trim())) {


                            if (LocationValueModel.getmLatitude() != 0.0 && LocationValueModel.getmLongitude() != 0.0) {



                                addressText.setText(Utility.getAddress(mActivity,LocationValueModel.getmLatitude(), LocationValueModel.getmLongitude()));

                            } else {
                               // getLocations("btn");



                            }
                        }




                        //  getLocations("btn");

                        return true;
                    }
                }
                return false;
            }
        });

        getPINCode();
    }



    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.singInBtn:

                if(!TextUtils.isEmpty(usernameText.getText().toString().trim()) &&
                        !TextUtils.isEmpty(phonenumberText.getText().toString().trim())
                && !TextUtils.isEmpty(emailText.getText().toString().trim()) &&
                        !TextUtils.isEmpty(streetnumber.getText().toString().trim()) &&
                        !TextUtils.isEmpty(city.getText().toString().trim()) &&
                        !TextUtils.isEmpty(state.getText().toString().trim()) &&
                        !TextUtils.isEmpty(idPinceode.getText().toString().trim()))
                {

                    if(Utility.isEmailValid(emailText.getText().toString().trim()))
                    {
                        if(Utility.isValidPin(idPinceode.getText().toString().trim()))
                        {
                            String steert="" , citys="", states="",idpincodes="";

                            steert = streetnumber.getText().toString().trim();
                            citys = city.getText().toString().trim();
                            states = state.getText().toString().trim();
                            idpincodes = idPinceode.getText().toString().trim();



                            SaveSharedPreference.setUserArea(mActivity,(steert +" "+ citys+" "+states+" "+idpincodes));

                            SaveSharedPreference.setUserStreetDoornumber(mActivity,steert);
                            SaveSharedPreference.setUsercity(mActivity,citys);
                            SaveSharedPreference.setUserstate(mActivity,states);
                            SaveSharedPreference.setUserPIN(mActivity,idpincodes);


                            showDialog();

                            setUserRegistration(usernameText.getText().toString().trim(),emailText.getText().toString().trim(),
                                    phonenumberText.getText().toString().trim(), SaveSharedPreference.getUserArea(mActivity),steert,idpincodes,states,citys);


                        }else {

                            Toast.makeText(mActivity, getResources().getString(R.string.invalid_pin), Toast.LENGTH_SHORT).show();

                        }




                    }else {
                        Toast.makeText(mActivity, "Please Enter Valid EmailId", Toast.LENGTH_SHORT).show();
                    }


                }else if(TextUtils.isEmpty(usernameText.getText().toString().trim()))
                {
                    Toast.makeText(mActivity, "Please Enter Name", Toast.LENGTH_SHORT).show();

                }else if(TextUtils.isEmpty(emailText.getText().toString().trim()))
                {
                    Toast.makeText(mActivity, "Please Enter Email", Toast.LENGTH_SHORT).show();

                }
                else if(TextUtils.isEmpty(phonenumberText.getText().toString().trim()))
                {
                    Toast.makeText(mActivity, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();



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



                break;

        }

    }



    private void setUserRegistration(String name,String email,String mobile,String address,String street,String pincode,String satate,String city)
    {
        try {
           // String url = SaveSharedPreference.getBaseURL(mActivity)+Url.APINAME_DASHBOARD+"/"+type+"/"+SaveSharedPreference.getHostFor(mActivity);


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

           // String address = "";

            JSONObject map = new JSONObject();
            map.put(Utility.UID,"2");
            map.put(Utility.STATE,"2");
            map.put(Utility.NAME,name);
            map.put(Utility.EMAILID,email);
            map.put(Utility.MOBILENO,mobile);
            map.put("street",street);
            map.put("pincode",pincode);
            map.put("state1",satate);
            map.put("area",city);
            //map.put(Utility.AGE,"2");
            map.put(Utility.ADDRESS,address);
            String url=Utility.BASE_REGISTER;

            Log.e("Params","--->"+map);

            JsonObjectRequest request  = new JsonObjectRequest(Request.Method.POST, url, map, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    cancelDialog();
                    try {
                        Log.e("Responce","--->"+response);
                        if(response.getString(Utility.API_RESPONCE_SUCCESS).equalsIgnoreCase("true")) {

                            SaveSharedPreference.setUserPhone(mActivity,phonenumberText.getText().toString());
                            SaveSharedPreference.setPrefUserRegisterId(mActivity,Utility.NullCheckJson(response,Utility.API_RESPONCE_REGISTERID));
                            SaveSharedPreference.setPrefUserRegisterId(mActivity,Utility.NullCheckJson(response,Utility.API_RESPONCE_REGISTERID));
                            SaveSharedPreference.setVerifystatus(mActivity,Utility.NullCheckJson(response,"verifystatus"));

                            SaveSharedPreference.setUserToken(mActivity,Utility.NullCheckJson(response,Utility.API_RESPONCE_TOKEN));
                            SaveSharedPreference.setHostFor(mActivity,Utility.NullCheckJson(response,Utility.API_RESPONCE_HOSTFOR));
                            SaveSharedPreference.setPrefUserRegisterId(mActivity,Utility.NullCheckJson(response,Utility.API_RESPONCE_REGISTERID));
                            SaveSharedPreference.setBaseURL(mActivity,Utility.NullCheckJson(response,Utility.API_RESPONCE_BASEURL));


                            SaveSharedPreference.setUserName(mActivity,name);
                            SaveSharedPreference.setPrefUserAddress(mActivity,address);
                            SaveSharedPreference.setPrefUserEmail(mActivity,email);

                            SaveSharedPreference.setUPIID(mActivity,Utility.NullCheckJson(response,"upiid"));

                           // SaveSharedPreference.getPrefUserRegisterId(mActivity,phonenumberText.getText().toString());

                            Log.e("number->","sdf--:"+SaveSharedPreference.getUserPhone(mActivity));

                            Toast.makeText(mActivity, Utility.NullCheckJson(response,"message"), Toast.LENGTH_SHORT).show();


                            viewPager.setCurrentItem(1,true);

                        }else {
                            Toast.makeText(mActivity, "Mobile number already exist. Please Login", Toast.LENGTH_SHORT).show();

                            Intent  i =new Intent(mActivity, GsingupActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                           // Toast.makeText(mActivity, Utility.NullCheckJson(response,"message"), Toast.LENGTH_SHORT).show();

                            mActivity.finish();
                        }

                    }catch (JSONException e)
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
            });

            request.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(mActivity).addToRequestQueue(request);



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
            String language = SaveSharedPreference.getAppLanguage(mActivity);
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
                                for (int i=0;i<datas.length();i++)
                                {
                                    JSONObject obj= datas.getJSONObject(i);

                                    cities.add(Utility.NullCheckJson(obj,"postcode"));


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
