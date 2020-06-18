package test.com.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

public class Adapter_Dashboard extends RecyclerView.Adapter<Adapter_Dashboard.Dashboard_ViewHolder> {
    Cursor data;
    DatabaseHelper database;

    public Adapter_Dashboard(Cursor data, DatabaseHelper database){
        this.database = database;
        this.data = data;

    }
    @NonNull
    @Override
    public Dashboard_ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.list_item_dashboard,viewGroup,false);
        return new Dashboard_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Dashboard_ViewHolder holder, final int i) {
        data.moveToPosition(i);
        // Getting data from Column 1 and Column 2
        String task = data.getString(0);
        String status = data.getString(1);
        holder.txtTask.setText(task);
        if(status.equals("true")){
            holder.switchStatus.setChecked(true);
            holder.txtTask.setPaintFlags(holder.txtTask.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else{
            holder.switchStatus.setChecked(false);
        }

        holder.switchStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                data.moveToPosition(i);
                String task = data.getString(0);
                String status = data.getString(1);
                if(status.equals("true")) {
                    status = "false";
                    holder.txtTask.setPaintFlags(holder.txtTask.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
                else{
                    status ="true";
                    holder.txtTask.setPaintFlags(holder.txtTask.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                database.updateData(task,status);
                //Adapter_Dashboard.this.runOnUiThread()
                data = database.getAllData();

               // notifyDataSetChanged();
                }

            });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.moveToPosition(i);
                String task = data.getString(0);
                database.deleteData(task);
                data = database.getAllData();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.getCount();
    }

    public class Dashboard_ViewHolder extends RecyclerView.ViewHolder  {
        TextView txtTask;
        Button btnDelete;
       // private boolean onBind;
        SwitchCompat switchStatus;
        public Dashboard_ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTask = itemView.findViewById(R.id.txtTask);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            switchStatus = itemView.findViewById(R.id.switchStatus);
          //  switchStatus.setOnCheckedChangeListener(this);
        }

//        @Override
//        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//            if(!onBind){
//
//            }
//        }
    }
}
