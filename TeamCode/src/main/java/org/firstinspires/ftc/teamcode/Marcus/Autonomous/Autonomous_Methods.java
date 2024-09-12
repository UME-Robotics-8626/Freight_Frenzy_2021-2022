package org.firstinspires.ftc.teamcode.Marcus.Autonomous;

import org.firstinspires.ftc.teamcode.Marcus.Hardware.Class.Motor_Hardware_1;
import org.firstinspires.ftc.teamcode.Marcus.Hardware.Main_Hardware;

public class Autonomous_Methods extends Main_Hardware {
    int pause = 500;

    public void move(double power, int sleep) {
        double[] powers = new double[4];
        power *= -1;

        powers[0] = power;
        powers[1] = power;
        powers[2] = power;
        powers[3] = power;
        Motor_Hardware_1.setPowers(Motor_Hardware_1.motors,powers);
        sleep(sleep);
        stopDrive();
        sleep(pause);
    }

    public void strafe(double power, int sleep) {
        double[] powers = new double[4];

        powers[0] = power;
        powers[1] = -power;
        powers[2] = -power;
        powers[3] = power;
        Motor_Hardware_1.setPowers(Motor_Hardware_1.motors,powers);
        sleep(sleep);
        stopDrive();
        sleep(pause);
    }

    public void turn(double power, int sleep) {
        double[] powers = new double[4];
        power *= -1;

        powers[0] = -power;
        powers[1] = power;
        powers[2] = -power;
        powers[3] = power;
        Motor_Hardware_1.setPowers(Motor_Hardware_1.motors,powers);
        sleep(sleep);
        stopDrive();
        sleep(pause);
    }

    public static void stopDrive() {
        double[] powers = new double[4];

        powers[0] = 0;
        powers[1] = 0;
        powers[2] = 0;
        powers[3] = 0;
        Motor_Hardware_1.setPowers(Motor_Hardware_1.motors,powers);
    }

    public void duck(double power, int sleep) {
        Motor_Hardware_1.setPowers(Motor_Hardware_1.carousel,power);
        sleep(sleep);
        Motor_Hardware_1.setPowers(Motor_Hardware_1.carousel,0);
        sleep(pause);
    }
}
