package robot.subsystems.climb.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import robot.Robot;
import robot.subsystems.climb.Climb;
import robot.subsystems.climb.Constants;
import robot.subsystems.climb.TiltUtils;

import static robot.Robot.climb;

/**
 * An FRC command, climbs to the third level of the HAB zone.
 * The motors are run at the execute, and using arbitrary feedforward, tipping is prevented.
 * This class only move upwards, the rest is done in separate commands.
 *
 * @author paulo
 */
public class RiseToHeightEncoders extends Command {
    private double targetHeight = Constants.LEVEL_THREE_LEG_LENGTH;

    //gamers, (lose yourself and) rise up
    public RiseToHeightEncoders(double targetHeight) {
        this.targetHeight = targetHeight;
        requires(climb);
    }

    public RiseToHeightEncoders(Climb.HAB_LEG_HEIGHTS height_state) {
        this(height_state.getHABHHeight());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double minimumLeg = Math.min(climb.getLegBLHeight(), Math.min(climb.getLegBRHeight(), Math.min(climb.getLegFLHeight(), climb.getLegFRHeight())));
        climb.setLegFLHeight(targetHeight, minimumLeg-climb.getLegFLHeight());
        climb.setLegFRHeight(targetHeight, minimumLeg-climb.getLegFRHeight());
        climb.setLegBLHeight(targetHeight, minimumLeg-climb.getLegBLHeight());
        climb.setLegBRHeight(targetHeight, minimumLeg-climb.getLegBRHeight());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() { //TODO: either try out an average, a timeout or an angle consideration aswell
        return climb.getLegFLHeight() > targetHeight - Constants.CLIMB_TOLERANCE &&
                climb.getLegFRHeight() > targetHeight - Constants.CLIMB_TOLERANCE &&
                climb.getLegBLHeight() > targetHeight - Constants.CLIMB_TOLERANCE &&
                climb.getLegBRHeight() > targetHeight - Constants.CLIMB_TOLERANCE;
    }

    // Called once after isFinished returns true
    protected void end() {
        //TODO: stop all motors
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        cancel();
    }
}