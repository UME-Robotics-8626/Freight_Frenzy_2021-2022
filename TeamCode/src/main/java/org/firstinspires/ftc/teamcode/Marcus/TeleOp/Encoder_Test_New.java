package org.firstinspires.ftc.teamcode.Marcus.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Marcus.Hardware.Class.Motor_Hardware_1;
import org.firstinspires.ftc.teamcode.Marcus.Hardware.Main_Hardware;

@Autonomous(name = "Not-Competition!!! Encoder_Test_New", group = "Encoder")
public class Encoder_Test_New extends Main_Hardware {

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();

    static final double	 COUNTS_PER_MOTOR_REV	= 1440 ;	// eg: TETRIX Motor Encoder
    static final double	 DRIVE_GEAR_REDUCTION	= 2.0 ;	 // This is < 1.0 if geared UP
    static final double	 WHEEL_DIAMETER_INCHES   = 4.0 ;	 // For figuring circumference
    static final double	 COUNTS_PER_INCH		 = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double	 DRIVE_SPEED			 = 0.6;
    static final double	 TURN_SPEED			  = 0.5;

    @Override
    public void runOpMode() {
        initTeleOp(Main_Hardware.RunMode.TeleOp, hardwareMap, telemetry);

        telemetry.addData("Status", "Resetting Encoders");
        telemetry.update();

        //Sets all the motors' names
        Motor_Hardware_1.hardwareMap(Motor_Hardware_1.motors, HMap, "front_right_drive", "front_left_drive", "back_right_drive", "back_left_drive");

        //Motor_Hardware_1.brakeBehaviour(Motor_Hardware_1.motors, DcMotor.ZeroPowerBehavior.BRAKE);
        //Motor_Hardware_1.EncoderMode(Motor_Hardware_1.motors, DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //Motor_Hardware_1.EncoderMode(Motor_Hardware_1.motors, DcMotor.RunMode.RUN_USING_ENCODER);

        Motor_Hardware_1.motors[0].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Motor_Hardware_1.motors[1].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Motor_Hardware_1.motors[2].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Motor_Hardware_1.motors[3].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        Motor_Hardware_1.motors[0].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Motor_Hardware_1.motors[1].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Motor_Hardware_1.motors[2].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Motor_Hardware_1.motors[3].setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Path0",  "Starting at %7d :%7d :%7d :%7d",
                Motor_Hardware_1.motors[0].getCurrentPosition(),
                Motor_Hardware_1.motors[1].getCurrentPosition(),
                Motor_Hardware_1.motors[2].getCurrentPosition(),
                Motor_Hardware_1.motors[3].getCurrentPosition());
        telemetry.update();

        waitForStart();

        encoderDrive(0.05, 100, 0, 0, 500,5);
        //encoderDrive(1, 10, 0, 0, 500,5);
        //encoderDrive(1, 0, 10, 0, 500,5);
        //encoderDrive(1, 0, 0, 10, 500,5);

        sleep(1000);	 // pause for servos to move

