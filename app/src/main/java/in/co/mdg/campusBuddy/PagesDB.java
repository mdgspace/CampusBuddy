package in.co.mdg.campusBuddy;

import android.provider.BaseColumns;

/**
 * Created by rc on 13/5/15.
 */


public final class PagesDB {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public PagesDB() {}

    /* Inner class that defines the table contents */
    public static abstract class PagesEntry implements BaseColumns {

        public static final String TABLE_NAME = "liked_pages";
        public static final String COLUMN_NAME_Pages_ID = "page_ID";
        public static final String COLUMN_NAME_Page_name = "page_name";




    }
}
