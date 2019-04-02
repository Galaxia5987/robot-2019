package robot.subsystems.drivetrain.sandstorm;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import org.ghrobotics.lib.mathematics.twodim.geometry.Pose2d;
import org.ghrobotics.lib.mathematics.units.LengthKt;
import org.ghrobotics.lib.mathematics.units.Rotation2dKt;
import robot.subsystems.command_groups.HatchScoring;
import robot.subsystems.command_groups.PlaceHatch;
import robot.subsystems.command_groups.RetractHatch;
import robot.subsystems.command_groups.TakeHatch;
import robot.subsystems.drivetrain.commands.DistanceDrive;
import robot.subsystems.drivetrain.commands.TurnAngle;
import robot.subsystems.drivetrain.commands.VisionDrive;
import robot.subsystems.drivetrain.ramsete.DrivePathVision;
import robot.subsystems.elevator.Constants;
import robot.subsystems.elevator.commands.ElevatorCommand;
import robot.subsystems.hatch_intake.commands.ExtensionPlate;
import robot.subsystems.hatch_intake.commands.Flower;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class TwoHatchRightRocket extends CommandGroup {

    public TwoHatchRightRocket(Constants.ELEVATOR_STATES height) {
        //Lift elevator before time
        addParallel(new ElevatorCommand(height));

        DrivePathVision toRocketCommand = new DrivePathVision(Paths.RIGHT_HAB_TO_NEAR_ROCKET, false);
        addSequential(toRocketCommand);

        addParallel(new ExtensionPlate(true));

        addSequential(new VisionDrive());
        addSequential(new WaitCommand(0.2));

        //Score hatch
        addSequential(new PlaceHatch(height));
        addSequential(new WaitCommand(0.5));

        addParallel(new RetractHatch());
        addSequential(new WaitCommand(0.5));

        List<Pose2d> toLoadingStation = new ArrayList<>();
        toLoadingStation.add(new Pose2d(LengthKt.getFeet(7.769), LengthKt.getFeet(2.982), Rotation2dKt.getDegree(180)));
        addSequential(new DrivePathVision(toLoadingStation, 0, 1, false, false));

        addSequential(new TurnAngle(175));

        addSequential(new WaitCommand(0.4));

        addSequential(new ExtensionPlate(true));
        addSequential(new Flower(true));
        addSequential(new VisionDrive());

        addSequential(new WaitCommand(0.2));

        addSequential(new TakeHatch());

        addSequential(new DistanceDrive(0.5));

        addSequential(new WaitCommand(0.3));
        List<Pose2d> driveWithHatch = new ArrayList<>();
        driveWithHatch.add(new Pose2d(LengthKt.getFeet(19.015), LengthKt.getFeet(5.938), Rotation2dKt.getDegree(0)));
        driveWithHatch.add(new Pose2d(LengthKt.getFeet(25.563), LengthKt.getFeet(4.055), Rotation2dKt.getDegree(30)));

        addSequential(new DrivePathVision(driveWithHatch, 0, 0, false, false));

        addSequential(new WaitCommand(0.5));
        addParallel(new ExtensionPlate(true));
        addSequential(new VisionDrive());
        addSequential(new WaitCommand(0.2));

        addSequential(new HatchScoring(height));
    }

}