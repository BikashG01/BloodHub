package com.bloodhubnp.bikas.project.DataModels;

/**
 * Created by bikas on 8/26/2017.
 */

public class Friendlist_Data {
    String fname, faddress, fbloodgroup, fnumber, fimage;
    String ip = "http://192.168.10.6/BloodHub/";

    public Friendlist_Data(String fname, String faddress, String fbloodgroup, String fnumber, String fimage) {
        this.fname = fname;
        this.faddress = faddress;
        this.fbloodgroup = fbloodgroup;
        this.fnumber = fnumber;
        this.fimage = fimage;
    }

    public String getFname() {
        return fname;
    }

    public String getFaddress() {
        return faddress;
    }

    public String getFbloodgroup() {
        return fbloodgroup;
    }

    public String getFnumber() {
        return fnumber;
    }

    public String getFimage() {
        return ip + fimage;
    }
}
