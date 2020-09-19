package com.example.ticketissueapp;

public class Profile1 {
    private String name;
    private String time;
    private String address;
    private String profilePic;
    private String dp;
    private boolean permission;

    public Profile1() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public boolean isPermission() {
        return permission;
    }

    public void setPermission(boolean permission) {
        this.permission = permission;
    }

    public Profile1(String name, String time, String address, String profilePic, String dp, boolean permission) {
        this.name=name;
        this.time=time;
        this.address=address;
        this.profilePic=profilePic;
        this.dp=dp;
        this.permission=permission;
    }



}