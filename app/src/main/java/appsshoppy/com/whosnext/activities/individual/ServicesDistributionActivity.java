package appsshoppy.com.whosnext.activities.individual;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import appsshoppy.com.whosnext.R;
import appsshoppy.com.whosnext.model.AddService;

public class ServicesDistributionActivity extends AppCompatActivity {

    private Button btnSubmit;
    private Spinner spinnerListedOnline, spinnerOfferAvailable;
    private CheckBox chkFeatured;

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

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        spinnerListedOnline = (Spinner) findViewById(R.id.spinnerListedOnline);
        spinnerOfferAvailable = (Spinner) findViewById(R.id.spinnerOfferAvailable);
        chkFeatured = (CheckBox) findViewById(R.id.chkFeatured);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                AddService addService = new AddService();
                if(chkFeatured.isChecked())
                    addService.setIsFeature("1");
                else
                    addService.setIsFeature("0");
                addService.setListedOnline(spinnerListedOnline.getSelectedItem().toString());
                addService.setOfferAvailable(spinnerOfferAvailable.getSelectedItem().toString());
                intent.putExtra("service_distribution",new Gson().toJson(addService));
                setResult(0,intent);
                finish();
            }
        });
    }
}
