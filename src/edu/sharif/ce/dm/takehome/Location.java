package edu.sharif.ce.dm.takehome;

/**
 * Created by IntelliJ IDEA.
 * User: Jrad
 * Date: Jan 26, 2006
 * Time: 4:01:31 PM
 */
public class Location {

    private String ip;

    private String country;

    private float longitute;

    private float latitude;

    private String city;

    // TODO convert to Java TM TimeZone standard
    private String timezone;

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitute() {
        return longitute;
    }

    public void setLongitute(float longitute) {
        this.longitute = longitute;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    Location( String ip ) {
        this.ip = ip;
    }
}