        telemetry.addData("Path", "Complete");
        telemetry.update();
    }

    public void encoderDrive(double speed, int forward, double horizontal, double rotational, int sleep, double timeoutS) {
        int[] target = new int[4];

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
			/*
			target[2] = Motor_Hardware_1.motors[0].getCurrentPosition() + (int)(forward + horizontal + rotational * COUNTS_PER_INCH);
			target[1] = Motor_Hardware_1.motors[1].getCurrentPosition() + (int)(forward - horizontal - rotational * COUNTS_PER_INCH);
			target[0] = Motor_Hardware_1.motors[2].getCurrentPosition() + (int)(forward - horizontal + rotational * COUNTS_PER_INCH);
			target[3] = Motor_Hardware_1.motors[3].getCurrentPosition() + (int)(forward + horizontal - rotational * COUNTS_PER_INCH);

			target[0] = Motor_Hardware_1.motors[0].getCurrentPosition() + (int)(forward * COUNTS_PER_INCH);
			target[1] = Motor_Hardware_1.motors[1].getCurrentPosition() + (int)(forward * COUNTS_PER_INCH);
			target[2] = Motor_Hardware_1.motors[2].getCurrentPosition() + (int)(forward * COUNTS_PER_INCH);
			target[3] = Motor_Hardware_1.motors[3].getCurrentPosition() + (int)(forward * COUNTS_PER_INCH);

			target[0] = Motor_Hardware_1.motors[0].getCurrentPosition() + (int)(forward * COUNTS_PER_INCH);
			target[1] = Motor_Hardware_1.motors[1].getCurrentPosition() + (int)(forward * COUNTS_PER_INCH);
			target[2] = Motor_Hardware_1.motors[2].getCurrentPosition() + (int)(forward * COUNTS_PER_INCH);
			target[3] = Motor_Hardware_1.motors[3].getCurrentPosition() + (int)(forward * COUNTS_PER_INCH);
			 */

            //Motor_Hardware_1.setTargetPosition(Motor_Hardware_1.motors,target[0],target[1],target[2],target[3]);
			/*
			Motor_Hardware_1.motors[0].setTargetPosition(target[0]);
			Motor_Hardware_1.motors[1].setTargetPosition(target[1]);
			Motor_Hardware_1.motors[2].setTargetPosition(target[2]);
			Motor_Hardware_1.motors[3].setTargetPosition(target[3]);
			*/

            target[0] = Motor_Hardware_1.motors[0].getCurrentPosition() + forward;
            target[1] = Motor_Hardware_1.motors[1].getCurrentPosition() + forward;
            target[2] = Motor_Hardware_1.motors[2].getCurrentPosition() + forward;
            target[3] = Motor_Hardware_1.motors[3].getCurrentPosition() + forward;

            Motor_Hardware_1.motors[0].setTargetPosition(target[0]);
            Motor_Hardware_1.motors[1].setTargetPosition(target[1]);
            Motor_Hardware_1.motors[2].setTargetPosition(target[2]);
            Motor_Hardware_1.motors[3].setTargetPosition(target[3]);
            // Turn On RUN_TO_POSITION
            //Motor_Hardware_1.EncoderMode(Motor_Hardware_1.motors, DcMotor.RunMode.RUN_TO_POSITION);

            Motor_Hardware_1.motors[0].setMode(DcMotor.RunMode.RUN_TO_POSITION);
            Motor_Hardware_1.motors[1].setMode(DcMotor.RunMode.RUN_TO_POSITION);
            Motor_Hardware_1.motors[2].setMode(DcMotor.RunMode.RUN_TO_POSITION);
            Motor_Hardware_1.motors[3].setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();

            //Motor_Hardware_1.setPowers(Motor_Hardware_1.motors, Math.abs(speed));
            Motor_Hardware_1.motors[0].setPower(.05);
            Motor_Hardware_1.motors[1].setPower(.05);
            Motor_Hardware_1.motors[2].setPower(.05);
            Motor_Hardware_1.motors[3].setPower(.05);

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
			/*
			while (opModeIsActive() &&
					(runtime.seconds() < timeoutS) &&
					(Motor_Hardware_1.motors[0].isBusy() || Motor_Hardware_1.motors[1].isBusy() || Motor_Hardware_1.motors[2].isBusy() || Motor_Hardware_1.motors[3].isBusy())) {

				// Display it for the driver.
				telemetry.addData("Path1",  "Running to %7d :%7d :%7d :%7d", target[0], target[1], target[2], target[3]);
				telemetry.addData("Path2",  "Running at %7d :%7d :%7d :%7d",
						Motor_Hardware_1.motors[0].getTargetPosition(),
						Motor_Hardware_1.motors[1].getTargetPosition(),
						Motor_Hardware_1.motors[2].getTargetPosition(),
						Motor_Hardware_1.motors[3].getTargetPosition());
				telemetry.addData("Path3",  "Running at %7d :%7d :%7d :%7d",
						Motor_Hardware_1.motors[0].getCurrentPosition(),
						Motor_Hardware_1.motors[1].getCurrentPosition(),
						Motor_Hardware_1.motors[2].getCurrentPosition(),
						Motor_Hardware_1.motors[3].getCurrentPosition());
				telemetry.update();
			}*/
            // &&
            //					(runtime.seconds() < timeoutS)
            while (!isStopRequested() &&
                    (Motor_Hardware_1.motors[0].getCurrentPosition() < Motor_Hardware_1.motors[0].getTargetPosition() ||
                            Motor_Hardware_1.motors[1].getCurrentPosition() < Motor_Hardware_1.motors[1].getTargetPosition() ||
                            Motor_Hardware_1.motors[2].getCurrentPosition() < Motor_Hardware_1.motors[2].getTargetPosition() ||
                            Motor_Hardware_1.motors[3].getCurrentPosition() < Motor_Hardware_1.motors[3].getTargetPosition()
                    )) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d :%7d :%7d", target[0], target[1], target[2], target[3]);
                telemetry.addData("Path2",  "Running at %7d :%7d :%7d :%7d",
                        Motor_Hardware_1.motors[0].getCurrentPosition(),
                        Motor_Hardware_1.motors[1].getCurrentPosition(),
                        Motor_Hardware_1.motors[2].getCurrentPosition(),
                        Motor_Hardware_1.motors[3].getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            //Motor_Hardware_1.setPowers(Motor_Hardware_1.motors, 0);

            Motor_Hardware_1.motors[0].setPower(0);
            Motor_Hardware_1.motors[1].setPower(0);
            Motor_Hardware_1.motors[2].setPower(0);
            Motor_Hardware_1.motors[3].setPower(0);

            // Turn off RUN_TO_POSITION
            //Motor_Hardware_1.EncoderMode(Motor_Hardware_1.motors, DcMotor.RunMode.RUN_USING_ENCODER);
            Motor_Hardware_1.motors[0].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            Motor_Hardware_1.motors[1].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            Motor_Hardware_1.motors[2].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            Motor_Hardware_1.motors[3].setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(sleep);   // optional pause after each move
        }
    }
}