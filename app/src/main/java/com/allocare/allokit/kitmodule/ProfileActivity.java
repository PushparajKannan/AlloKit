package com.allocare.allokit.kitmodule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.allocare.allokit.R;
import com.google.android.material.textfield.TextInputEditText;

public class ProfileActivity extends AppCompatActivity  {

    TextView VerifyOTP;

    Context mActivity;

    TextInputEditText usernameText, emailText, phonenumberText, addressText;

    ImageView backImg;

    TextInputEditText streetnumber, city,idPinceode,state;
    TextView save;
    
    String type= "";
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mActivity =this;
        VerifyOTP = findViewById(R.id.VerifyOTP);
        usernameText = findViewById(R.id.usernameText);
        emailText = findViewById(R.id.emailText);
        phonenumberText = findViewById(R.id.phonenumberText);
        addressText = findViewById(R.id.addressText);
        backImg = findViewById(R.id.backImg);
        save = findViewById(R.id.save);
        idPinceode = findViewById(R.id.idPinceode);
        city = findViewById(R.id.city);
        streetnumber = findViewById(R.id.streetnumber);
        state = findViewById(R.id.state);

        type = getIntent().getStringExtra("type");

        if(!SaveSharedPreference.getVerifystatus(mActivity).equalsIgnoreCase("No")) {
            VerifyOTP.setVisibility(View.GONE);
        }else {
            VerifyOTP.setVisibility(View.VISIBLE);

        }
            VerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i =new Intent(mActivity,SignOTPUpActivity.class);
                i.putExtra("type","verify");
                startActivity(i);
                //overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

            }
        });


        idPinceode.setText(SaveSharedPreference.getUserPIN(mActivity));
        city.setText(SaveSharedPreference.getUsercity(mActivity));
        state.setText(SaveSharedPreference.getUserstate(mActivity));
        streetnumber.setText(SaveSharedPreference.getUserStreetDoornumber(mActivity));

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



                        SaveSharedPreference.setUserArea(mActivity,(steert +" "+ citys+" "+states+" "+idpincodes));

                        SaveSharedPreference.setUserStreetDoornumber(mActivity,steert);
                        SaveSharedPreference.setUsercity(mActivity,citys);
                        SaveSharedPreference.setUserstate(mActivity,states);
                        SaveSharedPreference.setUserPIN(mActivity,idpincodes);

                        Toast.makeText(mActivity, getResources().getString(R.string.address_saved), Toast.LENGTH_SHORT).show();


                        /*if(type.equalsIgnoreCase("kit")) {
                            Intent i = new Intent(mActivity,Grocerykit.class);
                            startActivity(i);
                        }*/

                        finish();
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


        usernameText.setText(SaveSharedPreference.getUserName(mActivity));
        addressText.setText(SaveSharedPreference.getPrefUserAddress(mActivity));
        emailText.setText(SaveSharedPreference.getPrefUserEmail(mActivity));
        phonenumberText.setText(SaveSharedPreference.getUserPhone(mActivity));

        backImg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                ProfileActivity.super.onBackPressed();

            }
        });

    }
}
