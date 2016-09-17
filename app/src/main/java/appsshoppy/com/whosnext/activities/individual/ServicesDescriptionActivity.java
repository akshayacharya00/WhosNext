package appsshoppy.com.whosnext.activities.individual;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import appsshoppy.com.whosnext.R;
import appsshoppy.com.whosnext.model.AddService;
import appsshoppy.com.whosnext.util.Util;

public class ServicesDescriptionActivity extends AppCompatActivity {

    private EditText txtDescription;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_description);
        txtDescription = (EditText) findViewById(R.id.txtDescription);
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
        ((TextView)customView.findViewById(R.id.txtToolbarHeader)).setText("Description");
        parent.setContentInsetsAbsolute(0,0);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtDescription.getText().toString().trim().length()>0)
                {
                    Intent intent = new Intent();
                    AddService addService = new AddService();
                    addService.setDescription(txtDescription.getText().toString());
                    intent.putExtra("service_description",new Gson().toJson(addService));
                    setResult(0,intent);
                    finish();
                }
                else
                    Util.showAlert(ServicesDescriptionActivity.this,"Info","Please fill in all the details!");

            }
        });
    }
}
