package frc.robot.SubSystem.FuelControl;

import com.revrobotics.spark.SparkMax;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.SubSystem.FuelControl.Hopper.Hopper;
import frc.robot.SubSystem.FuelControl.Hopper.HopperIO;
import frc.robot.SubSystem.FuelControl.Shooter.Shooter;
import frc.robot.SubSystem.FuelControl.Shooter.ShooterIO;
import edu.wpi.first.wpilibj.Timer;

public class FuelControl{
    
    //for adjusting:
    ShuffleboardTab fuelTab;
    double hopperOutEnabledTimeSec = 0;
    double hopperOutDisabledTimeSec= 0;

    //logging:
    
    //functionals:
    HopperIO hopper;
    ShooterIO shooter;
    Timer hopperOutTimer;
    boolean hopperOutEnabled = false;;

    public FuelControl(SparkMax shootermotor, SparkMax hopperMotor) {
        fuelTab = Shuffleboard.getTab("Fuel Management");
        this.hopper = new Hopper(hopperMotor, fuelTab);
        this.shooter = new Shooter(hopperMotor, fuelTab);
    }

    public void startShooter() {
        shooter.shoot(false);
    }

    public void startShooterInverted() {
        if (!shooter.isShooting()) shooter.shoot(true);
    }


    //TODO: test if you can have it automatically start shooter when you set the hopper
    public void outtake() {
        if (!shooter.isShooting()) {
            return;
        }

        if (hopperOutEnabled && hopperOutTimer.hasElapsed(hopperOutEnabledTimeSec)) {
            hopperOutTimer.restart();
            hopperOutEnabled = false;
        }
        else if (!hopperOutEnabled && hopperOutTimer.hasElapsed(hopperOutDisabledTimeSec)) {
            hopperOutTimer.restart();
            hopperOutEnabled = true;
        }
        else if (!hopperOutTimer.isRunning()) hopperOutTimer.start();

        if (hopperOutEnabled) hopper.hopperOut();
        
    }

    public void intake() {
        if (shooter.isShooting()) hopper.hopperin();
    }

    public void periodic() {
        shooter.periodic();
        hopper.periodic();

         if (hopperOutTimer.hasElapsed(3)) { // 3 is arbitrary, but i doubt we'd run either of the times for more than 2 secs
            hopperOutTimer.stop();
            hopperOutTimer.reset();
        }
    }

    
}
