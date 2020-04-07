package com.allocare.thenibazzar.kitmodule;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {

    private static final String PREF_USER_NAME= "username";
    private static final String PREF_USER_TOKEN= "token";
    private static final String PREF_USER_QUAID= "quatid";
    private static final String PREF_USER_EMAIL= "user_email";
    private static final String PREF_USER_PHONE= "user_pho_no";
    private static final String PREF_USER_ECONTACT_NAME= "user_e_contact_name";
    private static final String PREF_USER_ECONATCT_NUMBER= "user_e_contact_number";
    private static final String PREF_USER_CITIZEN= "citizen";
    private static final String PREF_USER_REGISTER_ID= "registerid";
    private static final String PREF_USER_AREA= "area";
    private static final String PREF_USER_AREA_ID= "area_id";
    private static final String PREF_USER_TOWN_NAME= "town_name";
    private static final String PREF_USER_DOOR_NO= "door_no";
    private static final String PREF_USER_ADDRESS= "address";
    private static final String LAT= "lat";
    private static final String LANG= "lang";
    private static final String LANGUAGE= "language";
    private static final String isCONNECT= "isConnect";
    private static final String HOSTFOR= "hostfor";
    private static final String SBASEURL= "baseurl";
    private static final String USERLATITURDE= "Latitude";
    private static final String USERLONGITUDE= "Longitude";
    private static final String THALUKA= "thaluka";
    private static final String AREA= "area";
    private static final String VOLUNTEER= "volunteer";
    private static final String LOGO= "logo";
    private static final String PHONE= "Phonenumver";
    private static final String VERIFYSTATUS= "verifystatus";
    private static SharedPreferences.Editor editor;

    private static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, String userName)
    {
        editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.apply();
    }

    public static void setUserToken(Context ctx, String token)
    {
        editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_TOKEN, token);
        editor.apply();
    }

    public static void setQuaId(Context ctx, String token)
    {
        editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_QUAID, token);
        editor.apply();
    }

    public static String getQuaId(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_QUAID, "");
    }



    public static void setUserDetails(Context ctx, String user_email, String ph_no, String e_name, String e_no, String register_id,String area,String area_id)
    {
        editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_EMAIL, user_email);
        editor.putString(PREF_USER_PHONE, ph_no);
        editor.putString(PREF_USER_ECONTACT_NAME, e_name);
        editor.putString(PREF_USER_ECONATCT_NUMBER, e_no);
        editor.putString(PREF_USER_REGISTER_ID,register_id);
        editor.putString(PREF_USER_AREA,area);
        editor.putString(PREF_USER_AREA_ID,area_id);
        editor.apply();
    }


    /////////////////////
    public static void updateProfile(Context ctx, String u_name, String user_email, String ph_no, String town, String door, String address,String e_name,String e_no)
    {
        editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, u_name);
        editor.putString(PREF_USER_EMAIL, user_email);
        editor.putString(PREF_USER_PHONE, ph_no);
        editor.putString(PREF_USER_TOWN_NAME, town);
        editor.putString(PREF_USER_DOOR_NO, door);
        editor.putString(PREF_USER_ADDRESS,address);
        editor.putString(PREF_USER_ECONTACT_NAME,e_name);
        editor.putString(PREF_USER_ECONATCT_NUMBER,e_no);
        editor.apply();
    }

    public static void SetLatLang(Context context, String lat, String lang)
    {
        editor = getSharedPreferences(context).edit();
        editor.putString(LAT,lat);
        editor.putString(LANG,lang);
        editor.apply();
    }
    public static void setIsConnect(Context context, boolean isConnect)
    {
        editor = getSharedPreferences(context).edit();
        editor.putBoolean(isCONNECT,isConnect);
        editor.apply();
    }

    public static String getPrefUserArea(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_AREA, "");
    }

    public static String getPrefUserTownName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_TOWN_NAME, "");
    }
    public static String getPrefUserDoorNo(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_DOOR_NO, "");
    }


    public static void setPrefUserAddress(Context context, String lang) {
        editor = getSharedPreferences(context).edit();
        editor.putString(PREF_USER_ADDRESS,lang);
        editor.apply();
    }

    public static String getPrefUserAddress(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_ADDRESS, "");
    }


    public static String getPrefUserAreaId(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_AREA_ID, "");
    }


    public static Boolean getIsCONNECT(Context ctx)
    {
        return getSharedPreferences(ctx).getBoolean(isCONNECT, true);
    }
    public static String getLat(Context ctx)
    {
        return getSharedPreferences(ctx).getString(LAT, "");
    }
    public static String getLang(Context ctx)
    {
        return getSharedPreferences(ctx).getString(LANG, "");
    }

   /* public static void setUserName(Context context, String lang) {
        editor = getSharedPreferences(context).edit();
        editor.putString(PREF_USER_NAME,lang);
        editor.apply();
    }*/

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }



    public static void setPrefUserEmail(Context context, String lang)
    {
        editor = getSharedPreferences(context).edit();
        editor.putString(PREF_USER_EMAIL,lang);
        editor.apply();
    }

    public static String getPrefUserEmail(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_EMAIL, "");
    }

    public static String getPrefUserPhone(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_PHONE, "");
    }

    public static String getPrefUserEcontactName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_ECONTACT_NAME, "");
    }

    public static String getPrefUserEconatctNumber(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_ECONATCT_NUMBER, "");
    }

    public static String getPrefUserCitizen(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_CITIZEN, "");
    }

    public static String getPrefUserToken(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_TOKEN, "");
    }

    public static void clearUserName(Context ctx)
    {
        editor = getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        editor.apply();
    }
    public static void setAppLanguge(Context context, String lang)
    {
        editor = getSharedPreferences(context).edit();
        editor.putString(LANGUAGE,lang);
        editor.apply();
    }
    public static String getAppLanguage(Context ctx) {
        return getSharedPreferences(ctx).getString(LANGUAGE, "en");
    }


    public static void setHostFor(Context context, String lang)
    {
        editor = getSharedPreferences(context).edit();
        editor.putString(HOSTFOR,lang);
        editor.apply();
    }
    public static String getHostFor(Context ctx) {
        return getSharedPreferences(ctx).getString(HOSTFOR, "");
    }



    public static void setBaseURL(Context context, String lang)
    {
        editor = getSharedPreferences(context).edit();
        editor.putString(SBASEURL,lang);
        editor.apply();
    }
    public static String getBaseURL(Context ctx) {
        return getSharedPreferences(ctx).getString(SBASEURL, "");
    }


    public static void setUserLatitude(Context context, String lang) {
        editor = getSharedPreferences(context).edit();
        editor.putString(USERLATITURDE,lang);
        editor.apply();
    }
    public static String getUserLatitude(Context ctx) {
        return getSharedPreferences(ctx).getString(USERLATITURDE, "0.0");
    }


    public static void setUserLongitude(Context context, String lang) {
        editor = getSharedPreferences(context).edit();
        editor.putString(USERLONGITUDE,lang);
        editor.apply();
    }
    public static String getUserLongitude(Context ctx) {
        return getSharedPreferences(ctx).getString(USERLONGITUDE, "0.0");
    }

    public static void clearAllData(Context ctx) {
        editor = getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        editor.apply();
    }



    public static void setUserThaluka(Context context, String lang) {
        editor = getSharedPreferences(context).edit();
        editor.putString(THALUKA,lang);
        editor.apply();
    }
    public static String getUserThaluka(Context ctx) {
        return getSharedPreferences(ctx).getString(THALUKA, "");
    }

    public static void setUserArea(Context context, String lang) {
        editor = getSharedPreferences(context).edit();
        editor.putString(AREA,lang);
        editor.apply();
    }
    public static String getUserArea(Context ctx) {
        return getSharedPreferences(ctx).getString(AREA, "");
    }


    public static void setVolunteerId(Context context, String lang) {
        editor = getSharedPreferences(context).edit();
        editor.putString(VOLUNTEER,lang);
        editor.apply();
    }
    public static String getVolunteerId(Context ctx) {
        return getSharedPreferences(ctx).getString(VOLUNTEER, "");
    }

public static void setDistLogo(Context context, String lang) {
        editor = getSharedPreferences(context).edit();
        editor.putString(LOGO,lang);
        editor.apply();
    }
    public static String getDistLogo(Context ctx) {
        return getSharedPreferences(ctx).getString(LOGO, "");
    }

    public static void setUserPhone(Context context, String lang) {
        editor = getSharedPreferences(context).edit();
        editor.putString(PHONE,lang);
        editor.apply();
    }
    public static String getUserPhone(Context ctx) {
        return getSharedPreferences(ctx).getString(PHONE, "");
    }


    public static String getPrefUserRegisterId(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_REGISTER_ID, "");
    }

    public static void setPrefUserRegisterId(Context context, String lang) {
        editor = getSharedPreferences(context).edit();
        editor.putString(PREF_USER_REGISTER_ID,lang);
        editor.apply();
    }

    public static String getVerifystatus(Context ctx) {
        return getSharedPreferences(ctx).getString(VERIFYSTATUS, "No");
    }

    public static void setVerifystatus(Context context, String lang) {
        editor = getSharedPreferences(context).edit();
        editor.putString(VERIFYSTATUS,lang);
        editor.apply();
    }
}
