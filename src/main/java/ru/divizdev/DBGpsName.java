package ru.divizdev;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by diviz on 11.03.2017.
 */
public class DBGpsName {

    private final static String SELECT_DB_SQL_GEO_NAME = "select  name, country_code, ( (g.latitude - :1) * (g.latitude - :1) +\n" +
            " ( g.longitude - :2 ) * ( g.longitude - :2 ) ) * (10000) as distance\n" +
            "from GeoName \n" +
            "where latitude > :3 and latitude < :4\n" +
            "and longitude > :5 and longitude < :6\n" +
            "and distance < :7\n" +
            "order by ru desc\n" +
            "limit 100;";
    private final int distance = 5;
    private final int delta = 1;
    private final int maxDistance = 50;

    public GpsInfo getGeoName(Connection connection, double latitude, double longitude) throws SQLException {

        String city = "";
        String country = "";
        for (int i = distance, j = delta; i < maxDistance; j++, i *= 2) {
            PreparedStatement command = getCommand(connection, latitude, longitude, i, j);
            ResultSet resultSet = command.executeQuery();
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

    private PreparedStatement getCommand(Connection connection, double latitude, double longitude, int distance, int delta) throws SQLException {
        double latitudeMin = latitude - delta;
        double latitudeMax = latitude + delta;
        double longitudeMin = longitude - delta;

        double longitudeMax = longitude + delta;

        PreparedStatement result = connection.prepareStatement(SELECT_DB_SQL_GEO_NAME);

        result.setDouble(1, latitude);
        result.setDouble(2, longitude);
        result.setDouble(3, latitudeMin);
        result.setDouble(4, latitudeMax);
        result.setDouble(5, longitudeMin);
        result.setDouble(6, longitudeMax);
        result.setDouble(7, distance);

        return result;
    }

}
