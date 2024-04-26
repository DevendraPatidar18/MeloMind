
package com.example.melomind;
import android.content.Context;
import android.telephony.TelephonyCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

class CustomArrayAdapter extends ArrayAdapter<File> {

    final private ArrayList<File> dataList;
    private Context context;


    public CustomArrayAdapter(Context context, ArrayList<File> dataList) {
        super(context,0, dataList);
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list, parent, false);

            // Create a ViewHolder and store references to your views
            viewHolder = new ViewHolder();
            viewHolder.textView = convertView.findViewById(R.id.songtext);
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled; retrieve the ViewHolder
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Get the data item for this position
        File item = dataList.get(position);

        // Populate the data into the template view using the ViewHolder
        viewHolder.textView.setText(item.getName());

        return convertView;
    }

    // ViewHolder pattern to improve ListView performance
    private static class ViewHolder {
        TextView textView;
    }
}