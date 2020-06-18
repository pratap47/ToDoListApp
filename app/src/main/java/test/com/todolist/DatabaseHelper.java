package test.com.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "ToDoList.db";
    public static final String TABLE_NAME = "Task_List";
    public static final String COL_1 = "Task";
    public static final String COL_2 = "Status";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table " +  TABLE_NAME + " ( " + COL_1 + " TEXT PRIMARY KEY , " + COL_2 +" TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String query = "DROP TABLE IF EXISTS "+ TABLE_NAME;
        db.execSQL(query);
        onCreate(db);
    }
    public boolean insertData(String task){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,task);
        contentValues.put(COL_2,"false");
        long result  = db.insert(TABLE_NAME,null,contentValues);
        if(result!=-1){
            return true;
        }
        return false;
    }
    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query  = "Select * from " + TABLE_NAME;
        Cursor data  = db.rawQuery(query,null);
        return data;
    }
    public Integer deleteData(String task){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,"Task = ?",new String[]{task});
    }
    public boolean updateData(String task, String status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,task);
        contentValues.put(COL_2,status);
        db.update(TABLE_NAME,contentValues,"Task = ?",new String[] {task});
        return true;
    }
    public Cursor search(String string){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * From " + TABLE_NAME + " Where "+ COL_1 + " Like '" + string + "%';" ;
        Cursor data = db.rawQuery(query,null);
        return data;
    }
}
