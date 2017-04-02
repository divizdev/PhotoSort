package ru.divizdev;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by diviz on 24.02.2017.
 */
public class ConvertAlterName {

    private final int SIZE_PACKAGE = 1000;

    private final String dropDb = "drop table if exists AlterGeoName";
    private final String createDbSql = "create table if not exists AlterGeoName (\n" +
            "alternateNameId integer  primary key, \n" +
            "geonameid    integer ,  \n" +
            "isolanguage  text,  \n" +
            "alternateName text, \n" +
            "isPreferredName nvarchar, \n" +
            "isShortName   nvarchar,  \n" +
            "isColloquial  nvarchar, \n" +
            "isHistoric    nvarchar \n" +
            ")";

    private final String insertLineGeoName = "insert into AlterGeoName values(%d, %d, '%s', '%s', '%s', '%s', '%s', '%s')";

    private final String updateDbSql = "update GeoName2 set name = :1, ru = 1 where geonameid = :2";


    public Boolean CreateDB() {

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:sample.db")) {
            // create a database connection

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            statement.executeUpdate(dropDb);
            statement.executeUpdate(createDbSql);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean InsertListGeoName(List<AlterGeoName> list) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:sample.db")) {
            // create a database connection

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("PRAGMA synchronous = 0;");
            statement.executeUpdate("PRAGMA journal_mode = OFF;");
            statement.executeUpdate("BEGIN;");
            for (AlterGeoName item : list) {
                InsertLineDB(statement, item);
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

    private void InsertLineDB(Statement statement, AlterGeoName line) throws SQLException {

        String command = String.format(insertLineGeoName,
                line.getAlternateNameId(),
                line.getGeonameid(),
                line.getIsolanguage(),
                line.getAlternateName(),
                line.isPreferredNameDB(),
                line.isShortNameDB(),
                line.isColloquialDB(),
                line.isHistoricDB());
        statement.executeUpdate(command);
    }

    private void UpdateLineDB(Connection connection, AlterGeoName line) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(updateDbSql);
        statement.setString(1, line.getAlternateName());
        statement.setDouble(2, line.getGeonameid());
        statement.execute();
    }

    public void LoadFileToDB(String nameFile) {
        File file = new File(nameFile);
        Integer count = 0;
        int countFail = 0;

        List<AlterGeoName> listAlterGeoName = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] arr = line.split("\t");
                if (arr.length <= AlterGeoName.LENGTH_PARAM) {
                    String[] param = {"", "", "","","","","",""};
                    for (int i = 0; i < arr.length; i++) {
                        param[i] = arr[i];
                    }
                    if (param[2].equalsIgnoreCase("ru") || param[2].equalsIgnoreCase("rus")) {
                        AlterGeoName item = new AlterGeoName(
                                Integer.valueOf(arr[0]),
                                Integer.valueOf(arr[1]),
                                param[2].replace("'", ""),
                                param[3].replace("'", ""),
                                param[4].length() > 0 ? arr[4].charAt(0) : '0',
                                param[5].length() > 0 ? arr[5].charAt(0) : '0',
                                param[6].length() > 0 ? arr[6].charAt(0) : '0',
                                param[7].length() > 0 ? arr[7].charAt(0) : '0'
                        );
                        listAlterGeoName.add(item);
                    }
                }
                else {
                    countFail++;
                }
                if (listAlterGeoName.size() >= SIZE_PACKAGE) {
                    //InsertListGeoName(listAlterGeoName);
                    UpdateGeoName(listAlterGeoName);
                    listAlterGeoName.clear();
                    count++;
                    System.out.println();
                    System.out.printf("Сохранили %d записей AlterGeoName", (count * SIZE_PACKAGE));
                }
            }
            if (listAlterGeoName.size() != 0) {
//                InsertListGeoName(listAlterGeoName);
                UpdateGeoName(listAlterGeoName);
                listAlterGeoName.clear();
            }

            System.out.printf("\nЗаписей %d не прошли проверку", countFail);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Boolean UpdateGeoName(List<AlterGeoName> list) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:sample.db")) {
            // create a database connection

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("PRAGMA synchronous = 0;");
            statement.executeUpdate("PRAGMA journal_mode = OFF;");
            statement.executeUpdate("BEGIN;");
            for (AlterGeoName item : list) {
                UpdateLineDB(connection, item);
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
}
