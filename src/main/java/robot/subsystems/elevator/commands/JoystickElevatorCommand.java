package robot.subsystems.elevator.commands;

import edu.wpi.first.wpilibj.command.Command;
import robot.Robot;
import robot.subsystems.elevator.Elevator;

/**
 *
 */
public class JoystickElevatorCommand extends Command {
    private static final double XBOX_JOYSTICK_DEAD_BAND = 0;
    private static final double DOWN_SPEED_RATE = 0.02;
    private static final double UP_SPEED_RATE = 0.08;
    private Elevator elevator = Robot.elevator;


    public JoystickElevatorCommand() {
        requires(elevator);
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double y = -Robot.m_oi.xbox.getY(); // invert the input to make up positive and down negative
        double change;
        if(y > 0)
            change = y * UP_SPEED_RATE;
        else
            change = y * DOWN_SPEED_RATE;
        elevator.setHeight(elevator.getHeight() + change);
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
        end();
        cancel();
    }
}