package robot.subsystems.drivetrain.ramsete;

import org.ghrobotics.lib.mathematics.MathExtensionsKt;
import org.ghrobotics.lib.mathematics.twodim.geometry.Pose2d;
import org.ghrobotics.lib.mathematics.twodim.trajectory.TrajectoryGeneratorKt;
import org.ghrobotics.lib.mathematics.twodim.trajectory.types.Trajectory;
import org.ghrobotics.lib.mathematics.units.Length;
import org.ghrobotics.lib.mathematics.units.LengthKt;
import org.ghrobotics.lib.mathematics.units.Rotation2d;
import org.ghrobotics.lib.mathematics.units.Time;
import org.ghrobotics.lib.mathematics.units.derivedunits.Acceleration;
import org.ghrobotics.lib.mathematics.units.derivedunits.Velocity;
import org.ghrobotics.lib.subsystems.drive.TrajectoryTrackerOutput;
import robot.subsystems.drivetrain.Constants;
import robot.subsystems.drivetrain.pure_pursuit.Waypoint;

import java.util.ArrayList;
import java.util.List;

public final class RamseteTracker {
    private final double kBeta;
    private final double kZeta;

    protected double[] calculateState(Waypoint waypoint, Waypoint robot, Waypoint nextPoint) {

        double errorX = waypoint.getX() - robot.getX();
        double errorY = waypoint.getY() - robot.getY();

        double wd = waypoint.getSpeed() * waypoint.getCurvature();

        double k1 = 2.0 * this.kZeta * Math.sqrt(wd * wd + this.kBeta * waypoint.getSpeed() * waypoint.getSpeed());

        double angleError = Math.atan2(nextPoint.getY() - waypoint.getY(), nextPoint.getX() - waypoint.getX());
        angleError = 0;
        double linearVelocity = waypoint.getSpeed() * Math.cos(angleError) + k1 * errorX;
        double angularVelocity = wd + this.kBeta * waypoint.getSpeed() * sinc(angleError) * errorY + k1 * angleError;
        angularVelocity = -angularVelocity;
        double[] speeds = new double[2];
        speeds[0] = linearVelocity - (angularVelocity * Constants.ROBOT_WIDTH / 2);
        speeds[1] = linearVelocity + (angularVelocity * Constants.ROBOT_WIDTH / 2);
        return speeds;
    }

    public RamseteTracker(double kBeta, double kZeta) {
        this.kBeta = kBeta;
        this.kZeta = kZeta;
    }

    private double sinc(double theta) {
        return MathExtensionsKt.epsilonEquals(theta, 0.0D) ? 1.0D - 1.0D / 6.0D * theta * theta : Math.sin(theta) / theta;
    }

    public static List<Waypoint> waypoints() {
        List<Waypoint> waypoints = new ArrayList<>();
//        waypoints.add(new Waypoint(0, 0, 0.1524, 0.2, 0));
//        waypoints.add(new Waypoint(0, 0.3, 0.3048, 0.4, 0));
//        waypoints.add(new Waypoint(0, 0.4572, 0.4572, 0.7, 0));
//        waypoints.add(new Waypoint(0, 0.6096, 0.6096, 1, 0));
//        waypoints.add(new Waypoint(0, 0.8096, 0.8096, 0.6, 0));


        return waypoints;
    }

    public static void main(String[] args) {
//        Path path = new Path(new Waypoint[]{new Waypoint(0, 0), new Waypoint(0, 1)});
//        path.generateAll(robot.subsystems.drivetrain.pure_pursuit.Constants.WEIGHT_DATA, robot.subsystems.drivetrain.pure_pursuit.Constants.WEIGHT_SMOOTH, robot.subsystems.drivetrain.pure_pursuit.Constants.TOLERANCE, robot.subsystems.drivetrain.pure_pursuit.Constants.MAX_ACCEL, robot.subsystems.drivetrain.pure_pursuit.Constants.MAX_PATH_VELOCITY);
//        System.out.println(path);
//        RamseteTracker tracker = new RamseteTracker(2, 0.7);
//        System.out.println(path);
//        for (int i = 0; i < path.length(); i++) {
//            Waypoint nextPoint;
//            if (i < path.length() - 1) {
//                nextPoint = path.getWaypoint(i + 1);
//            } else {
//                nextPoint = new Waypoint(0, 0, 0, 0, 0);
//            }
//            System.out.println(tracker.calculateState(path.getWaypoint(i), new Waypoint(0, 0), nextPoint));
//        }
//        RamseteTracker tracker = new RamseteTracker(2, 0.7);
//        for (int i = 0; i < waypoints().size(); i++) {
//            Waypoint nextPoint;
//            if (i < waypoints().size() - 1) {
//                nextPoint = waypoints().get(i + 1);
//            } else {
//                nextPoint = new Waypoint(0, 0, 0, 0, 0);
//            }
//            System.out.println(Arrays.toString(tracker.calculateState(waypoints().get(i), new Waypoint(0, 0), nextPoint)));
//        }

        List<Pose2d> list = new ArrayList<>();
        list.add(new Pose2d(LengthKt.getMeter(0), LengthKt.getMeter(0), new Rotation2d(0)));
        list.add(new Pose2d(LengthKt.getMeter(1), LengthKt.getMeter(0), new Rotation2d(0)));
        Trajectory trajectory = TrajectoryGeneratorKt.getDefaultTrajectoryGenerator()
                .generateTrajectory(list, new ArrayList<>(), new Velocity(0.4, new Length(0.4)), new Velocity(0.2, new Length(0.2)), new Velocity(1.2, new Length(1.2)), new Acceleration<>(1, new Length(1)), false, true);
        org.ghrobotics.lib.mathematics.twodim.control.RamseteTracker ramseteTracker = new org.ghrobotics.lib.mathematics.twodim.control.RamseteTracker(2, 0.7);

        ramseteTracker.reset(trajectory);

        double x = 0;
        double y = 0;

        while (x < 1) {
            TrajectoryTrackerOutput output = ramseteTracker.nextState(new Pose2d(new Length(x), new Length(y), new Rotation2d(0)), new Time(System.currentTimeMillis()));
            double linearVelocity = output.getLinearVelocity().getValue();
            double angularVelocity = -1 * output.getAngularVelocity().getValue();
            double leftVelocity = linearVelocity - (angularVelocity * Constants.ROBOT_WIDTH / 2);
            double rightVelocity = linearVelocity + (angularVelocity * Constants.ROBOT_WIDTH / 2);
            System.out.println("Left: " + leftVelocity + " | Right: " + rightVelocity);
            x += 0.1;
            y += 0.1;
        }
    }

}
