package robot.subsystems.hatchIntake.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import robot.Robot;

public class FlowerTransportation extends InstantCommand {
    private boolean extend;

    public FlowerTransportation(boolean extend) {
        requires(Robot.GROUNDINTAKE);
        this.extend = extend;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        if (extend) {
            Robot.GROUNDINTAKE.ExtensionOpen();
        } else {
            Robot.GROUNDINTAKE.ExtensionClose();
        }
    }


    @Override
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    }
}
