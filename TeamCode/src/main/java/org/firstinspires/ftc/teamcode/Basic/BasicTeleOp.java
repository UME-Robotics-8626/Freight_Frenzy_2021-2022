package org.firstinspires.ftc.teamcode.Basic;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@Disabled
@TeleOp(name="Basic: TeleOpMode", group="TeleOp")//Adds the Basic TeleOpMode to the TeleOp group
public class BasicTeleOp extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    //Motors initialize
    private DcMotor backLeft = null;
    private DcMotor backRight = null;
    private DcMotor frontLeft = null;
    private DcMotor frontRight = null;

    // Setup a variable for each drive wheel to save power level for telemetry
    private double backLeftPower;
    private double backRightPower;
    private double frontLeftPower;
    private double frontRightPower;

    //Defines left stick and right stick drive inputs
    private double drive;
    private double strafe;
    private double turn;
    private double total;

    private double maxSpeed;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        backLeft  = hardwareMap.get(DcMotor.class, "back_left_drive");
        backRight  = hardwareMap.get(DcMotor.class, "back_right_drive");
        frontLeft = hardwareMap.get(DcMotor.class, "front_left_drive");
        frontRight = hardwareMap.get(DcMotor.class, "front_right_drive");

        // Set the motor directions. Two motors will need to be reversed
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);

        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        maxSpeed = 1.0;

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            drive = gamepad1.left_stick_y; //Checks the left stick on the controller for vertical offset
            strafe = gamepad1.left_stick_x; //Checks the left stick on the controller for horizontal offset
            turn  = gamepad1.right_stick_x; //Checks the right stick on the controller for horizontal offset
            total =  Math.abs(drive) + Math.abs(strafe) + Math.abs(turn);

            //This code is to ensure that no values would go over 1.0 or under -1.0, and to maintain aspect ratio so movement is not wonkified by large values

            if (total > maxSpeed) {
                drive /= total;
                strafe /= total;
                turn /= total;
            }

            //Calculate required movement based on given inputs

            backLeftPower	  = Range.clip(drive - strafe - turn, -maxSpeed, maxSpeed);
            backRightPower	  = Range.clip(drive + strafe + turn, -maxSpeed, maxSpeed);
            frontLeftPower	  = Range.clip(drive + strafe - turn, -maxSpeed, maxSpeed);
            frontRightPower   = Range.clip(drive - strafe + turn, -maxSpeed, maxSpeed);


            // Send calculated power to wheels
            backLeft.setPower(backLeftPower);
            backRight.setPower(backRightPower);
            frontLeft.setPower(frontLeftPower);
            frontRight.setPower(frontRightPower);

            // Show the elapsed game time and wheel power.
            //telemetry.addData("Status", "Run Time: " + runtime.toString());
            //telemetry.addData("Motors", "back left (%.2f), back right (%.2f), front left (%.2f), front right (%.2f)", backLeftPower, backRightPower, frontLeftPower, frontRightPower);
            //telemetry.update();
        }
    }
}