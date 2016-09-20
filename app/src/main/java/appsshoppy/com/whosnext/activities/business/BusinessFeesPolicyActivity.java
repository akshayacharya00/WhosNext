package appsshoppy.com.whosnext.activities.business;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import appsshoppy.com.whosnext.AppController;
import appsshoppy.com.whosnext.R;
import appsshoppy.com.whosnext.custom.volleyMultipart.VolleyMultipartRequest;
import appsshoppy.com.whosnext.util.Constants;
import appsshoppy.com.whosnext.util.Util;

public class BusinessFeesPolicyActivity extends AppCompatActivity {

    private ListView servicesListView;
    EditText txtOtherPolicy;
    private Spinner spinnerPersonalCancellation, spinnerArrivalTime, spinnerDepartureTime, spinnerCancellation, spinnerMinimumAge;
    private ImageButton btnDone;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_fee_policy);
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
                if(isValidForm())
                    setFeePolicies();
            }
        });
        actionBar.setCustomView(customView);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.navbar));
        Toolbar parent =(Toolbar) customView.getParent();
        ((TextView)customView.findViewById(R.id.txtToolbarHeader)).setText("Fees Policies");
        parent.setContentInsetsAbsolute(0,0);

        spinnerPersonalCancellation = (Spinner) findViewById(R.id.spinnerPersonalDetails);
        spinnerArrivalTime = (Spinner) findViewById(R.id.spinnerArrivalTIme);
        spinnerDepartureTime = (Spinner) findViewById(R.id.spinnerDepartureTime);
        spinnerCancellation = (Spinner) findViewById(R.id.spinnerCancellation);
        spinnerMinimumAge = (Spinner) findViewById(R.id.spinnerMinimumAge);
        txtOtherPolicy = (EditText) findViewById(R.id.txtOtherPolicy);
        mProgressView = findViewById(R.id.progress_overlay);

    }

    private boolean isValidForm()
    {
        if(spinnerPersonalCancellation.getSelectedItem().toString().equals("Cancellation Policy") || spinnerArrivalTime.getSelectedItem().toString().equals("Arrival Time")
                || spinnerDepartureTime.getSelectedItem().toString().equals("Departure Time") || spinnerCancellation.getSelectedItem().toString().equals("Minimum Age") || txtOtherPolicy.getText().toString().trim().length()==0)
        {
            Util.showAlert(BusinessFeesPolicyActivity.this,"Info","All fields on this page are mandatory!!");
            return false;
        }
        else
            return true;
    }

    private void setFeePolicies()
    {
        String authToken = AppController.getInstance().getAuthToken();
        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, Constants.kFeePolicy_API+authToken, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                Log.d("Response",resultResponse);
                Util.animateView(mProgressView, View.GONE, 0.4f, 200);
                JsonObject loginResponse = new Gson().fromJson(resultResponse,JsonObject.class);
                if(loginResponse.has("message"))
                {
                    Util.showAlert(BusinessFeesPolicyActivity.this,"Info",loginResponse.get("message").getAsString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Util.animateView(mProgressView, View.GONE, 0.4f, 200);
                Util.showAlert(BusinessFeesPolicyActivity.this,"Error","Server error!!!");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("BusinessPolicies[appointment_cancel_hours]",spinnerPersonalCancellation.getSelectedItem().toString());
                params.put("BusinessPolicies[spa_arrival_time]",spinnerArrivalTime.getSelectedItem().toString());
                params.put("BusinessPolicies[spa_departure_time]",spinnerDepartureTime.getSelectedItem().toString());
                params.put("BusinessPolicies[spa_cancel_hours]",spinnerCancellation.getSelectedItem().toString());
                params.put("BusinessPolicies[spa_minimum_age]",spinnerMinimumAge.getSelectedItem().toString());
                params.put("BusinessPolicies[other_policy]",txtOtherPolicy.getText().toString());

                return params;
            }
        };

        Util.animateView(mProgressView, View.VISIBLE, 0.4f, 200);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(request, Constants.kLogin_API);
    }
}
