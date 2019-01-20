/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package robot.subsystems.hatchIntake;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * An example subsystem.  You can replace me with your own Subsystem.
 */
public class groundIntake extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private final DoubleSolenoid grountIntake = new DoubleSolenoid(Ports.groundForward, Ports.groundReverse);
    private final AnalogInput proxSensor = new AnalogInput(Ports.prox);

    public groundIntake() {
        proxSensor.resetAccumulator();
    }

    public double voltage() {
        return proxSensor.getVoltage();
    }

    public void setClose() {
        grountIntake.set(DoubleSolenoid.Value.kReverse);
    }

    public void setOpen() {
        grountIntake.set(DoubleSolenoid.Value.kForward);
    }

    public boolean isInside() {
        return voltage() <= Constants.MIN_PROXIMITY;
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
}