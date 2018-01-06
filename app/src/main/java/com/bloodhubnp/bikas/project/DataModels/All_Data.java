package com.bloodhubnp.bikas.project.DataModels;

/**
 * Created by bikas on 8/4/2017.
 */

public class All_Data {
    private String name, distance, location, blood_group;
    private String image_path;

    public All_Data(String name, String distance, String location, String blood_group, String image_path) {
        this.name = name;
        this.distance = distance;
        this.location = location;
        this.blood_group = blood_group;
        this.image_path = image_path;
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

    public String getBlood_group() {
        return blood_group;
    }

    public String getImage_path() {
        return image_path;
    }
}
