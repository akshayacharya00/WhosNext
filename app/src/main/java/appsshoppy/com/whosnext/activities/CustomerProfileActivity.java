package appsshoppy.com.whosnext.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import appsshoppy.com.whosnext.AppController;
import appsshoppy.com.whosnext.R;
import appsshoppy.com.whosnext.custom.volleyMultipart.VolleyMultipartRequest;
import appsshoppy.com.whosnext.model.City;
import appsshoppy.com.whosnext.model.Country;
import appsshoppy.com.whosnext.model.User;
import appsshoppy.com.whosnext.util.Constants;
import appsshoppy.com.whosnext.util.Util;

public class CustomerProfileActivity extends AppCompatActivity {

    private ListView servicesListView;
    private Spinner countrySpinner, citySpinner;
    private ImageButton btnDone, imgProfilePic;
    private View mProgressView;
    private ArrayList<City> cityList;
    private ArrayList<Country> countryList;
    private ArrayList<String> cities, countries;
    private TextView lblCustomerName,lblCustomerEmail;
    private EditText txtFullName,txtProfileName,txtEmail,txtPhoneNumber,txtAddress,txtPostCode,txtPassword,txtRePassword;
    private User user;
    private int selectedCity;
    private RadioGroup radioGender;
    private RadioButton radioMale,radioFemale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_customer);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getSupportActionBar().setCustomView(R.layout.app_bar);

        lblCustomerName = (TextView) findViewById(R.id.lblCustomerName);
        lblCustomerEmail = (TextView) findViewById(R.id.lblCustomerEmail);
        txtFullName = (EditText) findViewById(R.id.txtFullName);
        txtProfileName = (EditText) findViewById(R.id.txtProfileName);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPhoneNumber = (EditText) findViewById(R.id.txtPhoneNumber);
        txtAddress = (EditText) findViewById(R.id.txtAddress);
        txtPostCode = (EditText) findViewById(R.id.txtPostCode);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtRePassword = (EditText) findViewById(R.id.txtRePassword);
        imgProfilePic = (ImageButton) findViewById(R.id.imgProfilePic);
        radioGender = (RadioGroup) findViewById(R.id.radioGender);
        radioMale = (RadioButton) findViewById(R.id.radioMale);
        radioFemale = (RadioButton) findViewById(R.id.radioFemale);
        btnDone = (ImageButton) findViewById(R.id.btnDone);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        View customView = getLayoutInflater().inflate(R.layout.app_bar_with_right_button, null);
        ((Button)customView.findViewById(R.id.btnRight)).setVisibility(View.GONE);
        actionBar.setCustomView(customView);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.navbar));
        Toolbar parent =(Toolbar) customView.getParent();
        ((TextView)customView.findViewById(R.id.txtToolbarHeader)).setText("My Account");
        parent.setContentInsetsAbsolute(0,0);

        mProgressView = findViewById(R.id.progress_overlay);

        //set country spinner
        countrySpinner = (Spinner) findViewById(R.id.countrySpinner);
        citySpinner = (Spinner) findViewById(R.id.citySpinner);
        /*Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        String country;
        for( Locale loc : locale ){
            country = loc.getDisplayCountry();
            if( country.length() > 0 && !countries.contains(country) ){
                countries.add( country );
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, countries);
        countrySpinner.setAdapter(adapter);*/
        cityList = new ArrayList<City>();
        countryList = new ArrayList<Country>();
        cities = new ArrayList<String>();
        countries = new ArrayList<String>();

        getUserData();
        getCountriesAndCities();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, countries);
        countrySpinner.setAdapter(adapter);

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, cities);
        citySpinner.setAdapter(cityAdapter);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserProfile();
            }
        });

    }

    private void getCountriesAndCities()
    {
        StringRequest request = new StringRequest(Request.Method.GET, Constants
                .kCountry_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Util.animateView(mProgressView, View.GONE, 0.4f, 200);
                JsonObject jsonObject = new Gson().fromJson(response,JsonObject.class);
                if(jsonObject.has("success"))
                {
                    if(jsonObject.get("success").getAsBoolean())
                    {
                        JsonArray countriesArray =  jsonObject.getAsJsonArray("cites");
                        for(int i=0;i<countriesArray.size();i++)
                        {
                            JsonObject countryObject =  countriesArray.get(i).getAsJsonObject();
                            Country country = new Country();
                            country.setId(new Integer(countryObject.get("id").getAsInt()).toString());
                            country.setCountry(countryObject.get("name").getAsString());
                            countries.add(countryObject.get("name").getAsString());
                            countryList.add(country);
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CustomerProfileActivity.this,android.R.layout.simple_spinner_item, countries);
                        countrySpinner.setAdapter(adapter);
                    }
                }

            }},new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Util.animateView(mProgressView, View.GONE, 0.4f, 200);
                Util.showAlert(CustomerProfileActivity.this,"Error","Server error!!!");
            }
        });

        Util.animateView(mProgressView, View.VISIBLE, 0.4f, 200);
        // Adding request to request queue
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(request, Constants.kCountry_API);

        request = new StringRequest(Request.Method.GET, Constants
                .kCity_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mProgressView.setVisibility(View.GONE);
                JsonObject jsonObject = new Gson().fromJson(response,JsonObject.class);
                if(jsonObject.has("success"))
                {
                    if(jsonObject.get("success").getAsBoolean())
                    {
                        JsonArray citiesArray =  jsonObject.getAsJsonArray("cites");
                        for(int i=0;i<citiesArray.size();i++)
                        {
                            JsonObject countryObject =  citiesArray.get(i).getAsJsonObject();
                            City city = new City();
                            city.setId(new Integer(countryObject.get("id").getAsInt()).toString());
                            city.setCity(countryObject.get("city_name").getAsString());
                            cities.add(countryObject.get("city_name").getAsString());
                            cityList.add(city);
                            if(user!=null)
                            {
                                if(new Integer(city.getId())==user.getCityId())
                                    selectedCity = i;
                            }
                        }
                        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(CustomerProfileActivity.this,android.R.layout.simple_spinner_item, cities);
                        citySpinner.setAdapter(cityAdapter);
                        citySpinner.setSelection(selectedCity);
                    }
                }

            }},new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Util.animateView(mProgressView, View.GONE, 0.4f, 200);
                Util.showAlert(CustomerProfileActivity.this,"Error","Server error!!!");
            }
        });
        Util.animateView(mProgressView, View.VISIBLE, 0.4f, 200);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(request, Constants.kCity_API);
    }

    private void getUserData(){
        user = new Gson().fromJson(AppController.getInstance().getFromSharedPreference("user_data"),User.class);
        lblCustomerName.setText(user.getProfileName());
        lblCustomerEmail.setText(user.getEmail());
        getAvatar(user.getAvatar());

        txtFullName.setText(user.getFirstName()+" "+user.getLastName());
        txtProfileName.setText(user.getProfileName());
        txtEmail.setText(user.getEmail());
        txtPhoneNumber.setText(user.getPhoneNo());
        txtAddress.setText(user.getAddress());
        txtPostCode.setText(user.getZipCode());

        if(user.getGender().equals("Male"))
            radioMale.setChecked(true);
        else
            radioFemale.setChecked(true);

    }

    private void getAvatar(String url){
        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        imgProfilePic.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        AppController.getInstance().addToRequestQueue(request);
    }

    private void saveUserProfile(){
        String authToken = AppController.getInstance().getAuthToken();
        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, Constants.kCustomer_Edit_Profile_API+authToken, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                Log.d("Response",resultResponse);
                mProgressView.setVisibility(View.GONE);
                JsonObject loginResponse = new Gson().fromJson(resultResponse,JsonObject.class);
                if(loginResponse.has("message"))
                {
                    Util.showAlert(CustomerProfileActivity.this,"Info",loginResponse.get("message").getAsString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                mProgressView.setVisibility(View.GONE);
                Util.showAlert(CustomerProfileActivity.this,"Error","Server error!!!");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("User[full_name]",txtFullName.getText().toString());
                params.put("User[profile_name]",txtProfileName.getText().toString());
                params.put("User[email]",txtEmail.getText().toString());
                params.put("User[phone_no]",txtPhoneNumber.getText().toString());
                if(txtPassword.getText().toString().trim().length()>0)
                {
                    params.put("User[newPassword]",txtPassword.getText().toString());
                    params.put("User[confirmPassword]",txtRePassword.getText().toString());
                }

                int selectedId = radioGender.getCheckedRadioButtonId();
                String gender = ((RadioButton) findViewById(selectedId)).getText().toString();
                if(gender == "Male")
                    params.put("User[gender]","1");
                else
                    params.put("User[gender]","2");

                String country = countrySpinner.getSelectedItem().toString();
                String countryId = "", cityId = "";
                for(Country countryObject : countryList)
                {
                    if(countryObject.getCountry().equals(country))
                        countryId = countryObject.getId();
                }

                String city = citySpinner.getSelectedItem().toString();

                for(City cityObject : cityList)
                {
                    if(cityObject.getCity().equals(city))
                        cityId = cityObject.getId();
                }

                params.put("User[country_id]",countryId);
                params.put("User[city_id]",cityId);

                params.put("User[zipcode]",txtPostCode.getText().toString());
                params.put("User[address]",txtAddress.getText().toString());
                return params;
            }
        };

        mProgressView.setVisibility(View.VISIBLE);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(request);

    }


}
