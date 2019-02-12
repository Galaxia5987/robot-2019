/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package robot.subsystems.climb;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Climbing subsystem for the 2019 robot 'GENESIS'
 *
 * @author paulo
 */
public class Climb extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    private TalonSRX talonFL = new TalonSRX(Ports.frontLeftMotor);
    private TalonSRX talonFR = new TalonSRX(Ports.frontRightMotor);
    private TalonSRX talonBL = new TalonSRX(Ports.backLeftMotor);
    private TalonSRX talonBR = new TalonSRX(Ports.backRightMotor);

    public Climb() { //TODO: add four encoders to each of the motors just as in the elevator code.
        talonFL.setInverted(Constants.FRONT_LEFT_TALON_REVERSE);
        talonFR.setInverted(Constants.FRONT_RIGHT_TALON_REVERSE);
        talonBL.setInverted(Constants.BACK_LEFT_TALON_REVERSE);
        talonBR.setInverted(Constants.BACK_RIGHT_TALON_REVERSE);

        //what the motor does when not given voltage (Brake - decelerate the motor, Coast - not stop the motor)
        talonFL.setNeutralMode(NeutralMode.Brake);
        talonFR.setNeutralMode(NeutralMode.Brake);
        talonBL.setNeutralMode(NeutralMode.Brake);
        talonBR.setNeutralMode(NeutralMode.Brake);

        /* set closed loop gains in slot0 */
        talonFL.config_kP(0, Constants.CLIMB_PIDFE[0], Constants.TALON_TIMEOUT_MS);
        talonFL.config_kI(0, Constants.CLIMB_PIDFE[1], Constants.TALON_TIMEOUT_MS);
        talonFL.config_kD(0, Constants.CLIMB_PIDFE[2], Constants.TALON_TIMEOUT_MS);
        talonFL.config_kF(0, Constants.CLIMB_PIDFE[3], Constants.TALON_TIMEOUT_MS);

        /* set closed loop gains in slot0 */
        //I chose opposite arms because its mostly arbitrary and it might be more accurate, we want to have the monitoring on opposite sides
        talonBR.config_kP(0, Constants.CLIMB_PIDFE[0], Constants.TALON_TIMEOUT_MS);
        talonBR.config_kI(0, Constants.CLIMB_PIDFE[1], Constants.TALON_TIMEOUT_MS);
        talonBR.config_kD(0, Constants.CLIMB_PIDFE[2], Constants.TALON_TIMEOUT_MS);
        talonBR.config_kF(0, Constants.CLIMB_PIDFE[3], Constants.TALON_TIMEOUT_MS);

        /* set closed loop gains in slot0 */
        talonFR.config_kP(0, Constants.CLIMB_PIDFE[0], Constants.TALON_TIMEOUT_MS);
        talonFR.config_kI(0, Constants.CLIMB_PIDFE[1], Constants.TALON_TIMEOUT_MS);
        talonFR.config_kD(0, Constants.CLIMB_PIDFE[2], Constants.TALON_TIMEOUT_MS);
        talonFR.config_kF(0, Constants.CLIMB_PIDFE[3], Constants.TALON_TIMEOUT_MS);

        /* set closed loop gains in slot0 */
        //I chose opposite arms because its mostly arbitrary and it might be more accurate, we want to have the monitoring on opposite sides
        talonBL.config_kP(0, Constants.CLIMB_PIDFE[0], Constants.TALON_TIMEOUT_MS);
        talonBL.config_kI(0, Constants.CLIMB_PIDFE[1], Constants.TALON_TIMEOUT_MS);
        talonBL.config_kD(0, Constants.CLIMB_PIDFE[2], Constants.TALON_TIMEOUT_MS);
        talonBL.config_kF(0, Constants.CLIMB_PIDFE[3], Constants.TALON_TIMEOUT_MS);

        configMotorEncoder(talonFL, Constants.FRONT_LEFT_FORWARD_HALL_REVERSED, Constants.FRONT_LEFT_REVERSE_HALL_REVERSED, FeedbackDevice.CTRE_MagEncoder_Relative);
        configMotorEncoder(talonBR, Constants.BACK_RIGHT_FORWARD_HALL_REVERSED, Constants.BACK_RIGHT_REVERSE_HALL_REVERSED, FeedbackDevice.CTRE_MagEncoder_Relative);
        configMotorEncoder(talonFR, Constants.FRONT_RIGHT_FORWARD_HALL_REVERSED, Constants.FRONT_RIGHT_REVERSE_HALL_REVERSED, FeedbackDevice.CTRE_MagEncoder_Relative);
        configMotorEncoder(talonBL, Constants.BACK_LEFT_FORWARD_HALL_REVERSED, Constants.BACK_LEFT_REVERSE_HALL_REVERSED, FeedbackDevice.CTRE_MagEncoder_Relative);
    }

    /**
     * Set the target height of the front left leg in meters.
     *
     * @param height    target height in meters.
     * @param legOffset the error of the leg from its ideal length. set to 0 if no correction is needed.
     */
    public void setLegFLHeight(double height, double legOffset) {//TODO: currently when the robot starts to tip, half the legs speed up, and the other half slow down. maybe we can set only two to slow down ect.
        talonFL.set(ControlMode.MotionMagic, metersToTicks(height), DemandType.ArbitraryFeedForward, Constants.CLIMB_PIDFE[4] * legOffset);
    }

    /**
     * Set the target height of the front right leg in meters.
     *
     * @param height    target height in meters.
     * @param legOffset the error of the leg from its ideal length. set to 0 if no correction is needed.
     */
    public void setLegFRHeight(double height, double legOffset) {
        talonFR.set(ControlMode.MotionMagic, metersToTicks(height), DemandType.ArbitraryFeedForward, Constants.CLIMB_PIDFE[4] * legOffset);
    }

    /**
     * Set the target height of the back left leg in meters.
     *
     * @param height    target height in meters.
     * @param legOffset the error of the leg from its ideal length. set to 0 if no correction is needed.
     */
    public void setLegBLHeight(double height, double legOffset) {
        talonBL.set(ControlMode.MotionMagic, metersToTicks(height), DemandType.ArbitraryFeedForward, Constants.CLIMB_PIDFE[4] * legOffset);
    }

    /**
     * @return height of the back right leg in meters
     */
    public void setLegBRHeight(double height, double legOffset) {
        talonBR.set(ControlMode.MotionMagic, metersToTicks(height), DemandType.ArbitraryFeedForward, Constants.CLIMB_PIDFE[4] * legOffset);
    }

    /**
     * @return height of the front left leg in meters
     */
    public double getLegFLHeight() {
        return ticksToMeters(talonFL.getSelectedSensorPosition(0));
    }

    /**
     * @return height of the front right leg in meters
     */
    public double getLegFRHeight() {
        return ticksToMeters(talonFR.getSelectedSensorPosition(0));
    }

    /**
     * @return height of the back left leg in meters
     */
    public double getLegBLHeight() {
        return ticksToMeters(talonBL.getSelectedSensorPosition(0));
    }

    /**
     * get the height of the leg in meters from the encoders.
     *
     * @return height of the leg in meters
     */
    public double getLegBRHeight() {
        return ticksToMeters(talonBR.getSelectedSensorPosition(0));
    }

    /**
     * auxiliary method used to shorten the code when configuring the motor controllers (the talons).
     *
     * @param motorController    the motor controller being configured
     * @param forwardLSReversed  is the forward limit switch reversed.
     * @param backwardLSReversed is the forward limit switch reversed.
     * @param feedbackDevice     the encoder type connected to the motor controller
     */
    private void configMotorEncoder(TalonSRX motorController, boolean forwardLSReversed, boolean backwardLSReversed, FeedbackDevice feedbackDevice) {
        motorController.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, Constants.TALON_TIMEOUT_MS);
        motorController.configForwardLimitSwitchSource(
                LimitSwitchSource.FeedbackConnector,
                forwardLSReversed ? LimitSwitchNormal.NormallyClosed : LimitSwitchNormal.NormallyOpen,
                Constants.TALON_TIMEOUT_MS
        );
        motorController.configReverseLimitSwitchSource(
                LimitSwitchSource.FeedbackConnector, backwardLSReversed
                        ? LimitSwitchNormal.NormallyClosed : LimitSwitchNormal.NormallyOpen,
                Constants.TALON_TIMEOUT_MS
        );
        motorController.configSelectedFeedbackSensor(feedbackDevice, 0, Constants.TALON_TIMEOUT_MS);
    }

    /**
     * auxiliary method used to make the code more understandable.
     *
     * @param meters size or length in meters
     * @return the ticks the motor needs to do.
     */
    private int metersToTicks(double meters) {
        return (int) (meters * Constants.TICKS_PER_METER);
    }

    /**
     * auxiliary method used to make the code more understandable.
     *
     * @param ticks amount of encoder ticks.
     * @return the respective height in meters.
     */
    private double ticksToMeters(int ticks) {
        return ticks / Constants.TICKS_PER_METER;
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
}