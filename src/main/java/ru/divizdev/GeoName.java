package ru.divizdev;

/**
 * Created by diviz on 24.02.2017.
 */
public class GeoName {
    //    geonameid         : integer id of record in geonames database
//    name              : name of geographical point (utf8) varchar(200)
//    asciiname         : name of geographical point in plain ascii characters, varchar(200)
//    alternatenames    : alternatenames, comma separated, ascii names automatically transliterated, convenience attribute from alternatename table, varchar(10000)
//    latitude          : latitude in decimal degrees (wgs84)
//    longitude         : longitude in decimal degrees (wgs84)
//    feature class     : see http://www.geonames.org/export/codes.html, char(1)
//    feature code      : see http://www.geonames.org/export/codes.html, varchar(10)
//    country code      : ISO-3166 2-letter country code, 2 characters
//    cc2               : alternate country codes, comma separated, ISO-3166 2-letter country code, 200 characters
//    admin1 code       : fipscode (subject to change to iso code), see exceptions below, see file admin1Codes.txt for display names of this code; varchar(20)
//    admin2 code       : code for the second administrative division, a county in the US, see file admin2Codes.txt; varchar(80)
//    admin3 code       : code for third level administrative division, varchar(20)
//    admin4 code       : code for fourth level administrative division, varchar(20)
//    population        : bigint (8 byte int)
//    elevation         : in meters, integer
//    dem               : digital elevation model, srtm3 or gtopo30, average elevation of 3''x3'' (ca 90mx90m) or 30''x30'' (ca 900mx900m) area in meters, integer. srtm processed by cgiar/ciat.
//    timezone          : the iana timezone id (see file timeZone.txt) varchar(40)
//    modification date : date of last modification in yyyy-MM-dd format
    public final static int LENGTH_PARAM = 19;

    private final int geonameid;
    private final String name;
    private final String asciiname;
    private final String alternatenames;
    private final String latitude;
    private final String longitude;
    private final String featureClass;
    private final String featureCode;
    private final String countryCode;
    private final String cc2;
    private final String admin1Code;
    private final String admin2Code;
    private final String admin3Code;
    private final String admin4Code;
    private final String population;
    private final String elevation;
    private final String dem;
    private final String timezone;
    private final String modificationDate;

    public GeoName(int geonameid, String name, String asciiname, String alternatenames, String latitude, String longitude, String featureClass, String featureCode, String countryCode, String cc2, String admin1Code, String admin2Code, String admin3Code, String admin4Code, String population, String elevation, String dem, String timezone, String modificationDate) {
        this.geonameid = geonameid;
        this.name = name;
        this.asciiname = asciiname;
        this.alternatenames = alternatenames;
        this.latitude = latitude;
        this.longitude = longitude;
        this.featureClass = featureClass;
        this.featureCode = featureCode;
        this.countryCode = countryCode;
        this.cc2 = cc2;
        this.admin1Code = admin1Code;
        this.admin2Code = admin2Code;
        this.admin3Code = admin3Code;
        this.admin4Code = admin4Code;
        this.population = population;
        this.elevation = elevation;
        this.dem = dem;
        this.timezone = timezone;
        this.modificationDate = modificationDate;
    }

    public int getGeonameid() {
        return geonameid;
    }


    public String getName() {
        return name;
    }


    public String getAsciiname() {
        return asciiname;
    }


    public String getAlternatenames() {
        return alternatenames;
    }


    public String getLatitude() {
        return latitude;
    }


    public String getLongitude() {
        return longitude;
    }


    public String getFeatureClass() {
        return featureClass;
    }


    public String getFeatureCode() {
        return featureCode;
    }


    public String getCountryCode() {
        return countryCode;
    }


    public String getCc2() {
        return cc2;
    }


    public String getAdmin1Code() {
        return admin1Code;
    }


    public String getAdmin2Code() {
        return admin2Code;
    }


    public String getAdmin3Code() {
        return admin3Code;
    }


    public String getAdmin4Code() {
        return admin4Code;
    }


    public String getPopulation() {
        return population;
    }


    public String getElevation() {
        return elevation;
    }


    public String getDem() {
        return dem;
    }


    public String getTimezone() {
        return timezone;
    }


    public String getModificationDate() {
        return modificationDate;
    }
}
