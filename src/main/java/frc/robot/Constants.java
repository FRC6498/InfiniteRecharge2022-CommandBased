// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.math.numbers.N2;
import edu.wpi.first.math.system.LinearSystem;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.math.util.Units;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static final double degreesToFalconTicks = 5.689;
    public static final double degreesToTurretTicks = 35.04;

    public static final class Drive {

        public static final int LeftLeaderId = 3;
        public static final int LeftFollowerId = 4;
        public static final int RightLeaderId = 1;
        public static final int RightFollowerId = 2;
        public static final int LeftPhotoeyePort = 0;
        public static final int RightPhotoeyePort = 0;
        // low=>high
        public static final int ShifterForwardSolenoidId = 2;
        // high=>low
        public static final int ShifterReverseSolenoidId = 3;
        public static final NeutralMode BrakeModeDefault = NeutralMode.Brake;
        public static final double DriveRampRate = 2;
        public static final double WheelDiameterInches = 5.5;
        public static final int DriverControllerId = 0;
        public static final double MinAutoTurnRate = 0.2;
        public static final double NormalAutoTurnRate = 1;
        public static final double AutoTurnDeadbandDegrees = 0.5;
        public static final double RotationsToInches = Math.PI * WheelDiameterInches; // 1 wheel rotation = 1 circumference travelled
        public static final double TicksPerRotation = 2048; // 2048 ticks per rotation
        // 4096 ticks per circumference travelled, so 4096/circumference = ticks per unit length
        public static final double EncoderTicksPerInch = TicksPerRotation / RotationsToInches; 
        public static final double TurnAnglekP = 1;
        public static final double TurnAnglekI = 0;
        public static final double TurnAnglekD = 0;
        public static final double TurnInPlaceDeadband = 0.1;
        public static final double GearRatio = 12;
        public static final double TrackWidth = Units.inchesToMeters(27.25);
        public static final double kS = 0;
        public static final double kVLinear = 0;
        public static final double kALinear = 0;
        public static final double kVAngular = 0;
        public static final double kAAngular = 0;
        public static final LinearSystem<N2, N2, N2> driveBaseSystem =
            LinearSystemId.identifyDrivetrainSystem(
                kVLinear, 
                kALinear, 
                kVAngular, 
                kAAngular
            );
        // units = m/s
        public static final double maxPathVelocity = 1;
        // units = m/s^2
        public static final double maxPathAcceleration = 1;

    }

    public static final class Intake {
        public static final int intakeVictorCANId = 5;
        public static final int leftPistonForwardChannel = 0;
        public static final int leftPistonReverseChannel = 0;
        public static final int rightPistonForwardChannel = 0;
        public static final int rightPistonReverseChannel = 0;
    }

    public static final class Shooter {
        // Turret
        public static final int yawMotorCANId = 10;
        public static final double turretPositionOffsetThreshold = 30;
        public static final double turretYaw_kP = 0.8;
        public static final double turretYaw_kD = 0;
        public static final double turretTicksPerRotation = 2048.0*(40.0/10.0)*(40.0/20.0)*(314.0/40.0);
        public static final double turretTicksRange = turretTicksPerRotation * 0.75;
        public static final double turretMaxPosition = 135+13.25;
        public static final double turretMinPosition = -135-13.25;
        public static final double turretSoftLimitOffset = 20;
        public static final double turretHomingVelocityStopThreshold = 0.1;

    }

    public static final class Vision {

        // Heights are in METERS
        // Pitches are in DEGREES (convert to radians)
        public static final double upperHubTargetHeight = Units.inchesToMeters(65);
        public static final double upperHubTargetPitch = Units.degreesToRadians(90);

        // limelight  
        public static final String limelightCameraName = "limelight";
        public static final double limelightHeightFromField = Units.inchesToMeters(27);
        public static final int upperHubPipelineID = 0;
        public static final double limelightPitch = Units.degreesToRadians(33);
        public static final double[] comparisonConstants = new double[] {
            limelightHeightFromField,
            upperHubTargetHeight,
            limelightPitch
        };

        // lifecam
        public static final String lifecamCameraName = "lifecam";
        public static final double lifecamHeightFromField = Units.inchesToMeters(26);
        public static final int redBallPipelineID = 1;
        public static final int blueBallPipelineID = 2;
        public static final double lifecamPitch = Units.degreesToRadians(limelightPitch+180);
    }
}
