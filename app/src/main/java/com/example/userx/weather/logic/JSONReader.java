package com.example.userx.weather.logic;

import org.json.JSONObject;

/**
 * Created by userx on 28/01/17.
 */

public interface JSONReader<T> {

    public T read(JSONObject obj);

    public Integer getType();

    public String getReq();

}// JSONReader
