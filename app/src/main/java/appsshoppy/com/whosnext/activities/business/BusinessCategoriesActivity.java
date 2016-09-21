package appsshoppy.com.whosnext.activities.business;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appsshoppy.com.whosnext.AppController;
import appsshoppy.com.whosnext.R;
import appsshoppy.com.whosnext.adapters.ServiceCategoryAdapter;
import appsshoppy.com.whosnext.custom.volleyMultipart.VolleyMultipartRequest;
import appsshoppy.com.whosnext.model.ServiceCategory;
import appsshoppy.com.whosnext.util.Constants;
import appsshoppy.com.whosnext.util.Util;

public class BusinessCategoriesActivity extends AppCompatActivity {

    private ListView categoryListView;
    private View mProgressView;
    private ArrayList<String> selectedServiceCategories, unselectedServiceCategories;
    private Spinner spinnerGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_categories);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getSupportActionBar().setCustomView(R.layout.app_bar);
        mProgressView = findViewById(R.id.progress_overlay);
        spinnerGender = (Spinner) findViewById(R.id.spinnerGender);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        View customView = getLayoutInflater().inflate(R.layout.app_bar_with_right_button, null);
        ((Button)customView.findViewById(R.id.btnRight)).setVisibility(View.VISIBLE);
        ((Button)customView.findViewById(R.id.btnRight)).setText("Done");
        ((Button)customView.findViewById(R.id.btnRight)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSelectedServices();
                for(String id: selectedServiceCategories)
                    modifyCategory(id,false);

                for(String id: unselectedServiceCategories)
                    modifyCategory(id,true);
            }
        });
        actionBar.setCustomView(customView);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.navbar));
        Toolbar parent =(Toolbar) customView.getParent();
        ((TextView)customView.findViewById(R.id.txtToolbarHeader)).setText("Categories");
        parent.setContentInsetsAbsolute(0,0);

        categoryListView = (ListView) findViewById(R.id.categoryListView);

        getServiceCategories();

    }

    private boolean isValidForm(){
        /*if(radioGender.getCheckedRadioButtonId()!=-1 && getSelectedServices().size()>0 && txtServicePrice.getText().toString().trim().length()>0 && txtServiceTitle.getText().toString().trim().length()>0 && txtServiceDuration.getText().toString().trim().length()>0)
        {
            return true;
        }
        else
        {
            Util.showAlert(BusinessCategoriesActivity.this,"Info","Please fill in all the details!");
        }*/
        return false;
    }

    private List<String > getSelectedServices(){
        selectedServiceCategories = new ArrayList<String>();
        unselectedServiceCategories = new ArrayList<String>();
        ArrayList<ServiceCategory> serviceArrayList = ((ServiceCategoryAdapter)categoryListView.getAdapter()).categoryList;
        for(ServiceCategory serviceCategory:serviceArrayList)
        {
            if(serviceCategory.isSelected())
                selectedServiceCategories.add(serviceCategory.getId());
            else
                unselectedServiceCategories.add(serviceCategory.getId());

        }
        return selectedServiceCategories;
    }

    private void getServiceCategories(){
        StringRequest request = new StringRequest(Request.Method.GET, Constants.kBusinessCategories_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response",response);
                Util.animateView(mProgressView, View.GONE, 0.4f, 200);
                JsonObject loginResponse = new Gson().fromJson(response,JsonObject.class);
                if(loginResponse.has("success"))
                {
                    if(loginResponse.get("success").getAsBoolean())
                    {
                        ArrayList<ServiceCategory> listData = new ArrayList<ServiceCategory>();

                        JsonArray serviceList = loginResponse.getAsJsonArray("data");
                        for(int i=0;i<serviceList.size();i++)
                        {
                            JsonObject serviceObject = serviceList.get(i).getAsJsonObject();

                            ServiceCategory serviceCategory = new ServiceCategory();
                            serviceCategory.setCategory(serviceObject.get("cat_title").getAsString());
                            serviceCategory.setId(serviceObject.get("id").getAsString());
                            serviceCategory.setSelected(false);
                            listData.add(serviceCategory);
                        }
                        categoryListView.setAdapter(new ServiceCategoryAdapter(BusinessCategoriesActivity.this,R.layout.service_category_list_row,listData));
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Util.animateView(mProgressView, View.GONE, 0.4f, 200);
                Util.showAlert(BusinessCategoriesActivity.this,"Error","Server error!!!");
            }
        });

        Util.animateView(mProgressView, View.VISIBLE, 0.4f, 200);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(request);
    }

    private void modifyCategory(final String id, boolean isRemove){
        String authToken = AppController.getInstance().getAuthToken();
        String URL = "";
        if(isRemove)
            URL = Constants.kBusinessRemoveCategory_API+authToken;
        else
            URL = Constants.kBusinessAddCategory_API+authToken;
        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, URL, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                Log.d("Response",resultResponse);
                Util.animateView(mProgressView, View.GONE, 0.4f, 200);
                JsonObject loginResponse = new Gson().fromJson(resultResponse,JsonObject.class);
                if(loginResponse.has("message"))
                {
                    Util.showAlert(BusinessCategoriesActivity.this,"Info",loginResponse.get("message").getAsString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Util.animateView(mProgressView, View.GONE, 0.4f, 200);
                Util.showAlert(BusinessCategoriesActivity.this,"Error","Server error!!!");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("BusinessCategories[category_id]",id);

                return params;
            }
        };

        Util.animateView(mProgressView, View.VISIBLE, 0.4f, 200);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(request);
    }
}
