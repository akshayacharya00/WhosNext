package appsshoppy.com.whosnext.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import appsshoppy.com.whosnext.R;
import appsshoppy.com.whosnext.activities.PickUpDateTimeActivity;
import appsshoppy.com.whosnext.adapters.ServicesListAdapter;
import appsshoppy.com.whosnext.model.Services;

/**
 * Created by akshayacharya on 31/07/16.
 */
public class FragmentServices extends Fragment
{
    private ListView servicesCutList, servicesColourList;
    private Button btnFavourite;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_services,container,false);

        servicesCutList = (ListView) v.findViewById(R.id.servicesListCut);
        servicesColourList = (ListView) v.findViewById(R.id.servicesListColour);
        btnFavourite = (Button) v.findViewById(R.id.btnFavourite);

        //create dummy data
        ArrayList<Services> servicesArrayList = new ArrayList<Services>();
        Services services = new Services();
        services.setServiceName("Hair cut by creative director");
        services.setServicePrice("$54.00");
        services.setServiceTime("1h");
        servicesArrayList.add(services);
        servicesArrayList.add(services);
        servicesArrayList.add(services);


        servicesCutList.setAdapter(new ServicesListAdapter(servicesArrayList,this.getActivity()));
        servicesColourList.setAdapter(new ServicesListAdapter(servicesArrayList,this.getActivity()));

        btnFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FragmentServices.this.getActivity(), PickUpDateTimeActivity.class));
            }
        });

        return v;
    }
}
