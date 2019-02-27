package com.labs.listtodo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomTaskAdapter extends ArrayAdapter<TaskBean> {

    public CustomTaskAdapter(Context context, ArrayList<TaskBean> tasks) {
        super(context, 0, tasks);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TaskBean taskbean = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row, parent, false);
        }

        // Lookup view for data population
        CheckBox checkBox = convertView.findViewById(R.id.checkBox);
        TextView title = convertView.findViewById(R.id.title_id);
        TextView description = convertView.findViewById(R.id.description_id);
        TextView date = convertView.findViewById(R.id.date_id);
        ImageView priority = convertView.findViewById(R.id.priority_id);

        // Populate the data into the template view using the data object
        title.setText(taskbean.getTitle());
        description.setText(taskbean.getDescription());

        switch (taskbean.getPriority()) {
            case 1:
                priority.setImageResource(R.drawable.square_green);
                break;

            case 2:
                priority.setImageResource(R.drawable.square_yellow);
                break;

            case 3:
                priority.setImageResource(R.drawable.square_red);
                break;
        }

        date.setText(taskbean.getDate().toString());
        checkBox.setChecked(taskbean.isActive());

        // Return the completed view to render on screen
        return convertView;
    }

}