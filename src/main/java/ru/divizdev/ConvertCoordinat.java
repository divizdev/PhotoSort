package ru.divizdev;

import org.jetbrains.annotations.Contract;

/**
 * Created by diviz on 25.02.2017.
 */
public class ConvertCoordinat {


    @Contract(pure = true)
    public static double secToDegrees(double degrees, double minutes, double seconds) {
        return degrees + ( minutes / 60d ) + (seconds / 6000d);
    }

}
