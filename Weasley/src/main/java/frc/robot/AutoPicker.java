package frc.robot;


import java.util.List;
import java.util.Optional;

import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.wpilibj2.command.SwerveControllerCommand;
import frc.robot.SubSystem.Climb.Climb;
import frc.robot.SubSystem.Climb.ClimbIO;
import frc.robot.SubSystem.FuelControl.FuelControl;
import frc.robot.SubSystem.Swerve.Drive;
import frc.robot.SubSystem.Vision.Vision;
import frc.robot.SubSystem.Vision.VisionIO;

public class AutoPicker {
    

    private static Drive drive;
    private static FuelControl fuelCrtl;
    private static ClimbIO climb;
    private static VisionIO vision;
    private static ChassisSpeeds DesiredChassisSpeeds;

    /**
     *  do this is the robotContainer
     * @param drive
     * @param fuelCrtl
     * @param climb
     * @param vision
     */
    public static void SupplySubSystems(Drive drive, FuelControl fuelCrtl, ClimbIO climb, VisionIO vision) {
    }

    public static enum AutoRoutines {
        SHOOT_BALLS, CLIMB, SHOOT_BALLS_AND_CLIMB, SHOOT_BALLS_AND_COLLECT_DEPOSITE, SHOOT_BALLS_AND_GO_MIDDLE 
    }

    public static void PickAuto(AutoRoutines routine) {
        switch (routine) {
            case SHOOT_BALLS: ShootBalls(); break;
            case CLIMB: findClimbRack(); Climb(); break;
            case SHOOT_BALLS_AND_CLIMB: ShootBalls(); findClimbRack(); Climb(); break; // could use recursion, not lazy enough
            case SHOOT_BALLS_AND_COLLECT_DEPOSITE: break;
            case SHOOT_BALLS_AND_GO_MIDDLE: break;
        }

    }

    /** shoot only */
    private static void ShootBalls() {
        
    }

    /** climb only 
     * MAKE SURE THE LIMITS ARE ON THE CLIMB FIRST!!!
    */
    private static void Climb() {

    }

    /**finds and moves the robot to the climb rack */
    private static void findClimbRack() {
        boolean AtClimb = false; //whether we are at the goal or not

        //this is going to be the holy mother of nesting
        do {
            //move closer to setpoint:
            Optional<ChassisSpeeds> desiredChassis = Optional.empty();
            Optional<List<PhotonTrackedTarget>>[] targetsOptional = vision.getTargets() ;
            for (Optional<List<PhotonTrackedTarget>> optionalTargetPerCam : targetsOptional) {
                if (optionalTargetPerCam.isEmpty()) continue;

                double maxAmbiguity = 1.1; // slightly heigher than pose ambig max from method
                for (PhotonTrackedTarget target : optionalTargetPerCam.get()) {
                    if (target.getPoseAmbiguity() > 0.2) continue; // we don't accept ambigous targets here

                    //filter for climb AprilTags
                    if ((target.getFiducialId() == 32 || target.getFiducialId() == 31 || target.getFiducialId() == 15 || target.getFiducialId() == 16)
                     && target.getPoseAmbiguity() < maxAmbiguity) {

                        maxAmbiguity = target.getPoseAmbiguity();

                        //
                        desiredChassis = Optional.of(new ChassisSpeeds(0,0,0));
                    }
                }
            }

            if (desiredChassis.isEmpty()) desiredChassis = Optional.of(getDesiredChassisSpeeds()); // use previous chassisSpeed;
            else {
                //set new chassisSpeeds
            }

            captureDesiredChassisSpeeds(desiredChassis.get());

            //check if we made it to setpoint
        } while (!AtClimb);

    }

    /** Shoot Balls, turns to climb rack, and does the whole climbing thing
     * MAKE SURE THE LIMITS ARE ON THE CLIMB FIRST!!!
    */
     private static void ShootAndClimb() {

    }

    private static void captureDesiredChassisSpeeds(ChassisSpeeds speeds) {
        DesiredChassisSpeeds = speeds;
    }

    private static ChassisSpeeds getDesiredChassisSpeeds() {
        return DesiredChassisSpeeds;
    }

    private double scaleDistanceWithControls(double distance) {
        return 0.0;
    }
}
