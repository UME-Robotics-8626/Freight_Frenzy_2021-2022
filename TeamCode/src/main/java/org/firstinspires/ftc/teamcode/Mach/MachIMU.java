package org.firstinspires.ftc.teamcode.Mach;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

import java.util.Locale;

/**
 * @author Samuel Mach
 *
 * CONTROLS: (Default POV mode)
 * X: Headless mode
 * A: POV mode
 * B: Tank mode
 * Y: Telemetry update
 *
 * DPad:
 * Turn to angle (0 set on init)
 *
 * Right Bumper: Carousel clockwise
 * Left Bumper: Carousel counter-clockwise
 *
 * Start:
 * Help menu
 *
 * Back:
 * Reset heading to 0
 *
 * POV:
 * Left stick: linear/strafe movement
 * Right stick: rotational movement
 *
 * Tank:
 * Left stick: left side forward and turn
 * Right stick: right side forward and turn
 *
 * Headless:
 * Left stick: linear/strafe movement
 * Right stick: rotational movement
 * */

@TeleOp(name = "Mach IMU", group = "TeleOp")
public class MachIMU extends MachHardware {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    private final ElapsedTime runtime = new ElapsedTime();

    //----------------------------------------------------------------------------------------------
    // Main logic
    //----------------------------------------------------------------------------------------------

    Orientation angles;
    Acceleration gravity;
    Direction direction;

    //BL, BR, FL, FR
    double[] power;

	/*
	private double targetX;
	private double targetY;
	private double targetRX;
	private double targetRY;
	*/

    private double cushion;

    private double maxSpeed;
    private double headingOffset;

    private Swap driveMode = Swap.POV;

    //For the intake
    private double extend;
    private double vertical;
    private double spin;
    private double dp;

    @Override
    public void runOpMode() {

        initialize();
        direction = Direction.NORTH;

        power = new double[4];
        maxSpeed = 1.0;
        cushion = 5.0;
        headingOffset = 0;

        //Print controls
        telemetry.addLine(" CONTROLS: (Default POV mode) \n" +
                "X: Headless mode \n" +
                "A: POV mode \n" +
                "B: Tank mode \n" +
                "Y: Telemetry update \n" +
                "DPad: \n" +
                "Turn to angle (0 set on init) \n\n" +
                "Start for additional help");
        //telemetry.update();

        // Set up our telemetry dashboard
        composeTelemetry();

        // Wait until we're told to go
        waitForStart();

        // Start the logging of measured acceleration
        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);

