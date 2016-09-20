package appsshoppy.com.whosnext.activities.individual;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import appsshoppy.com.whosnext.AppController;
import appsshoppy.com.whosnext.R;
import appsshoppy.com.whosnext.adapters.IndividualAddNewServicesListAdapter;
import appsshoppy.com.whosnext.custom.volleyMultipart.VolleyMultipartRequest;
import appsshoppy.com.whosnext.model.AddService;
import appsshoppy.com.whosnext.util.Constants;
import appsshoppy.com.whosnext.util.Util;

public class NotificationsActivity extends AppCompatActivity {

    private ListView servicesListView;
    private View mProgressView;
    public AddService addService;
    private static int REQ_SERVICE_PRICING = 1;
    private static int REQ_SERVICE_DESCRIPTION = 2;
    private static int REQ_SERVICE_DISTRIBUTION = 3;
    private boolean isPricingDone,isDistributionDone,isDescriptionDone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_services);
        mProgressView = findViewById(R.id.progress);
        addService = new AddService();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getSupportActionBar().setCustomView(R.layout.app_bar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        View customView = getLayoutInflater().inflate(R.layout.app_bar_with_right_button, null);
        ((Button)customView.findViewById(R.id.btnRight)).setText("Done");
        ((Button)customView.findViewById(R.id.btnRight)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewService();
            }
        });
        actionBar.setCustomView(customView);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.navbar));
        Toolbar parent =(Toolbar) customView.getParent();
        ((TextView)customView.findViewById(R.id.txtToolbarHeader)).setText("Add new Services");
        parent.setContentInsetsAbsolute(0,0);

        servicesListView = (ListView) findViewById(R.id.addServicesListView);
        //list data
        ArrayList<String> listData = new ArrayList<String>();
        listData.add("Services and Pricing");
        listData.add("Description");
        listData.add("Distribution");
        servicesListView.setAdapter(new IndividualAddNewServicesListAdapter(listData,this));

        //set onclick listener
        servicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0)
                {
                    startActivityForResult(new Intent(NotificationsActivity.this,ServicesPricingActivity.class),REQ_SERVICE_PRICING);
                }
                else if(position == 1)
                {
                    startActivityForResult(new Intent(NotificationsActivity.this,ServicesDistributionActivity.class),REQ_SERVICE_DISTRIBUTION);
                }
                else if(position == 2)
                {
                    startActivityForResult(new Intent(NotificationsActivity.this,ServicesDescriptionActivity.class),REQ_SERVICE_DESCRIPTION);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null) {
            if (requestCode == REQ_SERVICE_PRICING) {
                //addService = data.getExtras().getSerializable("STRING_GOES_HERE");
                if (data.hasExtra("service_pricing")) {
                    AddService resultData = new Gson().fromJson(data.getStringExtra("service_pricing"), AddService.class);
                    addService.setServiceTitle(resultData.getServiceTitle());
                    addService.setServiceFor(resultData.getServiceFor());
                    addService.setServiceDuration(resultData.getServiceDuration());
                    addService.setPrice(resultData.getPrice());
                    isPricingDone = true;
                }
            } else if (requestCode == REQ_SERVICE_DISTRIBUTION) {
                //addService = data.getExtras().getSerializable("STRING_GOES_HERE");
                if (data.hasExtra("service_distribution")) {
                    AddService resultData = new Gson().fromJson(data.getStringExtra("service_distribution"), AddService.class);
                    addService.setIsFeature(resultData.getIsFeature());
                    addService.setListedOnline(resultData.getListedOnline());
                    addService.setOfferAvailable(resultData.getOfferAvailable());
                    isDistributionDone = true;
                }
            } else if (requestCode == REQ_SERVICE_DESCRIPTION) {
                if (data.hasExtra("service_description")) {
                    AddService resultData = new Gson().fromJson(data.getStringExtra("service_description"), AddService.class);
                    addService.setDescription(resultData.getDescription());
                    isDescriptionDone = true;
                }
            }
        }
    }

    private void addNewService()
    {
        if(isPricingDone && isDescriptionDone && isDistributionDone)
        {
            String authToken = AppController.getInstance().getAuthToken();
            VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, Constants.kAddService_API+authToken, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    String resultResponse = new String(response.data);
                    mProgressView.setVisibility(View.GONE);
                    JsonObject loginResponse = new Gson().fromJson(resultResponse,JsonObject.class);
                    if(loginResponse.has("message"))
                    {
                        Util.showAlert(NotificationsActivity.this,"Info",loginResponse.get("message").getAsString());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    mProgressView.setVisibility(View.GONE);
                    Util.showAlert(NotificationsActivity.this,"Error","Server error!!!");
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("Services[title]",addService.getServiceTitle());
                    params.put("Services[service_for]",addService.getServiceFor());
                    params.put("Services[duration]",addService.getServiceDuration());
                    params.put("Services[listed_online]",addService.getListedOnline());
                    params.put("Services[is_feature]",addService.getIsFeature());
                    params.put("Services[offer_available]",addService.getOfferAvailable());
                    params.put("Services[description]",addService.getDescription());
                    params.put("Services[price]",addService.getPrice());

                    return params;
                }
            };

            mProgressView.setVisibility(View.VISIBLE);
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(request, Constants.kLogin_API);
        }
        else
            Util.showAlert(NotificationsActivity.this,"Info","Please fill in all the details!");

    }
}
