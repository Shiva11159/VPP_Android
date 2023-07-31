package com.application.vpp.activity;

public class Statename {

    String statename;
    String stateID;

    public String getStatename() {
        return statename;
    }

    public void setStatename(String statename) {
        this.statename = statename;
    }

    public String getStateID() {
        return stateID;
    }

    public void setStateID(String stateID) {
        this.stateID = stateID;
    }

    public Statename(String statename, String stateID) {
        this.statename = statename;
        this.stateID = stateID;
    }
}
