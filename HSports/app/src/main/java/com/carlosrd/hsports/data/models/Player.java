package com.carlosrd.hsports.data.models;

import com.google.gson.annotations.SerializedName;

public class Player {

    @SerializedName("name")
    private String name;

    @SerializedName("surname")
    private String surname;

    @SerializedName("date")
    private String dateOfBirth;

    @SerializedName("image")
    private String imageURL;

    public Player(String name, String surname, String dateOfBirth, String imageURL) {

        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.imageURL = imageURL;

    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getImageURL() {
        return imageURL;
    }
}
