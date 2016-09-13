package appsshoppy.com.whosnext.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import org.lucasr.twowayview.widget.TwoWayView;

import appsshoppy.com.whosnext.R;
import appsshoppy.com.whosnext.adapters.HorizontalListViewAdapter;

public class PickUpDateTimeActivity extends AppCompatActivity {

    private Button btnConfirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_up_date_time);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        setSupportActionBar(toolbar);


        //dummy data for time picker
        String[] items = {"2:00 PM", "2:30 PM", "3:00 PM","2:00 PM", "2:30 PM", "3:00 PM","2:00 PM", "2:30 PM", "3:00 PM","2:00 PM", "2:30 PM", "3:00 PM"};

        HorizontalListViewAdapter horizontalListViewAdapter = new HorizontalListViewAdapter(items);
        TwoWayView lvTest = (TwoWayView) findViewById(R.id.lvItems);
        lvTest.setAdapter(horizontalListViewAdapter);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PickUpDateTimeActivity.this,ConfirmDetailsActivity.class));
                finish();
            }
        });
    }

}
