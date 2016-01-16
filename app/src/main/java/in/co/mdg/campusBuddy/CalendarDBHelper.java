
        package in.co.mdg.campusBuddy;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

        /**
 * Created by rc on 13/5/15.
 */


public class CalendarDBHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "EventsReader.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + CalendarDB.CalendarEntry.TABLE_NAME + " (" +


                    CalendarDB.CalendarEntry.COLUMN_NAME_ID + INT_TYPE + COMMA_SEP +
                    CalendarDB.CalendarEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    CalendarDB.CalendarEntry.COLUMN_NAME_DAY + INT_TYPE + COMMA_SEP +
                    CalendarDB.CalendarEntry.COLUMN_NAME_MONTH + INT_TYPE + COMMA_SEP +
                    CalendarDB.CalendarEntry.COLUMN_NAME_YEAR + INT_TYPE + COMMA_SEP +
                    CalendarDB.CalendarEntry.COLUMN_NAME_STARTHOUR + INT_TYPE + COMMA_SEP +
                    CalendarDB.CalendarEntry.COLUMN_NAME_STARTMIN + INT_TYPE + COMMA_SEP +
                    CalendarDB.CalendarEntry.COLUMN_NAME_ENDHOUR + INT_TYPE + COMMA_SEP +
                    CalendarDB.CalendarEntry.COLUMN_NAME_ENDMIN + INT_TYPE + COMMA_SEP +
                    CalendarDB.CalendarEntry.COLUMN_NAME_DETAIL + TEXT_TYPE + COMMA_SEP +
                    CalendarDB.CalendarEntry.COLUMN_NAME_VENUE + TEXT_TYPE  + COMMA_SEP +
                    CalendarDB.CalendarEntry.COLUMN_NAME_COLOR + TEXT_TYPE  +
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
    public void putInformation(SQLiteDatabase db,  String title,int ID,  int day, int month, int year, int starthour, int startmin, int endhour, int endmin, String detail,  String venue, String type ){

        ContentValues values = new ContentValues();
        values.put(CalendarDB.CalendarEntry.COLUMN_NAME_ID, ID);
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
        values.put(CalendarDB.CalendarEntry.COLUMN_NAME_COLOR, venue);



        db.insert(
                CalendarDB.CalendarEntry.TABLE_NAME,
                null,
                values);



// http://developer.android.com/training/basics/data-storage/databases.html
// --> on this link, COLUMN_NAME_NULLABLE is used instead of null, but it is giving error here. What is the reason ??

    }
            public ArrayList<HashMap<String, Integer>> fetchNotificationData(int year,int month,int day,int hour,int minute) {
                ArrayList<HashMap<String, Integer>> arr = null;
//        Cursor cursor = ourDatabase.rawQuery("SELECT * FROM " + details_table
//                + " WHERE " + due_date + "='" + date + "'", null);
               SQLiteDatabase db = getReadableDatabase();
                String[] eventList = {
                        CalendarDB.CalendarEntry.COLUMN_NAME_ID,
                        CalendarDB.CalendarEntry.COLUMN_NAME_TITLE,
                        CalendarDB.CalendarEntry.COLUMN_NAME_DAY,
                        CalendarDB.CalendarEntry.COLUMN_NAME_MONTH,
                        CalendarDB.CalendarEntry.COLUMN_NAME_YEAR,
                        CalendarDB.CalendarEntry.COLUMN_NAME_STARTHOUR,
                        CalendarDB.CalendarEntry.COLUMN_NAME_STARTMIN,
                        CalendarDB.CalendarEntry.COLUMN_NAME_ENDHOUR,
                        CalendarDB.CalendarEntry.COLUMN_NAME_ENDMIN,
                        CalendarDB.CalendarEntry.COLUMN_NAME_DETAIL,
                        CalendarDB.CalendarEntry.COLUMN_NAME_VENUE,
                        CalendarDB.CalendarEntry.COLUMN_NAME_VENUE,
                        CalendarDB.CalendarEntry.COLUMN_NAME_COLOR,

                };


                    Cursor cr = db.query(CalendarDB.CalendarEntry.TABLE_NAME, eventList, null, null, null, null, null);
                if (cr != null && cr.getCount() > 0) {
                    arr = new ArrayList<HashMap<String, Integer>>();
                    cr.moveToFirst();
                    while (!cr.isAfterLast()) {

                        if(year==cr.getInt(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_YEAR))&&
                                month==cr.getInt(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_MONTH))&&
                                day==cr.getInt(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_DAY))&&
                                ((hour==(cr.getInt(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_STARTHOUR))-1))||
                                hour==(cr.getInt(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_STARTHOUR))))){
                            HashMap<String, Integer> map = new HashMap<String, Integer>();

                            map.put("Start day", day);
                            map.put("Start month", month);
                            map.put("Start year", year);
                            map.put("Start hour", hour+1);
//                            map.put("Start minute", Integer.toString(cr.getInt(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_STARTMIN))));


                            arr.add(map);
                        }
                        cr.moveToNext();
                    }
                }
                return arr;
            }}

