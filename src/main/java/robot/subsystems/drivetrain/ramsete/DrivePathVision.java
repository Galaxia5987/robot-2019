package robot.subsystems.drivetrain.ramsete;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import org.ghrobotics.lib.debug.LiveDashboard;
import org.ghrobotics.lib.mathematics.twodim.geometry.Pose2d;
import org.ghrobotics.lib.mathematics.twodim.geometry.Pose2dWithCurvature;
import org.ghrobotics.lib.mathematics.twodim.trajectory.TrajectoryGeneratorKt;
import org.ghrobotics.lib.mathematics.twodim.trajectory.types.TimedTrajectory;
import org.ghrobotics.lib.mathematics.units.LengthKt;
import org.ghrobotics.lib.mathematics.units.TimeUnitsKt;
import org.ghrobotics.lib.mathematics.units.derivedunits.AccelerationKt;
import org.ghrobotics.lib.mathematics.units.derivedunits.VelocityKt;
import org.ghrobotics.lib.subsystems.drive.TrajectoryTrackerOutput;
import robot.Robot;
import robot.subsystems.drivetrain.Constants;

import java.util.ArrayList;
import java.util.List;

import static robot.Robot.drivetrain;

/**
 *
 */
public class DrivePathVision extends Command {

    private final TimedTrajectory<Pose2dWithCurvature> trajectory;

    public DrivePathVision(Pose2d startingPoint, Pose2d endingPoint) {
        drivetrain.resetLocation(startingPoint);
        List<Pose2d> list = new ArrayList<>();
        list.add(startingPoint);
        list.add(endingPoint);
        this.trajectory = TrajectoryGeneratorKt.getDefaultTrajectoryGenerator()
                .generateTrajectory(
                        list,
                        Constants.constraints,
                        VelocityKt.getVelocity(LengthKt.getMeter(0.5)),
                        VelocityKt.getVelocity(LengthKt.getMeter(0.5)),
                        VelocityKt.getVelocity(LengthKt.getMeter(1)),
                        AccelerationKt.getAcceleration(LengthKt.getMeter(1)),
                        true,
                        true
                );
        requires(drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        drivetrain.trajectoryTracker.reset(trajectory);
        drivetrain.localization.reset(trajectory.getFirstState().getState().getPose());
        LiveDashboard.INSTANCE.setFollowingPath(true);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double angle = Robot.visionTable.getEntry("tape_distance").getDouble(0);

        TrajectoryTrackerOutput trackerOutput = drivetrain.trajectoryTracker.nextState(drivetrain.getRobotPosition(), TimeUnitsKt.getSecond(Timer.getFPGATimestamp()));

        LiveDashboard.INSTANCE.setPathX(drivetrain.trajectoryTracker.getReferencePoint().getState().getState().getPose().getTranslation().getX().getFeet());
        LiveDashboard.INSTANCE.setPathY(drivetrain.trajectoryTracker.getReferencePoint().getState().getState().getPose().getTranslation().getY().getFeet());
        LiveDashboard.INSTANCE.setPathHeading(drivetrain.trajectoryTracker.getReferencePoint().getState().getState().getPose().getRotation().getRadian());

        double linearVelocity = trackerOutput.getLinearVelocity().getValue(); // m/s
        double angularVelocity = trackerOutput.getAngularVelocity().getValue(); // rad/s

        double distanceFromLast = drivetrain.getRobotPosition().getTranslation().distance(trajectory.getLastState().getState().getPose().getTranslation());
        System.out.println(distanceFromLast);
        if (angle != 0.0 && distanceFromLast < Constants.distanceFromEnd && distanceFromLast > 0.5) {
            angularVelocity = angle * Constants.angleKp;
        }

        double tangentialVelocity = Constants.ROBOT_WIDTH / 2.0 * angularVelocity;

        double leftVelocity = linearVelocity - tangentialVelocity;
        double rightVelocity = linearVelocity + tangentialVelocity;
        drivetrain.setLeftVelocity(leftVelocity);
        drivetrain.setRightVelocity(rightVelocity);

    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return drivetrain.trajectoryTracker.isFinished();
    }

    // Called once after isFinished returns true
    protected void end() {
        LiveDashboard.INSTANCE.setFollowingPath(false);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}