package com.barbershop.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class dbhelper extends SQLiteOpenHelper {
public static final String dbname="userLogindb";
    public dbhelper(@Nullable Context context) {
        super(context, "userLogindb", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase mydb) {
        mydb.execSQL("create Table userLogindb(username TEXT primary key,password TEXT,mobileno TEXT,useremail TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase mydb, int i, int i1) {
    mydb.execSQL("drop Table if exists userLogindb");
    }

    public boolean insertData(String username,String password,String mobile_no,String user_email){
        SQLiteDatabase mydb=this.getReadableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("username",username);
        contentValues.put("password",password);
        contentValues.put("mobile_no",mobile_no);
        contentValues.put("user_email",user_email);
        long result=mydb.insert("userLogindb",null,contentValues);
        return result != -1;

    }
    public Boolean checkusername(String username){
        SQLiteDatabase mydb=this.getReadableDatabase();
        Cursor cursor=mydb.rawQuery("Select * from userLogindb where username=?",new String[]{username});
        return cursor.getCount() > 0;
    }

    public Boolean checkusernamepassword(String username,String password){
        SQLiteDatabase mydb=this.getReadableDatabase();
        Cursor cursor=mydb.rawQuery("Select * from userLogindb where username=? and password=?",new String[]{username,password});

        if(cursor.getCount()>0){
            return true;
        }else {
            return  false;
        }
    }
}
