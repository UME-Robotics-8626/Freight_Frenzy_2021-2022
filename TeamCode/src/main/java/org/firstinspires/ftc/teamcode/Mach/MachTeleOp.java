package org.firstinspires.ftc.teamcode.Mach;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.Range;
import java.util.Date;

//Outdated, use IMUTeleOp

@Disabled
@TeleOp(name= "RunMode: MachTeleOp", group = "TeleOp")
public class MachTeleOp extends MachHardware {

    private double[] motorPower;
    private double total;
    private int weight;
    private long timeSincePress;
    private long timeSinceDrift;
    private long delay;
    private Date currentDate;
    private static DriveMode driveMode = DriveMode.POV;
    double suggestedRange;
    double sr;

    private enum DriveMode{
        POV,
        Tank
    }

    @Override
    public void runOpMode() {
        initialize();

        suggestedRange = 0.65;
        sr = suggestedRange;
        double[] temp = new double[4];
        motorPower = new double[4];
        weight = 20;
        currentDate = new Date();
        timeSincePress = currentDate.getTime();
        delay = 1000;

        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            System.arraycopy(motorPower, 0, temp, 0, 4);
            driveChecks();
            drive(gamepad1, suggestedRange);
            motorPower = normalizeArray(motorPower,temp,weight, sr,15);
            setPower();
        }
    }
	/* For debugging
	private String arrayToString(double[] array) {
		StringBuilder string = new StringBuilder("[");
		for (double d: array) {
			string.append(d).append(", ");
		}
		string.substring(0,string.length()-2);
		string.append("]");
		return string.toString();
	}*/

    private void driveChecks() {
        drift();
        driveMode();
        turbo();
    }
    private void turbo() {
        if (gamepad1.a) {
            sr = 1;
        } else if (gamepad1.b) {
            sr = suggestedRange;
        }
    }

    private void drift() {
        if (gamepad1.left_bumper || gamepad1.right_bumper) {
            currentDate = new Date();
            long milliSeconds = currentDate.getTime();
            if (timeSinceDrift + delay >= milliSeconds) {
                if (gamepad1.left_bumper && gamepad1.right_bumper) {
                    weight = 20;
                    timeSinceDrift = milliSeconds;
                } else if (gamepad1.left_bumper && weight >= 5) {
                    weight -= 5;
                    timeSinceDrift = milliSeconds;
                } else if (gamepad1.right_bumper) {
                    weight += 5;
                    timeSinceDrift = milliSeconds;
                }
            }
        }
    }

    public double[] normalizeArray(double[] array, double[] tempArray, int tempWeight, double range, int roundTo){
        for (int i = 0; i < array.length; i++) {
            array[i]= normalize(tempArray[i],array[i],tempWeight,range,roundTo);
        }
        return array;
    }

    private void drive(Gamepad g, double range){
        switch (driveMode) {
            case Tank:
                driveTank(g.left_stick_y, g.right_stick_y, g.left_stick_x, g.right_stick_x, range);
                break;
            case POV:
                //Fallthrough
            default:
                drivePOV(g.left_stick_y,g.left_stick_x,g.right_stick_x, range);
        }
    }

    private void driveMode() {
        if (gamepad1.y) {
            currentDate = new Date();
            long milliSeconds = currentDate.getTime();
            switch (driveMode) {
                case POV:
                    if (milliSeconds >= delay + timeSincePress) {
                        timeSincePress = milliSeconds;
                        driveMode = DriveMode.Tank;
                    }
                    break;
                case Tank:
                    //Fallthrough
                default:
                    if (milliSeconds >= delay + timeSincePress) {
                        timeSincePress = milliSeconds;
                        driveMode = DriveMode.POV;
                    }
            }
        }
    }

    private void drivePOV(double forward, double right, double rotation, double range) {
        total =  Math.abs(forward) + Math.abs(right) + Math.abs(rotation);

        //This code is to ensure that no values would go over 1.0 or under -1.0, and to maintain aspect ratio so movement is not wonkified by large values
        if (total > range) {
            forward  /= total;
            right	 /= total;
            rotation /= total;
        }

        //Calculate required movement based on given inputs
        motorPower[0] = Range.clip(forward - right * 2 - rotation, -range, range);
        motorPower[1] = Range.clip(forward - right * 2 + rotation, -range, range);
        motorPower[2] = Range.clip(forward + right * .5 - rotation, -range, range);
        motorPower[3] = Range.clip(forward + right * .5 + rotation, -range, range);
    }

    private void driveTank(double left, double right, double leftH, double rightH, double range) {
        double strafe = leftH + rightH;
        total =  Math.abs(left) + Math.abs(right) + Math.abs(strafe);

        //This code is to ensure that no values would go over 1.0 or under -1.0, and to maintain aspect ratio so movement is not wonkified by large values
        if (total > range) {
            left   /= total;
            right  /= total;
            strafe /= total;
        }
        //Calculate required movement based on given inputs
        motorPower[0] = Range.clip(left + strafe, -range, range);
        motorPower[1] = Range.clip(right - strafe, -range, range);
        motorPower[2] = Range.clip(left - strafe, -range, range);
        motorPower[3] = Range.clip(right + strafe, -range, range);
    }

    private double normalize(double initial, double goal, int initialWeight, double range, int round){
        //Gets large number and small number
        double roundUp = Math.pow(10, round);
        double roundDown = Math.pow(0.1, round*0.5);

        //Ensures goal is inside the range
        goal = Range.clip(goal, -range, range);

        //Take the average weighted towards the initial
        goal += initial * initialWeight;
        goal /= initialWeight + 1;

        //Round (recommended round = 10) based on the large number
        goal = Math.floor(goal * roundUp)/roundUp;

        //Round to extremities and 0, based on the small number
        if (goal<roundDown&&goal>-roundDown){goal=0;}
        if (goal>range-roundDown){goal=range;}
        if (goal<-range+roundDown){goal=-range;}

        //Ensures goal is inside range
        goal = Range.clip(goal, -range, range);

        return goal;
    }
    private void setPower() {
        motors[0].setPower(motorPower[0]);
        motors[1].setPower(motorPower[1]);
        motors[2].setPower(motorPower[2]);
        motors[3].setPower(motorPower[3]);
    }
}