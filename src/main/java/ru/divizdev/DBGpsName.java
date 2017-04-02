package ru.divizdev;

import java.sql.*;
import java.util.Locale;

/**
 * Created by diviz on 11.03.2017.
 */
public class DBGpsName {

    private final static String SELECT_DB_SQL_GEO_NAME = "select  name, country_code, ( (g.latitude - %1$f) * (g.latitude - %1$f) +\n" +
            " ( g.longitude - %2$f ) * ( g.longitude - %2$f ) ) * (10000) as distance\n" +
            "from GeoName as g\n" +
            "where latitude > %3$f and latitude < %4$f\n" +
            "and longitude > %5$f and longitude < %6$f\n" +
            "and distance < %7$d\n" +
            "order by ru desc\n" +
            "limit 100;";
    private final int distance = 5;
    private final int delta = 1;
    private final int maxDistance = 50;

    public GpsInfo getGeoName(Statement statement, double latitude, double longitude) throws SQLException {

        String city = "";
        String country = "";
        for (int i = distance, j = delta; i < maxDistance; j++, i *= 2) {
            String command = getCommand(latitude, longitude, i, j);
            ResultSet resultSet = statement.executeQuery(command);
            if (resultSet.next()) {
                city = resultSet.getString(1);
                country = resultSet.getString(2);
                break;
            } else {
                continue;
            }
        }
        return new GpsInfo(city, country, latitude, longitude);
    }

    private String getCommand(double latitude, double longitude, int distance, int delta) {
        double latitudeMin = latitude - delta;
        double latitudeMax = latitude + delta;
        double longitudeMin = longitude - delta;

        double longitudeMax = longitude + delta;

        return String.format(Locale.ENGLISH, SELECT_DB_SQL_GEO_NAME, latitude, longitude,
                latitudeMin, latitudeMax,
                longitudeMin, longitudeMax, distance);
    }

}
