package org.firstinspires.ftc.teamcode.Marcus.Hardware.Class;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 *  @Author [Marcus Turley]
 * */
public class Servo_Hardware {

	//Creates a new array of Servos Objects
	public static Servo[] servos = new Servo[2];

	//Initializes the Servos
	public static void InitServos(HardwareMap hMap) {
		//Sets all the Servos' names
		hardwareMap(servos, hMap, "servo1", "servo2");
/*
		//Checks the RunMode if RunMode is not TeleOp or Autonomous
		if(runMode == Robot_Hardware.RunMode.TeleOp) {
			SetPositions(Servos,0,0,0,0);
		} else if(runMode == Robot_Hardware.RunMode.Autonomous) {
		} else {
			Prints unknown pointer exception
			Robot_Hardware.telemetry.addLine("Servo_Hardware:");
			Robot_Hardware.telemetry.addLine("RunMode is set to unknown variable.");
			Robot_Hardware.telemetry.update();
		}*/
	}

	//Sets all Servos names on the phone
	public static void hardwareMap(Servo[] servos, HardwareMap hardwareMap, String... names) {
		for(int i = 0; i < names.length; i++){
			servos[i] = hardwareMap.get(Servo.class, names[i]);
		}
	}

	//Sets all Servos positions based on their index
	public static void setPositions(Servo[] servos, double... positions) {
		for(int i = 0; i < positions.length; i++){
			if(servos.length != positions.length)
				positions[i] = 1;
			servos[i].setPosition(positions[i]);
		}
	}

	//Sets all Servos positions based on the length of the array
	public static void setPositions(Servo[] servos, int[] indexes, double... positions) {
		for(int i = 0; i < indexes.length; i++){
			servos[indexes[i]].setPosition(positions[i]);
		}
	}
}
