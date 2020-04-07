package com.allocare.thenibazzar.kitmodule;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {

    public static String BASE_REGISTER="http://jiovio.com/bazaar/api/register";
    public static String BASE_VERIFYOTP="http://jiovio.com/bazaar/api/verifyotp";

    //API_NAMES
          public static String APINAME_REGIGTER="register";
          public static String APINAME_SENDOTP="sendotp";
          public static String APINAME_VERIFYOTP="verifyotp";
          public static String APINAME_PRODUCT="grocery";
          public static String APINAME_ORDERS = "orders";
          public static String APINAME_PLACEORDER="placeorder";
          public static String APINAME_NOTIFICATION="notification";
          public static String APINAME_COUNT="count";
          public static String APINAME_DETAILS="details";
          public static String APINAME_BANNER="banner";


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

            add = address;

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

}
