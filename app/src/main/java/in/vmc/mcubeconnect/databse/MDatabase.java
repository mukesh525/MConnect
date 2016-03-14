package in.vmc.mcubeconnect.databse;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

import in.vmc.mcubeconnect.model.VisitData;

/**
 * Created by mukesh on 14/3/16.
 */
public class MDatabase {
    private SiteHelper mHelper;
    private SQLiteDatabase mDatabase;
    public static final int ALL = 0;
    public static final int LIKE = 1;
    public static final int OFFER = 2;
    public static final int VISIT = 3;

    public MDatabase(Context context) {
        mHelper = new SiteHelper(context);
        mDatabase = mHelper.getWritableDatabase();
    }

    public void insertAllSites(int table, ArrayList<VisitData> listMovies, boolean clearPrevious) {
        if (clearPrevious) {
            deleteMovies(table);
        }
        String sql = "INSERT INTO " + (table == ALL ? SiteHelper.TABLE_ALL : table == LIKE ? SiteHelper.TABLE_LIKE : table == OFFER ? SiteHelper.TABLE_OFFER : SiteHelper.TABLE_VISIT) + " VALUES (?,?,?,?,?,?,?,?,?,?,?);";
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        for (int i = 0; i < listMovies.size(); i++) {
            VisitData visitData = listMovies.get(i);
            statement.clearBindings();
            statement.bindString(2, visitData.getSitename());
            statement.bindString(3, visitData.getSiteid());
            statement.bindString(4, visitData.getSiteicon());
            statement.bindString(5, visitData.getNumber());
            statement.bindString(6, visitData.getOffer());
            statement.bindString(7, visitData.getBid());
            statement.bindString(8, visitData.getOffer_desc());
            statement.bindString(9, visitData.getSitedesc());
            statement.bindString(10, visitData.isLike() ? "1" : "0");
            statement.bindString(11, visitData.isDelete() ? "1" : "0");
            statement.execute();
        }
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }


    public void deleteMovies(int table) {
        mDatabase.delete(table == ALL ? SiteHelper.TABLE_ALL : table == LIKE ? SiteHelper.TABLE_LIKE : table == OFFER ? SiteHelper.TABLE_OFFER : SiteHelper.TABLE_VISIT, null, null);
    }


    public ArrayList<VisitData> getAllSites(int table) {
        ArrayList<VisitData> listMovies = new ArrayList<>();

        String[] columns = {SiteHelper.COLUMN_UID,
                SiteHelper.COLUMN_SITENAME,
                SiteHelper.COLUMN_SITE_ID,
                SiteHelper.COLUMN_SITEICON,
                SiteHelper.COLUMN_NUMBER,
                SiteHelper.COLUMN_OFFER,
                SiteHelper.COLUMN_BID,
                SiteHelper.COLUMN_OFFER_DESC,
                SiteHelper.COLUMN_SITE_DESC,
                SiteHelper.COLUMN_LIKE,
                SiteHelper.COLUMN_DELETE

        };

      //  Cursor cursor = mDatabase.query((table == ALL ? SiteHelper.TABLE_ALL : table == LIKE ? SiteHelper.TABLE_LIKE : table == OFFER ? SiteHelper.TABLE_OFFER : SiteHelper.TABLE_VISIT), columns, null, null, null, null, null);
        Cursor cursor = mDatabase.query((table == ALL ? SiteHelper.TABLE_ALL : table == LIKE ? SiteHelper.TABLE_LIKE : table == OFFER ? SiteHelper.TABLE_OFFER : SiteHelper.TABLE_VISIT), null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {

                VisitData visitData = new VisitData();
                visitData.setSitename(cursor.getString(cursor.getColumnIndex(SiteHelper.COLUMN_SITENAME)));
                visitData.setSiteid(cursor.getString(cursor.getColumnIndex(SiteHelper.COLUMN_SITE_ID)));
                visitData.setSiteicon(cursor.getString(cursor.getColumnIndex(SiteHelper.COLUMN_SITEICON)));
                visitData.setNumber(cursor.getString(cursor.getColumnIndex(SiteHelper.COLUMN_NUMBER)));
                visitData.setOffer(cursor.getString(cursor.getColumnIndex(SiteHelper.COLUMN_OFFER)));
                visitData.setBid(cursor.getString(cursor.getColumnIndex(SiteHelper.COLUMN_BID)));
                visitData.setOffer_desc(cursor.getString(cursor.getColumnIndex(SiteHelper.COLUMN_OFFER_DESC)));
                visitData.setSitedesc(cursor.getString(cursor.getColumnIndex(SiteHelper.COLUMN_SITE_DESC)));
                visitData.setLike(cursor.getString(cursor.getColumnIndex(SiteHelper.COLUMN_LIKE)).equals("1") ? true : false);
                visitData.setDelete(cursor.getString(cursor.getColumnIndex(SiteHelper.COLUMN_DELETE)).equals("1") ? true : false);

                listMovies.add(visitData);
            }
            while (cursor.moveToNext());
        }
        return listMovies;
    }


