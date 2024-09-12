package org.firstinspires.ftc.teamcode.Marcus.TeleOp;

import org.firstinspires.ftc.teamcode.Marcus.Framework.Controller_1;
import org.firstinspires.ftc.teamcode.Marcus.Framework.Converter;
import org.firstinspires.ftc.teamcode.Marcus.Hardware.Class.IMU_Hardware_1;
import org.firstinspires.ftc.teamcode.Marcus.Hardware.Main_Hardware;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 *  @Author [Marcus Turley]
 * */
@TeleOp(name = "Marcus: TeleOp", group = "TeleOP")
public class Run_TeleOp extends Main_Hardware {

    @Override
    public void runOpMode() {
        initTeleOp(RunMode.TeleOp, hardwareMap, telemetry);
        TeleOp_Methods TelOp = new TeleOp_Methods();

        waitForStart();
        while (opModeIsActive() && !isStopRequested()) {
            Controller_1 Gpad1 = new Controller_1(0.0, gamepad1);
            Controller_1 Gpad2 = new Controller_1(0.0, gamepad2);
            //Controller_1 Gpads = new Controller_1(0.0, Gpad1, Gpad2);

            telemetry.addData("Heading", IMU_Hardware_1.heading());
            telemetry.addData("Target", IMU_Hardware_1.target());
            telemetry.addData("Error", IMU_Hardware_1.error());
            telemetry.addData("InRange", IMU_Hardware_1.inRange());
            telemetry.addData("InTurn", TelOp.inTurn);
            telemetry.update();

            TelOp.move(Gpad1.sticks[0][0],Gpad1.sticks[0][1],Gpad1.sticks[1][1], Gpad1.hatSticks, this);
            TelOp.intake(Gpad2.buttons,Gpad2.sticks[1][0]);
            TelOp.moveCarousel(Gpad1.bumpers[0], Gpad1.bumpers[1]);
        }
    }
}