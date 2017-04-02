package ru.divizdev;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class Main {

    static long timeReadMetadata = 0, timeGetGeoName = 0, timeInsertPhotoInfo = 0;

    private static void accumulationStatistic(long startReadMetadata, long finishReadMetadata, long finishGetGeoName, long finishInsertPhotoInfo) {
        timeReadMetadata    += finishReadMetadata - startReadMetadata;
        timeGetGeoName      += finishGetGeoName - finishReadMetadata;
        timeInsertPhotoInfo += finishInsertPhotoInfo - finishGetGeoName;
    }

    private static void printStatistic(int iterationCount) {
        long timeReadMetadata = Main.timeReadMetadata / iterationCount,
             timeGetGeoName = Main.timeGetGeoName / iterationCount,
             timeInsertPhotoInfo = Main.timeInsertPhotoInfo / iterationCount;
        

        System.out.println("timeReadMetadata: " + timeReadMetadata);
        System.out.println("timeGetGeoName: " + timeGetGeoName);
        System.out.println("timeInsertPhotoInfo: " + timeInsertPhotoInfo);
        Main.timeReadMetadata = 0; Main.timeGetGeoName = 0; Main.timeInsertPhotoInfo = 0;
    }

    public static void main(String[] args) {

        String dirName = "D:\\develop\\SortPhoto\\Пленка\\";
        DBGpsName gpsName = new DBGpsName();
        DBPhotoInfo dbPhotoInfo = new DBPhotoInfo();
        Integer count = 0;
        Integer i = 0;
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
             DirectoryStream<Path> dirStream = Files.newDirectoryStream(Paths.get(dirName), "*.{jpeg,jpg,png,gif}")) {
            Statement statement = connection.createStatement();

            System.out.println("Start...");

            long start = System.nanoTime();

            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("PRAGMA synchronous = 0;");
            statement.executeUpdate("PRAGMA journal_mode = OFF;");
            statement.executeUpdate("BEGIN;");

            for (Path path : dirStream) {

                long startReadMetadata = System.nanoTime();
                Metadata metadata = ImageMetadataReader.readMetadata(path.toFile());
                Date date = new Date(0);
                GpsInfo gpsData = new GpsInfo("","", 0, 0);

                ExifSubIFDDirectory exif = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
                if (exif != null) {
                    date = exif.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
                }
                GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
                long finishReadMetadata = System.nanoTime();
                if (gpsDirectory != null) {
                    GeoLocation geoLocation = gpsDirectory.getGeoLocation();
                    if (geoLocation != null) {


                    gpsData = gpsName.getGeoName(statement, geoLocation.getLatitude(), geoLocation.getLongitude());
                    }
                }
                long finishGetGeoName = System.nanoTime();
                PhotoInfo photoInfo = new PhotoInfo(gpsData, date, path.getFileName().toString(), path.getParent().toString());
                dbPhotoInfo.insertDBPhotoInfo(statement, photoInfo);
                long finishInsertPhotoInfo = System.nanoTime();

                Main.accumulationStatistic(startReadMetadata, finishReadMetadata, finishGetGeoName, finishInsertPhotoInfo);
                count++;
                if (count > 99){

                    statement.execute("commit;");
                    i++;
                    System.out.println("Processed: " + count * i);
                    Main.printStatistic(count * i);
                    count = 0;
                    statement.executeUpdate("BEGIN;");

                }
            }

            long finish = System.nanoTime();

            System.out.println("Full time: " + (finish - start));

        } catch (SQLException | IOException | ImageProcessingException e) {
            e.printStackTrace();
        }

//        long start = System.nanoTime();
//        ConvertGeoName convertGeoName = new ConvertGeoName();
//        convertGeoName.CreateDB();
//
//        convertGeoName.LoadFileToBD("allCountries.txt");
//
//
//        ConvertAlterName convertAlterName = new ConvertAlterName();
////        convertAlterName.CreateDB();
//        convertAlterName.LoadFileToBD("alternateNames.txt");
//        long finish = System.nanoTime();
//        System.out.println("Full time: " + (finish - start));

//        System.out.printf("/n Широта %f, Долгота %f",
//                ConvertCoordinat.secToDegrees(45, 26, 8.19),
//                ConvertCoordinat.secToDegrees( 12, 19, 36.62) );
    }
}
