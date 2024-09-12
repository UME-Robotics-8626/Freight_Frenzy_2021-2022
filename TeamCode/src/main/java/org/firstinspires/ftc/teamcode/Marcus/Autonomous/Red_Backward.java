package org.firstinspires.ftc.teamcode.Marcus.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.Marcus.Hardware.Class.Motor_Hardware_1;
import org.firstinspires.ftc.teamcode.Marcus.Hardware.Main_Hardware;

@Autonomous(name = "Red-Backward", group = "Red")
public class Red_Backward extends Main_Hardware {

    @Override
    public void runOpMode() {
        initAutonomous(Main_Hardware.RunMode.TeleOp, hardwareMap, telemetry);
        Autonomous_Methods Methods = new Autonomous_Methods();

        waitForStart();

        Methods.strafe(-.75,500);
        Methods.turn(-1,750);

        Methods.strafe(-1,650);
        Methods.move(-.25,1000);
        Methods.duck(-1,3000);

        Methods.move(1,185);
        Methods.strafe(-1,50);
    }
}