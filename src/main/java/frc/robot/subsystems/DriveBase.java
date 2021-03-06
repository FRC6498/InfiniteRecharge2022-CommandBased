// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.Drive.*;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.surpriselib.DriveControlMode;

import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;

public class DriveBase extends SubsystemBase implements Loggable {
  // motors
  private final WPI_TalonFX leftLeader, rightLeader;
  private final WPI_TalonFX leftFollower, rightFollower;
  private final MotorControllerGroup leftMotors, rightMotors;
  private final DifferentialDriveOdometry odometry;
  private final DifferentialDrive diffDrive;
  // 
  @Log
  private final DoubleSolenoid  shifter; // gear shifter
  private final Compressor compressor;
  // imu
  private final AHRS gyro;

  private boolean isHighGear = false;
  private boolean driveInverted;
  private NeutralMode currentBrakeMode = NeutralMode.Coast;
  private DriveControlMode driveControlMode;
  private final SimpleMotorFeedforward driveFeedforward =
    new SimpleMotorFeedforward(
      kS,
      kVLinear,
      kALinear
    );

  public DriveBase()
  {
    leftLeader = new WPI_TalonFX(LeftLeaderId);
    leftFollower = new WPI_TalonFX(LeftFollowerId);
    rightLeader = new WPI_TalonFX(RightLeaderId);
    rightFollower = new WPI_TalonFX(RightFollowerId);

    leftLeader.configOpenloopRamp(DriveRampRate);
    leftFollower.configOpenloopRamp(DriveRampRate);
    rightLeader.configOpenloopRamp(DriveRampRate);
    rightFollower.configOpenloopRamp(DriveRampRate);

    leftFollower.follow(leftLeader);
    rightFollower.follow(rightLeader);

    leftMotors = new MotorControllerGroup(leftLeader, leftFollower);
    rightMotors = new MotorControllerGroup(rightLeader, rightFollower);
    rightMotors.setInverted(true);
    diffDrive = new DifferentialDrive(leftMotors, rightMotors);
    diffDrive.setSafetyEnabled(false);

    gyro = new AHRS(Port.kMXP);
    gyro.reset();
    odometry = new DifferentialDriveOdometry(gyro.getRotation2d());

    compressor = new Compressor(PneumaticsModuleType.CTREPCM);
    shifter = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, ShifterForwardSolenoidId, ShifterReverseSolenoidId);

    // engage brakes when neutral input
    setBrakeMode(NeutralMode.Coast);

    // setup encoders
    leftLeader.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
    rightLeader.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);

    driveControlMode = DriveControlMode.OPEN_LOOP;
  }

  // hardware methods

  public void resetSensors()
  {
    gyro.reset();
    leftLeader.setSelectedSensorPosition(0);
    rightLeader.setSelectedSensorPosition(0);
  }

  public Rotation2d getGyroAngle()
  {
    // negative angle because of the direction the gyro is mounted
    return Rotation2d.fromDegrees(gyro.getAngle());
  }

  @Log
  public double getHeading() {
    return getGyroAngle().getDegrees();
  }

  public double getTurnRate() {
    return gyro.getRate();
  }

  public void toggleBrakeMode() {
    if (currentBrakeMode == NeutralMode.Brake) {
      currentBrakeMode = NeutralMode.Coast;
      setBrakeMode(currentBrakeMode);
    } else {
      currentBrakeMode = NeutralMode.Brake;
      setBrakeMode(currentBrakeMode);
    }
  }

  public void setBrakeMode(NeutralMode brakeMode)
  {
    leftLeader.setNeutralMode(brakeMode);
    rightLeader.setNeutralMode(brakeMode);
    currentBrakeMode = brakeMode;
  }

  public void setInverted(boolean inverted) {
    driveInverted = inverted;
  }

  public boolean getInverted() {
    return driveInverted;
  }

  public void toggleInverted() {
    driveInverted = !driveInverted;
  }

  public void arcadeDrive(double throttle, double turn) {
    if (driveInverted) {
      throttle *= -1;
    }
    driveControlMode = DriveControlMode.OPEN_LOOP;
    diffDrive.arcadeDrive(throttle, turn, true);
  }

  public void toggleGear()
  {
    if (isHighGear) {
      shifter.set(Value.kReverse);
      isHighGear = false;
    } else {
      shifter.set(Value.kForward);
      isHighGear = true;
    }
  }

  @Log(name="Gear")
  public String getGear()
  {
    if (isHighGear) {
      return "High";
    } else {
      return "Low";
    }
  }

  public void stop()
  {
    arcadeDrive(0, 0);
  }

  @Log
  public String getBrakeMode() {
    return String.format("L1: {0}\nL2: {1}\nR1: {2}\nR2: {3}", 0,0,0,0);
  }

  public double getEncoderPosition()
  {
    return ((leftLeader.getSelectedSensorPosition() + leftFollower.getSelectedSensorPosition()) / 2) 
      + ((rightLeader.getSelectedSensorPosition() + rightFollower.getSelectedSensorPosition()) / 2);
  }

  @Log.BooleanBox(name = "Compressor Running")
  public boolean getPressure() {
    return compressor.enabled();
  }

  @Log(name = "Yaw (deg.)")
  public double getGyroAngleDegrees() {
    return getGyroAngle().getDegrees();
  }

  @Override
  public void periodic() {
    odometry.update(
      gyro.getRotation2d(), 
      leftLeader.getSelectedSensorPosition(), 
      rightLeader.getSelectedSensorPosition()
    );
  }

  //@Log.Graph
  public double getLeftVelocity() {
    return leftLeader.getSelectedSensorVelocity();
  }

  //@Log.Graph
  public double getRightVelocity() {
    return rightLeader.getSelectedSensorVelocity();
  }
  
  //@Log.Dial
  public double getSpeedRatio() {
    // 1.0 => both sides equal, 0.0 => left 0, right 1, 100.0 => right 0, left 1
    return MathUtil.applyDeadband(leftLeader.getSelectedSensorVelocity(), 0.01) / MathUtil.applyDeadband(rightLeader.getSelectedSensorVelocity(), 0.01);
  }

  @Log.Dial
  public double getMotorVoltageDifference() {
    return leftLeader.getSupplyCurrent() - rightLeader.getSupplyCurrent();
  }

  public void consumeTrapezoidState(TrapezoidProfile.State leftProfileState, TrapezoidProfile.State rightProfileState) {
    leftLeader.set(
      ControlMode.Position,
      leftProfileState.position,
      DemandType.ArbitraryFeedForward,
      driveFeedforward.calculate(leftProfileState.velocity)
    );

    rightLeader.set(
      ControlMode.Position,
      rightProfileState.position,
      DemandType.ArbitraryFeedForward,
      driveFeedforward.calculate(rightProfileState.velocity)
    );
  }
}
