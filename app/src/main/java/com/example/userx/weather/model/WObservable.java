package com.example.userx.weather.model;

import java.util.Observable;

/**
 * Created by userx on 24/01/17.
 */

public class WObservable extends Observable {

    private Weather data;

    public Weather getData() {
        return data;
    }// getData

    public void setData(Weather jObj) {
        setChanged();
        this.data = jObj;
    }// setData

}// WObservable
