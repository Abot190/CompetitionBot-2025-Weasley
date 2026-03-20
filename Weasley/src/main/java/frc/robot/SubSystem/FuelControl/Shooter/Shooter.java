package frc.robot.SubSystem.FuelControl.Shooter;

import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.math.controller.BangBangController;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.SubSystem.Logging.NerdLog;

public class Shooter implements ShooterIO {

    //for ShuffleBoard:
    ShuffleboardTab shuffleTab;
    double targetSpeedRadPerSec = 0.00;
    double RequestedVolts = 0.00;
    double bangCrtlTolerance = 0.00;

    //ShuffleBoard Entrys:
    GenericEntry targetSpeedEntry;
    GenericEntry requestedVoltageEntry;
    GenericEntry bangCrtlToleranceEntry;

    //logging:

    //functionals:
    BangBangController bangCrtl;
    SparkMax motor;
    RelativeEncoder encoder;

    /**
     * 
     * @param motor - take a guess
     * @param tab - preferred tab for more control
     */
    public Shooter(SparkMax motor, ShuffleboardTab tab) {
        this.motor = motor;
        this.shuffleTab = tab;
        configureMotor(false);
        this.encoder = motor.getEncoder();
        bangCrtl = new BangBangController();
        bangCrtl.setTolerance(bangCrtlTolerance);

        targetSpeedEntry = tab.add("target Shooter Speed RadPerSec", 0).getEntry();
        requestedVoltageEntry = tab.add("Shooter Voltage To Request", 0.00).getEntry();
        bangCrtlToleranceEntry = tab.add("Shoot BangBang Controller Tolerance", 0.00).getEntry();
    }
    
    @Override
    public void shoot(boolean invert) {
        bangCrtl.setSetpoint(targetSpeedRadPerSec);
        if (!invert) motor.setVoltage(RequestedVolts * bangCrtl.calculate(Units.rotationsPerMinuteToRadiansPerSecond(encoder.getVelocity())));
        else motor.setVoltage(RequestedVolts * bangCrtl.calculate(Units.rotationsPerMinuteToRadiansPerSecond(Math.abs(encoder.getVelocity()))));
    }

    @Override
    public void stop() {
        motor.setVoltage(0.00);
    }

    @Override
    public boolean isShooting() {
        return bangCrtl.atSetpoint();//Units.rotationsPerMinuteToRadiansPerSecond(encoder.getVelocity());
    }

    @Override
    public void periodic() {
        NerdLog.logBooleanVariable("Fuel Control/Shooter/ Shooter is Shooting", isShooting());
        NerdLog.logBooleanVariable("Fuel Control/Shooter/ Shooter is Above set Speed", Units.rotationsPerMinuteToRadiansPerSecond(encoder.getVelocity()) > targetSpeedRadPerSec);
        NerdLog.logDouble("Fuel Control/Shooter/ Encoder Velocity RadPerSec", Units.rotationsPerMinuteToRadiansPerSecond(encoder.getVelocity()));
        NerdLog.logDouble("Fuel Control/Shooter/ target speed RadPerSec", targetSpeedRadPerSec);
        targetSpeedRadPerSec = targetSpeedEntry.getDouble(0.00);
        RequestedVolts = requestedVoltageEntry.getDouble(0.00);
        bangCrtlTolerance = bangCrtlToleranceEntry.getDouble(0.00);
        bangCrtl.setTolerance(bangCrtlTolerance);

    }

    //hopper uses same config
    private void configureMotor(boolean inverted) {
        SparkMaxConfig config = new SparkMaxConfig();
         config.inverted(inverted)
        .voltageCompensation(12)
        .idleMode(IdleMode.kCoast)
        .smartCurrentLimit(20);
         config.signals.absoluteEncoderPositionAlwaysOn(true)
        .absoluteEncoderPositionPeriodMs((int) 1000/100)
        .absoluteEncoderVelocityPeriodMs(20)
        .appliedOutputPeriodMs(20)
        .busVoltagePeriodMs(20)
        .outputCurrentPeriodMs(20);
        motor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }
}
