package ru.divizdev;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;

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
            "values('%1$s', '%2$s', '%3$s', '%4$s', '%5$f', '%6$f', '%7$s')";




    private String getCommand(PhotoInfo info) {

        return String.format(Locale.ENGLISH, insertSQLPhoto, info.getPhotoName(), info.getDate(), info.getGpsInfo().getCountry(),
                                info.getGpsInfo().getCity(), info.getGpsInfo().getLatitude(), info.getGpsInfo().getLongitude(),
                                info.getPhotoPath());
    }

    public boolean insertDBPhotoInfo(Statement statement, PhotoInfo info) throws SQLException {
        String command = getCommand(info);
        return statement.execute(command);
    }



}
