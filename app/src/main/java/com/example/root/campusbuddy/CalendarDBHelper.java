
        package com.example.root.campusbuddy;


/**
 * Created by root on 14/5/15.
 */
        /*
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.preference.PreferenceManager;


public class CalendarDBHelper {
    private static final String DATABASE_NAME = "mycal.db";
    private static final String TABLE_NAME = "mainTable";
    private static final String KEY_ID = "_id";
    private toDoDBOpenHelper dbHelper;
    private SQLiteDatabase db;
    private final Context context;

    public CalendarDBHelper(Context _context)
    {
        context = _context;
        dbHelper = new toDoDBOpenHelper(context,DATABASE_NAME,null,1);
    }
    public void open() throws SQLiteException{

        try
        {
            db = dbHelper.getWritableDatabase();
        }catch(SQLiteException e)
        {
            e.printStackTrace();
            db = dbHelper.getReadableDatabase();
        }
    }
    public void dropDatabase(){
        context.deleteDatabase(DATABASE_NAME);
    }
    public Cursor queryDatabase()
    {
        String[] result_columns = new String[]{KEY_ID,"_dtstart","_dtend","_dtmodified","_attendee",
                "_uid","_dtcreated","_dtstamp","_desc","_location","_status","_schoolid","_summary","_allday"};
        Cursor allRows = db.query(true,TABLE_NAME, result_columns, null, null, null,
                null,null,null);

        return allRows;
    }
    public Cursor queryRow(String where)
    {
        String id="";
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if(prefs.getBoolean("cal1", false))
            id = "'"+prefs.getString("cal1ID", "")+"'";
        if(prefs.getBoolean("cal2", false))
            id += ",'"+prefs.getString("cal2ID", "")+"'";
        if(prefs.getBoolean("cal3", false))
            id += ",'"+prefs.getString("cal3ID", "")+"'";

        if(id.startsWith(","))
            id=id.substring(1);

        if(id.length()<1)
            id="''";

        where += " AND _schoolid IN ("+id+")";

        String[] result_columns = new String[]{KEY_ID,"_dtstart","_dtend","_dtmodified","_attendee",
                "_uid","_dtcreated","_dtstamp","_desc","_location","_status","_summary","_schoolid","_allday"};
        Cursor allRows = db.query(true,TABLE_NAME, result_columns, where, null,
                null, null,"_dtstart",null);
        return allRows;
    }

    public long insert(ContentValues contentValues)
    {
        return db.insert(TABLE_NAME, null, contentValues);
    }
    public boolean update(ContentValues contentValues,String where)
    {
        return db.update(TABLE_NAME, contentValues, where, null)>0;
    }

    public boolean remove(String where)
    {
        where = KEY_ID+"="+where;
        return db.delete(TABLE_NAME, where, null)>0;
    }
    public boolean removeSchool(String where)
    {
        where = "_schoolid='"+where+"'";
        return db.delete(TABLE_NAME, where, null)>0;
    }
    public void close()
    {
        db.close();
    }
    private static class toDoDBOpenHelper extends SQLiteOpenHelper{

        private static final String DATABASE_CREATE = "create table if not exists "+TABLE_NAME+" " +
                "("+KEY_ID+" integer primary key autoincrement, _dtstart integer not null, _dtend integer not null," +
                "_dtmodified integer, _uid text,_dtcreated integer,_dtstamp integer," +
                "_desc text,_location text,_status text,_schoolid text,_attendee text,_summary text,_allday boolean DEFAULT 0);";

        public toDoDBOpenHelper(Context context, String name,
                                CursorFactory factory, int version) {
            super(context, name, factory, version);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL("DROP TABLE IF EXISTS "+DATABASE_NAME);
            onCreate(db);
        }

    }
}
*/

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

        /**
 * Created by rc on 13/5/15.
 */


public class CalendarDBHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "EventsReader.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + CalendarDB.CalendarEntry.TABLE_NAME + " (" +


                    CalendarDB.CalendarEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    CalendarDB.CalendarEntry.COLUMN_NAME_DAY + INT_TYPE + COMMA_SEP +
                    CalendarDB.CalendarEntry.COLUMN_NAME_MONTH + INT_TYPE + COMMA_SEP +
                    CalendarDB.CalendarEntry.COLUMN_NAME_YEAR + INT_TYPE + COMMA_SEP +
                    CalendarDB.CalendarEntry.COLUMN_NAME_STARTHOUR + INT_TYPE + COMMA_SEP +
                    CalendarDB.CalendarEntry.COLUMN_NAME_STARTMIN + INT_TYPE + COMMA_SEP +
                    CalendarDB.CalendarEntry.COLUMN_NAME_ENDHOUR + INT_TYPE + COMMA_SEP +
                    CalendarDB.CalendarEntry.COLUMN_NAME_ENDMIN + INT_TYPE + COMMA_SEP +
                    CalendarDB.CalendarEntry.COLUMN_NAME_DETAIL + TEXT_TYPE + COMMA_SEP +
                    CalendarDB.CalendarEntry.COLUMN_NAME_VENUE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + CalendarDB.CalendarEntry.TABLE_NAME;

    private static CalendarDBHelper instance;


    private CalendarDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized CalendarDBHelper getInstance(Context context)
    {
        if (instance == null)
            instance = new CalendarDBHelper(context);

        return instance;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    public void putInformation(SQLiteDatabase db,  String title, int day, int month, int year, int starthour, int startmin, int endhour, int endmin, String detail,  String venue ){

        ContentValues values = new ContentValues();
        values.put(CalendarDB.CalendarEntry.COLUMN_NAME_TITLE, title);
        values.put(CalendarDB.CalendarEntry.COLUMN_NAME_DAY, day);
        values.put(CalendarDB.CalendarEntry.COLUMN_NAME_MONTH, month);
        values.put(CalendarDB.CalendarEntry.COLUMN_NAME_YEAR, year);
        values.put(CalendarDB.CalendarEntry.COLUMN_NAME_STARTHOUR, starthour);
        values.put(CalendarDB.CalendarEntry.COLUMN_NAME_STARTMIN, startmin);
        values.put(CalendarDB.CalendarEntry.COLUMN_NAME_ENDHOUR, endhour);
        values.put(CalendarDB.CalendarEntry.COLUMN_NAME_ENDMIN, endmin);
        values.put(CalendarDB.CalendarEntry.COLUMN_NAME_DETAIL, detail);
        values.put(CalendarDB.CalendarEntry.COLUMN_NAME_VENUE, venue);


        db.insert(
                CalendarDB.CalendarEntry.TABLE_NAME,
                null,
                values);



// http://developer.android.com/training/basics/data-storage/databases.html
// --> on this link, COLUMN_NAME_NULLABLE is used instead of null, but it is giving error here. What is the reason ??

    }

}