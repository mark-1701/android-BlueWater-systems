package com.example.loginapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import javax.crypto.SecretKey;

public class DatabaseSingUp {
    private Context context;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;
    private EncryptDecrypt encryptDecrypt;

    public DatabaseSingUp(Context context) {
        this.context = context;
        //INSTANCIA SQLITE
        databaseHelper = new DatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();
    }
    public boolean searchEmail(String email) {
        String[] projection = {DatabaseHelper.COLUMN_EMAIL};
        String selection = DatabaseHelper.COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }
    public boolean insertUser(User newUser) throws Exception {
        encryptDecrypt = new EncryptDecrypt(newUser.getNameKeyAlias(), context, null);
        //PARA SABER SI LA LLAVE YA EXISTE
        if (!encryptDecrypt.generateKey()) {
            return false;
        }
        SecretKey secretKey = encryptDecrypt.searchKey();
        byte[] encryptedData = encryptDecrypt.encryptData(newUser.getPassword(), secretKey);
        newUser.setIv(encryptDecrypt.getIv());

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, newUser.getName());
        values.put(DatabaseHelper.COLUMN_NUMBER, newUser.getNumber());
        values.put(DatabaseHelper.COLUMN_EMAIL, newUser.getEmail());
        values.put(DatabaseHelper.COLUMN_PASSWORD, encryptedData);
        values.put(DatabaseHelper.COLUMN_NAME_KEY_ALIAS, newUser.getNameKeyAlias());
        values.put(DatabaseHelper.COLUMN_IV, newUser.getIv());
        database.insert(DatabaseHelper.TABLE_NAME, null, values);
        return true;
    }
}
