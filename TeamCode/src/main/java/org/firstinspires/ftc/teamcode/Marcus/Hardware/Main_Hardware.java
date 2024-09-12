package org.firstinspires.ftc.teamcode.Marcus.Hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Marcus.Framework.LinearOpMode_Handler;
import org.firstinspires.ftc.teamcode.Marcus.Hardware.Class.IMU_Hardware_1;
import org.firstinspires.ftc.teamcode.Marcus.Hardware.Class.Motor_Hardware_1;

public class Main_Hardware extends LinearOpMode_Handler {

    public enum RunMode {
        TeleOp,
        Autonomous
    }

    public enum DriveMode {
        POV,
        Tank,
        Arcade,
    }

    //Creates new RunMode and DriveMode Objects
    public static RunMode runMode = RunMode.TeleOp;
    public static DriveMode driveMode = DriveMode.POV;

    public static HardwareMap HMap;
    //public static Telemetry telemetry;

    //Compiles inside of the RunOpMode Autonomous
    protected void initAutonomous(RunMode nRunMode, HardwareMap nHMap, Telemetry nTelemetry){
        runMode = nRunMode;
        HMap = nHMap;
        //telemetry = nTelemetry;
        //driveMode = newDriveMode;
        //runMode = RunMode.TeleOp;
        //	Motors
        Motor_Hardware_1.initMotors(nHMap);
        //	Servos
        //Servo_Hardware.InitServos(runMode);
        //	Sensors
        IMU_Hardware_1.initIMU(nHMap);
    }

    //Compiles inside of the RunOpMode TeleOp
    protected void initTeleOp(RunMode nRunMode, HardwareMap nHMap, Telemetry nTelemetry){
        runMode = nRunMode;
        HMap = nHMap;
        //telemetry = nTelemetry;
        //driveMode = newDriveMode;
        //runMode = RunMode.TeleOp;
        //	Motors
        Motor_Hardware_1.initMotors(nHMap);
        //	Servos
        //Servo_Hardware.InitServos(runMode);
        //	Sensors
        IMU_Hardware_1.initIMU(nHMap);
    }
}