package com.example.maintenancespace.controllers;

import com.example.maintenancespace.models.users.UserModel;

public class UserController
{
    private UserModel model;

    public UserController()
    {
        model = new UserModel();
    }
    public void fetchById()
    {
        model.getUser();
    }
    public void fetchFirstName()
    {
        model.getFirstName();
    }
    public void fetchLastName()
    {
        model.getLastName();
    }
}
