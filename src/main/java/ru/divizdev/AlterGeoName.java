package ru.divizdev;

/**
 * Created by diviz on 24.02.2017.
 */
public class AlterGeoName {
//    The table 'alternate names' :
//-----------------------------
//    alternateNameId   : the id of this alternate name, int
//    geonameid         : geonameId referring to id in table 'geoname', int
//    isolanguage       : iso 639 language code 2- or 3-characters; 4-characters 'post' for postal codes and 'iata','icao' and faac for airport codes, fr_1793 for French Revolution names,  abbr for abbreviation, link for a website, varchar(7)
//    alternate name    : alternate name or name variant, varchar(400)
//    isPreferredName   : '1', if this alternate name is an official/preferred name
//    isShortName       : '1', if this is a short name like 'California' for 'State of California'
//    isColloquial      : '1', if this alternate name is a colloquial or slang term
//    isHistoric        : '1', if this alternate name is historic and was used in the past


    private final int alternateNameId;
    private final int geonameid;
    private final String isolanguage;
    private final String alternateName;
    private final boolean isPreferredName;
    private final boolean isShortName;
    private final boolean isColloquial;
    private final boolean isHistoric;

    public final static int LENGTH_PARAM = 8;

    public AlterGeoName(int alternateNameId, int geonameid, String isolanguage, String alternateName,
                        char isPreferredName, char isShortName, char isColloquial, char isHistoric) {
        this.alternateNameId = alternateNameId;
        this.geonameid = geonameid;
        this.isolanguage = isolanguage;
        this.alternateName = alternateName;

        this.isPreferredName = convertCharToBoolean(isPreferredName);
        this.isShortName = convertCharToBoolean(isShortName);
        this.isColloquial = convertCharToBoolean(isColloquial);
        this.isHistoric = convertCharToBoolean(isHistoric);
    }


    private boolean convertCharToBoolean(char value) {
        return value == '1' ? true : false;
    }

    private char convertBooleanToChar(boolean value){
        return value ? '1' : '0';
    }

    public int getAlternateNameId() {
        return alternateNameId;
    }

    public int getGeonameid() {
        return geonameid;
    }

    public String getIsolanguage() {
        return isolanguage;
    }

    public String getAlternateName() {
        return alternateName;
    }

    public boolean isPreferredName() {
        return isPreferredName;
    }

    public boolean isShortName() {
        return isShortName;
    }

    public boolean isColloquial() {
        return isColloquial;
    }

    public boolean isHistoric() {
        return isHistoric;
    }

    public char isPreferredNameDB() {
        return convertBooleanToChar(isPreferredName);
    }

    public char isShortNameDB() {
        return convertBooleanToChar(isShortName);
    }

    public char isColloquialDB() {
        return convertBooleanToChar(isColloquial);
    }

    public char isHistoricDB() {
        return convertBooleanToChar(isHistoric);
    }
}
