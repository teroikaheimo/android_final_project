package com.final_project;

public class PlaceItem {
    private String name;
    private String id;

    public PlaceItem(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
