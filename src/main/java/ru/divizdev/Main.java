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

    private static void accumulationStatistic(long startReadMetadata,  long finishReadMetadata,  long finishGetGeoName,  long finishInsertPhotoInfo){
        timeReadMetadata += finishReadMetadata - startReadMetadata;
        timeGetGeoName += finishGetGeoName - finishReadMetadata;
        timeInsertPhotoInfo += finishInsertPhotoInfo - finishGetGeoName;
    }

    private static void printStatistic(int iterationCount){
        long timeReadMetadata = Main.timeReadMetadata / iterationCount
           , timeGetGeoName = Main.timeGetGeoName / iterationCount
           , timeInsertPhotoInfo = Main.timeInsertPhotoInfo / iterationCount;


        System.out.println("timeReadMetadata: " + timeReadMetadata);
        System.out.println("timeGetGeoName: " + timeGetGeoName);
        System.out.println("timeInsertPhotoInfo: " + timeInsertPhotoInfo);
    }

    public static void main(String[] args) {
        // write your code here
        String dirName = "D:\\develop\\SortPhoto\\Пленка\\";
        DBGpsName gpsName = new DBGpsName();
        DBPhotoInfo dbPhotoInfo = new DBPhotoInfo();
        Integer count = 0;
        Integer i = 0;
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
             DirectoryStream<Path> dirStream = Files.newDirectoryStream(Paths.get(dirName), "*.{jpeg,jpg,png,gif}")) {
            Statement statement = connection.createStatement();

            System.out.println("Start...");

            long start = System.currentTimeMillis();

            for (Path path : dirStream) {

                long startReadMetadata = System.currentTimeMillis();
                Metadata metadata = ImageMetadataReader.readMetadata(path.toFile());
                Date date = new Date(0);
                GpsInfo gpsData = new GpsInfo("","", 0, 0);

                ExifSubIFDDirectory exif = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
                if (exif != null) {
                    date = exif.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
                }
                GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
                long finishReadMetadata = System.currentTimeMillis();
                if (gpsDirectory != null) {
                    GeoLocation geoLocation = gpsDirectory.getGeoLocation();
                    if (geoLocation != null) {


                    gpsData = gpsName.getGeoName(statement, geoLocation.getLatitude(), geoLocation.getLongitude());
                    }
                }
                long finishGetGeoName = System.currentTimeMillis();
                PhotoInfo photoInfo = new PhotoInfo(gpsData, date, path.getFileName().toString(), path.getParent().toString());
                dbPhotoInfo.insertDBPhotoInfo(statement, photoInfo);
                long finishInsertPhotoInfo = System.currentTimeMillis();

                Main.accumulationStatistic(startReadMetadata, finishReadMetadata, finishGetGeoName, finishInsertPhotoInfo);
                count++;
                if (count > 99){
                    i++;
                    System.out.println("Processed: " + count * i);
                    Main.printStatistic(count);
                    count = 0;
                }
            }

            long finish = System.currentTimeMillis();

        } catch (SQLException | IOException | ImageProcessingException e) {
            e.printStackTrace();
        }


//        ConvertGeoName convertGeoName = new ConvertGeoName();
//        convertGeoName.CreateDB();
//        convertGeoName.LoadFileToBD("allCountries.txt");

//        ConvertAlterName convertAlterName = new ConvertAlterName();
//        convertAlterName.CreateDB();
//        convertAlterName.LoadFileToBD("alternateNames.txt");

//        System.out.printf("/n Широта %f, Долгота %f",
//                ConvertCoordinat.secToDegrees(45, 26, 8.19),
//                ConvertCoordinat.secToDegrees( 12, 19, 36.62) );
    }
}
