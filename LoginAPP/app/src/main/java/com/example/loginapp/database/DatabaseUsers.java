package com.example.loginapp.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.loginapp.model.EncryptDecrypt;
import com.example.loginapp.model.User;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;

public class DatabaseUsers {

    public static User globalUser;
    private Context context;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;
    private EncryptDecrypt encryptDecrypt;

    public DatabaseUsers(Context context) {
        this.context = context;
        //INSTANCIA SQLITE
        databaseHelper = new DatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();
    }

    public List<User> listInformation() throws Exception {
        List<User> dataList = new ArrayList<>();
        String[] projection = {DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_EMAIL, DatabaseHelper.COLUMN_NUMBER, DatabaseHelper.COLUMN_PASSWORD,  DatabaseHelper.COLUMN_NAME_KEY_ALIAS, DatabaseHelper.COLUMN_IV};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, projection, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMAIL));
                String number = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NUMBER));
                String nameKeyAlias = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME_KEY_ALIAS));
                byte[] passwordBD = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSWORD));
                byte[] iv = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IV));

                encryptDecrypt = new EncryptDecrypt(nameKeyAlias, context, iv);
                SecretKey secretKey = encryptDecrypt.searchKey();
                String descryptPassword = encryptDecrypt.descryptData(passwordBD, secretKey);

                User user = new User(id, name, number, email, descryptPassword, nameKeyAlias, null);
                dataList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return dataList;
    }
}
