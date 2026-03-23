package frc.robot.SubSystem.Vision.AimAssist;

import java.util.List;
import java.util.Optional;

import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.Num;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import frc.robot.SubSystem.Swerve.Drive;
import frc.robot.SubSystem.Vision.Vision;
import frc.robot.SubSystem.Vision.VisionIO;

public class AimAssist {
    VisionIO vision;
    int indexAimingCamera;
    double reccomendedHeading;
    
    ProfiledPIDController headingController = new ProfiledPIDController(0, 0, 0, 
        new TrapezoidProfile.Constraints(getreccomendedHeading(), getreccomendedHeading()));

    public AimAssist(VisionIO vision,  int indexAimingCamera) {
        this.vision = vision;
        this.indexAimingCamera = indexAimingCamera;

    }

    /**
     * 
     * @return
     */
    public double getreccomendedHeading() {
        return reccomendedHeading;
    }

    public void periodic() {

        Optional<List<PhotonTrackedTarget>>[] targets =  vision.getTargets();
        boolean aimingCameraHasTarget = false;;
        for (int i  = 0; i< targets.length; i++) {
            if (targets[i].isEmpty()) {
                continue;
            }
            
            //iterate over cameras with basic checks
            for (PhotonTrackedTarget target : targets[i].get()) {
                if (target.getPoseAmbiguity() > 0.2) continue;

                //check for hub apriltags on the inside of the alliance zones, trying to score from neutral is a major foul
                if (target.getFiducialId() == 9 || 
                target.getFiducialId() == 10 ||
                target.getFiducialId() == 8 ||
                target.getFiducialId() == 5 ||
                target.getFiducialId() == 11||
                target.getFiducialId() == 2 ||
                target.getFiducialId() == 18 ||
                target.getFiducialId() == 27 ||
                target.getFiducialId() == 25 ||
                target.getFiducialId() == 26 ||
                target.getFiducialId() == 21 ||
                target.getFiducialId() == 24) {
                    if (i == indexAimingCamera) {
                        reccomendedHeading = target.getYaw();
                    }
                    else if  (!aimingCameraHasTarget){
                        // if its not the aiming camera, get it out of sight:
                        reccomendedHeading = target.getYaw() < 0? 1 : -1;
                        // TODO: may need to bring this down if the camera Aiming Camera struggles to pick up the tag
                    }
                }
            }
        }
    }
}
