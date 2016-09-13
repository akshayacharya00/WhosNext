package appsshoppy.com.whosnext.activities.business;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import appsshoppy.com.whosnext.R;
import appsshoppy.com.whosnext.adapters.IndividualAddNewServicesListAdapter;

public class BusinessMyAccountActivity extends AppCompatActivity {

    private ListView businessAccountList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_my_account);
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
        ((TextView)customView.findViewById(R.id.txtToolbarHeader)).setText("My Account");
        parent.setContentInsetsAbsolute(0,0);


        businessAccountList = (ListView) findViewById(R.id.businessActivityList);
        //list data
        ArrayList<String> listData = new ArrayList<String>();
        listData.add("Business Information");
        listData.add("Change Password");
        listData.add("Payment Detail");
        listData.add("Photos");
        businessAccountList.setAdapter(new IndividualAddNewServicesListAdapter(listData,this));

        //set onclick listener
        businessAccountList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0)
                {
                    startActivity(new Intent(BusinessMyAccountActivity.this,BusinessInformationActivity.class));
                }
                else if(position == 1)
                {
                    startActivity(new Intent(BusinessMyAccountActivity.this,BusinessChangePasswordActivity.class));
                }
                else if(position == 2)
                {
                    startActivity(new Intent(BusinessMyAccountActivity.this,BusinessPaymentDetailsActivity.class));
                }
                else if(position == 3)
                {
                    startActivity(new Intent(BusinessMyAccountActivity.this,BusinessUploadPhotosActivity.class));
                }
            }
        });
    }

}
