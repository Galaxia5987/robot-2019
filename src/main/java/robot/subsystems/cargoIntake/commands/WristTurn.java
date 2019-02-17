package robot.subsystems.cargoIntake.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import robot.subsystems.cargoIntake.CargoIntake;
import robot.subsystems.cargoIntake.Constants;

import static robot.Robot.cargoIntake;

/**
 *
 */
public class WristTurn extends Command {
    private double angle;
    public WristTurn(double angle) {
        this.angle = angle;
        requires(cargoIntake);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);

    }

    // Called just before this Command runs the first time
    protected void initialize() {
        SmartDashboard.putString("command state", "start");
        cargoIntake.setWristAngle(angle);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        cargoIntake.setWristAngle(angle);
        SmartDashboard.putString("command state", "executing");

    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Math.abs(cargoIntake.getWristAngle() - angle) < 5;
    }

    // Called once after isFinished returns true
    protected void end() {
//        if (cargoIntake.getWristAngle() < 15)
//            cargoIntake.wrist.set(ControlMode.MotionMagic, 0);

        SmartDashboard.putString("command state", "end");
        System.out.println("Wrist command ended, reached degrees " + angle + "talon " + cargoIntake.getWristAngle());
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        this.cancel();
    }
}