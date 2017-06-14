package com.example.userx.weather.model;

/**
 * Created by userx on 24/01/17.
 */
import android.os.SystemClock;

import com.example.userx.weather.utils.Printer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Weather implements Serializable {

    private boolean init;

    private String city;
    private String country;
    private Date date;
    private String temperature;
    private String minTemp;
    private String maxTemp;
    private String description;
    private String wind;
    private Double windDirectionDegree;
    private String pressure;
    private String humidity;
    private String cloudiness;
    private String rain;
    private String snow;
    private Integer id;
    private Date sunrise;
    private Date sunset;

    private double lat;
    private double lon;

    public Weather() {
        setCity("");
        setCountry("NN");
        setDate(new Date(System.currentTimeMillis()));
        setTemperature("0");
        setMinTemp("0");
        setMaxTemp("0");
        setDescription("UNKNOWN");
        setWind("0");
        setWindDirectionDegree(0.0);
        setPressure("0");
        setHumidity("0");
        setCloudiness("0");
        setRain("0");
        setSnow("0");
        setId(0);
        setSunrise(new Date(System.currentTimeMillis()));
        setSunset(new Date(System.currentTimeMillis()));


    }// constructor

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public Double getWindDirectionDegree() {
        return windDirectionDegree;
    }

    public void setWindDirectionDegree(Double windDirectionDegree) {
        this.windDirectionDegree = windDirectionDegree;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(String minTemp) {
        this.minTemp = minTemp;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getRain() {
        return rain;
    }

    public void setRain(String rain) {
        this.rain = rain;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getSunrise() {
        return sunrise;
    }

    public void setSunrise(Date sunrise) {
        this.sunrise = sunrise;
    }

    public Date getSunset() {
        return sunset;
    }

    public void setSunset(Date sunset) {
        this.sunset = sunset;
    }

    public String getCloudiness() {
        return cloudiness;
    }

    public void setCloudiness(String cloudiness) {
        this.cloudiness = cloudiness;
    }

    public String getSnow() {
        return snow;
    }

    public void setSnow(String snow) {
        this.snow = snow;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }

    public boolean dayLight(Weather master) {

        if (master == null || master.sunrise == null || master.sunset == null) return true;

        int hour = Integer.parseInt(Printer.pHour(getDate()));
        int riseHour = Integer.parseInt(Printer.pHour(new Date(master.sunrise.getTime())));
        int setHour = Integer.parseInt(Printer.pHour(new Date(master.sunset.getTime())));

        if (hour > riseHour && hour < setHour) return true;

        return false;
    }// dayLight

    public ArrayList<String[]> toDetailsArray() {

        ArrayList<String[]> details = new ArrayList<>();

        details.add(new String[]{"Temperature:", Printer.pTemp(getTemperature(), true)});
        details.add(new String[]{"Minimum:", Printer.pTemp(getMinTemp(), true)});
        details.add(new String[]{"Maximum:", Printer.pTemp(getMaxTemp(), true)});
        details.add(new String[]{"Pressure:", getPressure() + " hPa"});
        details.add(new String[]{"Humidity:", getHumidity() + "%"});
        details.add(new String[]{"Cloudiness:", getCloudiness() + "%"});
        details.add(new String[]{"Wind:", getWind() + " m/s"});
        details.add(new String[]{"Rain level:", Printer.pRainSnow(getRain())});
        details.add(new String[]{"Snow level:", Printer.pRainSnow(getSnow())});

        return details;
    }// toDetailsArray

}// Weather
