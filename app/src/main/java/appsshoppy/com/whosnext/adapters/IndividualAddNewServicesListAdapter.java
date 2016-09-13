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

/**
 * Created by akshayacharya on 31/07/16.
 */
public class IndividualAddNewServicesListAdapter extends BaseAdapter {
    private Activity activity;
    List<String> servicesArrayList = Collections.EMPTY_LIST;
    private static LayoutInflater inflater = null;
    public Resources res;

    public IndividualAddNewServicesListAdapter(ArrayList<String> servicesArrayList, Activity context) {
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
            vi = inflater.inflate(R.layout.new_services_list_row_layout, null);

            /****** View Holder Object to contain faultfixlist_item.xml file elements ******/

            holder = new ViewHolder();
            holder.txtServiceName = (TextView) vi.findViewById(R.id.txtServiceName);
            vi.setTag(holder);
        }
        else {
            holder = (ViewHolder) vi.getTag();
        }

        holder.txtServiceName.setText(servicesArrayList.get(position));

        return vi;
    }


    public static class ViewHolder {
        TextView txtServiceName;
    }

}
