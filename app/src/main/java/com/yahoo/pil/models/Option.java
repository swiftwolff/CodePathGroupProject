package com.yahoo.pil.models;

/**
 * Created by jeffhsu on 2/24/15.
 */
public class Option {
    public void setName(String name) {
        this.name = name;
    }

    public void setDrawID(int drawID) {
        this.drawID = drawID;
    }

    public String getName() {

        return name;
    }

    public int getDrawID() {
        return drawID;
    }

    String name;
    int drawID;
}
