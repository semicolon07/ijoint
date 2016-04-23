package com.kanmanus.kmutt.sit.ijoint.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.kanmanus.kmutt.sit.ijoint.models.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskDataSource {
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	
	private String TABLE = MySQLiteHelper.TABLE_TASK;
	private String PRIMARY_KEY = MySQLiteHelper.COL_TASK_ID;
	
	private String[] allColumns = { MySQLiteHelper.COL_TASK_ID,
			MySQLiteHelper.COL_TASK_PID, MySQLiteHelper.COL_TASK_DATE,
			MySQLiteHelper.COL_TASK_SIDE, MySQLiteHelper.COL_TASK_TARGET_ANGLE,
			MySQLiteHelper.COL_TASK_NUMBER_OF_ROUND, MySQLiteHelper.COL_TASK_IS_ABF,
            MySQLiteHelper.COL_TASK_IS_SYNCED, MySQLiteHelper.COL_TASK_PERFORM_DATETIME };
	
	public TaskDataSource(Context context) {
	    dbHelper = new MySQLiteHelper(context);
	  }

	public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	public void close() {
	    dbHelper.close();
	  }

    public Task create(String tid, String pid, String date, String side, String target_angle,
                     String number_of_round, String is_abf, String is_synced, String perform_datetime) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COL_TASK_ID, tid);
        values.put(MySQLiteHelper.COL_TASK_PID, pid);
        values.put(MySQLiteHelper.COL_TASK_DATE, date);
        values.put(MySQLiteHelper.COL_TASK_SIDE, side);
        values.put(MySQLiteHelper.COL_TASK_TARGET_ANGLE, target_angle);
        values.put(MySQLiteHelper.COL_TASK_NUMBER_OF_ROUND, number_of_round);
        values.put(MySQLiteHelper.COL_TASK_IS_ABF, is_abf);
        values.put(MySQLiteHelper.COL_TASK_IS_SYNCED, is_synced);
        values.put(MySQLiteHelper.COL_TASK_PERFORM_DATETIME, perform_datetime);

        database.insert(TABLE, null, values);

        return this.get(tid);
    }

    public void edit(String tid, String pid, String date, String side, String target_angle,
                   String number_of_round, String is_abf) {
        database.execSQL("UPDATE " + TABLE + " SET "
            + MySQLiteHelper.COL_TASK_PID + " = '" + pid + "', "
            + MySQLiteHelper.COL_TASK_DATE + " = '" + date + "', "
            + MySQLiteHelper.COL_TASK_SIDE + " = '" + side + "', "
            + MySQLiteHelper.COL_TASK_TARGET_ANGLE + " = '" + target_angle + "', "
            + MySQLiteHelper.COL_TASK_NUMBER_OF_ROUND + " = '" + number_of_round + "', "
            + MySQLiteHelper.COL_TASK_IS_ABF + " = '" + is_abf + "'"
            + " WHERE " + PRIMARY_KEY + " = '" + tid + "'");
    }

    public void updateIsSynced(String tid, String isSynced) {

        database.execSQL("UPDATE " + TABLE + " SET "
            + MySQLiteHelper.COL_TASK_IS_SYNCED + " = '" + isSynced + "'"
            + " WHERE " + PRIMARY_KEY + " = '" + tid + "'");
    }

    public void updatePerformDateTime(String tid, String performDateTime) {

        database.execSQL("UPDATE " + TABLE + " SET "
            + MySQLiteHelper.COL_TASK_PERFORM_DATETIME + " = '" + performDateTime + "'"
            + " WHERE " + PRIMARY_KEY + " = '" + tid + "'");
    }

    public void delete(Task elem) {
        String tid = elem.tid;

        database.delete(TABLE, PRIMARY_KEY + " = '" + tid + "'", null);
    }

    public List<Task> getAll(String pid) {
        List<Task> elems = new ArrayList<Task>();

        Cursor cursor = database.query(TABLE, allColumns, MySQLiteHelper.COL_TASK_PID + " = '" + pid + "'", null, null, null, PRIMARY_KEY + " DESC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Task elem = cursorToElement(cursor);
            elems.add(elem);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return elems;
    }

    public Task get(String tid){
      Cursor cursor = database.query(TABLE, allColumns,
              PRIMARY_KEY + " = '" + tid + "'",
              null, null, null, null);

      cursor.moveToFirst();
      if (!cursor.isAfterLast()){
          Task elem = cursorToElement(cursor);

          return elem;
      }
      else
          return null;
    }

    public List<Task> getFinishedTasks(){
        List<Task> elems = new ArrayList<Task>();

        Cursor cursor = database.query(TABLE, allColumns, MySQLiteHelper.COL_TASK_IS_SYNCED + " = 'f'", null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Task elem = cursorToElement(cursor);
            elems.add(elem);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return elems;
    }

    public void delete(long id){
      database.execSQL("delete from " + TABLE + " where _id = " + id);
    }

    private Task cursorToElement(Cursor cursor) {
        Task elem = new Task(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));

        return elem;
    }
}
