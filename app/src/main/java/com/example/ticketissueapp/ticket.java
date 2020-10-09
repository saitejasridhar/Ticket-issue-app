package com.example.ticketissueapp;

import com.google.type.Date;

public class ticket {
    private String tissue;
    private String ttime;
    private String taddress;
    private String tissue_desc;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    private String userid;

    public String getTissue() {
        return tissue;
    }

    public void setTissue(String tissue) {
        this.tissue = tissue;
    }

    public String getTtime() {
        return ttime;
    }

    public void setTtime(String ttime) {
        this.ttime = ttime;
    }

    public String getTaddress() {
        return taddress;
    }

    public void setTaddress(String taddress) {
        this.taddress = taddress;
    }

    public String getTissue_desc() {
        return tissue_desc;
    }

    public void setTissue_desc(String tissue_desc) {
        this.tissue_desc = tissue_desc;
    }



    ticket(){}
    ticket(String tissue,String ttime,String taddress,String tissue_desc,String userid){
        this.tissue=tissue;
        this.ttime=ttime;
        this.taddress=taddress;
        this.tissue_desc=tissue_desc;
        this.userid=userid;
    }
}
