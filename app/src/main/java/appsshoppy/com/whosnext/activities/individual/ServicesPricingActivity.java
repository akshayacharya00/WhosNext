package appsshoppy.com.whosnext.activities.individual;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import appsshoppy.com.whosnext.AppController;
import appsshoppy.com.whosnext.R;
import appsshoppy.com.whosnext.adapters.ServiceCategoryAdapter;
import appsshoppy.com.whosnext.model.AddService;
import appsshoppy.com.whosnext.model.ServiceCategory;
import appsshoppy.com.whosnext.util.Constants;
import appsshoppy.com.whosnext.util.Util;

public class ServicesPricingActivity extends AppCompatActivity {

    private ListView categoryListView;
    private View mProgressView;
    private Button btnSumbit;
    private RadioGroup radioGender;
    private ArrayList<String> selectedServiceCategories;
    private EditText txtServiceTitle, txtServicePrice, txtServiceDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_pricing);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getSupportActionBar().setCustomView(R.layout.app_bar);
        mProgressView = findViewById(R.id.progress);
        radioGender = (RadioGroup) findViewById(R.id.radioGender);
        txtServiceTitle = (EditText) findViewById(R.id.txtServiceTitle);
        txtServicePrice = (EditText) findViewById(R.id.txtServicePrice);
        txtServiceDuration = (EditText) findViewById(R.id.txtServiceDuration);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        View customView = getLayoutInflater().inflate(R.layout.app_bar_with_right_button, null);
        ((Button)customView.findViewById(R.id.btnRight)).setVisibility(View.GONE);
        actionBar.setCustomView(customView);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.navbar));
        Toolbar parent =(Toolbar) customView.getParent();
        ((TextView)customView.findViewById(R.id.txtToolbarHeader)).setText("Services and Pricing");
        parent.setContentInsetsAbsolute(0,0);

        categoryListView = (ListView) findViewById(R.id.categoryListView);

        getServiceCategories();

        btnSumbit = (Button) findViewById(R.id.btnSubmit);
        btnSumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(isValidForm())
                    {
                        Intent intent = new Intent();
                        AddService addService = new AddService();
                        addService.setServiceTitle(txtServiceTitle.getText().toString());
                        addService.setPrice(txtServicePrice.getText().toString());
                        addService.setServiceDuration(txtServiceDuration.getText().toString());
                        int selectedId = radioGender.getCheckedRadioButtonId();
                        String gender = ((RadioButton) findViewById(selectedId)).getText().toString();
                        if(gender == "Male")
                            addService.setServiceFor("1");
                        else
                            addService.setServiceFor("2");

                        intent.putExtra("service_pricing",new Gson().toJson(addService));
                        setResult(0,intent);
                        finish();
                    }
            }
        });
    }

    private boolean isValidForm(){
        if(radioGender.getCheckedRadioButtonId()!=-1 && getSelectedServices().size()>0 && txtServicePrice.getText().toString().trim().length()>0 && txtServiceTitle.getText().toString().trim().length()>0 && txtServiceDuration.getText().toString().trim().length()>0)
        {
            return true;
        }
        else
        {
            Util.showAlert(ServicesPricingActivity.this,"Info","Please fill in all the details!");
        }
        return false;
    }

    private List<String > getSelectedServices(){
        selectedServiceCategories = new ArrayList<String>();
        ArrayList<ServiceCategory> serviceArrayList = ((ServiceCategoryAdapter)categoryListView.getAdapter()).categoryList;
        for(ServiceCategory serviceCategory:serviceArrayList)
        {
            if(serviceCategory.isSelected())
                selectedServiceCategories.add(serviceCategory.getCategory());
        }
        return selectedServiceCategories;
    }

    private void getServiceCategories(){
        String authToken = AppController.getInstance().getAuthToken();
        StringRequest request = new StringRequest(Request.Method.GET, Constants.kCategoryListing_API+authToken, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response",response);
                mProgressView.setVisibility(View.GONE);
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
                            serviceCategory.setCategory(serviceObject.get("Category Title").getAsString());
                            serviceCategory.setSelected(false);
                            listData.add(serviceCategory);
                        }
                        categoryListView.setAdapter(new ServiceCategoryAdapter(ServicesPricingActivity.this,R.layout.service_category_list_row,listData));
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressView.setVisibility(View.GONE);
                Util.showAlert(ServicesPricingActivity.this,"Error","Server error!!!");
            }
        });

        mProgressView.setVisibility(View.VISIBLE);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(request, Constants.kServiceListing_API);
    }
}
