package com.example.vallislee.fuel_consumption.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {

    //Database version
    private static final int DATABASE_VERSION = 1;
    // Database name
    private static final String DATABASE_NAME = "FuelApp.db";
    // Table name
    private static final String TABLE_NAME = "UsersInfo";
    private static final String TABLE_NAME2 = "FillUp";
    private static final String TABLE_NAME3 = "DriveB";

    // Construct Table columns names
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_HASH = "hash";
    private static final String KEY_USER_SALT = "salt";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(user_email TEXT  PRIMARY KEY, hash TEXT, salt BLOB)");
        db.execSQL("create table " + TABLE_NAME2 + "(user_email TEXT, date TEXT, mileage TEXT, gas_price TEXT, litres TEXT, id INTEGER PRIMARY KEY AUTOINCREMENT)");
        db.execSQL("create table " + TABLE_NAME3 + "(user_email TEXT, score TEXT, id INTEGER PRIMARY KEY AUTOINCREMENT)");
        // drive mode start and get all the data, once end driving; getting the average driving score.
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        onCreate(db);
    }

    // sign up
    public boolean userregis(String user_email, String hash, byte[] salt) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_EMAIL,user_email);
        contentValues.put(KEY_USER_HASH,hash);
        contentValues.put(KEY_USER_SALT,salt);
        long result = db.insert(TABLE_NAME,null,contentValues);
        return (result != -1);
    }

    // check whether user exist or not
    public boolean exist(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_USER_EMAIL + " = " + "'" + email + "'";
        Cursor cursor = db.rawQuery(Query,null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    // retrieve user's email and hash
    public String[] getUser(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_USER_EMAIL + " = " + "'" + email + "'";
        Cursor cursor = db.rawQuery(Query,null);
        if(cursor.getCount() >= 1){
            String[] result = new String[3];
            cursor.moveToFirst();
            result[0] = cursor.getString(0);
            result[1] = cursor.getString(1);
            cursor.close();
            return result;
        }else{
            cursor.close();
            return null;
        }
    }
    // retrieve user's password salt
    public byte[] getUser_salt(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_USER_EMAIL + " = " + "'" + email + "'";
        Cursor cursor = db.rawQuery(Query,null);
        if(cursor.getCount() >= 1){
            cursor.moveToFirst();
            byte[] result = cursor.getBlob(2);
            cursor.close();
            return result;
        }else{
            cursor.close();
            return null;
        }
    }
    // update user's password
    public boolean update_password(String user_email, String hash, byte[] salt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_EMAIL,user_email);
        contentValues.put(KEY_USER_HASH,hash);
        contentValues.put(KEY_USER_SALT,salt);
        long result = db.update(TABLE_NAME,contentValues,"user_email = " + "'" + user_email + "'", null);
        return (result != -1);
    }

    // check whether the fill up data exist or not
    public boolean fillUp_exist(String user_email){
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "SELECT * FROM " + TABLE_NAME2 + " WHERE " + KEY_USER_EMAIL + " = " + "'" + user_email + "'";
        Cursor cursor = db.rawQuery(Query,null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;

    }

    // insert fill up data
    public boolean fillup_insert(String user_email, String date, String mileage, String price, String litres) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_email", user_email);
        contentValues.put("date", date);
        contentValues.put("mileage", mileage);
        contentValues.put("gas_price", price);
        contentValues.put("litres", litres);
        long result = db.insert(TABLE_NAME2,null,contentValues);
        return (result != -1);

    }

    // query the last fill up date
    public String last_fillup_date(String user_email){
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "SELECT * FROM " + TABLE_NAME2 + " WHERE " + KEY_USER_EMAIL + " = " + "'" + user_email + "'" + " order by date desc ";
        Cursor cursor = db.rawQuery(Query,null);
        if(cursor.getCount() >= 1) {
            cursor.moveToFirst();
            String last_date = cursor.getString(1);
            cursor.close();
            return last_date;
        }else{
            cursor.close();
            return null;
        }
    }

    // getting the total mileages for current user
    public double mileages_fillup(String user_email){
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "SELECT * FROM " + TABLE_NAME2 + " WHERE " + KEY_USER_EMAIL + " = " + "'" + user_email + "'" + " order by mileage";
        Cursor cursor = db.rawQuery(Query,null);
        if(cursor.getCount() >= 1) {
            int count = cursor.getCount();
            String[] str_arr = new String[count];
            int i = 0;
            while(cursor.moveToNext()){
                str_arr[i] = cursor.getString(cursor.getColumnIndex("mileage"));
                i++;
            }
            double temp = Double.parseDouble(str_arr[count - 1]) - Double.parseDouble(str_arr[0]);
            double result = Math.round(temp*100.0)/100.0;
            cursor.close();
            return result;
        }else{
            cursor.close();
            return 0;
        }
    }

    // querying the litres usage
    public double litres_fillup(String user_email){
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "SELECT * FROM " + TABLE_NAME2 + " WHERE " + KEY_USER_EMAIL + " = " + "'" + user_email + "'" ;
        Cursor cursor = db.rawQuery(Query,null);
        if(cursor.getCount() >= 1) {
            double temp = 0;
            while(cursor.moveToNext()){
                temp += Double.parseDouble(cursor.getString(cursor.getColumnIndex("litres")));
            }
            double result = Math.round(temp*100.0)/100.0;
            cursor.close();
            return result;
        }else{
            cursor.close();
            return 0;
        }
    }

    // driving record
    public void add_score(String user_email, String score){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_email", user_email);
        contentValues.put("score", score);
        db.insert(TABLE_NAME3,null,contentValues);
    }

    // getting the average driving score of the given user email
    public double get_score(String user_email){
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "SELECT * FROM " + TABLE_NAME3 + " WHERE " + KEY_USER_EMAIL + " = " + "'" + user_email + "'" ;
        Cursor cursor = db.rawQuery(Query,null);
        int num = cursor.getCount();
        if(num >= 1) {
            double temp = 0;
            while(cursor.moveToNext()){
                temp += Double.parseDouble(cursor.getString(cursor.getColumnIndex("score")));
            }
            double result = Math.round((temp/num)*100.0)/100.0;
            cursor.close();
            return result;
        }else{
            cursor.close();
            return 0;
        }
    }


}
