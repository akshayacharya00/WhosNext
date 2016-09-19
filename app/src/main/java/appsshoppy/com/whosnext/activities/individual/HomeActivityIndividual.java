package appsshoppy.com.whosnext.activities.individual;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import appsshoppy.com.whosnext.R;
import appsshoppy.com.whosnext.activities.business.BusinessOpeningHoursActivity;

public class HomeActivityIndividual extends AppCompatActivity {

    private ImageButton btnServices, btnOpeningHours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout_individual);
        btnServices = (ImageButton) findViewById(R.id.btnServices);
        btnServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivityIndividual.this,ServicesActivity.class));
                //finish();
            }
        });

        btnOpeningHours = (ImageButton) findViewById(R.id.btnOpeningHours);
        btnOpeningHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivityIndividual.this,BusinessOpeningHoursActivity.class));
            }
        });

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
