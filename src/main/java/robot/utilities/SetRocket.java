package robot.utilities;

import edu.wpi.first.wpilibj.command.InstantCommand;
import robot.Robot;

/**
 *
 */
public class SetRocket extends InstantCommand {

    private final boolean hatch;

    public SetRocket(boolean hatch) {
        this.hatch = hatch;
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.visionTable.getEntry("target_type").setString("rocket");
        Robot.visionTable.getEntry("game_piece").setString(hatch ? "hatch" : "cargo");
    }

}