package robot.subsystems.gripper_wheels.commands;

import edu.wpi.first.wpilibj.command.Command;
import robot.subsystems.wrist_control.Constants.GRIPPER_SPEED;

import static robot.Robot.cargoIntake;
import static robot.Robot.gripperWheels;

/**
 * Instant Command in charge of controlling the speed of the wrist wheels, the wheels in charge of grabbing and releasing cargo.
 *
 * @author Lior
 */
public class GripperControl extends Command {
    private double speed;//speed of the gripper
    private double timeout = 0;
    private boolean useTrigger;

    public GripperControl(double speed){
        requires(gripperWheels);
        this.speed = speed;
    }

    public GripperControl(double speed, boolean useTrigger){
        this.useTrigger = useTrigger;
        requires(cargoIntake);
        this.speed = speed;
    }

    public GripperControl(GRIPPER_SPEED gripperSpeed, boolean useTrigger) {
        this(gripperSpeed.getValue(), useTrigger);
    }

    public GripperControl(GRIPPER_SPEED gripperSpeed) {
        this(gripperSpeed.getValue(), false);
    }

    public GripperControl(GRIPPER_SPEED gripperSpeed, boolean useTrigger, double timeout) {
        this(gripperSpeed, useTrigger);
        this.timeout = timeout;
    }

    public GripperControl(double speed, double timeout) {
        this(speed);
        this.timeout = timeout;
    }

    // Called just before this Command runs the first time
    protected void initialize() {

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (!(gripperWheels.isCargoInside() && speed < 0)){
            if(useTrigger)
                gripperWheels.setGripperSpeed(speed);
            else

                gripperWheels.setGripperSpeed(speed);
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if (speed < 0)
            return gripperWheels.isCargoInside() && speed < 0;
        else {
            return false;
        }
    }

    // Called once after isFinished returns true
    protected void end() {
        gripperWheels.setGripperSpeed(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}