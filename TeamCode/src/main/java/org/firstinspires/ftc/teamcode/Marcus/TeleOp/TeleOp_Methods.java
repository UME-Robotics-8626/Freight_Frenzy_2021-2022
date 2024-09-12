package org.firstinspires.ftc.teamcode.Marcus.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Marcus.Framework.Controller_1;
import org.firstinspires.ftc.teamcode.Marcus.Framework.Converter;
import org.firstinspires.ftc.teamcode.Marcus.Hardware.Class.IMU_Hardware_1;
import org.firstinspires.ftc.teamcode.Marcus.Hardware.Class.Motor_Hardware_1;
import org.firstinspires.ftc.teamcode.Marcus.Hardware.Main_Hardware;
public class TeleOp_Methods extends Main_Hardware {

    public boolean inTurn = false;

    private double powerGain = 1;

    public void changePower(boolean increase, boolean decrease){
        if(increase && powerGain < 1) {
            powerGain += 0.1;
        } else if(decrease && powerGain > 0) {
            powerGain -= 0.1;
        }
    }

    public void move(double forward, double horizontal, double rotational, boolean[] hatSticks, LinearOpMode instance) {
        double[] powers = new double[4];

        forward *= powerGain;
        horizontal *= powerGain;
        rotational *= powerGain;

        double totalPower = Math.abs(forward) + Math.abs(horizontal) + Math.abs(rotational);

        if(totalPower > 1) {
            forward /= totalPower;
            horizontal /= totalPower;
            rotational /= totalPower;
        }

        if(rotational != 0) inTurn = true;
        else inTurn = false;

        moveCompass(IMU_Hardware_1.target(), hatSticks);

        while(!inTurn || Converter.toEvent(Converter.toBoolean(hatSticks),4) && !IMU_Hardware_1.inRange() && !instance.isStopRequested()) {
            rotational = Range.clip(IMU_Hardware_1.error(),0,1);
        }

        switch(driveMode) {
            case POV:
                powers[0] = forward + horizontal + rotational;
                powers[1] = forward - horizontal - rotational;
                powers[2] = forward - horizontal + rotational;
                powers[3] = forward + horizontal - rotational;
                break;
            case Tank:
                powers[0] = forward;
                powers[1] = horizontal;
                powers[2] = forward;
                powers[3] = horizontal;
                break;
            case Arcade:
                powers[0] = forward - horizontal;
                powers[1] = forward - horizontal;
                powers[2] = forward + horizontal;
                powers[3] = forward + horizontal;
                break;
        }
        Motor_Hardware_1.setPowers(Motor_Hardware_1.motors, powers);
    }

    public void moveCompass(double dir, boolean[] direction) {
        pointCompass(direction);
        //IMU_Hardware_1.target = dir;
    }

    public void pointCompass(boolean[] direction){
        if(Converter.toEvent(direction[0],0)) {
            inTurn = false;
            IMU_Hardware_1.compass = IMU_Hardware_1.Compass.NORTH;
        } else if(Converter.toEvent(direction[1],1)) {
            inTurn = false;
            IMU_Hardware_1.compass = IMU_Hardware_1.Compass.SOUTH;
        } else if(Converter.toEvent(direction[2],2)) {
            inTurn = false;
            IMU_Hardware_1.compass = IMU_Hardware_1.Compass.EAST;
        } else if(Converter.toEvent(direction[3],3)) {
            inTurn = false;
            IMU_Hardware_1.compass = IMU_Hardware_1.Compass.WEST;
        }
        IMU_Hardware_1.orientCompass();
    }

    public void intake(boolean[] intake, double vertical){
        double[] powers = new double[2];

        powers[0] = Converter.toInt(intake[0]) + -Converter.toInt(intake[1]);
        powers[1] = -vertical + 0.1;

        Motor_Hardware_1.setPowers(Motor_Hardware_1.intake, powers);
    }

    public void moveCarousel(boolean right, boolean left){
        double power;
        if(right) power = .85;
        else if(left) power = -.85;
        else power = 0;

        Motor_Hardware_1.setPowers(Motor_Hardware_1.carousel, power);
    }
}