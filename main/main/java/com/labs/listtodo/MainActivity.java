package com.labs.listtodo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    int priority = -1;
    String title = "";
    String description = "";
    TaskBean taskBean;
    Boolean active;
    RadioGroup RBGroup;

    EditText titleTxt;
    EditText descriptionTxt;
    TaskBean focusMe;

    Toast myToast;
    int numDelete = 0;

    public static CustomTaskAdapter adapter;
    static DBManager db;

    static final ArrayList<TaskBean> tasks = new ArrayList<TaskBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add Database
        db = new DBManager(this);

        // Custom Adapter
        adapter = new CustomTaskAdapter(this, tasks);
//      deleteDatabase(db.TABLE_NAME);

        populate();
//        populateSQL();

//        Log.d("INSERT", "Inserting into database");
//        Log.d("COUNT", db.getTotalTasks() + "");

        // Populating List View
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        /**
         * Make sure to comment this block out!!!
         */
        db.addTask(new TaskBean("Lab 1", "To create multiple pages using new activity and toast.  Create a color changer and a tip calculator as well!", "Feb 20", 3, true));
        db.addTask(new TaskBean("Lab 2", "Create a BMI calculator, give knowledge to people about the type of body they have and amount of bodyfat percantage they're at!", "Feb 23", 3, false));
        db.addTask(new TaskBean("Lab 3", "Create a task list showing the user their tasks they need to do,  and have finished.  This software is able to create new task, mark them as finished and able to remove the finished task.", "Feb 24", 3, false));
        db.addTask(new TaskBean("Clean house", "Put away dishes, take trash out.", "Feb 27", 1, true));
        db.addTask(new TaskBean("Take Doggo out for walk", "Take Doggo out for walk! :D", "Feb 27", 2, true));

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

    public void popup(final View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        // Get the layout inflater
        final LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        final View containerView = inflater.inflate(R.layout.newtask, null);
        Toast.makeText(
                getApplicationContext(),
                title + "Done....1",
                Toast.LENGTH_LONG).show();

        builder.setView(containerView);
        builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                try {
                    title = ((EditText) containerView.findViewById(R.id.title_id)).getText().toString();
                    description = ((EditText) containerView.findViewById(R.id.description_id)).getText().toString();
                    active = false;

                    RadioButton rb_l = containerView.findViewById(R.id.rbt_low_id);
                    RadioButton rb_m = containerView.findViewById(R.id.rbt_medium_id);
                    RadioButton rb_h = containerView.findViewById(R.id.rbt_high_id);

                    if (rb_l.isChecked()) {
                        priority = 1;
                    } else if (rb_m.isChecked()) {
                        priority = 2;
                    } else if (rb_h.isChecked()) {
                        priority = 3;
                    }

                    if (!title.equals("") && !description.equals("") && priority != -1) {
                        Date d = new Date();

                        db.addTask(new TaskBean(title, description, d.toString(), priority, active));
                        String r = "Task: " + title + " has been created successfully";
                        toastMethod(r);
                        populate();

                    } else {
                        Toast.makeText(
                                getApplicationContext(),
                                "Title, description or priority is wrong!!" + "Title: " + title + "Description: " + description + "Priority: " + priority + "Active: " + active,
                                Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(
                            getApplicationContext(),
                            "Exception gone wrong...  Please figure it out!" + "Title: " + title + "Description: " + description + "Priority: " + priority + "Active: " + active,
                            Toast.LENGTH_LONG).show();
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
        numDelete = 0;
        numDelete = taskBean.getId();
        numDelete = 0;
        numDelete = taskBean.getId();
        title = taskBean.getTitle();


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

                                    db.deleteTask(numDelete);

                                    String r = "Item " + title + "  deleted successfully";
                                    toastMethod(r);

                                    populate();
                                    populateSQL();

                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // CANCEL
                            String r = "Item Not Deleted";
                            toastMethod(r);
                            dialog.dismiss();
                        }
                    });
                    builder1.show();
                    // Create the AlertDialog object and return it
                } catch (Exception ex) {

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

    public void yesCreateMethod(View v) {

        int k = adapter.getPosition(focusMe);
        db.deleteTask(k + 1);

        String r = "Item Deleted Successful";
        toastMethod(r);

        populateSQL();
        populate();

    }

    public void toastMethod(String k) {
        if (myToast != null) {
            myToast.cancel();
        }
        myToast = Toast.makeText(getApplicationContext(), k, Toast.LENGTH_LONG);
        myToast.show();
    }

    public void Adding(View v) {

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        String formattedDate = df.format(c);


        RBGroup.indexOfChild(v);
        int selectId = RBGroup.getCheckedRadioButtonId();
        RadioButton rb1 = (RadioButton) RBGroup.findViewById(selectId);

        String title = titleTxt.getText().toString();
        String description = descriptionTxt.getText().toString();
        String priorityTxt = rb1.getText().toString();
        int completed = 0;
        int color = 0;

        if (priorityTxt.equals("Low")) {
            color = R.drawable.square_green;
            priority = 1;
        } else if (priorityTxt.equals("Medium")) {
            color = R.drawable.square_yellow;
            priority = 2;
        } else if (priorityTxt.equals("High")) {
            color = R.drawable.square_red;
            priority = 3;
        }

        db.addTask(new TaskBean(title, description, formattedDate, priority, active));

        String r = "Task: " + title + " has been created successfully";
        toastMethod(r);
        populate();
    }

    public void deleteDatabase(View v) {
        db.deleteDatabase(db.TABLE_NAME);
        populate();
    }

    public void populateSQL() {
        db.deleteDatabase(db.TABLE_NAME);
        for (int x = 0; x < tasks.size(); x++) {
            TaskBean tempTask = tasks.get(x);
            db.addTask(tempTask);
        }
        adapter.notifyDataSetChanged();
    }

    public static void populate() {
        tasks.clear();
        for (int x = 0; x < db.getAllTasks().size(); x++) {
            tasks.add(db.getAllTasks().get(x));
        }
        adapter.notifyDataSetChanged();
    }


}
