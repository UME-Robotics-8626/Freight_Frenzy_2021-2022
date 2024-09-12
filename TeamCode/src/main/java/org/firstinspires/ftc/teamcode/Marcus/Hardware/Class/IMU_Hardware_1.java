package org.firstinspires.ftc.teamcode.Marcus.Hardware.Class;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Marcus.Framework.Converter;

/**
 *  @Author [Marcus Turley]
 * */
public class IMU_Hardware_1 {

	//Creates an new IMU Object
	public static BNO055IMU imu;

	//Creates variables for the orientation and gravity
	public static Orientation angles;

	//Creates a variable for the target heading
	public static double target;

	//Creates a variable for the target heading
	public static double range = 5;

	//Creates 2 variables that contain the maximum and minimum range the heading will be between
	private static final double min = 0;
	private static final double max = 360;

	private static final double scale = .02;

	//Creates an enum that contains different directions
	public enum Compass {
		NORTH,
		SOUTH,
		EAST,
		WEST
	}

	//Creates an instance of the enum Compass
	public static Compass compass = Compass.NORTH;

	//Initializes the IMU
	public static void initIMU(HardwareMap HMap){
		//Sets name of the IMU
		imu = HMap.get(BNO055IMU.class, "imu");

		//Sets parameters of the IMU
		BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
		parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
		parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
		parameters.calibrationDataFile = "BNO055IMUCalibration.json"; //Need to Calibrate

		//Sets executes the parameters of theIMU
		imu.initialize(parameters);
	}

	public static void orientCompass() {
		switch(IMU_Hardware_1.compass) {
			case NORTH:
				target = 0;
				break;
			case SOUTH:
				target = 180;
				break;
			case EAST:
				target = 90;
				break;
			case WEST:
				target = -90;
				break;
		}
	}

	//Gets the IMU's current orientation
	public static double heading() {
		angles = imu.getAngularOrientation();
		return Converter.inverse(angles.firstAngle,-180,180);
		/*
		if(IMU_Hardware_1.angles.firstAngle < 0){
			return Converter.inverse(IMU_Hardware_1.angles.firstAngle + 360, 0, 360);
		}
		return Converter.inverse(IMU_Hardware_1.angles.firstAngle, 0, 360);*/
		//return Convert.normalize((double) IMU_Hardware_1.angles.firstAngle, (double)-180, (double)180, headingMin, headingMax);
	}

	public static double target() {
		//target = Converter.forceNormalize(target, min, max);
		/*
		if(target < 0){
			if(target == 0)
				return 0;
			return Converter.inverse(target + 360, 0, 360);
		}
		return Converter.inverse(target, 0, 360);*/
		return target;
	}

	//Checks to see if the IMU's current heading is in range of the target heading
	public static boolean inRange() {
		return Math.abs(heading()) < range;
	}

	//Gets the IMU's distance to target heading
	public static double error() {
		//return Converter.forceNormalize((heading() - target()) * scale,-1,1);
		double distance = target() - heading();
		if (Math.abs(distance) > 180) {
			if (distance > 0) {
				distance -= 360;
			} else {
				distance += 360;
			}
		}
		return distance;
		/*
		double distance = heading() - target;
		if(Math.abs(distance) > 180){
			if(distance > 0){
				distance -= 360;
			} else {
				distance += 360;
			}
		}
		return Converter.normalize(-distance * scale, 0, 1);*/
	}

	//Gets the IMU's distance to target heading
	public static double error(double targetHeading) {
		return Converter.normalize((heading() - targetHeading) * scale,-1,1);
	}
}