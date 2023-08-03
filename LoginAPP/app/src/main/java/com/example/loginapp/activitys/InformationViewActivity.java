package com.example.loginapp.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.loginapp.R;
import com.example.loginapp.database.DatabaseUsers;
import com.example.loginapp.model.RegularExpressions;
import com.example.loginapp.model.User;

import java.util.Random;

public class InformationViewActivity extends AppCompatActivity {
    private EditText editTextIdVI, editTextNameVI, editTextNumberVI, editTextEmailVI, editTextPasswordVI;
    private Button buttonUpdate, buttonDelete;
    private RegularExpressions regularExp;
    private String firstEmail, email, name, number, firstPassword ,password;
    private int id;
    private User user;
    private DatabaseUsers databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_view);
        setSupportActionBar(findViewById(R.id.toolbar2));
        getSupportActionBar().setTitle("Editar Información");
        user = DatabaseUsers.globalUser;

        editTextIdVI = findViewById(R.id.editTextIdVI);
        editTextIdVI.setEnabled(false);
        editTextNameVI = findViewById(R.id.editTextNameVI);
        editTextNumberVI = findViewById(R.id.editTextNumberVI);
        editTextEmailVI = findViewById(R.id.editTextEmailVI);
        editTextPasswordVI = findViewById(R.id.editTextPasswordVI);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonDelete = findViewById(R.id.buttonDelete);
        regularExp = new RegularExpressions();
        databaseUsers = new DatabaseUsers(this);

        firstEmail = user.getEmail();
        firstPassword = user.getPassword();
        editTextIdVI.setText(Integer.toString(user.getId()));
        editTextNameVI.setText(user.getName());
        editTextNumberVI.setText(user.getNumber());
        editTextEmailVI.setText(user.getEmail());
        editTextPasswordVI.setText(user.getPassword());

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = Integer.valueOf(editTextIdVI.getText().toString().trim());
                email = editTextEmailVI.getText().toString().trim();
                name = editTextNameVI.getText().toString().trim();
                number = editTextNumberVI.getText().toString().trim();
                password = editTextPasswordVI.getText().toString();
                Boolean flag = true;
                if (email.isEmpty() | name.isEmpty() | number.isEmpty() | password.isEmpty()) {
                    Toast.makeText(InformationViewActivity.this, "Debes de llenar toda la información", Toast.LENGTH_SHORT).show();
                } else {
                    if (regularExp.validateEmail(email) && regularExp.validateName(name) && regularExp.validatePhone(number) && regularExp.validatePassword(password)) {
                        //VERIFICAR SI EXISTE EL CORREO ESTA DISPONIBLE
                        if (!firstEmail.equals(email)) {
                            if (databaseUsers.searchEmail(email)) {
                                flag = false;
                                Toast.makeText(InformationViewActivity.this, "Correo no disponible", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (flag) {
                            try {
                                Random random = new Random();
                                int randomNumber = random.nextInt(900000000) + 1000000000;
                                //SABER SI CREAR UNA LLAVE NUEVA
                                String nameKeyAlias = (!firstPassword.equals(password)) ? Integer.toString(randomNumber) : "0";
                                User user = new User(id, name, number, email, password, nameKeyAlias, null);
                                /*ACTUALIZAMOS LA INFORMACION*/
                                if (databaseUsers.updateUser(user)) {
                                    Toast.makeText(InformationViewActivity.this, "La cuenta fue modificada", Toast.LENGTH_LONG).show();
                                    Thread.sleep(4000);
                                    Intent next = new Intent(InformationViewActivity.this, SystemAdminActivity.class);
                                    startActivity(next);
                                } else {
                                    Toast.makeText(InformationViewActivity.this, "Error al crear llave", Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                            }
                        }
                    } else {
                        if (!regularExp.validateEmail(email)) {
                            Toast.makeText(InformationViewActivity.this, "Email no valido", Toast.LENGTH_SHORT).show();
                        }
                        if (!regularExp.validateName(name)) {
                            Toast.makeText(InformationViewActivity.this, "El nombre debe de contener al menos 3 cáracteres", Toast.LENGTH_SHORT).show();
                        }
                        if (!regularExp.validatePhone(number)) {
                            Toast.makeText(InformationViewActivity.this, "Numero no valido", Toast.LENGTH_SHORT).show();
                        }
                        if (!regularExp.validatePassword(password)) {
                            Toast.makeText(InformationViewActivity.this, "Password no valido", Toast.LENGTH_SHORT).show();
                            Toast.makeText(InformationViewActivity.this, "Debe tener un Máyuscula, una Mínuscula, un Número y un Signo", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    databaseUsers.deleteUser(user.getId());
                    Toast.makeText(InformationViewActivity.this, "El usuario se eliminó", Toast.LENGTH_LONG).show();
                    Thread.sleep(4000);
                    Intent next = new Intent(InformationViewActivity.this, SystemAdminActivity.class);
                    startActivity(next);
                } catch (Exception e) {
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu miMenu) {
        getMenuInflater().inflate(R.menu.toolbar_empty, miMenu);
        return true;
    }
}