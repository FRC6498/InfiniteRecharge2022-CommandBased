// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.DriveArcadeOpenLoop;
import frc.robot.commands.HomeTurret;
import frc.robot.commands.OpenLoopTurret;
import frc.robot.subsystems.DriveBase;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.VisionSystem;
import io.github.oblarg.oblog.Logger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  public DriverStation.Alliance startPosition;
  //@Log
  //SendableChooser<Command> autoChooser = new SendableChooser<>();
  // controllers
  XboxController driver = new XboxController(Constants.Drive.DriverControllerId);
  // subsystems
  private final DriveBase driveBase = new DriveBase();
  private final Intake intake = new Intake();
  private final VisionSystem visionSystem = new VisionSystem();
  private final Turret turret = new Turret();
  // commands
  //private final DriveArcadeOpenLoop arcadeCommand = new DriveArcadeOpenLoop(
  //  driveBase, 
  //  driver::getRightTriggerAxis, 
  //  driver::getLeftX, 
  //  driver::getLeftTriggerAxis
  //);
  //private final OpenLoopTurret openLoopTurret = new OpenLoopTurret(driver::getRightX, turret);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    Logger.configureLoggingAndConfig(this, false);
    // Configure the button bindings
    configureButtonBindings();
    //driveBase.setDefaultCommand(arcadeCommand);
    //turret.setDefaultCommand(openT);
    //turret.setDefaultCommand(new frc.robot.commands.TurretYaw(turret, visionSystem));
    /*turret.setDefaultCommand(new SequentialCommandGroup(
      new HomeTurret(turret),
      new OpenLoopTurret(() -> 0, turret)
      ));
    */
    //turret.setDefaultCommand(new OpenLoopTurret(driver::getRightX, turret));
    // configure autos
    //autoChooser.setDefaultOption("Leave Tarmac & Stop", simplestAuto);
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    // button commands
    //new JoystickButton(driver, Button.kRightBumper.value).whenPressed(new InstantCommand(driveBase::toggleGear, driveBase));
    // start/stop intake
    new JoystickButton(driver, Button.kX.value).whenHeld(new StartEndCommand(intake::startIntakeMotor, intake::stopIntakeMotor, intake));
    // set brake mode
    //new JoystickButton(driver, Button.kB.value).whenPressed(new InstantCommand(driveBase::toggleInverted, driveBase));
  }
  
  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return new PrintCommand("Auto!");
  }
}
