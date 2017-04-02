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

    private final static String CREATE_DB_SQL_GEO_NAME = "create table GeoName2 (" +
            "geonameid  integer PRIMARY KEY,\n" +
            "name  text,\n" +
            "latitude real,\n" +
            "longitude real,\n" +
            "country_code text  ,\n" +
            "timezone text,\n" +
            "ru integer)\n";

    private final static String INSERT_LINE_GEO_NAME = "insert into GeoName2 values(" +
            "%d, " +
            "'%s', " +
            "'%s', " +
            "'%s', " +
            "'%s', " +
            "'%s', " +
            "'%d' " +
            ")";

    public Boolean CreateDB() {

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:sample.db")) {
            // create a database connection

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("drop table if exists GeoName2");
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
                line.getLatitude(),
                line.getLongitude(),
                line.getCountryCode(),
                line.getTimezone(),
                0);
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
                if (arr.length == GeoName.LENGTH_PARAM && arr[6].equalsIgnoreCase("P")) {
                    GeoName geoName = new GeoName(
                            Integer.valueOf(arr[0]),
                            arr[1].replace("'",""),
                            arr[2].replace("'",""),
                            arr[3].replace("'",""),
                            arr[4].replace("'",""),
                            arr[5].replace("'",""),
                            arr[6].replace("'",""),
                            arr[7].replace("'",""),
                            arr[8].replace("'",""),
                            arr[9].replace("'",""),
                            arr[10].replace("'",""),
                            arr[11].replace("'",""),
                            arr[12].replace("'",""),
                            arr[13].replace("'",""),
                            arr[14].replace("'",""),
                            arr[15].replace("'",""),
                            arr[16].replace("'",""),
                            arr[17].replace("'",""),
                            arr[18].replace("'","")
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
