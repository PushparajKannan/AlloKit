package com.allocare.thenibazzar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.allocare.thenibazzar.kitmodule.SaveSharedPreference;
import com.allocare.thenibazzar.kitmodule.SignOTPUpActivity;
import com.allocare.thenibazzar.kitmodule.Utility;
import com.allocare.thenibazzar.kitmodule.VolleySingleton;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

public class GsingupActivity extends AppCompatActivity {
    CardView google_sign_up;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 0;

    Context mActivity;

    TextView phonenumberText;

    CardView singInBtn;

    ProgressDialog dialog ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gsingup);
        mActivity =this;

        dialog = new ProgressDialog(mActivity);
        dialog.setMessage("Loading...");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        google_sign_up = findViewById(R.id.google_signin);
        phonenumberText = findViewById(R.id.phonenumberText);
        singInBtn = findViewById(R.id.singInBtn);

        google_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();

            }
        });

        singInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(phonenumberText.getText().toString().trim()))
                {
                    showDialog();
                    setUserLogin(phonenumberText.getText().toString().trim());

                }else {
                    Toast.makeText(mActivity, "Please Enter Your Phone number", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            if(account!=null)
            {
                //account.getDisplayName();
                Log.e("---->", "" + account.getDisplayName());
                Log.e("---->", "" + account.getEmail());
                //Log.e("---->", "" + account.getPhotoUrl());


                SaveSharedPreference.setUserName(mActivity, account.getDisplayName());
                SaveSharedPreference.setPrefUserEmail(mActivity, account.getEmail());


            }

         //   SaveSharedPreference.setUserName(mActivity,name);
          //  SaveSharedPreference.setPrefUserAddress(mActivity,address);
          //  SaveSharedPreference.setPrefUserEmail(mActivity,email);
            // Signed in successfully, show authenticated UI.
            Intent intent = new Intent(GsingupActivity.this, SignOTPUpActivity.class);
            intent.putExtra("type","normal");
            startActivity(intent);
            signOut();

            finish();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("---->", "signInResult:failed code=" + e.getStatusCode());


        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {

        try {
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // ...
                            Log.e("gsignout","-->"+task.getResult());
                        }
                    });
        }catch (Exception e) {
           e.printStackTrace();
        }

    }

    private void setUserLogin(String mobile)
    {
        try {


            JSONObject map = new JSONObject();


            map.put(Utility.MOBILENO,mobile);



            Log.e("Params","--->"+map);
            String url = Utility.BASE_SENDOTP;



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


                            SaveSharedPreference.setUserName(mActivity,Utility.NullCheckJson(response,"name"));
                            SaveSharedPreference.setPrefUserAddress(mActivity,Utility.NullCheckJson(response,"address"));
                            SaveSharedPreference.setPrefUserEmail(mActivity,Utility.NullCheckJson(response,"emailid"));

                            SaveSharedPreference.setUserStreetDoornumber(mActivity,Utility.NullCheckJson(response,"street"));
                            SaveSharedPreference.setUsercity(mActivity,Utility.NullCheckJson(response,"area"));
                            SaveSharedPreference.setUserstate(mActivity,Utility.NullCheckJson(response,"state1"));
                            SaveSharedPreference.setUserPIN(mActivity,Utility.NullCheckJson(response,"pincode"));


                            SaveSharedPreference.setUserArea(mActivity,Utility.NullCheckJson(response,"address"));


                            // SaveSharedPreference.getPrefUserRegisterId(mActivity,phonenumberText.getText().toString());

                            Log.e("number->","sdf--:"+SaveSharedPreference.getUserPhone(mActivity));

                            Intent  i =new Intent(mActivity,SignOTPUpActivity.class);
                            i.putExtra("type","verify");
                            startActivity(i);
                            //overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

                            Toast.makeText(mActivity, Utility.NullCheckJson(response,"message"), Toast.LENGTH_SHORT).show();

                            finish();


                        }else {
                            Toast.makeText(mActivity, Utility.NullCheckJson(response,"message"), Toast.LENGTH_SHORT).show();
                           // Toast.makeText(mActivity, "Kindly register", Toast.LENGTH_SHORT).show();
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