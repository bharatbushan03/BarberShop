package com.barbershop.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class dbhelperforowner extends SQLiteOpenHelper {
    public static final String dbname="ownerdb";
    public dbhelperforowner(@Nullable Context context) {
        super(context, "ownerdb", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase mydb) {
        mydb.execSQL("create Table ownerdb(shopname TEXT primary key,password TEXT,shopemail TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase mydb, int i, int i1) {
        mydb.execSQL("drop Table if exists ownerdb");
    }

    public boolean insertData(String shopname,String password,String shopemail){
        SQLiteDatabase mydb=this.getReadableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("shopname",shopname);
        contentValues.put("password",password);
        contentValues.put("shopemail",shopemail);
        long result=mydb.insert("ownerdb",null,contentValues);
        return result != -1;

    }
    public Boolean checkshopname(String shopname){
        SQLiteDatabase mydb=this.getReadableDatabase();
        Cursor cursor=mydb.rawQuery("Select * from ownerdb where shopname=?",new String[]{shopname});
        return cursor.getCount() > 0;
    }

    public Boolean checkshopnamepassword(String shopname,String password){
        SQLiteDatabase mydb=this.getReadableDatabase();
        Cursor cursor=mydb.rawQuery("Select * from ownerdb where shopname=? and password=?",new String[]{shopname,password});

        if(cursor.getCount()>0){
            return true;
        }else {
            return  false;
        }
    }
}
