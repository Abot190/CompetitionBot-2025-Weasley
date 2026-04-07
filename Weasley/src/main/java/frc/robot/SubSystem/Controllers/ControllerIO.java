package frc.robot.SubSystem.Controllers;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.button.Trigger;

public interface ControllerIO {

    /**
     * positive x = forward
     * @return
     */
    public default double getDriveX() {return 0.0;}

    /**
     * positive Y = left
     * @return
     */
    public default double getDriveY() {return 0.0;}

    /**
     * positive Twist = counterClockwise
     * @return
     */
    public default double getDriveTwist() {return 0.0;}

    public default boolean startShooter() {return false;}

    public default boolean startShooterInverted() {return false;}

    public default boolean hopperOut() {return false;}

    public default boolean hopperIn() {return false;}

    public default boolean climbUp() {return false;}

    public default boolean climbDown() {return false;}

    public default boolean getAimAssistAdjustment() {
        return false;
    }

    public default boolean resetGyro() {
        return false;
    }
}