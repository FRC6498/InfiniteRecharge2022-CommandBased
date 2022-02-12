// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.surpriselib;

import edu.wpi.first.math.util.Units;
import frc.robot.Constants;

/**
 * Offers various angular<->angular and angular<->linear unit conversions
*/
public class UnitConverter {
    
    public static double rotationsToInches(final double rotations) {
        return rotations * (Constants.Drive.WheelDiameterInches * Math.PI);
    }

    public static double rpmToInchesPerSecond(final double rpm) {
        return rotationsToInches(rpm) / 60;
    }

    public static double inchesToRotations(final double inches) {
        return inches / (Constants.Drive.WheelDiameterInches * Math.PI);
    }

    public static double inchesPerSecondToRpm(final double inchesPerSecond) {
        return inchesToRotations(inchesPerSecond) * 60;
    }

    public static double encoderTicksToDegrees(Double ticks) {
        return ticks / (1 / Constants.degreesToTurretTicks);
    }

    public static double degreesToEncoderTicks(double degrees) {
        // 1 rot = 360 degrees
        // 1 rot = 2048 ticks
        // 1 degree = 1/360 rotation
        // 1 degree = 2048 ticks / 360
        // 1 degree = 5.689 ticks
        return degrees * Constants.degreesToTurretTicks;

    }

    public double radiansToEncoderTicks(double radians) {
        return degreesToEncoderTicks(Units.radiansToDegrees(radians));
    }
    
    public double encoderTicksToRadians(double ticks) {
        return Units.degreesToRadians(encoderTicksToDegrees(ticks));
    }
}
