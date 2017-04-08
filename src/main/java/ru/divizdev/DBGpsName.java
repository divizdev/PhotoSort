package ru.divizdev;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by diviz on 11.03.2017.
 */
public class DBGpsName {

    private final static String SELECT_DB_SQL_GEO_NAME = "select  name, country_code, ( (latitude - :1) * (latitude - :1) +\n" +
            " ( longitude - :2 ) * ( longitude - :2 ) )  as distance\n" +
            "from GeoName\n" +
            "where latitude > :3 and latitude < :4\n" +
            "and longitude > :5 and longitude < :6\n" +
            "order by ru desc, distance asc\n" +
            "limit 1;";

    private final int delta = 1;
    private final int maxDelta = 5;

    public GpsInfo getGeoName(Connection connection, double latitude, double longitude) throws SQLException {

        String city = "";
        String country = "";
        for (int j = delta; j < maxDelta;  j++) {
            PreparedStatement command = getCommand(connection, latitude, longitude, j);
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

    private PreparedStatement getCommand(Connection connection, double latitude, double longitude, int delta) throws SQLException {
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

        return result;
    }

}
