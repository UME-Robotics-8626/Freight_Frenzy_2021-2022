package org.firstinspires.ftc.teamcode.Basic;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Disabled
@Autonomous(group = "Autonomous", name = "Autonomous: DriveForward")
public class BasicAutonomousForward extends BasicHardware{

    // todo: write your code here
    public void runOpMode() {
        initialize();

        waitForStart();
        //Code goes here
        //sleep(10000);
        strafe(-.6,500);
        forward(.7,1250);
    }
    public void forward(double power, int wait) {
        backRight.setPower(power);
        backLeft.setPower(power);
        frontRight.setPower(power);
        frontLeft.setPower(power);
        sleep(wait);
        stopAllMotors();
    }
    public void turn(double power, int wait) {
        power *= -1;
        backRight.setPower(-power);
        backLeft.setPower(power);
        frontRight.setPower(-power);
        frontLeft.setPower(power);
        sleep(wait);
        stopAllMotors();
    }
    public void strafe(double power, int wait) {
        power *= -1;
        backRight.setPower(-power);
        backLeft.setPower(power);
        frontRight.setPower(power);
        frontLeft.setPower(-power);
        sleep(wait);
        stopAllMotors();
    }
    public void stopAllMotors() {
        backRight.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(0);
        frontLeft.setPower(0);
    }
}
