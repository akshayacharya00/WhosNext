package appsshoppy.com.whosnext.activities.business;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import appsshoppy.com.whosnext.AppController;
import appsshoppy.com.whosnext.R;
import appsshoppy.com.whosnext.custom.volleyMultipart.VolleyMultipartRequest;
import appsshoppy.com.whosnext.model.User;
import appsshoppy.com.whosnext.util.Constants;
import appsshoppy.com.whosnext.util.Util;

public class BusinessChangePasswordActivity extends AppCompatActivity {

    private Button btnUpdate;
    private EditText txtPassword,txtRePassword;
    private TextView txtCustomerName,txtCustomerEmail;
    private View mProgressView;
    private ImageButton imgProfilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_change_password);
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
        ((TextView)customView.findViewById(R.id.txtToolbarHeader)).setText("Change Password");
        parent.setContentInsetsAbsolute(0,0);

        //get user data
        User user = new Gson().fromJson(AppController.getInstance().getFromSharedPreference("user_data"),User.class);

        imgProfilePic = (ImageButton) findViewById(R.id.imgProfilePic);
        mProgressView = findViewById(R.id.progress_overlay);
        txtCustomerEmail = (TextView) findViewById(R.id.txtCustomerEmail);
        txtCustomerName = (TextView) findViewById(R.id.txtCustomerName);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        txtPassword = (EditText) findViewById(R.id.txtNewPassword);
        txtRePassword = (EditText) findViewById(R.id.txtRePassword);

        if(user!=null)
        {
            txtCustomerName.setText(user.getProfileName());
            txtCustomerEmail.setText(user.getEmail());
            if(user.getAvatar()!="")
                getAvatarImage(user.getAvatar());
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtPassword.getText().toString().equals(txtRePassword.getText().toString()))
                    changePassword();
                else
                    Util.showAlert(BusinessChangePasswordActivity.this,"Info","Passwords don't match!!!");
            }
        });
    }

    private void getAvatarImage(String url){
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

    private void changePassword(){
        String authToken = AppController.getInstance().getAuthToken();
        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, Constants.kChangePassword_API+authToken, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                Log.d("Response",resultResponse);
                Util.animateView(mProgressView, View.GONE, 0.4f, 200);
                JsonObject loginResponse = new Gson().fromJson(resultResponse,JsonObject.class);
                if(loginResponse.has("message"))
                {
                    Util.showAlert(BusinessChangePasswordActivity.this,"Info",loginResponse.get("message").getAsString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Util.animateView(mProgressView, View.GONE, 0.4f, 200);
                Util.showAlert(BusinessChangePasswordActivity.this,"Error","Server error!!!");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("User[newPassword]",txtPassword.getText().toString());
                params.put("User[confirmPassword]",txtRePassword.getText().toString());

                return params;
            }
        };

        Util.animateView(mProgressView, View.VISIBLE, 0.4f, 200);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(request, Constants.kLogin_API);
    }
}
