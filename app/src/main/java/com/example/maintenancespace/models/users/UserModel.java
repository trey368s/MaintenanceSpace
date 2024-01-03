package com.example.maintenancespace.models.users;

public class UserModel {
    private int userId;
    private String email;



    public UserModel(int userId, String email)
    {
        this.userId = userId;
        this.email = email;
    }

    public int getUser()
    {
        return userId;
    }
    public String getEmail(){return email;}
}
