package escapadetechnologies.com.realmtodolistexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements TaskAdapter.ItemDataListener{

    private Realm realm;
    EditText taskNameEdittext;
    Button addTask,deleteAll,nextTab;
    ListView task_list;
    BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        taskNameEdittext = findViewById(R.id.taskNameEdittext);
        addTask = findViewById(R.id.addTask);
        task_list = findViewById(R.id.task_list);
        deleteAll = findViewById(R.id.deleteAll);
        nextTab = findViewById(R.id.nextTab);

        realm = Realm.getDefaultInstance();


        RealmResults<Task> tasks = realm.where(Task.class).findAll();

        final TaskAdapter adapter = new TaskAdapter(tasks,this,this);


        task_list.setAdapter(adapter);


        /*receiver= new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    deleteAll.setVisibility(View.VISIBLE);
                }
            }
        };*/

        nextTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,MovieRecyclerViewActivity.class));
            }
        });



        task_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                final Task task = (Task) adapterView.getAdapter().getItem(position);
                final EditText taskEditText = new EditText(MainActivity.this);
                taskEditText.setText(task.getName());
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Edit Task")
                        .setView(taskEditText)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // TODO: 5/4/17 Save Edited Task
                            }
                        })
                        .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // TODO: 5/4/17 Delete Task
                            }
                        })
                        .create();
                dialog.show();

            }
        });

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taskNameEdittext.getText().toString().isEmpty()){
                    taskNameEdittext.setError("Please enter the task");
                    taskNameEdittext.requestFocus();
                }else {
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {

                            realm.createObject(Task.class,UUID.randomUUID().toString()).setName(taskNameEdittext.getText().toString());
                            /*try {
                                taskNameEdittext.setText("");
                            }catch (Exception e){
                                e.printStackTrace();
                            }*/


                            /*final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Do something after 5s = 5000ms
                                    taskNameEdittext.setText("");
                                }
                            }, 2000);*/



                        }

                    });

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            taskNameEdittext.setText("");
                        }
                    }, 500);


                }
            }
        });

        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //realm.where(Task.class).equalTo("done",true).findAll().deleteAllFromRealm();
                deleteAllDone();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        deleteAll.setVisibility(View.GONE);
                    }
                }, 1000);
            }
        });

    }

    private void deleteAllDone() {
        try {
            RealmResults<Task> tasks = realm.where(Task.class).findAll();

            realm.beginTransaction();

            tasks.deleteAllFromRealm();

            realm.commitTransaction();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void changeTaskDone(final String taskId) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                try {
                    Task task = realm.where(Task.class).equalTo("id",taskId).findFirst();
                    task.setDone(!task.isDone());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    public void  deleteTaskDone(final String taskId){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                try {
                    realm.where(Task.class).equalTo("id",taskId).findFirst().deleteFromRealm();

                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        });
    }

    /*@Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),new IntentFilter("data_notNull"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver,new IntentFilter("data_notNull"));
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public void sendData(String item) {
        if (item.equals("enableButton")) {
            deleteAll.setVisibility(View.VISIBLE);
        }else if (item.equals("disableButton")){
            deleteAll.setVisibility(View.GONE);
        }
    }
}
