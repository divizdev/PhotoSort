package ru.divizdev;

import java.util.Date;

/**
 * Created by diviz on 11.03.2017.
 */
public class PhotoInfo {

    private final GpsInfo gpsInfo;
    private final Date date;
    private final String photoName;
    private final String photoPath;

    public PhotoInfo(GpsInfo gpsInfo, Date date, String photoName, String photoPath) {
        this.gpsInfo = gpsInfo;
        this.date = date;
        this.photoName = photoName;
        this.photoPath = photoPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PhotoInfo photoInfo = (PhotoInfo) o;

        if (getGpsInfo() != null ? !getGpsInfo().equals(photoInfo.getGpsInfo()) : photoInfo.getGpsInfo() != null)
            return false;
        if (getDate() != null ? !getDate().equals(photoInfo.getDate()) : photoInfo.getDate() != null) return false;
        if (getPhotoName() != null ? !getPhotoName().equals(photoInfo.getPhotoName()) : photoInfo.getPhotoName() != null)
            return false;
        return getPhotoPath() != null ? getPhotoPath().equals(photoInfo.getPhotoPath()) : photoInfo.getPhotoPath() == null;
    }

    @Override
    public int hashCode() {
        int result = getGpsInfo() != null ? getGpsInfo().hashCode() : 0;
        result = 31 * result + (getDate() != null ? getDate().hashCode() : 0);
        result = 31 * result + (getPhotoName() != null ? getPhotoName().hashCode() : 0);
        result = 31 * result + (getPhotoPath() != null ? getPhotoPath().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PhotoInfo{" +
                "gpsInfo=" + gpsInfo +
                ", date=" + date +
                ", photoName='" + photoName + '\'' +
                ", photoPath='" + photoPath + '\'' +
                '}';
    }

    public GpsInfo getGpsInfo() {
        return gpsInfo;
    }

    public Date getDate() {
        return date;
    }

    public String getPhotoName() {
        return photoName;
    }

    public String getPhotoPath() {
        return photoPath;
    }
}
