package robot.subsystems.drivetrain.commands;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class HatchOrCargo extends Command {

    public HatchOrCargo() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        boolean pickHatch = true;//will later be smartdashboard input from image detection
        if (pickHatch)
            new HatchIntake().start();
        else
            new CargoIntake().start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
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