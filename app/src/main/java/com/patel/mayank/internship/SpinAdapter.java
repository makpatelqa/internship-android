package com.patel.mayank.internship;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Harsh on 10/20/2017.
 */

public class SpinAdapter extends BaseAdapter {

    ArrayList<String> catArraylist;
    Context c;

    LayoutInflater inflater;

    public SpinAdapter(ArrayList<String> catArraylist, Context c) {
        this.catArraylist = catArraylist;
        this.c = c;
    }

    @Override
    public int getCount() {
        return catArraylist.size();
    }

    @Override
    public Object getItem(int position) {
        return catArraylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
        {
            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.spinnercontent,parent,false);
        }

        TextView txt_v = (TextView) convertView.findViewById(R.id.txt_spincon);

        txt_v.setText(catArraylist.get(position).toString());

        return convertView;
    }
}
