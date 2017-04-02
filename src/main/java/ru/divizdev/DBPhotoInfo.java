package ru.divizdev;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by diviz on 11.03.2017.
 */
public class DBPhotoInfo {

//CREATE TABLE `photo` (
//            `id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
//            `photoName`	TEXT,
//            `date`	TEXT,
//            `country`	TEXT,
//            `city`	TEXT,
//            `latitude`	REAL,
//            `longitude`	REAL,
//            `photoPath`	TEXT
//);


    private final String insertSQLPhoto = "insert into photo(photoName, date, country, city, latitude, longitude, photoPath)\n" +
            "values(:1, :2, :3, :4, :5, :6, :7)";

    public boolean insertDBPhotoInfo(Connection connection, PhotoInfo info) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(insertSQLPhoto);
        preparedStatement.setString(1, info.getPhotoName());
        preparedStatement.setString(2, String.valueOf(info.getDate()));
        preparedStatement.setString(3, info.getGpsInfo().getCountry());
        preparedStatement.setString(4, info.getGpsInfo().getCity());
        preparedStatement.setDouble(5, info.getGpsInfo().getLatitude());
        preparedStatement.setDouble(6, info.getGpsInfo().getLongitude());
        preparedStatement.setString(7, info.getPhotoPath());

        return preparedStatement.execute();
    }



}
