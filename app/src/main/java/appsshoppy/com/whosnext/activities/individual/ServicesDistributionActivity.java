package appsshoppy.com.whosnext.activities.individual;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import appsshoppy.com.whosnext.R;

public class ServicesDistributionActivity extends AppCompatActivity {

    private ListView servicesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_distribution);
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
        ((TextView)customView.findViewById(R.id.txtToolbarHeader)).setText("Distribution");
        parent.setContentInsetsAbsolute(0,0);

        /*servicesListView = (ListView) findViewById(R.id.addServicesListView);
        //list data
        ArrayList<String> listData = new ArrayList<String>();
        listData.add("Services and Pricing");
        listData.add("Description");
        listData.add("Distribution");
        servicesListView.setAdapter(new IndividualAddNewServicesListAdapter(listData,this));*/
    }
}
