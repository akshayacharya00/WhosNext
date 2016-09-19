package appsshoppy.com.whosnext.activities.business;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import appsshoppy.com.whosnext.R;
import me.tittojose.www.timerangepicker_library.TimeRangePickerDialog;

public class BusinessOpeningHoursActivity extends AppCompatActivity implements TimeRangePickerDialog.OnTimeRangeSelectedListener{

    private ListView servicesListView;
    private TextView btnSetWorkingHours;
    public static final String TIMERANGEPICKER_TAG = "timerangepicker";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_opening_hours);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getSupportActionBar().setCustomView(R.layout.app_bar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        View customView = getLayoutInflater().inflate(R.layout.app_bar_with_right_button, null);
        ((Button) customView.findViewById(R.id.btnRight)).setText("Done");
        actionBar.setCustomView(customView);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.navbar));
        Toolbar parent = (Toolbar) customView.getParent();
        ((TextView) customView.findViewById(R.id.txtToolbarHeader)).setText("Business Opening Hours");
        parent.setContentInsetsAbsolute(0, 0);

        btnSetWorkingHours = (TextView) findViewById(R.id.btnSetWorkingHours);
        btnSetWorkingHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(BusinessOpeningHoursActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        btnSetWorkingHours.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();*/
                final TimeRangePickerDialog timePickerDialog = TimeRangePickerDialog.newInstance(
                        BusinessOpeningHoursActivity.this, false);
                timePickerDialog.show(BusinessOpeningHoursActivity.this.getSupportFragmentManager(), TIMERANGEPICKER_TAG);

            }
        });

    }

    @Override
    public void onTimeRangeSelected(int startHour, int startMin, int endHour, int endMin) {
            int
    }
}
