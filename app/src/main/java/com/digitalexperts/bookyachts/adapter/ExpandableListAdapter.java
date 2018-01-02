package com.digitalexperts.bookyachts.adapter;

/**
 * Created by Irtizaa on 1/11/2017.
 */


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalexperts.bookyachts.R;
import com.digitalexperts.bookyachts.customClasses.AppConstants;
import com.digitalexperts.bookyachts.interfaces.ShowDataInterface;
import com.digitalexperts.bookyachts.models.SubFacilityModel;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;


public class ExpandableListAdapter extends BaseExpandableListAdapter
{

    @BindView(R.id.switchFacilities)
    Switch switchFacilities;
    private Activity activity;
    // child data in format of header title, child title
    private HashMap<String, ArrayList<SubFacilityModel>> listDataChild;

    ShowDataInterface showTotalInterface;
    public ExpandableListAdapter(Activity activity,
                                 HashMap<String, ArrayList<SubFacilityModel>> listChildData) {
        this.activity = activity;
        showTotalInterface = (ShowDataInterface) activity;
        this.listDataChild = listChildData;
    }

    @Override
    public final SubFacilityModel getChild(int groupPosition, int childPosititon)
    {

        return listDataChild.get(listDataChild.keySet().toArray()[groupPosition].toString()).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public final View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent)
    {

        SubFacilityModel data = getChild(groupPosition, childPosition);




        if (convertView == null)
        {
            LayoutInflater infalInflater = (LayoutInflater) this.activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        final Switch switchFacilities = (Switch) convertView
                .findViewById(R.id.switchFacilities);



        final String key = AppConstants.listDataChild.keySet().toArray()[groupPosition].toString();
//        if(AppConstants.selectedFacilities.contains(data.getSubFacilityName()))
//        {
//            switchFacilities.setChecked(true);
//        }



        switchFacilities.setText(data.getSubFacilityName() + "   "+data.getSubFacilityPrice());

        switchFacilities.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
            {
                SubFacilityModel data = getChild(groupPosition, childPosition);
                if (isChecked)
                {

                    if (AppConstants.selectedFacilities.contains(data.getSubFacilityName()))
                    {
                        Toast.makeText(activity, "Already Added", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        AppConstants.subFacilityTotal += Float.parseFloat(data.getSubFacilityPrice());
                        showTotalInterface.showData(AppConstants.subFacilityTotal+"");
                        AppConstants.listDataChild.get(key).get(childPosition).setSelected(true);
                        AppConstants.selectedFacilities.add(data.getSubFacilityName());
                    }
                }
                else
                {
                    if (AppConstants.selectedFacilities.contains(data.getSubFacilityName()))
                    {
                        AppConstants.selectedFacilities.remove(data.getSubFacilityName());
                        AppConstants.subFacilityTotal -= Float.parseFloat(data.getSubFacilityPrice());
                        showTotalInterface.showData(AppConstants.subFacilityTotal+"");
                        AppConstants.listDataChild.get(key).get(childPosition).setSelected(false);
                    }
                }
            }
        });


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return listDataChild.get(listDataChild.keySet().toArray()[groupPosition].toString()).size();
    }

    @Override
    public final Object getGroup(int groupPosition)
    {
        return listDataChild.keySet().toArray()[groupPosition].toString();
    }

    @Override
    public int getGroupCount()
    {
        return listDataChild.keySet().toArray().length;
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public final View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent)
    {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null)
        {
            LayoutInflater infalInflater = (LayoutInflater) this.activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }
        TextView tvListHeader_listGroup_layout = (TextView) convertView
                .findViewById(R.id.tvListHeader);

        TextView tvTotalItems_listGroup_layout = (TextView) convertView
                .findViewById(R.id.tvTotalItems);

        // tvListHeader_listGroup_layout.setTypeface(null, Typeface.BOLD);
        tvListHeader_listGroup_layout.setText(headerTitle);

        int itemSize = listDataChild.get(listDataChild.keySet().toArray()[groupPosition].toString()).size();

        if(itemSize > 1)
        {
            tvTotalItems_listGroup_layout.setText(itemSize + " Sub Facilities");
        }
        else
        {
            tvTotalItems_listGroup_layout.setText(itemSize + " Sub Facility");
        }


        return convertView;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }
}
