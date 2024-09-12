package org.firstinspires.ftc.teamcode.Marcus.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Marcus.Hardware.Class.Motor_Hardware_1;
import org.firstinspires.ftc.teamcode.Marcus.Hardware.Main_Hardware;

@Autonomous(name = "Blue-Backward", group = "Blue")
public class Blue_Backward extends Main_Hardware {

    @Override
    public void runOpMode() {
        initAutonomous(Main_Hardware.RunMode.TeleOp, hardwareMap, telemetry);
        Autonomous_Methods Methods = new Autonomous_Methods();

        waitForStart();

        Methods.strafe(.75,450);
        Methods.move(-1,200);
        Methods.strafe(-75,100);

        Methods.duck(.85,3000);

        Methods.strafe(1,600);
        Methods.move(-1,100);
    }
}
