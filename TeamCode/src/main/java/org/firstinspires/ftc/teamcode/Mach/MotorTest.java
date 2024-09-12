package org.firstinspires.ftc.teamcode.Mach;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Disabled
@Autonomous(name="Motor Test", group="Autonomous")
public class MotorTest extends MachHardware {
    @Override
    public void runOpMode() {
        initialize();
        waitForStart();

        motors[0].setPower(1);
        telemetry.addLine("BackLeft");
        telemetry.update();
        sleep(500);
        motors[0].setPower(0);
        motors[1].setPower(1);
        telemetry.addLine("BackRight");
        telemetry.update();
        sleep(500);
        motors[1].setPower(0);
        motors[2].setPower(1);
        telemetry.addLine("FrontLeft");
        telemetry.update();
        sleep(500);
        motors[2].setPower(0);
        motors[3].setPower(1);
        telemetry.addLine("FrontRight");
        telemetry.update();
        sleep(500);
        motors[3].setPower(0);
    }
}