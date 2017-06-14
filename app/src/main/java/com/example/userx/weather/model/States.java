package com.example.userx.weather.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by userx on 24/01/17.
 */

public final class States {

    public static final Map<String, String> STATES = new HashMap<>();

    static {
        STATES.put("801", "few clouds");
        STATES.put("802", "scattered clouds ");
        STATES.put("803", "broken clouds");
        STATES.put("804", "overcast clouds");
    }// static

}// States
