package org.firstinspires.ftc.teamcode.Mach;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Disabled
@Autonomous(name="Pushbot: Auto Drive By Encoder", group="Pushbot")
public class EncoderAutonomous extends MachHardware {

    enum DriveMode {
        DRIVE,
        STRAFE,
        TURN
    }

    /* Declare OpMode members. */
    private final ElapsedTime	 runtime = new ElapsedTime();

    static final double	 COUNTS_PER_MOTOR_REV	 = 1440 ;	// eg: TETRIX Motor Encoder
    static final double	 DRIVE_GEAR_REDUCTION	 = 2.0 ;	 // This is < 1.0 if geared UP
    static final double	 WHEEL_DIAMETER_INCHES   = 4.0 ;	 // For figuring circumference
    static final double	 COUNTS_PER_INCH		 = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double	 DRIVE_SPEED			 = 0.3;
    static final double	 TURN_SPEED				 = 0.5;
    static final double  STRAFE_SPEED			 = 0.7;
    static final double  DEGREES_TO_INCHES		 = 11.0/90;



    @Override
    public void runOpMode() {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        initialize();

        // Send telemetry message to signify robot waiting;
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
        encoderDrive(10,  0,  0, 4.0, 60000, DriveMode.DRIVE);  // S1: Forward 20 Inches with 5 Sec timeout
        //encoderDrive(0, -10,  0, 5.0, 1000, DriveMode.STRAFE);  //Strafe 10 inches left
        //encoderDrive(0,   0, 90, 5.0, 1000, DriveMode.TURN);  //Turn 90 degrees right


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
    public void encoderDrive(double forward, double strafe, double turn, double timeoutS, long sleepTime, DriveMode dm) {
        // Ensure that the opmode is still active
        if (opModeIsActive()) {
            int[] newTarget = new int[4];
            double speed;
            boolean busyMotors = true;
            switch (dm) {
                case STRAFE:
                    speed = STRAFE_SPEED;
                    break;
                case TURN:
                    speed = TURN_SPEED;
                    break;
                default:
                    speed = DRIVE_SPEED;
            }

            // Determine new target position, and pass to motor controller
            double turnCounts = turn * DEGREES_TO_INCHES * COUNTS_PER_INCH;

            newTarget[0] = motors[0].getCurrentPosition() - (int) (forward * COUNTS_PER_INCH - strafe * COUNTS_PER_INCH + turnCounts);
            newTarget[1] = motors[1].getCurrentPosition() - (int) (forward * COUNTS_PER_INCH + strafe * COUNTS_PER_INCH - turnCounts);
            newTarget[2] = motors[2].getCurrentPosition() - (int) (forward * COUNTS_PER_INCH + strafe * COUNTS_PER_INCH + turnCounts);
            newTarget[3] = motors[3].getCurrentPosition() - (int) (forward * COUNTS_PER_INCH - strafe * COUNTS_PER_INCH - turnCounts);

            motors[0].setTargetPosition(newTarget[0]);
            motors[1].setTargetPosition(newTarget[1]);
            motors[2].setTargetPosition(newTarget[2]);
            motors[3].setTargetPosition(newTarget[3]);

            announce(newTarget[0] + "\n" +
                    newTarget[1] + "\n" +
                    newTarget[2] + "\n" +
                    newTarget[3]);
            runtime.reset();
            motors[0].setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motors[1].setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motors[2].setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motors[3].setMode(DcMotor.RunMode.RUN_TO_POSITION);

            motors[0].setPower(speed);
            motors[1].setPower(speed);
            motors[2].setPower(speed);
            motors[3].setPower(speed);

            while (opModeIsActive() &&
                    busyMotors &&
                    runtime.seconds() < timeoutS) {
                //Wait for path to complete
                busyMotors = motors[0].isBusy() && motors[1].isBusy() && motors[2].isBusy() && motors[3].isBusy();
            }
            announce(motors[0].getCurrentPosition() + "\n" +
                    motors[1].getCurrentPosition() + "\n" +
                    motors[2].getCurrentPosition() + "\n" +
                    motors[3].getCurrentPosition());

            // Stop all motion;
            motors[0].setPower(0);
            motors[1].setPower(0);
            motors[2].setPower(0);
            motors[3].setPower(0);

            sleep(sleepTime);   // optional pause after each move
        }
    }
    public void announce(String s) {
        telemetry.addLine(s);
        telemetry.update();
    }
}