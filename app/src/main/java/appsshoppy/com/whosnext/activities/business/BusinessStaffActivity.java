package appsshoppy.com.whosnext.activities.business;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import appsshoppy.com.whosnext.adapters.BusinessSearchStaffListAdapter;
import appsshoppy.com.whosnext.adapters.BusinessStaffListAdapter;
import appsshoppy.com.whosnext.model.Staff;
import appsshoppy.com.whosnext.util.Constants;
import appsshoppy.com.whosnext.util.Util;

public class BusinessStaffActivity extends AppCompatActivity {

    private ListView businessStaffList, businessOlderStaffList, businessSearchStaffList;
    private ImageButton btnDone;
    private ImageView imgSearch;
    private EditText txtSearch;
    private TextView txtSearchHeader, txtOldStaff;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_staff);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getSupportActionBar().setCustomView(R.layout.app_bar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        View customView = getLayoutInflater().inflate(R.layout.app_bar_with_right_button, null);
        ((Button)customView.findViewById(R.id.btnRight)).setVisibility(View.GONE);
        actionBar.setCustomView(customView);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.navbar));
        Toolbar parent =(Toolbar) customView.getParent();
        ((TextView)customView.findViewById(R.id.txtToolbarHeader)).setText("Staff");
        parent.setContentInsetsAbsolute(0,0);

        mProgressView = findViewById(R.id.progress_overlay);

        imgSearch = (ImageView) findViewById(R.id.imgSearch);
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });

        businessStaffList = (ListView) findViewById(R.id.businessStaffList);
        businessOlderStaffList = (ListView) findViewById(R.id.businessOlderStaffList);
        businessSearchStaffList = (ListView) findViewById(R.id.businessSearchStaffList);
        //list data
        ArrayList<Staff> listData = new ArrayList<Staff>();
        Staff staff = new Staff();
        staff.setStaffName("Staff Name");
        listData.add(staff);
        listData.add(staff);
        listData.add(staff);
        businessStaffList.setAdapter(new BusinessStaffListAdapter(listData,this,false));
        //listData.clear();
        //listData.add(staff);
        businessOlderStaffList.setAdapter(new BusinessStaffListAdapter(listData,this,true));
        txtSearchHeader = (TextView) findViewById(R.id.txtSearchHeader);
        txtOldStaff = (TextView) findViewById(R.id.txtOldStaff);
        txtSearch = (EditText) findViewById(R.id.txtSearch);
        txtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });

        getStaffMembers();
    }

    void performSearch()
    {
        //hide other stuff
        txtSearchHeader.setText("Search Results By Name");
        txtOldStaff.setVisibility(View.GONE);
        businessOlderStaffList.setVisibility(View.GONE);
        businessStaffList.setVisibility(View.GONE);
        businessSearchStaffList.setVisibility(View.VISIBLE);
        ArrayList<Staff> listData = new ArrayList<Staff>();
        Staff staff = new Staff();
        staff.setStaffName("Staff Name");
        listData.add(staff);
        listData.add(staff);
        listData.add(staff);
        businessSearchStaffList.setAdapter(new BusinessSearchStaffListAdapter(listData,this));

    }

    private void getStaffMembers(){
        String authToken = AppController.getInstance().getAuthToken();
        StringRequest request = new StringRequest(Request.Method.GET, Constants.kStaffMember_API+authToken, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response",response);
                Util.animateView(mProgressView, View.GONE, 0.4f, 200);
                JsonObject loginResponse = new Gson().fromJson(response,JsonObject.class);
                if(loginResponse.has("success"))
                {
                    if(loginResponse.get("success").getAsBoolean())
                    {
                        ArrayList<Staff> listData = new ArrayList<Staff>();

                        JsonArray serviceList = loginResponse.getAsJsonArray("data");
                        for(int i=0;i<serviceList.size();i++)
                        {
                            JsonObject serviceObject = serviceList.get(i).getAsJsonObject();

                            Staff staff = new Staff();
                            staff.setId(serviceObject.get("Staff Id").getAsString());
                            staff.setStaffName(serviceObject.get("Staff Name").getAsString());
                            staff.setStaffImage(serviceObject.get("Staff Image").getAsString());
                            listData.add(staff);
                        }
                        businessOlderStaffList.setAdapter(new BusinessStaffListAdapter(listData,BusinessStaffActivity.this,true));
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Util.animateView(mProgressView, View.GONE, 0.4f, 200);
                Util.showAlert(BusinessStaffActivity.this,"Error","Server error!!!");
            }
        });

        Util.animateView(mProgressView, View.VISIBLE, 0.4f, 200);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(request);
    }
}
