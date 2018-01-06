package com.bloodhubnp.bikas.project.DataModels;

/**
 * Created by bikas on 8/24/2017.
 */

public class Blood_Data {
    private String name, distance, location;
    private String image_path;
    private String number;
    public String ip = "http://192.168.10.6/BloodHub/";


    public Blood_Data(String name, String location, String distance, String image_path, String number) {
        this.name = name;
        this.location = location;
        this.distance = distance;
        this.image_path = number;
        this.number = image_path;

    }

    public String getName() {
        return name;
    }

    public String getDistance() {
        return distance;
    }

    public String getLocation() {
        return location;
    }

    public String getNumber() {
        return number;
    }


    public String getImage_path() {
        return ip + image_path;
    }
}
