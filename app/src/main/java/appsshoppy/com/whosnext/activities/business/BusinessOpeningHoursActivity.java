package appsshoppy.com.whosnext.activities.business;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appsshoppy.com.whosnext.AppController;
import appsshoppy.com.whosnext.R;
import appsshoppy.com.whosnext.custom.volleyMultipart.VolleyMultipartRequest;
import appsshoppy.com.whosnext.model.City;
import appsshoppy.com.whosnext.model.Country;
import appsshoppy.com.whosnext.util.Constants;
import appsshoppy.com.whosnext.util.Util;
import me.tittojose.www.timerangepicker_library.TimeRangePickerDialog;

public class BusinessOpeningHoursActivity extends AppCompatActivity implements TimeRangePickerDialog.OnTimeRangeSelectedListener{

    private ListView servicesListView;
    private TextView btnSetWorkingHours;
    public static final String TIMERANGEPICKER_TAG = "timerangepicker";
    private String startTime,endTime;
    private View mProgressView;
    private ToggleButton chkbox1,chkbox2,chkbox3,chkbox4,chkbox5,chkbox6,chkbox7;
    private String openingDays = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_opening_hours);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getSupportActionBar().setCustomView(R.layout.app_bar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        View customView = getLayoutInflater().inflate(R.layout.app_bar_with_right_button, null);
        ((Button) customView.findViewById(R.id.btnRight)).setText("Done");
        ((Button) customView.findViewById(R.id.btnRight)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSelectedDays();
                setBusinessTimings();
            }
        });
        actionBar.setCustomView(customView);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.navbar));
        Toolbar parent = (Toolbar) customView.getParent();
        ((TextView) customView.findViewById(R.id.txtToolbarHeader)).setText("Business Opening Hours");
        parent.setContentInsetsAbsolute(0, 0);

        btnSetWorkingHours = (TextView) findViewById(R.id.txtWorkingHours);
        btnSetWorkingHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TimeRangePickerDialog timePickerDialog = TimeRangePickerDialog.newInstance(
                        BusinessOpeningHoursActivity.this, false);
                timePickerDialog.show(BusinessOpeningHoursActivity.this.getSupportFragmentManager(), TIMERANGEPICKER_TAG);

            }
        });

        //default timings
        startTime = "9:00 AM";
        endTime = "5:00 PM";

        mProgressView = findViewById(R.id.progress);
    }

    @Override
    public void onTimeRangeSelected(int startHour, int startMin, int endHour, int endMin) {
        startTime = Util.getTime(startHour,startMin);
        endTime = Util.getTime(endHour,endMin);
        btnSetWorkingHours.setText(startTime+" - "+endTime);
    }

    private void getSelectedDays()
    {
        chkbox1 = (ToggleButton) findViewById(R.id.chk1);
        chkbox2 = (ToggleButton) findViewById(R.id.chk2);
        chkbox3 = (ToggleButton) findViewById(R.id.chk3);
        chkbox4 = (ToggleButton) findViewById(R.id.chk4);
        chkbox5 = (ToggleButton) findViewById(R.id.chk5);
        chkbox6 = (ToggleButton) findViewById(R.id.chk6);
        chkbox7 = (ToggleButton) findViewById(R.id.chk7);

        List<String> selectedDays = new ArrayList<String>();

        if(chkbox1.isChecked())
            selectedDays.add("1");
        if(chkbox2.isChecked())
            selectedDays.add("2");
        if(chkbox3.isChecked())
            selectedDays.add("3");
        if(chkbox4.isChecked())
            selectedDays.add("4");
        if(chkbox5.isChecked())
            selectedDays.add("5");
        if(chkbox6.isChecked())
            selectedDays.add("6");
        if(chkbox7.isChecked())
            selectedDays.add("7");

        openingDays = TextUtils.join(",",selectedDays);

    }

    private void setBusinessTimings(){
        String authToken = AppController.getInstance().getAuthToken();
        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, Constants.kBusinessHours_API+authToken, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                Log.d("Response",resultResponse);
                mProgressView.setVisibility(View.GONE);
                JsonObject loginResponse = new Gson().fromJson(resultResponse,JsonObject.class);
                if(loginResponse.has("message"))
                {
                    Util.showAlert(BusinessOpeningHoursActivity.this,"Info",loginResponse.get("message").getAsString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                mProgressView.setVisibility(View.GONE);
                Util.showAlert(BusinessOpeningHoursActivity.this,"Error","Server error!!!");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("UserBusinessTiming[open_time]",startTime);
                params.put("UserBusinessTiming[close_time]",endTime);
                params.put("UserBusinessTiming[day_id]",openingDays);


                return params;
            }
        };

        mProgressView.setVisibility(View.VISIBLE);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(request, Constants.kLogin_API);
    }


}
