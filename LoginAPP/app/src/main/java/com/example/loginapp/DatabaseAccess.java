package com.example.loginapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Base64;
import android.widget.Toast;

import java.io.File;

import javax.crypto.SecretKey;

public class DatabaseAccess {
    private Context context;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;
    private EncryptDecrypt encryptDecrypt;

    public DatabaseAccess(Context context) {
        this.context = context;
        //INSTANCIA SQLITE
        databaseHelper = new DatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();
    }

    //BUSCAR EL CORREO Y EL PASSWORD DEL INICIO DE SESION
    public boolean searchInformation(String email, String password) throws Exception {
        String[] projection = {DatabaseHelper.COLUMN_EMAIL, DatabaseHelper.COLUMN_PASSWORD, DatabaseHelper.COLUMN_NAME_KEY_ALIAS, DatabaseHelper.COLUMN_IV};
        String selection = DatabaseHelper.COLUMN_EMAIL + "= ?";
        String[] selectionArgs = {email};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            String nameKeyAlias = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME_KEY_ALIAS));
            byte[] passwordBD = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSWORD));
            byte[] iv = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IV));

            encryptDecrypt = new EncryptDecrypt(nameKeyAlias, context, iv);
            SecretKey secretKey = encryptDecrypt.searchKey();
            String descryptPassword = encryptDecrypt.descryptData(passwordBD, secretKey);

            if (password.equals(descryptPassword)) {
                cursor.close();
                return true;
            } else {
                Toast.makeText(context, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                cursor.close();
                return false;
            }

        } else {
            Toast.makeText(context, "Correo no encontrado", Toast.LENGTH_SHORT).show();
            cursor.close();
            return false;
        }
    }

    //PERMITE MOSTRAR UN BLOQUE DE INFORMACION DEL USUARIO EN ESPECIFICO
    public void loadInformation(int i) {
        String[] projection = {DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_PASSWORD, DatabaseHelper.COLUMN_NAME_KEY_ALIAS};
        String selection = DatabaseHelper.COLUMN_ID + "= ?";
        String[] selectionArgs = {String.valueOf(i)};
        StringBuilder dataToShow = new StringBuilder();

        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
                byte[] password = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSWORD));
                String alias = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));

                String passwordStr = Base64.encodeToString(password, Base64.DEFAULT);

                dataToShow.append("Name: ").append(name).append("\n");
                dataToShow.append("Password: ").append(passwordStr).append("\n");
                dataToShow.append("Alias: ").append(alias).append("\n\n");
            } while (cursor.moveToNext());
        }
        cursor.close();
        Toast.makeText(context, dataToShow.toString(), Toast.LENGTH_LONG).show();
    }

    //ELIMINAR LA BASE DE DATOS
    public void deleteDatabase() {
        File databaseFile = context.getApplicationContext().getDatabasePath(DatabaseHelper.DATABASE_NAME);
        if (databaseFile.exists()) {
            try {
                database.deleteDatabase(databaseFile);
                // La base de datos se ha eliminado exitosamente
            } catch (Exception e) {
                // Ocurrió un error al eliminar la base de datos
            }
        } else {
            // La base de datos no existe, no es necesario eliminarla
        }
    }
}
