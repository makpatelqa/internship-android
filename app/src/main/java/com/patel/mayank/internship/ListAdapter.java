package com.patel.mayank.internship;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Harsh on 10/17/2017.
 */

public class ListAdapter extends BaseAdapter {

    ArrayList<Jobs> arrayjob;
    Context c;

    LayoutInflater layoutInflater;

    public ListAdapter(ArrayList<Jobs> arrayjob, Context c) {
        this.arrayjob = arrayjob;
        this.c = c;
    }

    @Override
    public int getCount() {
        return arrayjob.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayjob.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(layoutInflater ==  null)
        {
            layoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.listcontents,parent,false);
        }

        TextView txt_tit = (TextView) convertView.findViewById(R.id.txt_name);
        TextView txt_loc = (TextView) convertView.findViewById(R.id.txt_loc);

        txt_tit.setText(arrayjob.get(position).getTitle());
        txt_loc.setText(arrayjob.get(position).getLocation());


        return convertView;
    }
}
