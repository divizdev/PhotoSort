package ru.divizdev;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by diviz on 24.02.2017.
 */
public class ConvertGeoName {


    private final static int SIZE_PACKAGE = 1000;

    private final static String CREATE_DB_SQL_GEO_NAME = "create table if not exists GeoName (" +
            "geonameid  integer PRIMARY KEY,\n" +
            "name  text,\n" +
//            "asciiname text,\n" +
//            "alternatenames  text,\n" +
            "latitude real,\n" +
            "longitude real,\n" +
            "feature_class text    ,\n" +
            "feature_code text  ,\n" +
            "country_code text  ,\n" +
            "cc2 text,\n" +
            "admin1_code  text,\n" +
            "admin2_code  text,\n" +
            "admin3_code  text,\n" +
            "admin4_code  text,\n" +
            "population   text,\n" +
            "elevation    text,\n" +
            "dem text,\n" +
            "timezone text,\n" +
            "modification_date datetime)\n";

    private final static String INSERT_LINE_GEO_NAME = "insert into GeoName values(" +
            "%d, " +
            "'%s', " +
//            "'%s', " +
//            "'%s', " +
            "'%s', " +
            "'%s', " +
            "'%s', " +
            "'%s', " +
            "'%s', " +
            "'%s', " +
            "'%s', " +
            "'%s', " +
            "'%s', " +
            "'%s', " +
            "'%s', " +
            "'%s', " +
            "'%s', " +
            "'%s', " +
            "'%s' )";

    public Boolean CreateDB() {

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:sample.db")) {
            // create a database connection

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("drop table if exists GeoName");
            statement.executeUpdate(CREATE_DB_SQL_GEO_NAME);
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
            return false;
        }

        return true;

    }


    public boolean InsertListGeoName(List<GeoName> list) {
                try (Connection connection = DriverManager.getConnection("jdbc:sqlite:sample.db")) {
                    // create a database connection

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("PRAGMA synchronous = 0;");
            statement.executeUpdate("PRAGMA journal_mode = OFF;");
            statement.executeUpdate("BEGIN;");
            for (GeoName geoName : list) {
                InsertLineDB(statement, geoName);
            }
            statement.execute("commit;");

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    private void InsertLineDB(Statement statement, GeoName line) throws SQLException {

        String command = String.format(INSERT_LINE_GEO_NAME,
                line.getGeonameid(),
                line.getName(),
//                line.getAsciiname(),
//                line.getAlternatenames(),
                line.getLatitude(),
                line.getLongitude(),
                line.getFeatureClass(),
                line.getFeatureCode(),
                line.getCountryCode(),
                line.getCc2(),
                line.getAdmin1Code(),
                line.getAdmin2Code(),
                line.getAdmin3Code(),
                line.getAdmin4Code(),
                line.getPopulation(),
                line.getElevation(),
                line.getDem(),
                line.getTimezone(),
                line.getModificationDate());
        statement.executeUpdate(command);
    }

    public void printTable() {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:sample.db")) {
            // create a database connection

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            ResultSet rs = statement.executeQuery("select * from GeoName");
            while (rs.next()) {
                // read the result set
                System.out.println("name = " + rs.getString("geonameid"));
                System.out.println("id = " + rs.getInt("country_code"));
            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());

        }

    }

    public void LoadFileToBD(String nameFile) {
        File file = new File(nameFile);
        Integer count = 0;

        List<GeoName> listGeoName = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] arr = line.split("\t");
                if (arr.length == GeoName.LENGTH_PARAM) {
                    GeoName geoName = new GeoName(
                            Integer.valueOf(arr[0]),
                            arr[1].replace("'", ""),
                            arr[2].replace("'", ""),
                            arr[3].replace("'", ""),
                            arr[4].replace("'", ""),
                            arr[5].replace("'", ""),
                            arr[6].replace("'", ""),
                            arr[7].replace("'", ""),
                            arr[8].replace("'", ""),
                            arr[9].replace("'", ""),
                            arr[10].replace("'", ""),
                            arr[11].replace("'", ""),
                            arr[12].replace("'", ""),
                            arr[13].replace("'", ""),
                            arr[14].replace("'", ""),
                            arr[15].replace("'", ""),
                            arr[16].replace("'", ""),
                            arr[17].replace("'", ""),
                            arr[18].replace("'", "")
                    );
                    listGeoName.add(geoName);
                }
                if (listGeoName.size() >= SIZE_PACKAGE) {
                    InsertListGeoName(listGeoName);
                    listGeoName.clear();
                    count++;
                    System.out.printf("Сохранили %d записей GeoName", (count * SIZE_PACKAGE));
                    System.out.println();
                }
            }
            if (listGeoName.size() != 0) {
                InsertListGeoName(listGeoName);
                listGeoName.clear();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
