package com.allocare.thenibazzar.kitmodule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.allocare.thenibazzar.MainActivity;
import com.allocare.thenibazzar.R;

public class ProfileActivity extends AppCompatActivity  {

    TextView VerifyOTP;

    Context mActivty;

    TextView usernameText, emailText, phonenumberText, addressText;

    ImageView backImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mActivty =this;
        VerifyOTP = findViewById(R.id.VerifyOTP);
        usernameText = findViewById(R.id.usernameText);
        emailText = findViewById(R.id.emailText);
        phonenumberText = findViewById(R.id.phonenumberText);
        addressText = findViewById(R.id.addressText);
        backImg = findViewById(R.id.backImg);

        if(!SaveSharedPreference.getVerifystatus(mActivty).equalsIgnoreCase("No")) {
            VerifyOTP.setVisibility(View.GONE);
        }else {
            VerifyOTP.setVisibility(View.VISIBLE);

        }
            VerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i =new Intent(mActivty,SignOTPUpActivity.class);
                i.putExtra("type","verify");
                startActivity(i);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

            }
        });


        usernameText.setText(SaveSharedPreference.getUserName(mActivty));
        addressText.setText(SaveSharedPreference.getPrefUserAddress(mActivty));
        emailText.setText(SaveSharedPreference.getPrefUserEmail(mActivty));
        phonenumberText.setText(SaveSharedPreference.getUserPhone(mActivty));

        backImg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                ProfileActivity.super.onBackPressed();

            }
        });

    }
}
