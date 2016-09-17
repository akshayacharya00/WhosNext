package appsshoppy.com.whosnext.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import appsshoppy.com.whosnext.R;
import appsshoppy.com.whosnext.model.ServiceCategory;

public class ServiceCategoryAdapter extends ArrayAdapter<ServiceCategory> {

  public ArrayList<ServiceCategory> categoryList;
    private Context context;

  public ServiceCategoryAdapter(Context context, int textViewResourceId,
                         ArrayList<ServiceCategory> categoryList) {
   super(context, textViewResourceId, categoryList);
   this.categoryList = new ArrayList<ServiceCategory>();
   this.categoryList.addAll(categoryList);
      this.context = context;
  }

  private class ViewHolder {
   TextView code;
   CheckBox name;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {

   ViewHolder holder = null;
   Log.v("ConvertView", String.valueOf(position));

   if (convertView == null) {
   LayoutInflater vi = (LayoutInflater)context.getSystemService(
     Context.LAYOUT_INFLATER_SERVICE);
   convertView = vi.inflate(R.layout.service_category_list_row, null);

   holder = new ViewHolder();
   holder.code = (TextView) convertView.findViewById(R.id.txtCategory);
   holder.name = (CheckBox) convertView.findViewById(R.id.chkCategory);
   convertView.setTag(holder);

    holder.name.setOnClickListener( new View.OnClickListener() {  
     public void onClick(View v) {  
      CheckBox cb = (CheckBox) v ;
      ServiceCategory serviceCategory = (ServiceCategory) cb.getTag();
      serviceCategory.setSelected(cb.isChecked());
     }  
    });  
   } 
   else {
    holder = (ViewHolder) convertView.getTag();
   }

   ServiceCategory serviceCategory = categoryList.get(position);
   holder.code.setText(serviceCategory.getCategory());
   holder.name.setChecked(serviceCategory.isSelected());
   holder.name.setTag(serviceCategory);

   return convertView;

  }

 }