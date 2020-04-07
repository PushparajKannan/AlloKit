package com.allocare.thenibazzar.notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.allocare.thenibazzar.R;
import com.allocare.thenibazzar.kitmodule.SaveSharedPreference;
import com.allocare.thenibazzar.kitmodule.Utility;
import com.allocare.thenibazzar.kitmodule.VolleySingleton;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity {

    private NotificationAdapter adapter;

    private ArrayList<NotificationModel> notificationModels;
    private RecyclerView notification_recycler_view;

    Context mActivity;
    ImageView backImg;

    @Override
    protected void attachBaseContext(Context newBase) {
        //  String lang_code = "en"; //load it from SharedPref
        Log.e("topLAng","-->"+ SaveSharedPreference.getAppLanguage(newBase));

        // Context context = Utility.changeLang(newBase, SaveSharedPreference.getAppLanguage(newBase));
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        mActivity=this;



        notification_recycler_view = findViewById(R.id.notification_recyclerview);
        backImg = findViewById(R.id.backImg);
        addData();
        adapter = new NotificationAdapter(notificationModels, mActivity);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        notification_recycler_view.setLayoutManager(mLayoutManager);
        notification_recycler_view.setItemAnimator(new DefaultItemAnimator());
       // notification_recycler_view.addItemDecoration(new DividerItemDecoration(notification_recycler_view.getContext(), DividerItemDecoration.VERTICAL));
        notification_recycler_view.setAdapter(adapter);

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationActivity.super.onBackPressed();
            }
        });

    }

    private void addData() {
        notificationModels = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("registerid", SaveSharedPreference.getPrefUserRegisterId(mActivity));
        } catch (
                JSONException e) {
            e.printStackTrace();
        }
        //   String URL = SaveSharedPreference.getBaseURL(mActivity)+"/"+Url.APINAME_NEWS+"/"+type+"/"+SaveSharedPreference.getHostFor(mActivity)+"/"+offsets+"/20";

        String url = SaveSharedPreference.getBaseURL(mActivity)+ Utility.APINAME_NOTIFICATION;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Notification_DETAILS", "----->" + response);
                        try {
                            if (response.getString("success").equalsIgnoreCase("true")) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    NotificationModel notificationModel = new NotificationModel();
                                    notificationModel.setId(jsonObject.getString("id"));
                                    notificationModel.setType(jsonObject.getString("type"));
                                    notificationModel.setData1(jsonObject.getString("data1"));
                                    notificationModel.setData2(jsonObject.getString("data2"));
                                    notificationModel.setComment(jsonObject.getString("comment"));
                                    notificationModel.setDate(jsonObject.getString("date"));
                                    notificationModel.setQty(jsonObject.getString("qty"));
                                    notificationModel.setTotalprice(jsonObject.getString("totalprice"));
                                    notificationModel.setProductname(jsonObject.getString("productname"));
                                    notificationModel.setProductimg(jsonObject.getString("productimg"));

                                    notificationModels.add(notificationModel);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Notification_ERROR", "----->" + error.getMessage());
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<>();
                headers.put("token", SaveSharedPreference.getPrefUserToken(mActivity));
                return headers;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(mActivity).addToRequestQueue(jsonObjectRequest);
    }
}
