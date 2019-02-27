package com.labs.listtodo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    CustomTaskAdapter adapter;
    int priority = -1;
    String title = "";
    String description = "";
    TaskBean taskBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Construct the data source
        ArrayList<TaskBean> tasks = new ArrayList<TaskBean>();

        // Create the adapter to convert the array to views
        adapter = new CustomTaskAdapter(this, tasks);

        // Attach the adapter to a ListView
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        adapter.add(new TaskBean(true, "Lab 1", "To create multiple pages using new activity and toast.  Create a color changer and a tip calculator as well!", "Feb 20", 3));
        adapter.add(new TaskBean(true, "Lab 2", "Create a BMI calculator, give knowledge to people about the type of body they have and amount of bodyfat percantage they're at!", "Feb 23", 3));
        adapter.add(new TaskBean(false, "Lab 3", "Create a task list showing the user their tasks they need to do,  and have finished.  This software is able to create new task, mark them as finished and able to remove the finished task.", "Feb 24", 3));
        adapter.add(new TaskBean(false, "Clean house", "Put away dishes, take trash out.", "Feb 27", 1));
        adapter.add(new TaskBean(false, "Take Doggo out for walk", "Take Doggo out for walk! :D", "Feb 27", 2));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TaskBean taskB = adapter.getItem(i);

                if (taskB.isActive() == false) {
                    adapter.getItem(i).setActive(true);
                    Toast.makeText(getApplicationContext(), i + ": " + taskB.getTitle() + " is done!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    adapter.getItem(i).setActive(false);
                    Toast.makeText(getApplicationContext(), i + ": " + taskB.getTitle() + " is not done!",
                            Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int k, long l) {
                deleteInfo(view, k);
                return true;
            }
        });
    }

    public void popup(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        // Get the layout inflater
        final LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        final View containerView = inflater.inflate(R.layout.newtask, null);


        builder.setView(containerView);
        builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                try {

                    title = ((EditText) containerView.findViewById(R.id.title_id)).getText().toString();
                    description = ((EditText) containerView.findViewById(R.id.description_id)).getText().toString();

                    RadioButton rb_l = (RadioButton) containerView.findViewById(R.id.rbt_low_id);
                    RadioButton rb_m = (RadioButton) containerView.findViewById(R.id.rbt_medium_id);
                    RadioButton rb_h = (RadioButton) containerView.findViewById(R.id.rbt_high_id);

                    if (rb_l.isChecked()) {
                        // priority = R.drawable.square_green;
                        priority = 1;

                    } else if (rb_m.isChecked()) {
                        // priority = R.drawable.square_yellow;

                        priority = 2;
                    } else if (rb_h.isChecked()) {
                        //priority = R.drawable.square_red;
                        priority = 3;
                    }

                    if (!title.equals("") && !description.equals("") && priority != -1) {
                        Date d = new Date();

                        adapter.add(new TaskBean(false, title, description, d.toString(), priority));

                        Toast.makeText(
                                getApplicationContext(),
                                "Done....",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ek) {

                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    public void deleteInfo(View view, int k) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        // Get the layout inflater
        final LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        final View containerView1 = inflater.inflate(R.layout.description, null);

        TextView Title_del = ((TextView) containerView1.findViewById(R.id.title));
        TextView Description_del = ((TextView) containerView1.findViewById(R.id.description));
        ImageView priority_del = ((ImageView) containerView1.findViewById(R.id.priorityy));

        taskBean = adapter.getItem(k);

        Title_del.setText(taskBean.getTitle());
        Description_del.setText(taskBean.getDescription());

        switch (taskBean.getPriority()) {
            case 1:
                priority_del.setImageResource(R.drawable.square_green);
                break;

            case 2:
                priority_del.setImageResource(R.drawable.square_yellow);
                break;

            case 3:
                priority_del.setImageResource(R.drawable.square_red);
                break;
        }

        builder.setView(containerView1);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                try {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                    builder1.setMessage("Confirm?")
                            .setTitle("Confirmation")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    adapter.remove(taskBean);
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(getApplicationContext(), " Deleted!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // CANCEL
                            dialog.dismiss();
                        }
                    });
                    builder1.show();
                    // Create the AlertDialog object and return it
                } catch (Exception ek) {

                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflate = getMenuInflater();
        inflate.inflate(R.menu.menufile, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.activity_id:
                adapter.sort(new Comparator<TaskBean>() {
                    public int compare(TaskBean obj1, TaskBean obj2) {
                        // ## Ascending order
                        return Boolean.compare(obj2.isActive(), obj1.isActive());
                    }
                });
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Sorted according to status.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.pririty_id:
                adapter.sort(new Comparator<TaskBean>() {
                    public int compare(TaskBean obj1, TaskBean obj2) {
                        // ## Ascending order
                        return Integer.compare(obj2.getPriority(), obj1.getPriority());
                    }

                });
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Sorted according to priority.", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
