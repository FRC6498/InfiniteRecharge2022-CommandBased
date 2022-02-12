// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Turret;

public class HomeTurret extends CommandBase {
  Turret turret;
  /** Creates a new HomeTurret. */
  public HomeTurret(Turret turret) {
    this.turret = turret;
    addRequirements(turret);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    DriverStation.reportWarning("HOMING STARTED", false);
    turret.homed = false;
    turret.centered = false;
    turret.setSoftLimitsEnable(false);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //System.out.println("WHAT");
    if (!turret.homed) {
      System.out.println("HOMING");
      turret.openLoop(0.1);
      // limit switch trigger
      if (turret.getForwardLimitSwitch()) {
        turret.reset(Rotation2d.fromDegrees(270));
        turret.openLoop(0);   
        turret.homed = true;
        System.out.println("FORWARD SWITCH TRIGGERED");
      }
      if (turret.getReverseLimitSwitch()) {
        turret.reset(Rotation2d.fromDegrees(0));
        turret.openLoop(0);   
        turret.homed = true;   
        System.out.println("REVERSE SWITCH TRIGGERED");
      }
      if (turret.getYawVelocityDegreesPerSecond() < Constants.Shooter.turretHomingVelocityStopThreshold) {
        // fallback
        turret.openLoop(0);
        //turret.homed = true;
        System.out.println("VELOCITY THRESHOLD TRIGGERED");
      }
    }
    if (turret.homed) {
      if (!turret.centered) {
        System.out.println("CENTERING");
        turret.setAbsoluteAngleGoal(Rotation2d.fromDegrees(135));
        
        if (turret.getPositionError() < 0.5) {
          System.out.println("CENTERED");
          turret.centered = true;
        }
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    turret.setSoftLimitsEnable(true);
    System.out.println("TURRET CENTERED");
    System.out.println("SOFT LIMITS ENABLED");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return turret.centered;
  }
}
