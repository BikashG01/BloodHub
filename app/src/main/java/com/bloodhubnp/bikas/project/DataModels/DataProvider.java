package com.bloodhubnp.bikas.project.DataModels;

/**
 * Created by bikas on 8/6/2017.
 */

public class DataProvider {
    private String name;
    private String address;
    private String bloodgroup;
    private String distance;
    private String ph_number;
    private String image_url;
    public String ip = "http://192.168.10.6:81/BloodHub/";


    public String getImage_url() {
        return ip + image_url;
    }


    public String getPh_number() {
        return ph_number;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public String getDistance() {
        return distance;
    }

    public DataProvider(String name, String address, String bloodgroup, String distance, String image_url, String ph_number) {
        this.image_url = image_url;
        this.name = name;
        this.address = address;
        this.bloodgroup = bloodgroup;
        this.distance = distance;
        this.ph_number = ph_number;
    }
}
