/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

import robot.subsystems.climb.commands.*;

import edu.wpi.first.wpilibj.buttons.POVButton;
import org.ghrobotics.lib.mathematics.twodim.geometry.Pose2d;
import org.ghrobotics.lib.mathematics.twodim.geometry.Pose2dWithCurvature;
import org.ghrobotics.lib.mathematics.twodim.geometry.Rectangle2d;
import org.ghrobotics.lib.mathematics.twodim.trajectory.TrajectoryGeneratorKt;
import org.ghrobotics.lib.mathematics.twodim.trajectory.constraints.CentripetalAccelerationConstraint;
import org.ghrobotics.lib.mathematics.twodim.trajectory.constraints.TimingConstraint;
import org.ghrobotics.lib.mathematics.twodim.trajectory.constraints.VelocityLimitRegionConstraint;
import org.ghrobotics.lib.mathematics.twodim.trajectory.types.TimedTrajectory;
import org.ghrobotics.lib.mathematics.units.LengthKt;
import org.ghrobotics.lib.mathematics.units.Rotation2dKt;
import org.ghrobotics.lib.mathematics.units.derivedunits.AccelerationKt;
import org.ghrobotics.lib.mathematics.units.derivedunits.VelocityKt;
import robot.auxiliary.Trigger;
import robot.subsystems.cargo_intake.Constants;
import robot.subsystems.cargo_intake.commands.GripperControl;
import robot.subsystems.cargo_intake.commands.WristTurn;
import robot.subsystems.commandGroups.CargoScoring;
import robot.subsystems.commandGroups.HatchScoring;
import robot.subsystems.commandGroups.ShiftButton;
import robot.subsystems.drivetrain.ramsete.LoadingStation;
import robot.subsystems.elevator.commands.ElevatorCommand;
import robot.subsystems.hatch_intake.commands.CloseBoth;
import robot.subsystems.hatch_intake.commands.Gripper;
import robot.subsystems.hatch_intake.commands.GripperTransportation;

import java.util.ArrayList;
import java.util.List;

