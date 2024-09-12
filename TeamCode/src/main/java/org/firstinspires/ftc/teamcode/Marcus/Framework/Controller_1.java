package org.firstinspires.ftc.teamcode.Marcus.Framework;

import com.qualcomm.robotcore.hardware.Gamepad;

public class Controller_1 extends LinearOpMode_Handler {

    private static final Converter converter = new Converter();

    //Instantiates arrays for the Gamepad inputs
    public double[][] sticks = new double[2][2];
    public boolean[] sticksButtons = new boolean[2];
    public double[] triggers = new double[2];

    public boolean[] buttons = new boolean[4];
    public boolean[] hatSticks = new boolean[4];
    public boolean[] bumpers = new boolean[2];
    public boolean[] menus = new boolean[2];

    public double stickDeadband = 0.05;

    //Concatenates controller inputs together
    public Controller_1 (double newStickDeadband, Gamepad Gamepad) {
        sticks[0][0] = Gamepad.left_stick_y;
        sticks[0][1] = Gamepad.left_stick_x;
        sticksButtons[0] = Gamepad.left_stick_button;

        sticks[1][0] = Gamepad.right_stick_y;
        sticks[1][1] = Gamepad.right_stick_x;
        sticksButtons[1] = Gamepad.right_stick_button;

        triggers[0] = Gamepad.right_trigger;
        triggers[1] = Gamepad.left_trigger;

        buttons[0] = Gamepad.a;
        buttons[1] = Gamepad.b;
        buttons[2] = Gamepad.x;
        buttons[3] = Gamepad.y;

        hatSticks[0] = Gamepad.dpad_up;
        hatSticks[1] = Gamepad.dpad_down;
        hatSticks[2] = Gamepad.dpad_right;
        hatSticks[3] = Gamepad.dpad_left;

        bumpers[0] = Gamepad.right_bumper;
        bumpers[1] = Gamepad.left_bumper;

        menus[0] = Gamepad.start;
        menus[1] = Gamepad.guide;

        stickDeadband = newStickDeadband;
    }

    //Sets the Gamepad inputs
    public Controller_1 (double newStickDeadband, Controller_1... Gamepads) {
        for(Controller_1 Gpad : Gamepads) {
            sticks[0][0] += Gpad.sticks[0][0];
            sticks[0][1] += Gpad.sticks[0][1];
            sticksButtons[0] |= Gpad.sticksButtons[0];

            sticks[1][0] += Gpad.sticks[1][0];
            sticks[1][1] += Gpad.sticks[1][1];
            sticksButtons[1] |= Gpad.sticksButtons[1];

            triggers[0] += Gpad.triggers[0];
            triggers[1] += Gpad.triggers[1];

            buttons[0] |= Gpad.buttons[0];
            buttons[1] |= Gpad.buttons[1];
            buttons[2] |= Gpad.buttons[2];
            buttons[3] |= Gpad.buttons[3];

            hatSticks[0] |= Gpad.hatSticks[0];
            hatSticks[1] |= Gpad.hatSticks[1];
            hatSticks[2] |= Gpad.hatSticks[2];
            hatSticks[3] |= Gpad.hatSticks[3];

            bumpers[0] |= Gpad.bumpers[0];
            bumpers[1] |= Gpad.bumpers[1];

            menus[0] |= Gpad.menus[0];
            menus[1] |= Gpad.menus[1];

            stickDeadband = newStickDeadband;
        }
    }

    //Sets the sticks to run under certain conditions
    public void conditionSticks() {
        for(double i = 0, j = 0; i < sticks.length - 1; i += 0.5, j++) {
            if(sticks[(int)i][(int)j] < stickDeadband)
                sticks[(int)i][(int)j] = 0;
            if(j >= 1){
                j = 0;
            }
        }
    }

    //An easier way to reference the toEvent Method for controller inputs
    public static boolean press(boolean input, int ID) {
        return Converter.toEvent(input, ID);
    }

    //An easier way to reference the toEvent Method for controller inputs
    public static boolean press(double input, int ID) {
        return Converter.toEvent(Converter.toBoolean(input, 1), ID);
    }

    //Persists the input for a set amount of time
    public boolean persist(boolean input, int time) {
        if(input){
            return true;
        }
        sleep(time);
        return false;
    }
/*
	//Returns when a combination of inputs have been pressed starting from the first value put into the method
	public static <T> boolean Rollover(T ... inputs) {
		boolean[] boolInputs = new boolean[inputs.length];
		for(int i = 0; i < inputs.length; i++){
			if(inputs[i] == Integer.class || inputs[i] == Double.class ){
				boolInputs[i] = (int)inputs[i] != 0;
			}
		}

		for(int i = 0; i < inputs.length && boolInputs[i]; i++) {
			return boolInputs[i];
		}
		return false;
	}*/
}
