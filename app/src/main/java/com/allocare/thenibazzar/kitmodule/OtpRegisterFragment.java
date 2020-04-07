package com.allocare.thenibazzar.kitmodule;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.allocare.thenibazzar.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class OtpRegisterFragment extends Fragment implements View.OnClickListener {

    TextView title;

    TextInputEditText usernameText, phonenumberText, addressText,emailText;

    CardView singInBtn;

    SignOTPUpActivity mActivity;

    CustomViewPager viewPager;
    ProgressDialog dialog ;



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
        return view;
    }




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dialog = new ProgressDialog(mActivity);
        dialog.setMessage("Loading...");

        singInBtn.setOnClickListener(this);
       // addressText.setText(Utility.getAddress(mActivity,LocationValueModel.getmLatitude(), LocationValueModel.getmLongitude()));

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

    }



    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.singInBtn:

                if(!TextUtils.isEmpty(usernameText.getText().toString().trim()) && !TextUtils.isEmpty(phonenumberText.getText().toString().trim())
                && !TextUtils.isEmpty(emailText.getText().toString().trim()) && !TextUtils.isEmpty(addressText.getText().toString().trim()))
                {

                    if(Utility.isEmailValid(emailText.getText().toString().trim()))
                    {

                        showDialog();
                        setUserRegistration(usernameText.getText().toString().trim(),emailText.getText().toString().trim(),
                                phonenumberText.getText().toString().trim(), addressText.getText().toString().trim());

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



                }else if(TextUtils.isEmpty(addressText.getText().toString().trim()))
                {
                    Toast.makeText(mActivity, "Please Enter Address", Toast.LENGTH_SHORT).show();

                }



                break;

        }

    }



    private void setUserRegistration(String name,String email,String mobile,String address)
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


                           // SaveSharedPreference.getPrefUserRegisterId(mActivity,phonenumberText.getText().toString());

                            Log.e("number->","sdf--:"+SaveSharedPreference.getUserPhone(mActivity));

                            viewPager.setCurrentItem(1,true);
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

}
