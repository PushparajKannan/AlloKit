package com.allocare.allokit.kitmodule;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {


    public static String APPCODEVERSION="4";


    public static String BASE_REGISTER="http://jiovio.com/allokitadmin/api/register";
    public static String BASE_VERIFYOTP="http://jiovio.com/allokitadmin/api/verifyotp";
    public static String BASE_SENDOTP="http://jiovio.com/allokitadmin/api/sendotp";
    public static String BASE_POSTCODE="http://jiovio.com/allokitadmin/api/postcode";

    //API_NAMES
          public static String APINAME_REGIGTER="register";
        //  public static String APINAME_SENDOTP="sendotp";
          public static String APINAME_VERIFYOTP="verifyotp";
          public static String APINAME_PRODUCT="grocery";
          public static String APINAME_ORDERS = "orders";
          public static String APINAME_PLACEORDER="placeorder";
          public static String APINAME_NOTIFICATION="notification";
          public static String APINAME_COUNT="count";
          public static String APINAME_DETAILS="details";
          public static String APINAME_BANNER="banner";
          public static String APINAME_SENDOTP="sendotp";
          public static String APINAME_ORDERCANCEL="ordercancel";
          public static String APINAME_PRODETAILS="prodetails";
          public static String APINAME_APPUPDATE="appupdate";


          //USE_FULL
    public static String  UID="uid";
    public static String  ID="id";
    public static String  STATE="state";
    public static String  NAME="name";
    public static String  EMAILID="emailid";
    public static String  MOBILENO="mobileno";
    public static String  AGE="age";
    public static String  GENDER="gender";
    public static String  ADDRESS="address";
    public static String  REGISTERID="registerid";
    public static String  OTP="otp";
    public static String  PRICE="price";
    public static String  IMAGE="image";


    public static String API_RESPONCE_SUCCESS="success";
    public static String API_RESPONCE_ERROR="error";
    public static String API_RESPONCE_TOKEN="token";
    public static String API_RESPONCE_HOSTFOR="hostfor";
    public static String API_RESPONCE_BASEURL="baseurl";
    public static String API_RESPONCE_REGISTERID="registerid";
    public static String API_RESPONCE_MESSAGE="message";


    public static String getAddress(Context activity,double lat, double longi) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(activity, Locale.getDefault());

        String add ="";

        try {
            addresses = geocoder.getFromLocation(lat, longi, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            String sublocality = addresses.get(0).getSubLocality();
            String adminarea = addresses.get(0).getAdminArea();

            Log.e("city","-->"+ city);
            Log.e("state","-->"+ state);
            Log.e("country","-->"+ country);
            Log.e("postalCode","-->"+ postalCode);
            Log.e("knownName","-->"+ knownName);
            Log.e("adminarea","-->"+ adminarea);
            Log.e("sublocality","-->"+ sublocality);




            add = address;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return add;

    }

    public static String getBinCode(Context activity,double lat, double longi) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(activity, Locale.getDefault());

        String add ="";

        try {
            addresses = geocoder.getFromLocation(lat, longi, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            String sublocality = addresses.get(0).getSubLocality();
            String adminarea = addresses.get(0).getAdminArea();

            Log.e("city","-->"+ city);
            Log.e("state","-->"+ state);
            Log.e("country","-->"+ country);
            Log.e("postalCode","-->"+ postalCode);
            Log.e("knownName","-->"+ knownName);
            Log.e("adminarea","-->"+ adminarea);
            Log.e("sublocality","-->"+ sublocality);




            add = postalCode;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return add;

    }


    public static String NullCheckJson(JSONObject jsonobject, String Key) {
        String helloStr = "";

        try {
            if (jsonobject.isNull(Key)) {
                helloStr = "";
            } else {
                helloStr = jsonobject.getString(Key);
            }


        } catch (Exception e) {  e.printStackTrace();    }
        return helloStr;

    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static boolean isValidMobile(String
                                         phone) {
        boolean check = false;
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            if (phone.length() < 10 || phone.length() > 10) {
                // if(phone.length() != 10) {
                check = false;

            } else {
                check = true;
            }
        } else {
            check = false;
        }
        return check;
    }




    public static void clearEdittext(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText) view).setText("");
            }

            if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
                clearEdittext((ViewGroup) view);
        }
    }

    public static ContextWrapper changeLang(Context context, String lang_code){
        Locale sysLocale;

        Resources rs = context.getResources();
        Configuration config = rs.getConfiguration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sysLocale = config.getLocales().get(0);
        } else {
            sysLocale = config.locale;
        }


        if (!lang_code.equals("") && !sysLocale.getLanguage().equals(lang_code)) {
            Locale locale = new Locale(lang_code);
            Locale.setDefault(locale);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                config.setLocale(locale);
            } else {
                config.locale = locale;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                context = context.createConfigurationContext(config);
            } else {
                context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
            }
        }

        return new ContextWrapper(context);
    }

    public static void setLocale(Activity context, String langCode) {
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

        context.getBaseContext().getResources().updateConfiguration(config, context.getBaseContext().getResources().getDisplayMetrics());
    }

    public static boolean isValidPin(String pinnumber)
    {
        String regex = "^[1-9][0-9]{5}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(pinnumber);

        return matcher.matches();
    }


    public boolean isInternetConnected(Context ctx) {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;


    }


    public static BroadcastReceiver iConnectionReceiver(Context ctx)
    {
        return new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
                String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
                boolean isFailover = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);

                NetworkInfo currentNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                NetworkInfo otherNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);

                if(currentNetworkInfo.isConnected()){
                    Toast.makeText(ctx, "Connected", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ctx, "Not Connected", Toast.LENGTH_LONG).show();
                }
            }
        };
    }

   /* private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
            boolean isFailover = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);

            NetworkInfo currentNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            NetworkInfo otherNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);

            if(currentNetworkInfo.isConnected()){
                Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(), "Not Connected", Toast.LENGTH_LONG).show();
            }
        }
    };*/

}
