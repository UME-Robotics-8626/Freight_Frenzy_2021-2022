package org.firstinspires.ftc.teamcode.Marcus.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Marcus.Hardware.Class.Motor_Hardware_1;
import org.firstinspires.ftc.teamcode.Marcus.Hardware.Main_Hardware;
@Disabled
@Autonomous(name = "Marcus: Encoder_Test", group = "Encoder")
public class Encoder_Test extends Main_Hardware {

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();

    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.5;

    //Creates a new array of DCMotor Objects
    public static DcMotor[] motors = new DcMotor[4];
    @Override
    public void runOpMode() {
        initTeleOp(RunMode.TeleOp, hardwareMap, telemetry);

        telemetry.addData("Status", "Resetting Encoders");
        telemetry.update();

        //Sets all the motors' names
        Motor_Hardware_1.hardwareMap(motors, HMap, "front_right_drive", "front_left_drive", "back_right_drive", "back_left_drive");
        Motor_Hardware_1.brakeBehaviour(motors, DcMotor.ZeroPowerBehavior.BRAKE);
        /*
        Motor_Hardware_1.EncoderMode(motors, DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Motor_Hardware_1.EncoderMode(motors, DcMotor.RunMode.RUN_USING_ENCODER);
        */
        motors[0].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motors[1].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motors[2].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motors[3].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        motors[0].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motors[1].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motors[2].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motors[3].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0",  "Starting at %7d :%7d :%7d :%7d",
                motors[0].getCurrentPosition(),
                motors[1].getCurrentPosition(),
                motors[2].getCurrentPosition(),
                motors[3].getCurrentPosition());
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Step through each leg of the path,
        // Note: Reverse movement is obtained by setting a negative distance (not speed)
        encoderDrive(1,  10,  0, 0, 5.0);  // S1: Forward 50 Inches with 5 Sec timeout
        encoderDrive(1,   0, 10, 0, 5.0);  // S2: Turn Right 50 Inches with 5 Sec timeout
        encoderDrive(1, 0, 0, 10, 5.0);  // S3: Reverse 50 Inches with 5 Sec timeout

        sleep(1000);     // pause for servos to move

        telemetry.addData("Path", "Complete");
        telemetry.update();
    }

    /*
     *  Method to perform a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    public void encoderDrive(double speed,
                             double forward, double horizontal, double rotational,
                             double timeoutS) {
        int[] target = new int[4];

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            double maxPower = Math.abs(forward) + Math.abs(horizontal) + Math.abs(rotational);
            if(maxPower > 1) {
                forward /= maxPower;
                horizontal /= maxPower;
                rotational /= maxPower;
            }
            // Determine new target position, and pass to motor controller
            target[0] = motors[0].getCurrentPosition() + (int)(forward - horizontal - rotational * COUNTS_PER_INCH);
            target[1] = motors[1].getCurrentPosition() + (int)(forward + horizontal - rotational * COUNTS_PER_INCH);
            target[2] = motors[2].getCurrentPosition() + (int)(forward - horizontal - rotational * COUNTS_PER_INCH);
            target[3] = motors[3].getCurrentPosition() + (int)(forward + horizontal - rotational * COUNTS_PER_INCH);
            //Motor_Hardware_1.setTargetPosition(motors,target[0],target[0],target[0],target[0]);
            motors[0].setTargetPosition(target[0]);
            motors[1].setTargetPosition(target[1]);
            motors[2].setTargetPosition(target[2]);
            motors[3].setTargetPosition(target[3]);

            // Turn On RUN_TO_POSITION
            //Motor_Hardware_1.EncoderMode(motors, DcMotor.RunMode.RUN_TO_POSITION);
            motors[0].setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motors[1].setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motors[2].setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motors[3].setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            Motor_Hardware_1.setPowers(motors, Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (motors[0].isBusy() || motors[1].isBusy() || motors[2].isBusy() || motors[3].isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d :%7d :%7d", target[0], target[1], target[2], target[3]);
                telemetry.addData("Path2",  "Running at %7d :%7d :%7d :%7d",
                        motors[0].getCurrentPosition(),
                        motors[1].getCurrentPosition(),
                        motors[2].getCurrentPosition(),
                        motors[3].getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            Motor_Hardware_1.setPowers(motors, 0);

            // Turn off RUN_TO_POSITION
            //Motor_Hardware_1.EncoderMode(motors, DcMotor.RunMode.RUN_USING_ENCODER);
            motors[0].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motors[1].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motors[2].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motors[3].setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(250);   // optional pause after each move
        }
    }
}
