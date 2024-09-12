package org.firstinspires.ftc.teamcode.Marcus.Hardware.Class;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 *  @Author [Marcus Turley]
 * */
public class CRServo_Hardware {

	//Creates a new array of CRServo Objects
	public static CRServo[] CRServos = new CRServo[4];

	//Initializes the CRServos
	public static void initCRServos(HardwareMap HMap) {
		//Sets all the CRServos' names
		hardwareMap(CRServos, HMap, "CRServo1", "CRServo12");
		//Sets all the CRServos' directions to forward
		setDirections(CRServos, CRServo.Direction.FORWARD, CRServo.Direction.FORWARD);
	}

	//Sets all CRServos names on the phone
	public static void hardwareMap(CRServo[] CRServos, HardwareMap hardwareMap, String... names) {
		for(int i = 0; i < names.length; i++){
			CRServos[i] = hardwareMap.get(CRServo.class, names[i]);
		}
	}
	//Sets all CRServos direction to a direction
	public static void setDirections(CRServo[] CRServos, CRServo.Direction... directions) {
		for(int i = 0; i < directions.length; i++){
			CRServos[i].setDirection(directions[i]);
		}
	}

	//Sets all CRServos power based on their index
	public static void setPowers(CRServo[] CRServos, double... powers) {
		for(int i = 0; i < powers.length; i++){
			CRServos[i].setPower(powers[i]);
		}
	}

	//Sets all CRServos power based on the length of the array
	public static void setPowers(CRServo[] CRServos, int[] indexes, double... powers) {
		for(int i = 0; i < indexes.length; i++){
			CRServos[indexes[i]].setPower(powers[i]);
		}
	}
}