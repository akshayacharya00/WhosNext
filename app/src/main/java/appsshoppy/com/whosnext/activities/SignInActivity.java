package appsshoppy.com.whosnext.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appsshoppy.com.whosnext.AppController;
import appsshoppy.com.whosnext.R;
import appsshoppy.com.whosnext.activities.individual.HomeActivityIndividual;
import appsshoppy.com.whosnext.model.User;
import appsshoppy.com.whosnext.util.Constants;
import appsshoppy.com.whosnext.util.Util;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class SignInActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

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
    private ImageButton btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        setupActionBar();



        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        btnSignIn =  (ImageButton) findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEmailView.getText().toString().trim().length() >0 && mPasswordView.getText().toString().length()>0)
                    performLogin();
                else{
                    Util.showAlert(SignInActivity.this,"Info","Please fill in username/password!!!");
                }
                //startActivity(new Intent(SignInActivity.this,HomeActivity.class));
                //finish();
            }
        });

    }


    private void performLogin()
    {
        StringRequest loginRequest = new StringRequest(Request.Method.POST, Constants.kLogin_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("LoginResponse",response);
                mProgressView.setVisibility(View.GONE);
                JsonObject loginResponse = new Gson().fromJson(response,JsonObject.class);
                if(loginResponse.has("success"))
                {
                    if(loginResponse.get("success").getAsBoolean())
                    {
                        //login success
                        String role = loginResponse.get("data").getAsJsonObject().get("role_name").getAsString();
                        AppController.getInstance().role = role;
                        parseLoginResponse(loginResponse.get("data").getAsJsonObject());
                        String authKey = loginResponse.get("data").getAsJsonObject().get("auth_key").getAsString();
                        AppController.getInstance().saveToSharedPreferences(Constants.AUTH_KEY,authKey);
                        if(role.equals("Individual Service Provider") || role.equals("Business") || role.equals("Customer"))
                        {
                            startActivity(new Intent(SignInActivity.this, HomeActivityIndividual.class));
                            finish();
                        }
                    }
                    else
                        Util.showAlert(SignInActivity.this,"Info",loginResponse.get("message").toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressView.setVisibility(View.GONE);
                Util.showAlert(SignInActivity.this,"Error","Server error!!!");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                //dummy data
                //hansh1@gmail.com, 123456 // Staff
                //abhi007@gmail.com, 123456 // ISP
                //punit.kameriya@gmail.com 123456 //Customer
                //abhi@gmail.com 123456 // Customer
                params.put("Loginfront[email]",mEmailView.getText().toString());
                params.put("Loginfront[password]",mPasswordView.getText().toString());
                return params;
            }
        };

        mProgressView.setVisibility(View.VISIBLE);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(loginRequest, Constants.kLogin_API);

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
                new ArrayAdapter<>(SignInActivity.this,
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

    private void parseLoginResponse(JsonObject loginResponse){
        User user = new User();
        user.setFirstName(loginResponse.has("first_name")?loginResponse.get("first_name").getAsString():"");
        user.setLastName(loginResponse.has("last_name")?loginResponse.get("last_name").getAsString():"");
        user.setGender(loginResponse.has("gender")?loginResponse.get("gender").getAsString():"");
        user.setInstaUrl(loginResponse.has("instagram_url")?loginResponse.get("instagram_url").getAsString():"");
        user.setProfileName(loginResponse.has("profile_name")?loginResponse.get("profile_name").getAsString():"");
        user.setEmail(loginResponse.has("email")?loginResponse.get("email").getAsString():"");
        user.setLocationOnMap(loginResponse.has("location_on_map")?loginResponse.get("location_on_map").getAsString():"");
        user.setLatitude(loginResponse.has("lat")?loginResponse.get("lat").getAsString():"");
        user.setLogitude(loginResponse.has("lng")?loginResponse.get("lng").getAsString():"");
        user.setAbout(loginResponse.has("about")?loginResponse.get("about").getAsString():"");
        user.setAvatar(loginResponse.has("avatar")?loginResponse.get("avatar").getAsString():"");
        user.setBannerImage(loginResponse.has("banner_image")?loginResponse.get("banner_image").getAsString():"");
        user.setCoutryId(loginResponse.has("country_id")?loginResponse.get("country_id").getAsInt():0);
        user.setCityId(loginResponse.has("city_id")?loginResponse.get("city_id").getAsInt():0);
        user.setZipCode(loginResponse.has("zipcode")?loginResponse.get("zipcode").getAsString():"");
        user.setAddress(loginResponse.has("address")?loginResponse.get("address").getAsString():"");
        user.setPhoneNo(loginResponse.has("phone_no")?loginResponse.get("phone_no").getAsString():"");
        user.setPaypalEmail(loginResponse.has("paypal_email")?loginResponse.get("paypal_email").getAsString():"");
        user.setWebUrl(loginResponse.has("website_url")?loginResponse.get("website_url").getAsString():"");
        user.setPremium(loginResponse.has("is_premium")?loginResponse.get("is_premium").getAsBoolean():false);
        //if(AppController.role.equals("Business"))
        {
            user.setAcceptCreditCard(loginResponse.has("Accept credit card")?loginResponse.get("Accept credit card").getAsString():"");
            user.setWheelChair(loginResponse.has("Wheelchair")?loginResponse.get("Wheelchair").getAsString():"");
            user.setParking(loginResponse.has("Parking")?loginResponse.get("Parking").getAsString():"");
        }

        user.setStatus(loginResponse.get("status").getAsString());

        AppController.getInstance().saveToSharedPreferences("user_data",new Gson().toJson(user,User.class));

    }
}

