package robot.subsystems.commandGroups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import robot.subsystems.elevator.Constants;
import robot.subsystems.elevator.commands.ElevatorCommand;
import robot.subsystems.hatch_intake.commands.PlaceHatch;

/**
 *This command group gets a level as parameter and then rise to this level,
 * at then place the hatch
 * @author Orel
 */
public class HatchScoring extends CommandGroup {



    /**
     * @param elevatorState of the elevator
     */
    public HatchScoring(Constants.ELEVATOR_STATES elevatorState) {
        addSequential(new ElevatorCommand(elevatorState));
        addSequential(new PlaceHatch());

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