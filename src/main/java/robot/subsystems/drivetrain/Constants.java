package robot.subsystems.drivetrain;

import org.ghrobotics.lib.mathematics.twodim.geometry.Pose2dWithCurvature;
import org.ghrobotics.lib.mathematics.twodim.geometry.Rectangle2d;
import org.ghrobotics.lib.mathematics.twodim.trajectory.constraints.CentripetalAccelerationConstraint;
import org.ghrobotics.lib.mathematics.twodim.trajectory.constraints.TimingConstraint;
import org.ghrobotics.lib.mathematics.twodim.trajectory.constraints.VelocityLimitRegionConstraint;
import org.ghrobotics.lib.mathematics.units.LengthKt;
import org.ghrobotics.lib.mathematics.units.derivedunits.AccelerationKt;
import org.ghrobotics.lib.mathematics.units.derivedunits.VelocityKt;

import java.util.ArrayList;
import java.util.List;

import static robot.Robot.isRobotA;

public class Constants {
    public final static double ROBOT_WIDTH = 0.74; //the distance between the left and right wheels on the robot
    public static final double MAX_RATE = isRobotA ? 0.3 : 0.3;
    public static final double DISTANCE_PER_PULSE = isRobotA ? (0.2032 * Math.PI) / 231 : (0.2032 * Math.PI) / 231;  //diameter of the wheel is 0.2032 meters (8 inches), the encoder sends 226 pulses every 360 degree turn
    public static final boolean LEFT_ENCODER_REVERSED = isRobotA ? false : false;
    public static final boolean RIGHT_ENCODER_REVERSED = isRobotA ? false : false;
    public static final double LEFT_TICKS_PER_METER = isRobotA ? 2138.7750882690398 : 2138.7750882690398; // measured average distance (robot a), [1m / (diameter=0.1524 * pi)] * (ticks_per_meter=1024s) (robot b)
    public static final double RIGHT_TICKS_PER_METER = isRobotA ? 1864 : 2138.7750882690398;
    public static final int TALON_RUNNING_TIMEOUT_MS = isRobotA ? 0 : 0;
    public static final int TALON_TIMEOUT_MS = isRobotA ? 10 : 10;
    public static final double MOTION_ACCELERATION =  1; //Motion acceleration in M/S^2
    public static final double MOTION_CRUISE_VELOCITY = 2.5; //Motion acceleration in M/S
    public static final double ENDING_TOLERANCE = 0.05;
    public static final double START_VELOCITY = -1.18;
    public static final double END_VELOCITY = -0.82;
    public static final double MIN_AIM = 0;

    // (8 inches), the encoder sends 226
    // pulses every 360 degree turn
    static final boolean LEFT_MASTER_REVERSED = isRobotA ? false : false;
    static final boolean LEFT_SLAVE1_REVERSED = isRobotA ? false : false;
    static final boolean LEFT_SLAVE2_REVERSED = isRobotA ? false : false;
    static final boolean RIGHT_MASTER_REVERSED = isRobotA ? true : true;
    static final boolean RIGHT_SLAVE1_REVERSED = isRobotA ? true : true;
    static final boolean RIGHT_SLAVE2_REVERSED = isRobotA ? true : true;

    public static final double SLOW_JOYSTICK_SPEED = 0.9; //multiplied by joystick value, keep at 1 for no changes.

    public static double[] PIDVisionTurn = isRobotA ? new double[]{0.045, 0.0002, 0} : new double[]{0, 0, 0};

    /*
    Ramsete constants
     */
    public static final double[] PIDFLeft = isRobotA ? new double[]{1.5, 0.002, 10, 1.3} : new double[]{1.5, 0, 10, 1.3};
    public static final double[] PIDFRight = isRobotA ? new double[]{1.85, 0.002, 10, 1.45} : new double[]{1.5, 0, 10, 1.3};
    public static final double kZeta = 0.7;
    public static final double kBeta = isRobotA ? 2.3 : 2.7;
    public static final double distanceFromEnd = 2;
    public static final double angleKp = isRobotA ? 1.6 : 1.37;
    public static double pathAngleKp = isRobotA ? 2.3 : 2;
    public static final List<TimingConstraint<Pose2dWithCurvature>> constraints = new ArrayList<>();

    static {
        constraints.add(new CentripetalAccelerationConstraint(AccelerationKt.getAcceleration(LengthKt.getMeter(1.2192))));
        constraints.add(new VelocityLimitRegionConstraint(new Rectangle2d(LengthKt.getFeet(4), LengthKt.getFeet(7), LengthKt.getFeet(8), LengthKt.getFeet(20)), VelocityKt.getVelocity(LengthKt.getFeet(3))));
    }
}
