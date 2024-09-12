package org.firstinspires.ftc.teamcode.Basic;

// @Note This lesson will explain what a Basic_Hardware is, how it functions, and how to structure it.
// @Note @Description is not essential to understanding what is going on but is useful it you lose track
//This contains the folder the porogram is found in within in the Android Studio project and must be inside program; typically it is above all the other code.

//We import LinearOpMode because it is essential for RunTime
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

//We can create a class called Basic_Hardware and extend LinearOpMode.
//LinearOpMode contains a number of methods and variables that help structure and define the program for runtime.
public class BasicHardware extends LinearOpMode {
    //A 'Basic_Hardware' is a structure that holds all of the .
    //You can imagine a hardware as a box of legos.
    //When you want to add something to your lego collection you must add something to your lego box.

    //Here is an example of how to define a motor.
    DcMotor backLeft;
    DcMotor frontLeft;
    DcMotor backRight;
    DcMotor frontRight;

    // We can do the same for Servos and CRServos
    Servo ClampRight;
    Servo ClampLeft;

    CRServo ClawRight;
    CRServo ClawLeft;

    DigitalChannel touch1;
    DigitalChannel touch2;
    DigitalChannel touch3;

    VuforiaLocalizer vuforia;
    TFObjectDetector tfod;

    private static final String VUFORIA_KEY =
            "AUH3W7//////AAABmRPeTi20x0n1vDw0WNnEaB85+/2MLZ8wvrAecldHMZYx0zw8T/JNFB7k8UfpsZGqwPNVgsWRHKlPk29EFCNgAZo9e+aqmobPLwzHEr5dm1EdFPQizLMKES9UdOSIdNb/Sx2cO8oI5iURlnGtF267JIi+oqlYZawFr0ERoqA9lmlRZpE4ak83vkqYn+2iFHXJoBvxZCvOu2O6toN7tO5LhnItem0I4iFrQNw9378YiVyIP4I7nE5XtlYHKmhiBdyTXkGyvXTUUI/lzpQVxU0Z9ynL0c4t09v54i/qQ1racrZG1CrrVMLVT8m7+L8+0dUu3Zv7zLl/G3MpvnufDs2KoA5VRjta0gnmVJoUME2npfZW";

    private static final String TFOD_MODEL_ASSET = "FreightFrenzy_BCDM.tflite";
    private static final String[] LABELS = {
            "Ball",
            "Cube",
            "Duck",
            "Marker"
    };

    //This will be called inside of Basic_RunMode_TeleOp inside the runOpMode method
    protected void initialize() {
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

        //We can follow these same steps for the  rest of the objects with a few slight changes (mainly class reference name changes).

		/*COMMENTED OUT BECAUSE WE DONT HAVE SERVOS OR SENSORS ON THE TEST ROBOT

		ClampRight = hardwareMap.get(Servo.class, "servo1");	//@Description This assigns the ClampRight to RightClamp
		ClampLeft = hardwareMap.get(Servo.class, "servo2");	//@Description This assigns the ClampLeft to LeftClamp

		ClampRight.setDirection(Servo.Direction.FORWARD);
		ClampLeft.setDirection(Servo.Direction.FORWARD);

		ClawRight = hardwareMap.get(CRServo.class, "crservo1");	//@Description This assigns the LeftClaw to RightClaw
		ClawLeft = hardwareMap.get(CRServo.class, "crservo2");	//@Description This assigns the LeftClaw to LeftClaw

		ClawRight.setDirection(CRServo.Direction.FORWARD);
		ClawLeft.setDirection(CRServo.Direction.FORWARD);

		touch1 = hardwareMap.get(DigitalChannel.class, "touchsensor1");
		touch2 = hardwareMap.get(DigitalChannel.class, "touchsensor2");
		touch3 = hardwareMap.get(DigitalChannel.class, "touchsensor3");

		// set the digital channel to input.
		touch1.setMode(DigitalChannel.Mode.INPUT);
		touch2.setMode(DigitalChannel.Mode.INPUT);
		touch3.setMode(DigitalChannel.Mode.INPUT);*/

        backLeft.setPower(0);
        frontLeft.setPower(0);
        backRight.setPower(0);
        frontRight.setPower(0);
    }

    protected void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "webcam1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    protected void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        //tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
    }

    @Override
    public void runOpMode() throws InterruptedException {

    }
}