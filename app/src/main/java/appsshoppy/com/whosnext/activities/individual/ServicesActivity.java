package appsshoppy.com.whosnext.activities.individual;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import appsshoppy.com.whosnext.AppController;
import appsshoppy.com.whosnext.R;
import appsshoppy.com.whosnext.adapters.IndividualServicesListAdapter;
import appsshoppy.com.whosnext.model.IndividualService;
import appsshoppy.com.whosnext.util.Constants;
import appsshoppy.com.whosnext.util.Util;

public class ServicesActivity extends AppCompatActivity {

    private ListView servicesListView;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getSupportActionBar().setCustomView(R.layout.app_bar);

        mProgressView = findViewById(R.id.progress);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        View customView = getLayoutInflater().inflate(R.layout.app_bar_with_right_button, null);
        actionBar.setCustomView(customView);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.navbar));
        Toolbar parent =(Toolbar) customView.getParent();
        ((TextView)customView.findViewById(R.id.txtToolbarHeader)).setText("Services");
        parent.setContentInsetsAbsolute(0,0);


        //right button listener
        ((Button)customView.findViewById(R.id.btnRight)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ServicesActivity.this,AddNewServicesActivity.class));
            }
        });

        servicesListView = (ListView) findViewById(R.id.servicesListView);
        //create dummy data
        /*IndividualService individualService = new IndividualService();
        individualService.setServiceName("Facial, Hair Cut, Head, Shoulder Massage, Shaving & Foot massage");
        individualService.setServiceAvailability("Always");
        individualService.setServiceTime("75 Min(approx)");
        individualService.setServicePrice("$25.00");
        ArrayList<IndividualService> listData = new ArrayList<IndividualService>();
        listData.add(individualService);
        listData.add(individualService);
        listData.add(individualService);
        listData.add(individualService);
        listData.add(individualService);
        servicesListView.setAdapter(new IndividualServicesListAdapter(listData,this));
        */
        getServiceListing();

    }

    private void getServiceListing()
    {
        String authToken = AppController.getInstance().getAuthToken();
        StringRequest request = new StringRequest(Request.Method.GET, Constants.kServiceListing_API+authToken, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response",response);
                mProgressView.setVisibility(View.GONE);
                JsonObject loginResponse = new Gson().fromJson(response,JsonObject.class);
                if(loginResponse.has("success"))
                {
                    if(loginResponse.get("success").getAsBoolean())
                    {
                        ArrayList<IndividualService> listData = new ArrayList<IndividualService>();

                        JsonArray serviceList = loginResponse.getAsJsonArray("data");
                        for(int i=0;i<serviceList.size();i++)
                        {
                            JsonObject serviceObject = serviceList.get(i).getAsJsonObject();

                            IndividualService individualService = new IndividualService();
                            individualService.setServiceName(serviceObject.get("Service Title").getAsString());
                            individualService.setServiceAvailability(serviceObject.get("Offer").getAsString());
                            individualService.setServiceTime(serviceObject.get("Time").getAsString()+" Min(approx)");
                            individualService.setServicePrice("$"+serviceObject.get("Price").getAsString());
                            listData.add(individualService);
                        }
                        servicesListView.setAdapter(new IndividualServicesListAdapter(listData,ServicesActivity.this));
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressView.setVisibility(View.GONE);
                Util.showAlert(ServicesActivity.this,"Error","Server error!!!");
            }
        });

        mProgressView.setVisibility(View.VISIBLE);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(request, Constants.kServiceListing_API);
    }
}
