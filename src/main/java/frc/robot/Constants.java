// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

    public static final class Drive {

        public static final int LeftLeaderId = 3;
        public static final int LeftFollowerId = 4;
        public static final int RightLeaderId = 1;
        public static final int RightFollowerId = 2;
        public static final int LeftPhotoeyePort = 0;
        public static final int RightPhotoeyePort = 0;
        public static final int ShifterSolenoidId = 0;
        public static final int VelocityControlSlot = 0;
        public static final double VelocityControlkP = 0;
        public static final double VelocityControlkI = 0;
        public static final double VelocityControlkD = 0;
        public static final double VelocityControlkF = 0;
        public static final int VelocityControlIZone = 0;
        public static final double VelocityControlRampRate = 0;
        public static final int BaseLockSlot = 0;
        public static final double BaseLock_kP = 0;
        public static final double BaseLock_kI = 0;
        public static final double BaseLock_kD = 0;
        public static final double BaseLock_kF = 0;
        public static final double BaseLockIZone = 0;
        public static final double BaseLockRampRate = 0;
        public static final NeutralMode BrakeModeDefault = NeutralMode.Brake;
        public static final double DriveRampRate = 2;
        public static final double WheelDiameterInches = 0;
        public static final int DriverControllerId = 0;
        public static final double MinAutoTurnRate = 0.2;
        public static final double NormalAutoTurnRate = 1;
        public static final double AutoTurnDeadbandDegrees = 0.5;
        public static final double RotationsToInches = Math.PI * WheelDiameterInches; // 1 wheel rotation = 1 circumference travelled
        public static final double TicksPerRotation = 4096; // 4096 ticks per rotation
        // 4096 ticks per circumference travelled, so 4096/circumference = ticks per unit length
        public static final double EncoderTicksPerInch = TicksPerRotation / RotationsToInches; 
        public static final double InchesToMeters = 0.0254;
        public static final double TurnAnglekP = 0;
        public static final double TurnAnglekI = 0;
        public static final double TurnAnglekD = 0;
        public static final double TurnInPlaceDeadband = 0;

    }

    public static final class Vision {

        public static final String LimelightCameraName = "limelight";
        public static final int AutoPipelineID = 0;
        // Heights are in METERS
        public static final double LimelightHeightFromField = 0;
        public static final double UpperHubTargetHeight = 0;
        // Pitches are in DEGREES
        public static final double LimelightPitch = 0;
        public static final double UpperHubTargetPitch = 0;

    }
}
