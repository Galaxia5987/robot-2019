package robot.subsystems.drivetrain.commands;

import com.stormbots.MiniPID;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import robot.Robot;

import static robot.Robot.drivetrain;

/**
 *
 */
public class AutoDrive extends Command {
    private MiniPID speedPid = new MiniPID(0.6, 0.0013, 0.9);
    private MiniPID turnPid = new MiniPID(0.02, 0, 0.1);
    private NetworkTableEntry angleEntry = Robot.visionTable.getEntry("tape_angle");
    private NetworkTableEntry distanceEntry = Robot.visionTable.getEntry("tape_distance");
    private boolean stop = false;
    private Timer timer = new Timer();
    private double lastDistance = 0;
    private boolean driven = false;

    public AutoDrive() {
        speedPid.setOutputLimits(-0.75, 0.75);
        requires(drivetrain);
    }

    protected void initialize() {
        stop = false;
        driven = false;
        timer.reset();
    }

    protected void execute() {
        double angle = angleEntry.getDouble(0);
        double distance = distanceEntry.getDouble(0);
        if (distance == 0) {
            stop = true;
        }
        double speed = speedPid.getOutput(distance, 0.6);
        double turn = turnPid.getOutput(angle, 0);
        if (stop) {
            if (!driven) {
                drivetrain.driveDistance(lastDistance - 0.3);
                driven = true;
            }
        } else {
            if (angle > 1) {
                turn -= 0.05;
            } else if (angle < -1) {
                turn += 0.05;
            }
            drivetrain.setSpeed(speed - turn, speed + turn);
            lastDistance = distance;
        }

    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
        timer.stop();
        drivetrain.setSpeed(0, 0);
    }

    protected void interrupted() {
        end();
    }
}