// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.Shooter.*;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.sensors.AbsoluteSensorRange;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.surpriselib.UnitConverter;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;

public class Turret extends SubsystemBase implements Loggable {
  
  TalonFX yawMotor = new TalonFX(yawMotorCANId);
  Rotation2d angleGoal = Rotation2d.fromDegrees(0);
  double rotationError = 0.0;
  public boolean homed = false;
  public boolean centered = false;
  double openLoopDemand;
  public boolean enabled = true;

  public Turret() {
    yawMotor.configFactoryDefault();
    yawMotor.configPeakOutputForward(0.2);
    yawMotor.configPeakOutputReverse(-0.2);
    yawMotor.config_kP(0, turretYaw_kP);
    yawMotor.config_kI(0, 0);
    yawMotor.config_kD(0, turretYaw_kD);
    //yawMotor.configIntegratedSensorAbsoluteRange(AbsoluteSensorRange.Unsigned_0_to_360);
    yawMotor.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
    yawMotor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
    yawMotor.setNeutralMode(NeutralMode.Brake);
  }

  @Log.BooleanBox(name = "Forward Limit Switch")
  public boolean getForwardLimitSwitch() {
    if (yawMotor.isFwdLimitSwitchClosed() == 1) {
      return true;
    } else return false;
  }

  public boolean getReverseLimitSwitch() {
    if (yawMotor.isRevLimitSwitchClosed() == 1) {
      return true;
    } else return false;
  }

  public void resetSensors() {
    yawMotor.setSelectedSensorPosition(0);
  }

  @Log
  public double getYawMotorOutputCurrent() {
    return yawMotor.getStatorCurrent();
  }

  public double getYawVelocityDegreesPerSecond() {
    return UnitConverter.encoderTicksToDegrees(yawMotor.getSelectedSensorVelocity());
  }

  @Log(name = "Open Loop Demand")
  public double getOpenLoopDemand() {
    return openLoopDemand;
  }

  public void openLoop(double demand) {
    openLoopDemand = demand;
    yawMotor.set(ControlMode.PercentOutput, demand);
    //DriverStation.reportWarning(yawMotor.getLastError().toString(), false);
  }

  public void setAngleGoal(Rotation2d angle) {
    // we need to convert the relative angle from PV into an absolute angle
    // convert angle target to encoder units
    //                                 add current rotation to make absolute           rotations                         ticks
    angleGoal = angle;

    yawMotor.set(ControlMode.Position, yawMotor.getSelectedSensorPosition() + (angle.getRadians() / 2 * Math.PI) * turretTicksPerRotation);
  }
  public void reset(Rotation2d angle) {
    yawMotor.setSelectedSensorPosition((angle.getRadians() / 2 * Math.PI) * turretTicksPerRotation);
  }

  public Rotation2d getAngle() {
    return Rotation2d.fromDegrees(Units.radiansToDegrees(yawMotor.getSelectedSensorPosition() / turretTicksPerRotation * 2 * Math.PI));
  }

  public void setSoftLimitsEnable(boolean enable) {
    if (enable) {
      yawMotor.configForwardSoftLimitThreshold(turretMaxPosition - turretSoftLimitOffset);
      yawMotor.configReverseSoftLimitThreshold(turretMinPosition + turretSoftLimitOffset);
    } else {
      yawMotor.configSoftLimitDisableNeutralOnLOS(true, 50);
    }
  }

  @Log.BooleanBox(name = "Turret Homed")
  public boolean getTurretHomed() {
    return homed;
  }

  @Log.BooleanBox(name = "Turret Centered")
  public boolean getTurretCentered() {
    return centered;
  }

  public void setEnabled(boolean enabled) { this.enabled = enabled; }

  public boolean getEnabled() { return enabled; }

  @Log(name = "Angle Goal (deg)")
  public double getAngleGoalDegrees() {
    return angleGoal.getDegrees();
  }
  @Log(name = "Current Angle (deg)")
  public double getCurrentAngleDegrees() {
    return getAngle().getDegrees();
  }
}
