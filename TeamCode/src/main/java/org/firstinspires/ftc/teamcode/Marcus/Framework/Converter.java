package org.firstinspires.ftc.teamcode.Marcus.Framework;

import com.qualcomm.robotcore.util.ElapsedTime;

public class Converter extends LinearOpMode_Handler {
    //Converts any number to boolean
    public static boolean toBoolean(int value, double range) {
        return value > range;
    }

    //Converts any number to boolean
    public static boolean toBoolean(double value, double range) {
        return value > range;
    }

    //Converts any number to boolean
    public static boolean toBoolean(boolean[] value) {
        boolean finalValue = true;
        for(boolean b : value){
            finalValue &= b;
        }
        return finalValue;
    }

    //Converts boolean to int
    public static int toInt(boolean input) {
        return input ? 1 : 0;
    }

    public static boolean[] events = new boolean[1000];
    //Converts the value to an Event allowing for the boolean to occur true only once when it is set to true.
    public static boolean toEvent(boolean input, int ID) {
        if(input && events[ID]) {
            events[ID] = false;
            return true;
        }
        if(!input) events[ID] = true;
        return false;
    }

    private ElapsedTime runtime = new ElapsedTime();

    //Converts the value to an Event allowing for the boolean to occur true only once when it is set to true.
    public boolean persist(boolean input, double time) {
        if(time <= 0.001) time = 0.001;
        if(runtime.seconds() >= time) {
            runtime.reset();
            return input;
        }
        return false;
    }

    //Converts the value to the normalized value
    public static double inverse(double value, double min, double max) {
        if(value != 0 || min != 0 || max != 0)
            return (max + min) - value;
        return 0;
    }

    //Converts the value to the normalized value
    public static double normalize(double value, double min, double max) {
        if(value != 0 || min != 0 || max != 0)
            return Math.max(0,Math.min(1,(value - min) / (max - min)));
        return 0;
    }

    //Converts the value to the normalized value between a custom range
    public static double forceNormalize(double value, double min, double max) {
        return Math.max(min,Math.min(max,((value - min) / (max - min)) * max));
    }
}