    private static class SiteHelper extends SQLiteOpenHelper {
        public static final String TABLE_ALL ="all_sites";
        public static final String TABLE_LIKE ="like_sites";
        public static final String TABLE_OFFER ="offer_sites";
        public static final String TABLE_VISIT ="visit_sites";
        public static final String COLUMN_UID ="_id";
        public static final String COLUMN_SITENAME = "sitename";
        public static final String COLUMN_SITE_ID = "siteid";
        public static final String COLUMN_SITEICON = "siteicon";
        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMN_OFFER = "offer";
        public static final String COLUMN_BID = "bid";
        public static final String COLUMN_OFFER_DESC = "offerdesc";
        public static final String COLUMN_LIKE = "like";
        public static final String COLUMN_DELETE = "del";
        public static final String COLUMN_SITE_DESC = "sitedesc";

        private static final String CREATE_TABLE_ALL_SITES = "CREATE TABLE " + TABLE_ALL + " (" +
                COLUMN_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_SITENAME + " TEXT," +
                COLUMN_SITE_ID + " TEXT," +
                COLUMN_SITEICON + " TEXT," +
                COLUMN_NUMBER + " TEXT," +
                COLUMN_OFFER + " TEXT," +
                COLUMN_BID + " TEXT," +
                COLUMN_OFFER_DESC + " TEXT," +
                COLUMN_SITE_DESC + " TEXT," +
                COLUMN_LIKE + " TEXT," +
                COLUMN_DELETE + " TEXT" +
                ");";
        private static final String CREATE_TABLE_LIKE_SITES = "CREATE TABLE " + TABLE_LIKE + " (" +
                COLUMN_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_SITENAME + " TEXT," +
                COLUMN_SITE_ID + " TEXT," +
                COLUMN_SITEICON + " TEXT," +
                COLUMN_NUMBER + " TEXT," +
                COLUMN_OFFER + " TEXT," +
                COLUMN_BID + " TEXT," +
                COLUMN_OFFER_DESC + " TEXT," +
                COLUMN_SITE_DESC + " TEXT," +
                COLUMN_LIKE + " TEXT," +
                COLUMN_DELETE + " TEXT" +
                ");";
        private static final String CREATE_TABLE_OFFER_SITES = "CREATE TABLE " + TABLE_OFFER + " (" +
                COLUMN_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_SITENAME + " TEXT," +
                COLUMN_SITE_ID + " TEXT," +
                COLUMN_SITEICON + " TEXT," +
                COLUMN_NUMBER + " TEXT," +
                COLUMN_OFFER + " TEXT," +
                COLUMN_BID + " TEXT," +
                COLUMN_OFFER_DESC + " TEXT," +
                COLUMN_SITE_DESC + " TEXT," +
                COLUMN_LIKE + " TEXT," +
                COLUMN_DELETE + " TEXT" +
                ");";
        private static final String CREATE_TABLE_VISIT_SITES = "CREATE TABLE " + TABLE_VISIT + " (" +
                COLUMN_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_SITENAME + " TEXT," +
                COLUMN_SITE_ID + " TEXT," +
                COLUMN_SITEICON + " TEXT," +
                COLUMN_NUMBER + " TEXT," +
                COLUMN_OFFER + " TEXT," +
                COLUMN_BID + " TEXT," +
                COLUMN_OFFER_DESC + " TEXT," +
                COLUMN_SITE_DESC + " TEXT," +
                COLUMN_LIKE + " TEXT," +
                COLUMN_DELETE + " TEXT" +
                ");";

        private static final String DB_NAME = "Mconnect.db";
        private static final int DB_VERSION = 9;

        private Context mContext;

        public SiteHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d("TABLE","on create called");
            try {
                db.execSQL(CREATE_TABLE_ALL_SITES);
                db.execSQL(CREATE_TABLE_LIKE_SITES);
                db.execSQL(CREATE_TABLE_OFFER_SITES);
                db.execSQL(CREATE_TABLE_VISIT_SITES);
                Log.d("TABLE","onCreate Called");
            } catch (SQLiteException exception) {
                Log.d("TABLE", exception.getMessage().toString());
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                // L.m("upgrade table box office executed");
                Log.d("TABLE", "onUpgrade Called");
                db.execSQL(" DROP TABLE " + TABLE_ALL + " IF EXISTS;");
                db.execSQL(" DROP TABLE " + TABLE_LIKE + " IF EXISTS;");
                db.execSQL(" DROP TABLE " + TABLE_VISIT + " IF EXISTS;");
                db.execSQL(" DROP TABLE " + TABLE_OFFER + " IF EXISTS;");
                onCreate(db);
            } catch (SQLiteException exception) {
                //  L.t(mContext, exception + "");
                Log.d("TABLE",exception.getMessage().toString());
            }
        }

    }
}
