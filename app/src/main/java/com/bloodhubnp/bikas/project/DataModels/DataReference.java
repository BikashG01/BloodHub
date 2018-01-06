package com.bloodhubnp.bikas.project.DataModels;

import java.io.Serializable;

/**
 * Created by bikas on 7/24/2017.
 */

public class DataReference implements Serializable {
    String u_name;
    String u_number;
    String u_status;
    String image_url;
    public int type;

    public DataReference(String u_name, String u_number, int type, String image_url) {
        this.u_name = u_name;
        this.u_number = u_number;
        this.type = type;
        this.image_url = image_url;
    }

    public String getU_name() {
        return u_name;
    }

    public String getU_number() {
        return u_number;
    }

    public int getType() {
        return type;
    }

    public String getImage_url() {
        return image_url;
    }
}
