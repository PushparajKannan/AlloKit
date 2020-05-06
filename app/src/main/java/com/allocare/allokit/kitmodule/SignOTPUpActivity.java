package com.allocare.allokit.kitmodule;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.allocare.allokit.R;
import com.google.android.material.textfield.TextInputEditText;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class SignOTPUpActivity extends AppCompatActivity implements IGPSActivity{

    public CustomViewPager viewPager;

    TextView title;
    TextView signin, verifytxt;

    RelativeLayout title_lay;
    private GPS gps;

    public Context mActivity;

    private static final int PERMISSION_REQUEST_CODE_MAIN = 300;

    ProgressDialog dialog ;

    TextInputEditText addressText;

    AlertDialog alert;


    String type="normal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signotpup);

        mActivity = this;

        Bundle i = getIntent().getExtras();


        if(i!=null) {
            type = i.getString("type");

        }





        dialog = new ProgressDialog(mActivity);
        dialog.setMessage(getResources().getString(R.string.loading));

        viewPager = findViewById(R.id.viewPager);
        title = findViewById(R.id.title);
        signin = findViewById(R.id.signin);

        title_lay = findViewById(R.id.title_lay);
        alert = new AlertDialog.Builder(mActivity)
                .setTitle(getResources().getString(R.string.LocationisNotEnable))  // GPS not found
                .setMessage(getResources().getString(R.string.needtoenable)) // Want to enable?
                .setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(getResources().getString(R.string.No), null).create();

        //AlertDialog alert = new AlertDialog.Builder(mActivity).create();

        Pager adapter = new Pager(getSupportFragmentManager(),2);

       // viewPager.setOffscreenPageLimit(0);
        viewPager.setAdapter(adapter);
        viewPager.setScrollEnable(false);




        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if(position == 0)
                {

                    title.setText("Register");
                    signin.setText("Sign up");
                    title_lay.setVisibility(View.VISIBLE);

                }else {

                    title_lay.setVisibility(View.GONE);
                   // signin.setVisibility(View.GONE);
                   // verifytxt.


                    title.setText("Verification");
                   // signin.setText(getResources().getString(R.string.Verify));
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        if(type.equalsIgnoreCase("verify")) {
            viewPager.setCurrentItem(1);
        }else {
            viewPager.setCurrentItem(0);



        }


    }

    private void getLocationWithPermission() {

        if(checkPermissionMain()) {
         //   showDialog();
            gps = new GPS(this);
        }else {
            requestPermissionMain();
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

        cancelDialog();

        addressText = findViewById(R.id.addressText);


        if(addressText!=null) {
            if (TextUtils.isEmpty(addressText.getText().toString().trim())) {
                addressText.setText(Utility.getAddress(mActivity, LocationValueModel.getmLatitude(), LocationValueModel.getmLongitude()));

            }
        }

    }





    @Override
    public void displayGPSSettingsDialog() {

       // alertShow();

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

    public class Pager extends FragmentStatePagerAdapter
    {
        int tabCount;
        public Pager(@NonNull FragmentManager fm, int tabCount)
        {
            super(fm);
            this.tabCount =  tabCount;
        }

        @NonNull
        @Override
        public Fragment getItem(int position)
        {
            switch (position) {
          /*  case 0:
                Profile profile = new Profile();
                return profile;*/

           /* case 1:
                GuideLiness guideLiness = new GuideLiness();
                return guideLiness;*/
                case 0:
                    OtpRegisterFragment otpregister = new OtpRegisterFragment();
                    return otpregister;

                case 1:
                    OTPVerifyFragment otpfrag = new OTPVerifyFragment();
                    return otpfrag;

          /*  case 3:
                EmergencyContacts emergencyContacts = new EmergencyContacts();
                return emergencyContacts;

            case 4:
                Terms_and_Conditions terms_and_conditions = new Terms_and_Conditions();
                return terms_and_conditions;

            case 5:
                AboutUs aboutUs = new AboutUs();
                return  aboutUs;*/
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        alertDismiss();
    }

    @Override
    protected void onResume() {

        alertDismiss();

        super.onResume();

        if(checkPermissionMain()) {
            if(gps!=null) {
                if (!gps.isRunning()) gps.resumeGPS();
            }
        }

        getLocationWithPermission();


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
