package test.com.todolist;

import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {
    Button btnMakeNewTask,  btnCancel;
    EditText txtTaskName , edtSearch;
    AlertDialog alertDialog;
    DatabaseHelper database;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtSearch = findViewById(R.id.edtSearch);
        recyclerView = findViewById(R.id.recyclerView);
        database = new DatabaseHelper(this);

        //Getting data
        Cursor data = database.getAllData();

        //displaying data in recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new Adapter_Dashboard(data,database));
        //For search
        search();
    }
    public void search(){
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Cursor data = database.search(charSequence.toString());
                recyclerView.setAdapter(new Adapter_Dashboard(data,database));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.dashboard_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addTask:
                // User chose the "Add Task" item, show the add task UI...
                showAddTaskDialog();

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    public void showAddTaskDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View addTaskDialog = getLayoutInflater().inflate(R.layout.add_task_dialog,null);

        builder.setView(addTaskDialog);
        alertDialog = builder.create();
        alertDialog.show();

        txtTaskName = addTaskDialog.findViewById(R.id.edtTaskName);
        btnMakeNewTask = addTaskDialog.findViewById(R.id.btnSubmittask);
        btnCancel = addTaskDialog.findViewById(R.id.btnCancel);

        btnMakeNewTask.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSubmittask:
                String taskName = txtTaskName.getText().toString();
                boolean isInserted = database.insertData(taskName);
                if(isInserted) {
                    Cursor data = database.getAllData();

                    recyclerView.setAdapter(new Adapter_Dashboard(data,database));
                    Toast.makeText(Dashboard.this, "Data Insterted", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(Dashboard.this,"Data Instertion Failed", Toast.LENGTH_LONG).show();
                alertDialog.dismiss();
                break;
            case R.id.btnCancel:
                alertDialog.dismiss();
                break;
        }
    }
}
