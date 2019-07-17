package com.poscoict.posledger.assets.controller;

public class userDto {

    private String id;
    private String date;
    private String name;
    private String passwd;

    public userDto() {

    }

    public userDto(String id, String date, String name, String passwd) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.passwd = passwd;
    }
}
