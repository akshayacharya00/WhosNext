package appsshoppy.com.whosnext.activities.business;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import appsshoppy.com.whosnext.R;
import appsshoppy.com.whosnext.adapters.BusinessDashboardListAdapter;
import appsshoppy.com.whosnext.model.BusinessDashboard;

public class BusinessDashboardActivity extends AppCompatActivity {

    private ListView businessDashboardList;
    private ImageButton btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_dashboard);
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
        ((TextView)customView.findViewById(R.id.txtToolbarHeader)).setText("Dashboard");
        parent.setContentInsetsAbsolute(0,0);


        businessDashboardList = (ListView) findViewById(R.id.businessDashboardList);
        //list data
        ArrayList<BusinessDashboard> listData = new ArrayList<BusinessDashboard>();
        BusinessDashboard businessDashboard = new BusinessDashboard();
        businessDashboard.setServiceStatus("Finished (today)");
        listData.add(businessDashboard);
        businessDashboard = new BusinessDashboard();
        businessDashboard.setServiceStatus("Cancelled (today)");
        listData.add(businessDashboard);
        listData.add(businessDashboard);
        listData.add(businessDashboard);

        businessDashboardList.setAdapter(new BusinessDashboardListAdapter(listData,this));
    }
}
