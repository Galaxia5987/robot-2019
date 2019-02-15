package robot.subsystems.cargoIntake;

public class Constants {
    public final static double GRIPPER_WHEELS_SPEED = 0.75;
    public final static double INITIAL_ANGLE = 0;//initial angle of the wrist
    public final static double INTAKE_ANGLE = 0;
    public final static double FOLDED_ANGLE = 165;//folded angle represents the angle in which the wrist is folded back inside the robot (number felt cute might delete later)

    public static final double MAX_VELOCITY = 2732; //currently in encoder units.
    public static final double MAX_ACCEL = 1366;

    final static double kP = 3;
    final static double kI = 0;
    final static double kD = 120;
    final static double kF = 0.3744509516837482;
    final static int IZone = 50;

    final static double TICKS_PER_DEGREE = 11.73333333333333333333333*4; // (reduction=66/16) * (ticks_per_revolution=1024) / 360deg

    final static boolean WRIST_MOTOR_REVERSED = false;
    final static boolean WRIST_LIMIT_REVESED = true;//might need to be changed

    final static int TALON_TIME_OUT = 10;

    final static double CARGO_IN_VOLTAGE = 2.5;//felt cute might delete later
    final static boolean SENSOR_PHASE = false;


    public final static double WRIST_RADIUS = 0;
}
