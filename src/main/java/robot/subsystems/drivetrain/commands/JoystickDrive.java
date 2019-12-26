/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package robot.subsystems.drivetrain.commands;

import edu.wpi.first.wpilibj.command.Command;
import robot.OI;
import robot.Robot;
import robot.subsystems.climb.Constants;

import static robot.Robot.*;

/**
 * Control the Drivetrain via the joysticks.
 */
public class JoystickDrive extends Command {
    public JoystickDrive() {
        requires(drivetrain);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    private static double curve(double x) {
        double b = 4, a = -1.1, c = 4.9, d = 2.3;
        return (b * Math.pow(x, 3) + a * Math.pow(x, 5) + c * x + d * Math.pow(x, 9)) / (a + b + c + d);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {

    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        if ((Math.abs(Robot.m_oi.leftStick.getY()) < 0.1 || Math.abs(Robot.m_oi.rightStick.getY()) < 0.1) &&
                (Math.abs(Robot.m_oi.leftStick.getZ()) > 0.1))
            drivetrain.setSpeed(robot.subsystems.drivetrain.Constants.FORWARD_SPEED_CONSTANT * Math.signum(Robot.m_oi.leftStick.getZ())
                    , robot.subsystems.drivetrain.Constants.FORWARD_SPEED_CONSTANT * Math.signum(Robot.m_oi.leftStick.getZ()));
        else {
            // 1: linear
            // 2: bell
            // 3: x^3
            // 4: e^C(x - 1)
            int option = 6;
            final int C = 5;
            double leftInput = -Robot.m_oi.leftStick.getY();
            double rightInput = -Robot.m_oi.rightStick.getY();

            double leftOutput, rightOutput;
            switch (option) {
                case 1:
                    leftOutput = leftInput;
                    rightOutput = rightInput;
                    break;
                case 2:
                    leftOutput = bell(leftInput, C);
                    rightOutput = bell(rightInput, C);
                    break;
                case 3:
                    leftOutput = Math.pow(leftInput, 3);
                    rightOutput = Math.pow(rightInput, 3);
                    break;
                case 4:
                    if (leftInput > 0) {
                        leftOutput = Math.pow(Math.E, C * (leftInput - 1));
                    } else {
                        leftOutput = -Math.pow(Math.E, C * (-leftInput - 1));
                    }
                    if (rightInput > 0) {
                        rightOutput = Math.pow(Math.E, C * (rightInput - 1));
                    } else {
                        rightOutput = -Math.pow(Math.E, C * (-rightInput - 1));
                    }
                    break;
                case 6:
                    leftOutput = 0.7 * curve(leftInput);
                    rightOutput = 0.7 * curve(rightInput);

                    break;
                default:
                    throw new IllegalArgumentException("Number must be 1-4");
            } //TODO: put this in subsystem
            if (climb.isClimbing()) //maybe also lower speeds when back legs are lowered.
            {
                gripperWheels.setGripperSpeed(Math.min(0.9, Math.max(0, rightOutput))); //TODO: not good, no requires
                drivetrain.setSpeed(rightOutput / Constants.DRIVE_CLIMB_DRIVETRAIN_DIVISOR + OI.rightStick.getX() * 0.1,
                        rightOutput / Constants.DRIVE_CLIMB_DRIVETRAIN_DIVISOR - OI.rightStick.getX() * 0.1);
            } else {
                drivetrain.setSpeed(leftOutput, rightOutput);
                climb.setWheelSpeed((rightOutput + leftOutput) * (0.8 / 2));
            }
        }
    }

    private double bell(double x, double c) {
        return 2 / (1 + Math.pow(Math.E, -c * x)) - 1;
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        drivetrain.setSpeed(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }

}
