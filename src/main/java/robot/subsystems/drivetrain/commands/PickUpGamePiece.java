package robot.subsystems.drivetrain.commands;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.CommandGroup;
import robot.subsystems.drivetrain.pure_pursuit.Constants;
import robot.subsystems.drivetrain.pure_pursuit.Path;
import robot.subsystems.drivetrain.pure_pursuit.PurePursue;
import robot.subsystems.drivetrain.pure_pursuit.Waypoint;

import static robot.Robot.navx;

/**
 *
 */
public class PickUpGamePiece extends CommandGroup {

    NetworkTableEntry targetAngleEntry;
    NetworkTableEntry targetDistanceEntry;

    public PickUpGamePiece() {
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        NetworkTable table = inst.getTable("vision");
        targetAngleEntry = table.getEntry("Angle from target");
        targetDistanceEntry = table.getEntry("Distance from target");
        double targetDistance = targetDistanceEntry.getDouble(0);
        double targetAngle = targetAngleEntry.getDouble(0);
        Waypoint target = new Waypoint(Math.sin(targetAngle) * targetDistance, Math.cos(targetAngle) * targetDistance);
        Waypoint middleWP = new Waypoint(Math.tan(navx.getAngle()) * target.getY(), target.getY());
        Path path = new Path(new Waypoint[]{middleWP, target});
        path.generateAll(Constants.WEIGHT_DATA, Constants.WEIGHT_SMOOTH, Constants.TOLERANCE, Constants.MAX_ACCEL, Constants.MAX_PATH_VELOCITY);
        addSequential(new PurePursue(path, Constants.LOOKAHEAD_DISTANCE, Constants.kP, Constants.kA, Constants.kV, true, false));
        addSequential(new HatchOrCargo(new HatchIntake(), new CargoIntake()));

        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
    }
}