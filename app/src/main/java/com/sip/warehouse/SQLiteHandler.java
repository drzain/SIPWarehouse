package com.sip.warehouse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 5;

    // Database Name
    private static final String DATABASE_NAME = "android_api";

    // Login table name
    private static final String TABLE_USER = "user";
    private static final String TABLE_RECEIVE = "receive";
    private static final String TABLE_GRADING = "grading";

    //Grading Table Columns Names
    private static final String KEY_ID_GRADING = "id_grading";
    private static final String KEY_GRADING = "id_select";
    private static final String KEY_KIK = "kik";
    private static final String KEY_ASSET_DESC = "asset_desc";
    private static final String KEY_PLAT = "plat";
    private static final String KEY_MAN_YEAR = "man_year";
    private static final String KEY_COLOUR = "colour";
    private static final String KEY_CHASIS = "chasis";
    private static final String KEY_MACHINE = "machine";
    private static final String KEY_RECEIVE_DATE = "receive_date";
    private static final String KEY_ASSET_TYPE = "asset_type";
    private static final String KEY_IS_GRADING = "is_grading";

    //Receive Table Columns Names
    private static final String KEY_ID_RECEIVE = "id_receive";
    private static final String KEY_ID_WAREHOUSE = "id_warehouse";
    private static final String KEY_CUSTOMER_NAME = "customer_name";
    private static final String KEY_CODE = "code";
    private static final String KEY_RECEIVE_PLAT = "receive_plat";
    private static final String KEY_RECEIVE_DESC = "receive_desc";
    private static final String KEY_YEAR = "year";
    private static final String KEY_RECEIVE_TIPE = "receive_tipe";
    private static final String KEY_IS_RECEIVE = "is_receive";


    // Login Table Columns names
    private static final String KEY_TOKEN = "token";
    private static final String KEY_EXPIRED = "expired";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ROLE = "role";
    private static final String KEY_PROVIDER = "provider";
    private static final String KEY_PROVIDERID = "providerid";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_UPDATED_AT = "updated_at";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TOKEN + " TEXT," + KEY_EXPIRED + " TEXT,"
                + KEY_NAME + " TEXT," + KEY_USERNAME + " TEXT UNIQUE," + KEY_EMAIL + " TEXT,"
                + KEY_ROLE + " TEXT," + KEY_PROVIDER + " TEXT," + KEY_PROVIDERID + " TEXT,"
                + KEY_CREATED_AT + " TEXT," + KEY_UPDATED_AT + " TEXT" + ")";

        String CREATE_GRADING_TABLE = "CREATE TABLE " + TABLE_GRADING + "("
                + KEY_ID_GRADING + " INTEGER PRIMARY KEY," + KEY_GRADING + " TEXT," + KEY_KIK + " TEXT," + KEY_ASSET_DESC + " TEXT,"
                + KEY_PLAT + " TEXT," + KEY_MAN_YEAR + " TEXT," + KEY_COLOUR + " TEXT," +KEY_CHASIS + " TEXT,"
                + KEY_MACHINE + " TEXT," + KEY_RECEIVE_DATE + " TEXT," + KEY_ASSET_TYPE + " TEXT,"
                + KEY_IS_GRADING + " TEXT" + ")";

        String CREATE_RECEIVE_TABLE = "CREATE TABLE " + TABLE_RECEIVE + "("
                + KEY_ID_RECEIVE + " INTEGER PRIMARY KEY," + KEY_ID_WAREHOUSE + " TEXT," + KEY_CUSTOMER_NAME + " TEXT,"
                + KEY_CODE + " TEXT," + KEY_RECEIVE_PLAT + " TEXT," + KEY_RECEIVE_DESC + " TEXT,"
                + KEY_YEAR + " TEXT," + KEY_RECEIVE_TIPE + " TEXT," + KEY_IS_RECEIVE + " TEXT" + ")";

        String CREATE_HISTORY_TABLE = "";

        db.execSQL(CREATE_LOGIN_TABLE);
        db.execSQL(CREATE_GRADING_TABLE);
        db.execSQL(CREATE_RECEIVE_TABLE);

        Log.e(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GRADING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECEIVE);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String token, String expired, String name, String username, String email, String role, String provider, String providerid, String created_at, String updated_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TOKEN, token); // Token
        values.put(KEY_EXPIRED, expired); // Expired
        values.put(KEY_NAME, name); // Name
        values.put(KEY_USERNAME, username); // Username
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_ROLE, role); // Role
        values.put(KEY_PROVIDER, provider); // Provider
        values.put(KEY_PROVIDERID, providerid); // Providerid
        values.put(KEY_CREATED_AT, created_at); // Created At
        values.put(KEY_UPDATED_AT, updated_at); // Updated At

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.e(TAG, "New user inserted into sqlite: " + id);
    }

    public void addGrading(String id_select, String kik, String asset_desc, String plat, String man_year, String colour, String chasis, String machine, String receive_date, String asset_type, String is_grading){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues datas = new ContentValues();
        datas.put(KEY_GRADING, id_select);
        datas.put(KEY_KIK, kik);
        datas.put(KEY_ASSET_DESC, asset_desc);
        datas.put(KEY_PLAT, plat);
        datas.put(KEY_MAN_YEAR, man_year);
        datas.put(KEY_COLOUR, colour);
        datas.put(KEY_CHASIS, chasis);
        datas.put(KEY_MACHINE, machine);
        datas.put(KEY_RECEIVE_DATE, receive_date);
        datas.put(KEY_ASSET_TYPE, asset_type);
        datas.put(KEY_IS_GRADING, is_grading);

        long id_grading = db.insert(TABLE_GRADING, null, datas);
        db.close();

        Log.e(TAG, "New Grading inserted into sqlite: " + id_grading);

    }

    public void addReceive(String id_warehouse, String customer_name, String code, String receive_plat, String receive_desc, String year, String receive_tipe, String is_receive){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues data = new ContentValues();
        data.put(KEY_ID_WAREHOUSE, id_warehouse);
        data.put(KEY_CUSTOMER_NAME, customer_name);
        data.put(KEY_CODE, code);
        data.put(KEY_RECEIVE_PLAT, receive_plat);
        data.put(KEY_RECEIVE_DESC, receive_desc);
        data.put(KEY_YEAR, year);
        data.put(KEY_RECEIVE_TIPE, receive_tipe);
        data.put(KEY_IS_RECEIVE, is_receive);

        long id_receive = db.insert(TABLE_RECEIVE, null, data);
        db.close();

        Log.e(TAG, "New Receive inserted into sqlite: " + id_receive);

    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("token", cursor.getString(1));
            user.put("expired", cursor.getString(2));
            user.put("name", cursor.getString(3));
            user.put("username", cursor.getString(4));
            user.put("email", cursor.getString(5));
            user.put("role", cursor.getString(6));
            user.put("provider", cursor.getString(7));
            user.put("providerid", cursor.getString(8));
            user.put("created_at", cursor.getString(9));
            user.put("updated_at", cursor.getString(10));
        }
        cursor.close();
        db.close();
        // return user
        Log.e(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    public HashMap<String, String> getGradingDetails(){
        HashMap<String, String> grading = new HashMap<>();
        String selectQuery = "SELECT * FROM " + TABLE_GRADING;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            grading.put("id_select", cursor.getString(1));
            grading.put("kik", cursor.getString(2));
            grading.put("asset_desc", cursor.getString(3));
            grading.put("plat", cursor.getString(4));
            grading.put("man_year", cursor.getString(5));
            grading.put("colour", cursor.getString(6));
            grading.put("chasis", cursor.getString(7));
            grading.put("machine", cursor.getString(8));
            grading.put("receive_date", cursor.getString(9));
            grading.put("asset_type", cursor.getString(10));
            grading.put("is_grading", cursor.getString(11));
        }
        cursor.close();
        db.close();

        Log.e(TAG, "Fetching grading from Sqlite: " + grading.toString());

        return grading;
    }

    public HashMap<String, String> getReceiveDetails(){
        HashMap<String,String> receive = new HashMap<>();
        String selectQuery = "SELECT * FROM " + TABLE_RECEIVE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            receive.put("id_warehouse",cursor.getString(1));
            receive.put("customer_name", cursor.getString(2));
            receive.put("code", cursor.getString(3));
            receive.put("receive_plat", cursor.getString(4));
            receive.put("receive_desc", cursor.getString(5));
            receive.put("year", cursor.getString(6));
            receive.put("receive_type", cursor.getString(7));
            receive.put("is_receive", cursor.getString(8));
        }
        cursor.close();
        db.close();

        Log.e(TAG, "Fetching receive from sqlite: " + receive.toString());

        return receive;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.e(TAG, "Deleted all user info from sqlite");
    }

    public void deleteGrading() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_GRADING, null, null);
        db.close();

        Log.e(TAG, "Delete all grading info from sqlite");
    }

    public void deleteReceive() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_RECEIVE, null, null);
        db.close();

        Log.e(TAG, "Delete all receive info from sqlite");
    }

}
