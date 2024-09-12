package org.firstinspires.ftc.teamcode.Marcus.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Marcus.Hardware.Class.Motor_Hardware_1;
import org.firstinspires.ftc.teamcode.Marcus.Hardware.Main_Hardware;

@Autonomous(name = "Red-Forward", group = "Red")
public class Red_Forward extends Main_Hardware {

    @Override
    public void runOpMode() {
        initAutonomous(Main_Hardware.RunMode.TeleOp, hardwareMap, telemetry);
        Autonomous_Methods Methods = new Autonomous_Methods();

        waitForStart();
        Methods.strafe(-.75,650);
        Methods.turn(.75,200);
        Methods.move(1,750);
    }
}