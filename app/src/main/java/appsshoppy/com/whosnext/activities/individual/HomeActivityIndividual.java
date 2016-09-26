package appsshoppy.com.whosnext.activities.individual;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import appsshoppy.com.whosnext.AppController;
import appsshoppy.com.whosnext.R;
import appsshoppy.com.whosnext.activities.CustomerProfileActivity;
import appsshoppy.com.whosnext.activities.MainActivity;
import appsshoppy.com.whosnext.activities.business.BusinessCategoriesActivity;
import appsshoppy.com.whosnext.activities.business.BusinessFeesPolicyActivity;
import appsshoppy.com.whosnext.activities.business.BusinessMyAccountActivity;
import appsshoppy.com.whosnext.activities.business.BusinessOpeningHoursActivity;
import appsshoppy.com.whosnext.activities.business.BusinessStaffActivity;

public class HomeActivityIndividual extends AppCompatActivity {

    private ImageButton btnServices, btnOpeningHours, btnFeePolicy, btnProfile, btnCategories, btnStaff, btnLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AppController.role.equals("Business"))
            setContentView(R.layout.drawer_layout);
        else if(AppController.role.equals("Customer"))
            setContentView(R.layout.drawer_layout_customer);
        else
            setContentView(R.layout.drawer_layout_individual);
        btnServices = (ImageButton) findViewById(R.id.btnServices);
        if(btnServices!=null) {
            btnServices.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(HomeActivityIndividual.this, ServicesActivity.class));
                    //finish();
                }
            });
        }

        btnOpeningHours = (ImageButton) findViewById(R.id.btnOpeningHours);
        if(btnOpeningHours!=null) {
            btnOpeningHours.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(HomeActivityIndividual.this, BusinessOpeningHoursActivity.class));
                }
            });
        }

        btnFeePolicy = (ImageButton) findViewById(R.id.btnFeePolicy);
        if(btnFeePolicy!=null) {
            btnFeePolicy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(HomeActivityIndividual.this, BusinessFeesPolicyActivity.class));
                }
            });
        }

        btnProfile = (ImageButton) findViewById(R.id.btnProfile);
        if(btnProfile!=null) {
            btnProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(AppController.role.equals("Business"))
                        startActivity(new Intent(HomeActivityIndividual.this, BusinessMyAccountActivity.class));
                    else
                        startActivity(new Intent(HomeActivityIndividual.this, CustomerProfileActivity.class));
                }
            });
        }

        btnCategories = (ImageButton) findViewById(R.id.btnCategories);
        if(btnCategories!=null) {
            btnCategories.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(HomeActivityIndividual.this, BusinessCategoriesActivity.class));
                }
            });
        }

        btnStaff = (ImageButton) findViewById(R.id.btnStaff);
        if(btnStaff!=null) {
            btnStaff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(HomeActivityIndividual.this, BusinessStaffActivity.class));
                }
            });
        }

        btnLogOut = (ImageButton) findViewById(R.id.btnLogOut);
        if(btnLogOut!=null) {
            btnLogOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(HomeActivityIndividual.this, MainActivity.class));
                }
            });
        }


        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getSupportActionBar().setCustomView(R.layout.app_bar);

        /*ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        View customView = getLayoutInflater().inflate(R.layout.app_bar_with_right_button, null);
        actionBar.setCustomView(customView);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.navbar));
        Toolbar parent =(Toolbar) customView.getParent();
        ((TextView)customView.findViewById(R.id.txtToolbarHeader)).setText("Services");
        parent.setContentInsetsAbsolute(0,0);*/


    }
}
