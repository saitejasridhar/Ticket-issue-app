package com.example.ticketissueapp;

import com.google.type.Date;

public class ticket {
    private String issue;
    private Date time;
    private String address;

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIssue_desc() {
        return issue_desc;
    }

    public void setIssue_desc(String issue_desc) {
        this.issue_desc = issue_desc;
    }

    private String issue_desc;

    ticket(){}
    ticket(String issue,Date time,String address,String issue_desc){
        this.issue=issue;
        this.time=time;
        this.address=address;
        this.issue_desc=issue_desc;
    }
}
