package ru.divizdev;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import jdk.internal.util.xml.impl.ReaderUTF16;

import java.io.*;
import java.nio.CharBuffer;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Main {

    public static void main(String[] args) {
	// write your code here

//        Metadata metadata = null;
//        try {
//            metadata = ImageMetadataReader.readMetadata(new File("D:\\test.jpg"));
//
//        } catch (ImageProcessingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Directory directoryDate = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
//        Directory gps = metadata.getFirstDirectoryOfType(GpsDirectory.class);
//        Collection<Tag> tags = gps.getTags();
//        for (Directory directory : metadata.getDirectories()) {
//            for (Tag tag : directory.getTags()) {
//                System.out.println(tag);
//            }
//        }
//
//        ConvertGeoName convertGeoName = new ConvertGeoName();
//        convertGeoName.CreateDB();
//        convertGeoName.LoadFileToBD("RU.txt");

        ConvertAlterName convertAlterName = new ConvertAlterName();

        convertAlterName.CreateDB();
        convertAlterName.LoadFileToBD("alternateNames.txt");

    }
}
