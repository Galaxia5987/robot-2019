package robot.subsystems.elevator.commands;

import edu.wpi.first.wpilibj.command.Command;
import robot.Robot;

/**
 * Control the raw output of the elevator using the joystick.
 */
public class JoystickElevatorSpeed extends Command {

    public JoystickElevatorSpeed() {
        requires(Robot.elevator);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double speed = 1 * Robot.m_oi.ElevatorStick();
        Robot.elevator.setSpeed(speed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}