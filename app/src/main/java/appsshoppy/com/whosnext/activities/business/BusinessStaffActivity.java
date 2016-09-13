package appsshoppy.com.whosnext.activities.business;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import appsshoppy.com.whosnext.R;
import appsshoppy.com.whosnext.adapters.BusinessSearchStaffListAdapter;
import appsshoppy.com.whosnext.adapters.BusinessStaffListAdapter;
import appsshoppy.com.whosnext.model.Staff;

public class BusinessStaffActivity extends AppCompatActivity {

    private ListView businessStaffList, businessOlderStaffList, businessSearchStaffList;
    private ImageButton btnDone;
    private ImageView imgSearch;
    private EditText txtSearch;
    private TextView txtSearchHeader, txtOldStaff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_staff);
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
        ((TextView)customView.findViewById(R.id.txtToolbarHeader)).setText("Staff");
        parent.setContentInsetsAbsolute(0,0);

        imgSearch = (ImageView) findViewById(R.id.imgSearch);
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });

        businessStaffList = (ListView) findViewById(R.id.businessStaffList);
        businessOlderStaffList = (ListView) findViewById(R.id.businessOlderStaffList);
        businessSearchStaffList = (ListView) findViewById(R.id.businessSearchStaffList);
        //list data
        ArrayList<Staff> listData = new ArrayList<Staff>();
        Staff staff = new Staff();
        staff.setStaffName("Staff Name");
        listData.add(staff);
        listData.add(staff);
        listData.add(staff);
        businessStaffList.setAdapter(new BusinessStaffListAdapter(listData,this,false));
        //listData.clear();
        //listData.add(staff);
        businessOlderStaffList.setAdapter(new BusinessStaffListAdapter(listData,this,true));
        txtSearchHeader = (TextView) findViewById(R.id.txtSearchHeader);
        txtOldStaff = (TextView) findViewById(R.id.txtOldStaff);
        txtSearch = (EditText) findViewById(R.id.txtSearch);
        txtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });
    }

    void performSearch()
    {
        //hide other stuff
        txtSearchHeader.setText("Search Results By Name");
        txtOldStaff.setVisibility(View.GONE);
        businessOlderStaffList.setVisibility(View.GONE);
        businessStaffList.setVisibility(View.GONE);
        businessSearchStaffList.setVisibility(View.VISIBLE);
        ArrayList<Staff> listData = new ArrayList<Staff>();
        Staff staff = new Staff();
        staff.setStaffName("Staff Name");
        listData.add(staff);
        listData.add(staff);
        listData.add(staff);
        businessSearchStaffList.setAdapter(new BusinessSearchStaffListAdapter(listData,this));

    }
}
