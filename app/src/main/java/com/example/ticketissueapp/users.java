package com.example.ticketissueapp;

public class users {
    private String name;
    private String email;
    private String phone;

    public users()
    {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public users(String name, String email, String phone)
    {
        this.email=email;
        this.name=name;
        this.phone=phone;
    }
}