import static robot.subsystems.cargo_intake.Constants.SLOW_DRIVE;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {

    public static final double WRIST_ROTATE_RATE = 20;
    /**
     * The rate at which the lift will goes down with the xbox joystick.
     */
    public static final double DOWN_SPEED_RATE = 0.05;
    /**
     * The rate at which the lift will goes up with the xbox joystick.
     */
    public static final double UP_SPEED_RATE = 0.05;
    /**
     * The Y value area in which the xbox joystick won't make the lift move.
     */
    public static double XBOX_JOYSTICK_DEAD_BAND = 0;

    public static Joystick leftStick = new Joystick(0);
    public static Joystick rightStick = new Joystick(1);
    public static XboxController xbox = new XboxController(2);
    public static Button a = new JoystickButton(xbox, 1);
    public static Button b = new JoystickButton(xbox, 2);
    public static Button x = new JoystickButton(xbox, 3);
    public static Button y = new JoystickButton(xbox, 4);
    public static Button lb = new JoystickButton(xbox, 5);
    public static Button rb = new JoystickButton(xbox, 6);
    public static Button select = new JoystickButton(xbox, 7);
    public static Button start = new JoystickButton(xbox, 8);
    public static Button ls = new JoystickButton(xbox, 9);
    public static Button rs = new JoystickButton(xbox, 10);

    public static Button povd = new POVButton(xbox, 180);
    public static Button povr = new POVButton(xbox, 90);
    public static Button povl = new POVButton(xbox, 270);
    public static Button povu = new POVButton(xbox, 0);

    public static Button RT = new Trigger(xbox, GenericHID.Hand.kRight);
    public static Button LT = new Trigger(xbox, GenericHID.Hand.kLeft);

    public static Button lsLeft = new JoystickButton(leftStick, 4);
    public static Button lsRight = new JoystickButton(leftStick, 5);
    public static Button lsMid = new JoystickButton(leftStick, 3);
    public static Button lsBottom = new JoystickButton(leftStick, 2);

    public static Button ul = new JoystickButton(leftStick, 5);
    public static Button dl = new JoystickButton(leftStick, 3);
    public static Button ur = new JoystickButton(leftStick, 6);
    public static Button dr = new JoystickButton(leftStick, 4);

    public static Button trigger = new JoystickButton(leftStick, 1);
    public static Button back_button = new JoystickButton(leftStick, 2);

    public static Button nineLeft = new JoystickButton(leftStick, 9);
    public static Button eightLeft = new JoystickButton(leftStick, 8);
    public static Button twelveLeft = new JoystickButton(leftStick, 12);

    public static int left_x_stick = 0;
    public static int left_y_stick = 1;
    public static int left_trigger = 2;
    public static int right_trigger = 3;
    public static int right_x_stick = 4;
    public static int right_y_stick = 5;

    public static Button left_joystick_six = new JoystickButton(leftStick, 6);
    public static Button left_joystick_seven = new JoystickButton(leftStick, 7);
    public static Button left_joystick_eight = new JoystickButton(leftStick, 8);
    public static Button left_joystick_nine = new JoystickButton(leftStick, 9);
    public static Button left_joystick_ten = new JoystickButton(leftStick, 10);

    public OI() {
        //REMOVED COMMAND GROUP CARGO SCORING AND HATCH SCORING, THEY STUCK THE CODE
        povd.whenPressed(new ShiftButton(xbox, 7,
                new CargoScoring(0, true),
                new ShiftButton(xbox, 4,
                        new CargoScoring(0, false),
                        new ElevatorCommand(0))));
        povr.whenPressed(new ShiftButton(xbox, 7,
                new CargoScoring(1, true),
                new ShiftButton(xbox, 8,
                        new HatchScoring(robot.subsystems.elevator.Constants.ELEVATOR_STATES.LEVEL1_HATCH, false),
                        new ShiftButton(xbox, 4,
                                new CargoScoring(1, false),
                                new ElevatorCommand(robot.subsystems.elevator.Constants.ELEVATOR_STATES.LEVEL1_HATCH)))));
        povl.whenPressed(new ShiftButton(xbox, 7,
                new CargoScoring(2, true),
                new ShiftButton(xbox, 8,
                        new HatchScoring(robot.subsystems.elevator.Constants.ELEVATOR_STATES.LEVEL2_HATCH, false),
                        new ShiftButton(xbox, 4,
                                new CargoScoring(2, false),
                                new ElevatorCommand(robot.subsystems.elevator.Constants.ELEVATOR_STATES.LEVEL2_HATCH)))));
        povu.whenPressed(new ShiftButton(xbox, 7,
                new CargoScoring(3, true),
                new ShiftButton(xbox, 8,
                        new HatchScoring(robot.subsystems.elevator.Constants.ELEVATOR_STATES.LEVEL3_HATCH, false),
                        new ShiftButton(xbox, 4,
                                new CargoScoring(3, false),
                                new ElevatorCommand(robot.subsystems.elevator.Constants.ELEVATOR_STATES.LEVEL3_CARGO)))));

        RT.whileHeld(new GripperControl(Constants.GRIPPER_SPEED.SHIP, true));
        LT.whileHeld(new GripperControl(Constants.GRIPPER_SPEED.INTAKE, false));

        a.whenPressed(new Gripper());
        lb.whenPressed(new GripperTransportation(false));
        rb.whenPressed(new GripperTransportation(true));
        //y.whenPressed(new WristTurn(Constants.WRIST_ANGLES.SHIP));
        b.whenPressed(new WristTurn(Constants.WRIST_ANGLES.INITIAL));
        x.whenPressed(new WristTurn(Constants.WRIST_ANGLES.INTAKE));
        //TODO: add right stick to control the cargo intake
        select.whenPressed(new CloseBoth());
        nineLeft.toggleWhenPressed(new LoadingStation(generateLoadingStationTrajectory()));
        // Place cargo backward

        /*

        |||||||
        |||3|||
        |||||||
        |||||||   Rocket
        |||2|||
        |||||||
        |||||||
        |||1|||
        |||||||

        |||||||
        |     |
        |     |    Bay
        |||||||
        |||0|||
        |||||||

         */
        /*
        lsBottom.whenPressed(new CargoScoring(0, true));
        lsLeft.whenPressed(new CargoScoring(1, true));
        lsMid.whenPressed(new CargoScoring(2, true));
        lsRight.whenPressed(new CargoScoring(3, true));

        // Score cargo
        rsBottom.whenPressed(new CargoScoring(0, false));
        rsLeft.whenPressed(new CargoScoring(1, false));
        rsMid.whenPressed(new CargoScoring(2, false));
        rsRight.whenPressed(new CargoScoring(3, false));
        */
    }

    /* instead of defining the joysticks in each default command, all of them call these methods */
    public double leftDriveStick() { // TODO: might need name refactoring
        return -SLOW_DRIVE * leftStick.getY();
    }

    public double rightDriveStick() {
        return -SLOW_DRIVE * rightStick.getY();
    }

    public double WristStick() {
        return -xbox.getRawAxis(left_y_stick);
    }

    public double ElevatorStick() {
        return -xbox.getRawAxis(right_y_stick);

    }

    public boolean enableElevator() {
        return xbox.getRawButton(10);
    }


    public boolean enableWrist() {
        return xbox.getRawButton(9);
    }

    private TimedTrajectory<Pose2dWithCurvature> generateLoadingStationTrajectory() {
        List<TimingConstraint<Pose2dWithCurvature>> constraints = new ArrayList<>();
        constraints.add(new CentripetalAccelerationConstraint(AccelerationKt.getAcceleration(LengthKt.getMeter(1.2192))));
        constraints.add(new VelocityLimitRegionConstraint(new Rectangle2d(LengthKt.getFeet(4), LengthKt.getFeet(7), LengthKt.getFeet(8), LengthKt.getFeet(20)), VelocityKt.getVelocity(LengthKt.getFeet(3))));


        List<Pose2d> list = new ArrayList<>();
        list.add(Robot.drivetrain.getRobotPosition());
        list.add(new Pose2d(LengthKt.getFeet(11.5), LengthKt.getFeet(23), Rotation2dKt.getDegree(180)));
        return TrajectoryGeneratorKt.getDefaultTrajectoryGenerator()
                .generateTrajectory(
                        list,
                        constraints,
                        VelocityKt.getVelocity(LengthKt.getMeter(0.5)),
                        VelocityKt.getVelocity(LengthKt.getMeter(0.5)),
                        VelocityKt.getVelocity(LengthKt.getMeter(1)),
                        AccelerationKt.getAcceleration(LengthKt.getMeter(1)),
                        true,
                        true
                );
    }
}