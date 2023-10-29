package com.example.maintenancespace.models.users;

public class UserModel {
    private int UserId;
    private String FirstName;
    private String LastName;


    public UserModel(int UserId, String FirstName, String LastName)
    {
        this.UserId = UserId;
        this.FirstName = FirstName;
        this.LastName = LastName;
    }

    public int getUser()
    {
        return UserId;
    }
    public String getFirstName()
    {
        return FirstName;
    }
    public String getLastName()
    {
        return LastName;
    }
}
