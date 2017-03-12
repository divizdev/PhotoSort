package ru.divizdev;

/**
 * Created by diviz on 11.03.2017.
 */
public class GpsInfo {

    private final String city;
    private final String country;
    private final double latitude;
    private final double longitude;

    public GpsInfo(String city, String country, double latitude, double longitude) {
        this.city = city;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return getCountry() + ":" + getCity();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GpsInfo gpsInfo = (GpsInfo) o;

        if (Double.compare(gpsInfo.getLatitude(), getLatitude()) != 0) return false;
        if (Double.compare(gpsInfo.getLongitude(), getLongitude()) != 0) return false;
        if (getCity() != null ? !getCity().equals(gpsInfo.getCity()) : gpsInfo.getCity() != null) return false;
        return getCountry() != null ? getCountry().equals(gpsInfo.getCountry()) : gpsInfo.getCountry() == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getCity() != null ? getCity().hashCode() : 0;
        result = 31 * result + (getCountry() != null ? getCountry().hashCode() : 0);
        temp = Double.doubleToLongBits(getLatitude());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getLongitude());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
