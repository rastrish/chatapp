package com.example.rishabh.chatapp;

public class User
{
    private String name,image,USerId;

    public User()
    {}
    public User(String name, String image,String USerId)
    {
        this.name = name;
        this.image = image;
        this.USerId = USerId;
    }

    public String getUserId() {
        return USerId;
    }

    public void setUSerId(String USerId) {
        this.USerId = USerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
