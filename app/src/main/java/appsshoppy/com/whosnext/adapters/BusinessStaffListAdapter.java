package appsshoppy.com.whosnext.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import appsshoppy.com.whosnext.R;
import appsshoppy.com.whosnext.model.Staff;

/**
 * Created by akshayacharya on 31/07/16.
 */
public class BusinessStaffListAdapter extends BaseAdapter {
    private Activity activity;
    List<Staff> servicesArrayList = Collections.EMPTY_LIST;
    private static LayoutInflater inflater = null;
    public Resources res;
    private boolean isOldStaff;

    public BusinessStaffListAdapter(ArrayList<Staff> servicesArrayList, Activity context, Boolean isOldStaff) {
        this.servicesArrayList = servicesArrayList;
        this.activity = context;
        this.isOldStaff = isOldStaff;

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.servicesArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.servicesArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {
            /****** Inflate faultfixlist_item.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.business_staff_new_row, null);

            if(this.isOldStaff)
                vi.findViewById(R.id.btnAccept).setVisibility(View.GONE);
            /****** View Holder Object to contain faultfixlist_item.xml file elements ******/

            holder = new ViewHolder();
            //holder.txtStaffName = (TextView) vi.findViewById(R.id.txtBusinessDashboardLabel);
            vi.setTag(holder);
        }
        else {
            holder = (ViewHolder) vi.getTag();
        }

        //holder.txtServiceName.setText(servicesArrayList.get(position).getServiceName());
        //holder.txtStaffName.setText(servicesArrayList.get(position).getStaffName());

        return vi;
    }


    public static class ViewHolder {
        TextView txtStaffName;

    }

}
