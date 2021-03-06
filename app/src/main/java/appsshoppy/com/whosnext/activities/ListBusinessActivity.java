package appsshoppy.com.whosnext.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

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
import appsshoppy.com.whosnext.custom.volleyMultipart.VolleyMultipartRequest;
import appsshoppy.com.whosnext.model.City;
import appsshoppy.com.whosnext.model.Country;
import appsshoppy.com.whosnext.util.Constants;
import appsshoppy.com.whosnext.util.Util;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class ListBusinessActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private EditText txtFullName, txtBusinessName, txtPhoneNumber, txtEmail, txtPassword, txtRePassword, txtCountry, txtCity, txtAddress, txtPostCode;
    Spinner countrySpinner, citySpinner;
    private ImageButton imgSubmit;
    private ArrayList<City> cityList;
    private ArrayList<Country> countryList;
    private ArrayList<String> cities, countries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_business);
        setupActionBar();



        // Set up the login form.
        txtFullName = (EditText) findViewById(R.id.txtFullName);
        txtBusinessName = (EditText) findViewById(R.id.txtBusinessName);
        txtPhoneNumber = (EditText) findViewById(R.id.txtPhoneNumber);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtRePassword = (EditText) findViewById(R.id.txtRePassword);
        //txtCountry = findViewById(R.id.countrySpinner)
        txtAddress = (EditText) findViewById(R.id.txtAddress);
        txtPostCode = (EditText) findViewById(R.id.txtPostcode);
        imgSubmit = (ImageButton) findViewById(R.id.imgSubmit);

        //on click listener
        imgSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidLoginForm())
                {
                    //send data to server
                    attemptRegistration();
                }
                else
                    Util.showAlert(ListBusinessActivity.this,"Info","Please fill in all the details!");
            }
        });


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        countrySpinner = (Spinner) findViewById(R.id.countrySpinner);
        citySpinner = (Spinner) findViewById(R.id.citySpinner);

        cityList = new ArrayList<City>();
        countryList = new ArrayList<Country>();
        cities = new ArrayList<String>();
        countries = new ArrayList<String>();

        getCountriesAndCities();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, countries);
        countrySpinner.setAdapter(adapter);

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, cities);
        citySpinner.setAdapter(cityAdapter);
    }

    private void getCountriesAndCities()
    {
        StringRequest request = new StringRequest(Request.Method.GET, Constants
                .kCountry_API, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    mProgressView.setVisibility(View.GONE);
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
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ListBusinessActivity.this,android.R.layout.simple_spinner_item, countries);
                            countrySpinner.setAdapter(adapter);
                        }
                    }

            }},new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    mProgressView.setVisibility(View.GONE);
                    Util.showAlert(ListBusinessActivity.this,"Error","Server error!!!");
                }
        });

        mProgressView.setVisibility(View.VISIBLE);
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
                        }
                        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(ListBusinessActivity.this,android.R.layout.simple_spinner_item, cities);
                        citySpinner.setAdapter(cityAdapter);
                    }
                }

            }},new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                mProgressView.setVisibility(View.GONE);
                Util.showAlert(ListBusinessActivity.this,"Error","Server error!!!");
            }
        });
        mProgressView.setVisibility(View.VISIBLE);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(request, Constants.kCity_API);
    }

    private void attemptRegistration()
    {
        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, Constants.kRegister_Business_API, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                Log.d("Response",resultResponse);
                mProgressView.setVisibility(View.GONE);
                JsonObject loginResponse = new Gson().fromJson(resultResponse,JsonObject.class);
                if(loginResponse.has("message"))
                {
                    Util.showAlert(ListBusinessActivity.this,"Info",loginResponse.get("message").getAsString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                mProgressView.setVisibility(View.GONE);
                Util.showAlert(ListBusinessActivity.this,"Error","Server error!!!");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("User[full_name]",txtFullName.getText().toString());
                params.put("User[profile_name]",txtBusinessName.getText().toString());
                params.put("User[email]",txtEmail.getText().toString());
                params.put("User[newPassword]",txtPassword.getText().toString());
                params.put("User[confirmPassword]",txtRePassword.getText().toString());
                params.put("User[phone_no]",txtPhoneNumber.getText().toString());

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
                params.put("User[role_id]","2");
                return params;
            }
        };

        mProgressView.setVisibility(View.VISIBLE);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(request, Constants.kLogin_API);
    }

    private boolean isValidLoginForm()
    {
        if(txtFullName.getText().toString().trim().length()>0 && txtBusinessName.getText().toString().trim().length()>0 && txtPhoneNumber.getText().toString().trim().length()>0 && txtEmail.getText().toString().trim().length()>0 && txtPassword.getText().toString().trim().length()>0
                && txtRePassword.getText().toString().trim().length()>0 && txtAddress.getText().toString().trim().length()>0 && txtPostCode.getText().toString().trim().length()>0 )
        {
            if(txtPassword.getText().toString().equals(txtRePassword.getText().toString()))
                return true;
            else
                Util.showAlert(ListBusinessActivity.this,"Error","Password and Re-Password didn't match!");
        }
        else
            Util.showAlert(ListBusinessActivity.this,"Error","Please fill in all the details");

        return false;
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(ListBusinessActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

