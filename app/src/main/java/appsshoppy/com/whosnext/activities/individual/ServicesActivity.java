package appsshoppy.com.whosnext.activities.individual;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import appsshoppy.com.whosnext.R;
import appsshoppy.com.whosnext.adapters.IndividualServicesListAdapter;
import appsshoppy.com.whosnext.model.IndividualService;

public class ServicesActivity extends AppCompatActivity {

    private ListView servicesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getSupportActionBar().setCustomView(R.layout.app_bar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        View customView = getLayoutInflater().inflate(R.layout.app_bar_with_right_button, null);
        actionBar.setCustomView(customView);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.navbar));
        Toolbar parent =(Toolbar) customView.getParent();
        ((TextView)customView.findViewById(R.id.txtToolbarHeader)).setText("Services");
        parent.setContentInsetsAbsolute(0,0);


        //right button listener
        ((Button)customView.findViewById(R.id.btnRight)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ServicesActivity.this,AddNewServicesActivity.class));
            }
        });

        servicesListView = (ListView) findViewById(R.id.servicesListView);
        //create dummy data
        IndividualService individualService = new IndividualService();
        individualService.setServiceName("Facial, Hair Cut, Head, Shoulder Massage, Shaving & Foot massage");
        individualService.setServiceAvailability("Always");
        individualService.setServiceTime("75 Min(approx)");
        individualService.setServicePrice("$25.00");
        ArrayList<IndividualService> listData = new ArrayList<IndividualService>();
        listData.add(individualService);
        listData.add(individualService);
        listData.add(individualService);
        listData.add(individualService);
        listData.add(individualService);
        servicesListView.setAdapter(new IndividualServicesListAdapter(listData,this));
    }
}
