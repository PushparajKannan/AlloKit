package com.allocare.allokit.kitmodule;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.allocare.allokit.R;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class OTPVerifyFragment extends Fragment implements View.OnClickListener {


   /* @Override
    public void onAttach(@NonNull Context context) {

     //   language = PreferenceManager.getDefaultSharedPreferences(context).getString("language", "en");

        super.onAttach(LocaleHelper.OnAttach(context, SaveSharedPreference.getAppLanguage(context)));
    }*/

   OtpEditText otpText;
    //Please type the verification code send \n to 7845923565

    CardView verrifyBtn;

    SignOTPUpActivity mActivity;
    TextView title, otpTitle;

    ProgressDialog dialog ;
    TextView skip;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.otpverify_frame, container, false);
       // title = ((SignOTPUpActivity) getActivity()).findViewById(R.id.title);

        mActivity  = ((SignOTPUpActivity) getActivity());
        title = mActivity.findViewById(R.id.title);
        verrifyBtn = view.findViewById(R.id.verrifyBtn);

        otpText = view.findViewById(R.id.otpText);
        otpTitle = view.findViewById(R.id.otpTitle);
        skip = view.findViewById(R.id.skip);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dialog = new ProgressDialog(mActivity);
        dialog.setMessage(getResources().getString(R.string.loading));
        verrifyBtn.setOnClickListener(this);
        verrifyBtn.setVisibility(View.VISIBLE);

        //Please type the verification code send \n to 7845923565

        String val =  getResources().getString(R.string.pls_enter_verify)+ SaveSharedPreference.getUserPhone(mActivity);

        otpTitle.setText(val);

        skip.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {


        super.setUserVisibleHint(isVisibleToUser);


        if(isVisibleToUser) {

            if (otpTitle != null) {
                String val = "Please type the verification code send \n to " + SaveSharedPreference.getUserPhone(mActivity);
                otpTitle.setText(val);
            }



        }



    }





    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.verrifyBtn:

                if(!TextUtils.isEmpty(otpText.getText().toString().trim()))
                {
                    showDialog();
                   setUserVerification(otpText.getText().toString().trim());
                }else {
                    Toast.makeText(mActivity, getResources().getString(R.string.pls_enter_otp), Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.skip:

                Intent  i =new Intent(mActivity,OrderSumActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
              //  mActivity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                mActivity.finish();
               //

                break;
        }
    }



    private void setUserVerification(String otp)
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
            map.put(Utility.REGISTERID,SaveSharedPreference.getPrefUserRegisterId(mActivity));
            map.put(Utility.STATE,"2");
            map.put(Utility.MOBILENO,SaveSharedPreference.getUserPhone(mActivity));
            //map.put(Utility.AGE,"2");
            map.put(Utility.OTP,otp);


            String url=Utility.BASE_VERIFYOTP;

            Log.e("Params","--->"+map);

            JsonObjectRequest request  = new JsonObjectRequest(Request.Method.POST, url, map, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    cancelDialog();
                    try {
                        Log.e("Responce","--->"+response);
                        if(response.getString(Utility.API_RESPONCE_SUCCESS).equalsIgnoreCase("true")) {

                           // SaveSharedPreference.setUserPhone(mActivity,phonenumberText.getText().toString());
                           // SaveSharedPreference.setPrefUserRegisterId(mActivity,Utility.NullCheckJson(response,Utility.API_RESPONCE_REGISTERID));
                            // SaveSharedPreference.getPrefUserRegisterId(mActivity,phonenumberText.getText().toString());

                            //SaveSharedPreference.setUserToken(mActivity,Utility.NullCheckJson(response,Utility.API_RESPONCE_TOKEN));
                           // SaveSharedPreference.setHostFor(mActivity,Utility.NullCheckJson(response,Utility.API_RESPONCE_HOSTFOR));
                            SaveSharedPreference.setPrefUserRegisterId(mActivity,Utility.NullCheckJson(response,Utility.API_RESPONCE_REGISTERID));
                           // SaveSharedPreference.setBaseURL(mActivity,Utility.NullCheckJson(response,Utility.API_RESPONCE_BASEURL));

                            SaveSharedPreference.setVerifystatus(mActivity,Utility.NullCheckJson(response,"verifystatus"));

                            Toast.makeText(mActivity, getResources().getString(R.string.verifide_otp), Toast.LENGTH_SHORT).show();


                            // Intent i =new Intent(mActivity,Grocerykit.class);
                            Intent i =new Intent(mActivity,OrderSumActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(i);
                            //mActivity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                            // HomeActivity.class is the activity to go after showing the splash screen.
                            mActivity.finish();
                        }else {

                            Toast.makeText(mActivity, getResources().getString(R.string.otp_miss_match), Toast.LENGTH_SHORT).show();
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
