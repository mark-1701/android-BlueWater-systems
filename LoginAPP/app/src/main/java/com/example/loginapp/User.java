package com.example.loginapp;

public class User {
    private int id;
    private String name;
    private String number;
    private String email;
    private String password;
    private String nameKeyAlias;
    private byte[] iv;

    public User() {
    }

    public User(int id, String name, String number, String email, String password, String nameKeyAlias, byte[] iv) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.email = email;
        this.password = password;
        this.nameKeyAlias = nameKeyAlias;
        this.iv = iv;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNameKeyAlias() {
        return nameKeyAlias;
    }

    public void setNameKeyAlias(String nameKeyAlias) {
        this.nameKeyAlias = nameKeyAlias;
    }

    public byte[] getIv() {
        return iv;
    }

    public void setIv(byte[] iv) {
        this.iv = iv;
    }
}
