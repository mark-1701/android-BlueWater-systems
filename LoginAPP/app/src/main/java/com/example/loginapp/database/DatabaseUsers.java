package com.example.loginapp.database;

import android.content.ContentValues;
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
    public boolean updateUser(User user) throws Exception {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, user.getName());
        values.put(DatabaseHelper.COLUMN_NUMBER, user.getNumber());
        values.put(DatabaseHelper.COLUMN_EMAIL, user.getEmail());
        //SABER SI SE VA A CREAR UNA LLAVE NUEVA
        if (!user.getNameKeyAlias().equals("0")) {
            encryptDecrypt = new EncryptDecrypt(user.getNameKeyAlias(), context, null);
            //PARA SABER SI LA LLAVE YA EXISTE
            if (!encryptDecrypt.generateKey()) {
                return false;
            }
            SecretKey secretKey = encryptDecrypt.searchKey();
            byte[] encryptedData = encryptDecrypt.encryptData(user.getPassword(), secretKey);
            user.setIv(encryptDecrypt.getIv());
            values.put(DatabaseHelper.COLUMN_PASSWORD,  encryptedData);
            values.put(DatabaseHelper.COLUMN_NAME_KEY_ALIAS, user.getNameKeyAlias());
            values.put(DatabaseHelper.COLUMN_IV, user.getIv());
        }
        String selection = DatabaseHelper.COLUMN_ID + "=?";
        String[] selectionArgs={Integer.toString(user.getId())};
        database.update(databaseHelper.TABLE_NAME, values, selection, selectionArgs);
        return true;
    }

    public void deleteUser(int id){
        String selection = DatabaseHelper.COLUMN_ID + "=?";
        String[] selectionArgs = {Integer.toString(id)};
        database.delete(DatabaseHelper.TABLE_NAME, selection, selectionArgs);
    }
}