        // Loop and update the dashboard
        while (opModeIsActive() && !isStopRequested()) {
            //RUN CODE
            direction = determineDirection(0);
            telemetry.addData("Direction",direction);

            move();

            telemetry.addLine(doubleArrayToString(power));

            intake();

            telemetry.addLine("Spin: " + spin + ", Extend: " + extend + ", Vertical: " + vertical + ", Duck: " + dp);

            motors[0].setPower(power[0]);
            motors[1].setPower(power[1]);
            motors[2].setPower(power[2]);
            motors[3].setPower(power[3]);
            rotors.setPower(spin);
            slide.setPower(extend);
            lift.setPower(vertical);
            duck.setPower(dp);

            telemetry.update();
        }
    }

    public void move() {
        if (gamepad1.start) {
            help();
        }

        if (gamepad1.back) {
            resetHeading();
        }
        telemetry.addLine("Heading offset: " + headingOffset);

        if (gamepad1.dpad_left || gamepad1.dpad_right || gamepad1.dpad_down || gamepad1.dpad_up)
        {turnToAngle(dpadToAngle());}
        else {
            if (gamepad1.a && !gamepad1.b) {
                driveMode = Swap.POV;
            } else if (!gamepad1.a && gamepad1.b) {
                driveMode = Swap.TANK;
            } /*else if (gamepad1.x) {
				driveMode = Swap.HEADLESS; //Will be used later for headless mode
			}*/

            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            double total;
            double total2;
            if (driveMode == Swap.HEADLESS) {
				/*
				targetX = gamepad1.left_stick_x;
				targetY = gamepad1.left_stick_y;
				targetRX = gamepad1.right_stick_x;
				targetRY = gamepad1.right_stick_y;
				double heading = getHeading();
				double targetAngle = 0;
				double motorX = 0;
				double motorY = 0;

				double cd = cushion/180; //Cushion out of 1, instead of 180

				//Create a, b, and c variables corresponding to the lengths of the triangle formed on the right stick
				double a = Math.abs(targetRY);
				double b = Math.abs(targetRX);
				double c = Math.sqrt(Math.pow(a,2) + Math.pow(b,2));

				//Find the angle of the triangle
				targetAngle = Math.acos((Math.pow(a,2)+Math.pow(b,2)-Math.pow(c,2))/2*a*b);
				boolean doNothing = false;

				//Convert the angle from triangle form to usable input
				if (targetRX > cd) {
					if (targetRY > cd) {
						targetAngle = newAngle(0,targetAngle);
					} else if (targetRY < -cd) {
						targetAngle = newAngle(180, -targetAngle);
					} else {
						targetAngle = -90;
					}
				} else if (targetRX < -cd){
					if (targetRY > 0) {
						targetAngle = newAngle(0,-targetAngle);
					} else if (targetRY < -cd){
						targetAngle = newAngle(180,targetAngle);
					} else {
						targetAngle = 90;
					}
				} else if (targetRY < -cd){
					targetAngle = -180;
				} else if (targetRY > cd){
					targetAngle = 0;
				} else {
					doNothing = true;
				}

				//Turn that angle into motor power
				if (!doNothing) {
					turnToAngle(targetAngle); //Motor powers for the angle are now stored in power[]
				}
				//Check latitudinal and longitudinal requirements to reach target location

				//Set motor power based on rotation and longitude/latitude requirements
				 */

            } else if (driveMode == Swap.TANK) {
                double leftForward = gamepad1.left_stick_y;
                double rightForward = gamepad1.right_stick_y;
                double leftStrafe = gamepad1.left_stick_x;
                double rightStrafe = gamepad1.right_stick_x;

                total = Math.abs(leftForward) + Math.abs(leftStrafe);
                if (total > maxSpeed) {
                    leftStrafe /= total;
                    leftForward /= total;
                }
                total = Math.abs(rightForward) + Math.abs(rightStrafe);
                if (total > maxSpeed) {
                    rightStrafe /= total;
                    rightForward /= total;
                }

                power[0]	  = Range.clip(leftForward - leftStrafe, -maxSpeed, maxSpeed);
                power[1]	  = Range.clip(rightForward + rightStrafe, -maxSpeed, maxSpeed);
                power[2]	  = Range.clip(leftForward + leftStrafe, -maxSpeed, maxSpeed);
                power[3]	  = Range.clip(rightForward - rightStrafe, -maxSpeed, maxSpeed);
            } else {
                //POV Mode, default
                double drive = gamepad1.left_stick_y; //Checks the left stick on the controller for vertical offset
                double strafe = gamepad1.left_stick_x; //Checks the left stick on the controller for horizontal offset
                double turn = gamepad1.right_stick_x; //Checks the right stick on the controller for horizontal offset

                total =  Math.abs(drive) + Math.abs(strafe) + Math.abs(turn);

                double drive2 = gamepad2.left_stick_y/4; //Checks the left stick on the controller for vertical offset
                double turn2 = gamepad2.right_stick_x/4; //Checks the right stick on the controller for horizontal offset
                total2 =  Math.abs(drive2) + Math.abs(turn2);

                //This code is to ensure that no values would go over 1.0 or under -1.0, and to maintain aspect ratio so movement is not wonkified by large values

                if (total + total2 > maxSpeed) {
                    drive /= total;
                    drive2 /= total;
                    strafe /= total;
                    turn /= total;
                    turn2 /= total;
                }

                //Calculate required movement based on given inputs
                /*
                power[0]	= Range.clip(drive - strafe + turn, -maxSpeed, maxSpeed);
                power[1]	= Range.clip(drive + strafe - turn, -maxSpeed, maxSpeed);
                power[2]	= Range.clip(drive + strafe + turn, -maxSpeed, maxSpeed);
                power[3]	= Range.clip(drive - strafe - turn, -maxSpeed, maxSpeed);
                */
                power[0]	= Range.clip((drive + drive2) - strafe + (turn + turn2), -maxSpeed, maxSpeed);
                power[1]	= Range.clip((drive + drive2) + strafe - (turn + turn2), -maxSpeed, maxSpeed);
                power[2]	= Range.clip((drive + drive2) + strafe + (turn + turn2), -maxSpeed, maxSpeed);
                power[3]	= Range.clip((drive + drive2) - strafe - (turn + turn2), -maxSpeed, maxSpeed);
            }
        }
    }

    public void intake() {
        extend = Range.clip(gamepad2.right_trigger - gamepad2.left_trigger,-1,1);
        vertical = Range.clip(-gamepad2.right_stick_y + 0.1,-maxSpeed,maxSpeed)/1.5;
        if (gamepad2.b && !gamepad2.a) {
            spin = -0.8;
        } else if (gamepad2.a && !gamepad2.b) {
            spin = 0.8;
        } else {
            spin = 0;
        }
        if (gamepad1.right_bumper && !gamepad1.left_bumper) {dp = .9;}
        else if (gamepad1.left_bumper && !gamepad1.right_bumper) {dp = -.9;}
        else {dp = 0;}
    }

    public int dpadToAngle() {
        //Multi Angles first, then cardinal directions
        if (gamepad1.dpad_down && gamepad1.dpad_left) {
            return 135;
        } else if (gamepad1.dpad_down && gamepad1.dpad_right) {
            return -135;
        } else if (gamepad1.dpad_up && gamepad1.dpad_left) {
            return 45;
        } else if (gamepad1.dpad_up && gamepad1.dpad_right) {
            return -45;
        } else if (gamepad1.dpad_left) {
            return 90;
        } else if (gamepad1.dpad_right) {
            return -90;
        } else if (gamepad1.dpad_up) {
            return 0;
        } else if (gamepad1.dpad_down) {
            return -180;
        }
        telemetry.addLine("Direction failed, no dpads pressed");
        return 0;
    }

    public void turnToAngle(double angle) {
        double heading = getHeading();
        angle = newAngle(angle,0);
        double distance = distanceToAngle(heading, angle);
        double powerMargin;

        if (Math.abs(distance) > 90) {
            powerMargin = maxSpeed;
        } else if (Math.abs(distance) > cushion) {
            powerMargin = 3*maxSpeed/4;
        } else {
            powerMargin = 0;
        }

        if (distance < 0) {
            powerMargin *= -1;
        }

        power[0] = powerMargin;
        power[1] = -powerMargin;
        power[2] = powerMargin;
        power[3] = -powerMargin;
    }

    //This method takes a double input for the North direction and returns the direction the robot is facing
    public Direction determineDirection(double north) {
        double heading = getHeading();

        north = newAngle(north, 0); //Ensure north is between -180 and 179.9... (inclusive)
        //Return cardinal directions
        double south = newAngle(north, 180);
        double east = newAngle(north, -90);
        double west = newAngle(north, 90);
        if (isBetween(newAngle(north, -23), newAngle(north,23), heading)) {
            return Direction.NORTH;
        } else if (isBetween(newAngle(east, -23), newAngle(east,23), heading)) {
            return Direction.EAST;
        } else if (isBetween(newAngle(south, -23), newAngle(south,23), heading)) {
            return Direction.SOUTH;
        } else if (isBetween(newAngle(west, -23), newAngle(west,23), heading)) {
            return Direction.WEST;
        }
        //Return secondary directions
        double northeast = newAngle(north, -45);
        double northwest = newAngle(north, 45);
        double southeast = newAngle(south, 45);
        double southwest = newAngle(west, 45);
        if (isBetween(newAngle(northeast, -23), newAngle(northeast,23), heading)) {
            return Direction.NORTHEAST;
        } else if (isBetween(newAngle(northwest, -23), newAngle(northwest,23), heading)) {
            return Direction.NORTHWEST;
        } else if (isBetween(newAngle(southeast, -23), newAngle(southeast,23), heading)) {
            return Direction.SOUTHEAST;
        } else if (isBetween(newAngle(southwest, -23), newAngle(southwest,23), heading)) {
            return Direction.SOUTHWEST;
        }

        telemetry.addLine("Failed to find correct direction!");
        return Direction.FAIL;
    }

    //This method takes the bottom, top, and between and returns whether it is between the bottom and top
    public boolean isBetween(double bottom, double top, double toCheck) {
        //Ensure angles are in the proper range
        bottom = newAngle(bottom, 0);
        top = newAngle(top, 0);
        toCheck = newAngle(toCheck, 0);

        boolean between = top >= toCheck && bottom <= toCheck;
        boolean wrap = bottom > top;
        if (wrap) {
            between = bottom <= toCheck || top >= toCheck;
        }
        return between;
    }

    public double distanceToAngle(double main, double target) {
        main = newAngle(main, 0);
        target = newAngle(target, 0);
        double distance = target - main;
		if (Math.abs(distance) > 180) {
			if (distance > 0) {
				distance -= 360;
			} else {
				distance += 360;
			}
		}
        distance = newAngle(distance, 0);
        return distance;
    }

    //This method takes any angle and converts it to an angle between -180 and 179.9... (inclusive) after adding an offset
    public double newAngle(double angle, double offset) {
        double newAngle = angle + offset;
        int i = 0;
        while ((newAngle < -180 || newAngle >= 180) && !isStopRequested()) {
            if (newAngle < -180) {
                newAngle += 360;
            } else if (newAngle >= 180) {
                newAngle -= 360;
            };
            i++;
            if (i > 25) {
                telemetry.addLine("LOOP BROKEN, ERROR AT " + (angle + offset));
                break;
            }
        }
        return newAngle;
    }

    public void resetHeading() {
        headingOffset = -getHeading();
    }

    //This method returns which direction the robot is facing
    public double getHeading() {
        return newAngle(imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle, headingOffset);
    }

    public String doubleArrayToString(double[] array) {
        StringBuilder toString = new StringBuilder("[");
        for (double d: array) {
            toString.append(d).append(", ");
        }
        toString = new StringBuilder(toString.substring(0, toString.length() - 2));
        toString.append("]");
        return toString.toString();
    }

    public void help() {
        telemetry.clear();
        int help = 0;
        double wait = runtime.milliseconds();
        while (gamepad1.start && !isStopRequested()) {
            telemetry.addLine("----------------==================----------------");
            telemetry.addLine("Menu " + (help + 1) + " of 3");
            telemetry.addLine("Use the bumpers to scroll\n" +
                    "Release \"start\" to leave menu");
            switch (help) {
                case 0:
                    telemetry.addLine(" CONTROLS: (Default POV mode) \n" +
                            "X: Headless mode \n" +
                            "A: POV mode \n" +
                            "B: Tank mode \n" +
                            "Y: Telemetry update \n" +
                            "DPad: Turn to angle (0 set on init)");
                    break;
                case 1:
                    telemetry.addLine("POV Mode (Default): \n" +
                            "Left stick moves \n" +
                            "Right stick rotates");
                    telemetry.addLine("Tank Mode:\n" +
                            "Left stick controls left side\n" +
                            "Right stick controls right side");
                    break;
                case 2:
                    telemetry.addLine("\nHeadless Mode:\n" +
                            "Operates exactly the same as POV,\n" +
                            "Except the direction the robot is facing\n" +
                            "does not affect which way the robot moves\n" +
                            "And rotation is turn-to instead of turn-direction");
                    break;
                default:
                    telemetry.addLine("A strange error has occurred. It seems the help menu is temporarily unavailable.");
                    help = 0;
            }
            telemetry.addLine("----------------==================----------------");
            telemetry.update();
            if (gamepad1.right_bumper && !gamepad1.left_bumper && help < 2 && runtime.milliseconds() > wait + 3000) {
                help++;
                wait = runtime.milliseconds();
            } else if (gamepad1.left_bumper && !gamepad1.right_bumper && help > 0 && runtime.milliseconds() > wait + 2000) {
                help--;
                wait = runtime.milliseconds();
            }
        }
    }

    //----------------------------------------------------------------------------------------------
    // Telemetry Configuration
    //----------------------------------------------------------------------------------------------
    void composeTelemetry() {

        // At the beginning of each telemetry update, grab a bunch of data
        // from the IMU that we will then display in separate lines.
        telemetry.addAction(new Runnable() { @Override public void run()
        {
            // Acquiring the angles is relatively expensive; we don't want
            // to do that in each of the three items that need that info, as that's
            // three times the necessary expense.
            angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            gravity  = imu.getGravity();
        }
        });

        telemetry.addLine()
                .addData("status", new Func<String>() {
                    @Override public String value() {
                        return imu.getSystemStatus().toShortString();
                    }
                })
                .addData("calib", new Func<String>() {
                    @Override public String value() {
                        return imu.getCalibrationStatus().toString();
                    }
                });

        telemetry.addLine()
                .addData("heading", new Func<String>() {
                    @Override public String value() {
                        return formatAngle(angles.angleUnit, angles.firstAngle);
                    }
                })
                .addData("roll", new Func<String>() {
                    @Override public String value() {
                        return formatAngle(angles.angleUnit, angles.secondAngle);
                    }
                })
                .addData("pitch", new Func<String>() {
                    @Override public String value() {
                        return formatAngle(angles.angleUnit, angles.thirdAngle);
                    }
                });

        telemetry.addLine()
                .addData("grvty", new Func<String>() {
                    @Override public String value() {
                        return gravity.toString();
                    }
                })
                .addData("mag", new Func<String>() {
                    @Override public String value() {
                        return String.format(Locale.getDefault(), "%.3f",
                                Math.sqrt(gravity.xAccel*gravity.xAccel
                                        + gravity.yAccel*gravity.yAccel
                                        + gravity.zAccel*gravity.zAccel));
                    }
                });
    }

    //----------------------------------------------------------------------------------------------
    // Formatting
    //----------------------------------------------------------------------------------------------

    String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    String formatDegrees(double degrees){
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }
}