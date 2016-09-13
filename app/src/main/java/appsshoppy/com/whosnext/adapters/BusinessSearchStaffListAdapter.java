package appsshoppy.com.whosnext.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import appsshoppy.com.whosnext.R;
import appsshoppy.com.whosnext.model.Staff;

/**
 * Created by akshayacharya on 31/07/16.
 */
public class BusinessSearchStaffListAdapter extends BaseAdapter {
    private Activity activity;
    List<Staff> servicesArrayList = Collections.EMPTY_LIST;
    private static LayoutInflater inflater = null;
    public Resources res;

    public BusinessSearchStaffListAdapter(ArrayList<Staff> servicesArrayList, Activity context) {
        this.servicesArrayList = servicesArrayList;
        this.activity = context;

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
            vi = inflater.inflate(R.layout.business_staff_search_row, null);

            /****** View Holder Object to contain faultfixlist_item.xml file elements ******/

            holder = new ViewHolder();
            holder.btnInvite = (Button) vi.findViewById(R.id.btnInvite);

            holder.btnInvite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(BusinessSearchStaffListAdapter.this.activity);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.invitation_pop_up);
                    dialog.show();
                }
            });

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
        Button btnInvite;
    }

}
